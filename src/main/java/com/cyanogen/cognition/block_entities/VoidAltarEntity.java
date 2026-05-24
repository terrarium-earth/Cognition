package com.cyanogen.cognition.block_entities;

import com.cyanogen.cognition.registries.RegisterBlockEntities;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;

@SuppressWarnings("FieldCanBeLocal")
public class VoidAltarEntity extends BlockEntity {

    // rates are in XP/s

    private final float baseRate = 12;
    private final int target = 720;
    private final int xpStep = 20;
    private final int gardenStep = 20;

    public VoidAltarEntity(BlockPos pos, BlockState blockState) {
        super(RegisterBlockEntities.VOID_ALTAR.get(), pos, blockState);
    }

    //-----------BEHAVIOR-----------//

    public static <T> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {

        if(blockEntity instanceof VoidAltarEntity altar && !level.isClientSide){

            if(level.getGameTime() % altar.xpStep == 0){

//                System.out.println("=================");
//                System.out.println("stored value: " + altar.orbValue);
//                System.out.println("=================");

                if(altar.orbValue >= altar.target){

                    ServerLevel server = (ServerLevel) level;
                    ExperienceOrb orb = new ExperienceOrb(server, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, altar.target);
                    server.addFreshEntity(orb);
                    altar.setOrbValue(0);
                }
                else{
                    altar.incrementOrbValue(altar.baseRate);
                }

            }
            if(level.getGameTime() % altar.gardenStep == 0){
                altar.gardenStep(pos, level);
            }
        }

    }

    //-----------GARDEN-----------//

    public void gardenStep(BlockPos pos, Level level){
        double multiplier = scanGarden(pos.below(), level);
    }

    public double scanGarden(BlockPos center, Level level){ //usable area of 56 blocks

        int radius = 4;
        double radiusInternal = 4.15;

        int x1 = center.getX() - radius;
        int x2 = center.getX() + radius;
        int z1 = center.getZ() - radius;
        int z2 = center.getZ() + radius;
        int y = center.getY();

        for(int x = x1; x <= x2; x++){
            for(int z = z1; z <= z2; z++){

                BlockPos target = new BlockPos(x,y,z);
                if(MiscUtils.straightLineDistance(target, center) <= radiusInternal){

                    //1. spawn crystals
                    if(level.getBlockState(target).is(Tags.Blocks.OBSIDIANS) && level.getBlockState(target.above()).isEmpty()){
                        spawnCluster(target.above(), level);
                    }
                    //2. age existing crystals
                }

            }

        }
        return 1;
    }

    public void spawnCluster(BlockPos pos, Level level){

        double resonantSpawnChance = 0.2 / 56;
        double interferenceSpawnChance = 0.75 / 56;

        if(Math.random() <= resonantSpawnChance){
            level.setBlockAndUpdate(pos, Blocks.AMETHYST_CLUSTER.defaultBlockState());
        }
        else if(Math.random() <= interferenceSpawnChance){
            level.setBlockAndUpdate(pos, Blocks.YELLOW_CANDLE.defaultBlockState());
        }
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
