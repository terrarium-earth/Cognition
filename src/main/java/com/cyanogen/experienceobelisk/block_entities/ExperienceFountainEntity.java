package com.cyanogen.experienceobelisk.block_entities;

import com.cyanogen.experienceobelisk.registries.RegisterBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ExperienceFountainEntity extends ExperienceReceivingEntity {

    public ExperienceFountainEntity(BlockPos pPos, BlockState pState) {
        super(RegisterBlockEntities.EXPERIENCEFOUNTAIN_BE.get(), pPos, pState);
    }

    //-----------PASSIVE BEHAVIOR-----------//

    public static <T> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {

        long time = level.getGameTime();

        BlockEntity entity = level.getBlockEntity(pos);
        boolean hasSignal = level.hasNeighborSignal(pos);

        if(entity instanceof ExperienceFountainEntity fountain && fountain.isBound && hasSignal){

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
                        orb.value = obelisk.getFluidAmount() / 20;
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
                        orb.value = obelisk.getFluidAmount() / 20;
                        obelisk.setFluid(0);
                    }

                    orb.setDeltaMovement(0,0.1,0);
                    server.addFreshEntity(orb);
                }
            }
        }
    }

    //-----------NBT-----------//

    private int activityState = 0;

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

    @Override
    public void load(CompoundTag tag)
    {
        super.load(tag);
        this.activityState = tag.getInt("ActivityState");
    }

    @Override
    protected void saveAdditional(CompoundTag tag)
    {
        super.saveAdditional(tag);
        tag.putInt("ActivityState", activityState);
    }

    //sends CompoundTag out with nbt data
    @Override
    public CompoundTag getUpdateTag()
    {
        CompoundTag tag = super.getUpdateTag();
        tag.putInt("ActivityState", activityState);

        return tag;
    }
}
