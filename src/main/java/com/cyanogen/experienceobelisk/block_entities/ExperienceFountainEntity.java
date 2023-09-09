package com.cyanogen.experienceobelisk.block_entities;

import com.cyanogen.experienceobelisk.registries.RegisterBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;

public class ExperienceFountainEntity extends ExperienceReceivingEntity implements IAnimatable {

    public ExperienceFountainEntity(BlockPos pPos, BlockState pState) {
        super(RegisterBlockEntities.EXPERIENCEFOUNTAIN_BE.get(), pPos, pState);
    }

    //-----------ANIMATIONS-----------//

    private <E extends BlockEntity & IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        AnimationController controller = event.getController();
        controller.transitionLengthTicks = 0;

        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return new AnimationFactory(this);
    }

    //-----------PASSIVE BEHAVIOR-----------//

    public static <T> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {

        if(blockEntity instanceof ExperienceFountainEntity fountain && fountain.isBound){

            BlockEntity boundEntity = level.getBlockEntity(fountain.getBoundPos());
            boolean hasPlayerAbove = false;

            List<Player> playerList = level.getEntitiesOfClass(Player.class, new AABB(pos, pos.east().south().above()));
            if(!playerList.isEmpty()){
                hasPlayerAbove = true;
            }

            if(boundEntity instanceof ExperienceObeliskEntity obelisk
                    && !level.isClientSide
                    && obelisk.getFluidAmount() > 0
                    && (level.hasNeighborSignal(pos) || hasPlayerAbove)){

                int value = 4;
                int interval = 20;

                switch (fountain.getActivityState()) {
                    case 1 -> {
                        value = 20;
                        interval = 10;
                    }
                    case 2 -> {
                        value = 100;
                        interval = 5;
                    }
                    case 3 -> {
                        value = 1000;
                        interval = 2;
                    }
                }

                if(value > obelisk.getFluidAmount() / 20){
                    value = obelisk.getFluidAmount() / 20;
                }

                if(level.getGameTime() % interval == 0){
                    ServerLevel server = (ServerLevel) level;
                    ExperienceOrb orb = new ExperienceOrb(server, pos.getX() + 0.5, pos.getY() + 0.7, pos.getZ() + 0.5, value);
                    obelisk.drain(value * 20);
                    orb.setDeltaMovement(0,0.1 + 0.5 * Math.random(),0);
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
        if(activityState > 3){
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
