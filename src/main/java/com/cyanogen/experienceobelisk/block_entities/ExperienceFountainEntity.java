package com.cyanogen.experienceobelisk.block_entities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ExperienceFountainEntity extends BlockEntity {

    public ExperienceFountainEntity(BlockPos pPos, BlockState pState) {
        super(ModTileEntitiesInit.EXPERIENCEFOUNTAIN_BE.get(), pPos, pState);
    }

    //-----------PASSIVE BEHAVIOR-----------//

    public static <T> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {

        long time = level.getGameTime();

        BlockEntity entity = level.getBlockEntity(pos);

        if(entity instanceof ExperienceFountainEntity fountain && fountain.isBound && fountain.activityState != 0){

            BlockEntity boundEntity = level.getBlockEntity(fountain.getBoundPos());

            if(boundEntity instanceof ExperienceObeliskEntity obelisk && !level.isClientSide && obelisk.getFluidAmount() > 0){

                ServerLevel server = (ServerLevel) level;
                ExperienceOrb orb = new ExperienceOrb(server, pos.getX() + 0.5, pos.getY() + 0.7, pos.getZ() + 0.5, 0);

                if(fountain.activityState == 1 && time % 20 == 0){ //slow

                    if(obelisk.getFluidAmount() >= 200){
                        orb.value = 10;
                        obelisk.drain(200);
                    }
                    else{
                        orb.value = obelisk.getFluidAmount();
                        obelisk.setFluid(0);
                    }

                    orb.setDeltaMovement(0,0.1,0);
                    server.addFreshEntity(orb);
                }
                else if(fountain.activityState == 2 && time % 5 == 0){ //fast

                    if(obelisk.getFluidAmount() >= 1000){
                        orb.value = 50;
                        obelisk.drain(1000);
                    }
                    else{
                        orb.value = obelisk.getFluidAmount();
                        obelisk.setFluid(0);
                    }

                    orb.setDeltaMovement(0,0.1,0);
                    server.addFreshEntity(orb);
                }
            }
        }
    }

    //-----------NBT-----------//

    private int boundX;
    private int boundY;
    private int boundZ;

    private int activityState = 0;  //0 - off, 1 - slow, 2 - fast

    public boolean isBound = false;

    public void setBound(){
        this.isBound = true;
        setChanged();
    }

    public int getActivityState(){
        return activityState;
    }

    public void cycleActivityState(){
        activityState = activityState + 1;
        if(activityState > 2){
            activityState = 0;
        }
        setChanged();
    }

    public void setBoundPos(BlockPos pos){
        this.boundX = pos.getX();
        this.boundY = pos.getY();
        this.boundZ = pos.getZ();
        setChanged();
    }

    public BlockPos getBoundPos(){
        return new BlockPos(boundX, boundY, boundZ);
    }

    @Override
    public void load(CompoundTag tag)
    {
        super.load(tag);

        this.isBound = tag.getBoolean("isBound");
        this.boundX = tag.getInt("BoundX");
        this.boundY = tag.getInt("BoundY");
        this.boundZ = tag.getInt("BoundZ");
        this.activityState = tag.getInt("ActivityState");
    }

    @Override
    protected void saveAdditional(CompoundTag tag)
    {
        super.saveAdditional(tag);

        tag.putBoolean("isBound", isBound);
        tag.putInt("BoundX", boundX);
        tag.putInt("BoundY", boundY);
        tag.putInt("BoundZ", boundZ);
        tag.putInt("ActivityState", activityState);
    }

    //sends CompoundTag out with nbt data
    @Override
    public CompoundTag getUpdateTag()
    {
        CompoundTag tag = super.getUpdateTag();

        tag.putBoolean("isBound", isBound);
        tag.putInt("BoundX", boundX);
        tag.putInt("BoundY", boundY);
        tag.putInt("BoundZ", boundZ);
        tag.putInt("ActivityState", activityState);

        return tag;
    }

    //gets packet to send to client
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
