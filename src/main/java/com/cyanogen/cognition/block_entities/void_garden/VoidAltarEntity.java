package com.cyanogen.cognition.block_entities.void_garden;

import com.cyanogen.cognition.block.void_garden.AbstractClusterBlock;
import com.cyanogen.cognition.registries.RegisterBlockEntities;
import com.cyanogen.cognition.registries.RegisterBlocks;
import com.cyanogen.cognition.utils.MiscUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("FieldCanBeLocal")
public class VoidAltarEntity extends BlockEntity {

    private final int minBaseRate = 10; //base rate of production, in XP/s, at y levels above 60
    private final int maxBaseRate = 16; //maximum base rate of production, in XP/s, at y = -63
    private final int target = 5345; //XP value of orbs the void altar spawns
    private final int xpStep = 20; //time interval in ticks at which the void altar updates its internal XP reservoir
    private final int gardenStep = 20; //time interval in ticks at which the void garden is updated

    public VoidAltarEntity(BlockPos pos, BlockState blockState) {
        super(RegisterBlockEntities.VOID_ALTAR.get(), pos, blockState);
    }

    public void printBuffer(){
        System.out.println("=================");
        System.out.println("current rate: " + getRate() + " XP/s");
        System.out.println("stored value: " + orbValue);
        //todo:
    }

    //-----------BEHAVIOR-----------//

    public static <T> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {

        if(blockEntity instanceof VoidAltarEntity altar && !level.isClientSide){

            if((level.getGameTime() + 2) % altar.xpStep == 0){ //XP handling

                if(altar.orbValue >= altar.target){

                    ServerLevel server = (ServerLevel) level;
                    ExperienceOrb orb = new ExperienceOrb(server, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, altar.target);
                    server.addFreshEntity(orb);
                    altar.setOrbValue(0);
                }
                else{
                    altar.incrementOrbValue(altar.getRate());
                }

                if(level.hasNeighborSignal(pos)){
                    altar.printBuffer();
                }

            }
            if((level.getGameTime() + 3) % altar.gardenStep == 0){ //garden handling
                altar.updateGarden();
            }
        }

    }

    public int getRate(){
        return Math.round(Math.min(target, getBaseRate() * getGardenEffect()));
    }

    public float getBaseRate(){
        int y = getBlockPos().getY();
        return y >= 60 ? minBaseRate : minBaseRate + (maxBaseRate - minBaseRate) * Math.abs(y - 60f) / 123;
    }

    public float getGardenEffect(){

        float effect = 1f;
        float multiplier;
        if(level == null) return effect;

        for(BlockPos pos : MiscUtils.get2DAreaOfEffect(getBlockPos(), 4, 4.15f)){
            if(level.getBlockEntity(pos) instanceof AbstractClusterEntity cluster){
                Optional<Integer> stage = level.getBlockState(pos).getOptionalValue(AbstractClusterBlock.STAGE);

                if(stage.isPresent()){
                    multiplier = cluster.isResonance ? 1.5f : 1 / 1.5f;
                    effect *= (multiplier * AbstractClusterBlock.stageMultiplier(stage.get()));
                }
            }
        }
        return effect;
    }

    public void updateGarden(){

        float resonantSpawnChance = 1f / 600;
        float interferenceSpawnChance = 1f / 120;
        boolean resonantToSpawn = Math.random() <= resonantSpawnChance;
        boolean interferenceToSpawn = Math.random() <= interferenceSpawnChance;
        List<BlockPos> validLocations = new ArrayList<>();

        if(level == null) return;
        for(BlockPos pos : MiscUtils.get2DAreaOfEffect(getBlockPos().below(), 4, 4.15f)){
            if(level.getBlockState(pos).is(Tags.Blocks.OBSIDIANS) && level.getBlockState(pos.above()).canBeReplaced()){
                validLocations.add(pos);
            }
        }

        if(validLocations.isEmpty()) return;
        BlockPos target = validLocations.get(MiscUtils.randomIntInRange(0, validLocations.size() - 1));
        if(resonantToSpawn){
            level.setBlockAndUpdate(target, RegisterBlocks.RESONANCE_CLUSTER.get().defaultBlockState());
        }
        else if(interferenceToSpawn){
            level.setBlockAndUpdate(target, RegisterBlocks.INTERFERENCE_CLUSTER.get().defaultBlockState());
        }

        //Only spawn a maximum of one cluster per step
        //Resonance clusters take precedence over interference clusters
    }

    //-----------NBT-----------//

    private float orbValue = 0;

    public void incrementOrbValue(float increment){
        orbValue += increment;
        setChanged();
    }

    public void setOrbValue(float value){
        orbValue = value;
        setChanged();
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {

        super.loadAdditional(tag, provider);
        this.orbValue = tag.getFloat("OrbValue");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {

        super.saveAdditional(tag, provider);
        tag.putFloat("OrbValue", orbValue);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider provider) {

        super.handleUpdateTag(tag, provider);
        this.orbValue = tag.getFloat("OrbValue");
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {

        CompoundTag tag = super.getUpdateTag(provider);
        tag.putFloat("OrbValue", orbValue);

        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this, BlockEntity::getUpdateTag);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider provider) {
        CompoundTag tag = pkt.getTag();
        this.orbValue = tag.getFloat("OrbValue");
        super.onDataPacket(net, pkt, provider);
    }

}
