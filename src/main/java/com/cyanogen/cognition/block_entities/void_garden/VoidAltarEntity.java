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
    private final int target = 4000; //XP value of orbs the void altar spawns
    private final int xpStep = 20; //time interval in ticks at which the void altar updates its internal XP reservoir
    private final int gardenStep = 20; //time interval in ticks at which the void garden is updated

    private final float resonantSpawnChance = 1f / 180; //chance per second for a resonance cluster to spawn (Avg. interval 180s = 3 mins)
    private final float interferenceSpawnChance = 1f / 400; //chance per second for an interference cluster to spawn (Avg. interval 400s = 6 mins 40s)
    public static final int resonanceLifespan = 720; //lifetime of a resonance cluster in seconds. (720s = 12 mins)
    public static final int interferenceLifespan = 2400; //lifetime of an interference cluster in seconds (2400s = 40 mins)

    public VoidAltarEntity(BlockPos pos, BlockState blockState) {
        super(RegisterBlockEntities.VOID_ALTAR.get(), pos, blockState);
    }

    //-----------BEHAVIOR-----------//

    public static <T> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {

        if(blockEntity instanceof VoidAltarEntity altar && !level.isClientSide){

            if((level.getGameTime() + 2) % altar.xpStep == 0){ //XP handling

                if(altar.buffer >= altar.target){

                    ServerLevel server = (ServerLevel) level;
                    ExperienceOrb orb = new ExperienceOrb(server, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, altar.target);
                    server.addFreshEntity(orb);
                    altar.setBuffer(0);
                }
                else{
                    altar.incrementBuffer(altar.getRate());
                }

                if(level.hasNeighborSignal(pos)){
                    altar.printBuffer(); //todo
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
        if(level == null) return effect;

        for(BlockPos pos : MiscUtils.get2DAreaOfEffect(getBlockPos(), 4, 4.15f)){
            if(level.getBlockEntity(pos) instanceof AbstractClusterEntity cluster){
                Optional<Integer> stage = level.getBlockState(pos).getOptionalValue(AbstractClusterBlock.STAGE);

                if(stage.isPresent()){
                    effect *= AbstractClusterBlock.stageMultiplier(stage.get(), cluster.isResonance);
                }
            }
        }

        return effect;
    }

    public void updateGarden(){

        boolean resonantToSpawn = Math.random() <= resonantSpawnChance;
        boolean interferenceToSpawn = Math.random() <= interferenceSpawnChance;
        boolean toSpawn = resonantToSpawn || interferenceToSpawn;
        List<BlockPos> validLocations = new ArrayList<>();
        BlockPos targetPos;

        if(toSpawn && level != null){

            for(BlockPos pos : MiscUtils.get2DAreaOfEffect(getBlockPos().below(), 4, 4.15f)){
                if(level.getBlockState(pos).is(Tags.Blocks.OBSIDIANS) && level.getBlockState(pos.above()).canBeReplaced()){
                    validLocations.add(pos.above());
                }
            }

            targetPos = validLocations.get(MiscUtils.randomIntInRange(0, validLocations.size() - 1));
            if(resonantToSpawn){
                level.setBlockAndUpdate(targetPos, RegisterBlocks.RESONANCE_CLUSTER.get().defaultBlockState());
            }
            else {
                level.setBlockAndUpdate(targetPos, RegisterBlocks.INTERFERENCE_CLUSTER.get().defaultBlockState());
            }

            printGardenStatus(resonantToSpawn, interferenceToSpawn, validLocations.size(), targetPos); //todo
        }

        //Only spawn a maximum of one cluster per step
        //Resonance clusters take precedence over interference clusters
    }

    //-----------NBT-----------//

    private int buffer = 0;

    public void incrementBuffer(int increment){
        buffer += increment;
        setChanged();
    }

    public void setBuffer(int value){
        buffer = value;
        setChanged();
    }

    public int getBuffer(){
        return buffer;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {

        super.loadAdditional(tag, provider);
        this.buffer = tag.getInt("Buffer");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {

        super.saveAdditional(tag, provider);
        tag.putInt("Buffer", buffer);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider provider) {

        super.handleUpdateTag(tag, provider);
        this.buffer = tag.getInt("Buffer");
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {

        CompoundTag tag = super.getUpdateTag(provider);
        tag.putInt("Buffer", buffer);

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
        this.buffer = tag.getInt("Buffer");
        super.onDataPacket(net, pkt, provider);
    }

    //-----------DEBUG-----------//

    public void printBuffer(){
        System.out.println("======== BUFFER ========");
        System.out.println("current rate: " + getRate() + " XP/s");
        System.out.println("stored value: " + buffer);
    }

    public void printGardenStatus(boolean resonantToSpawn, boolean interferenceToSpawn, int validLocs, BlockPos target){
        System.out.println("======== GARDEN ========");
        System.out.println("resonant to spawn: " + resonantToSpawn);
        System.out.println("interference to spawn: " + interferenceToSpawn);
        System.out.println("found " + validLocs + " valid locations");
        System.out.println("spawning at " + target);
    }

}
