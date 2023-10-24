package com.cyanogen.experienceobelisk.block_entities;

import com.cyanogen.experienceobelisk.registries.RegisterBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;

public class ExperienceFountainEntity extends ExperienceReceivingEntity implements IAnimatable {

    public ExperienceFountainEntity(BlockPos pos, BlockState state) {
        super(RegisterBlockEntities.EXPERIENCEFOUNTAIN_BE.get(), pos, state);
    }

    //-----------ANIMATIONS-----------//

    private <E extends BlockEntity & IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        AnimationController controller = event.getController();
        controller.transitionLengthTicks = 0;

        BlockEntity entity = event.getAnimatable();

        if(entity instanceof ExperienceFountainEntity fountain){
            if(controller.getCurrentAnimation() == null || !toName(fountain.activityState).equals(controller.getCurrentAnimation().animationName)){
                switch(fountain.activityState){
                    case 0 -> controller.setAnimation(new AnimationBuilder().addAnimation("slow"));
                    case 1 -> controller.setAnimation(new AnimationBuilder().addAnimation("moderate"));
                    case 2 -> controller.setAnimation(new AnimationBuilder().addAnimation("fast"));
                    case 3 -> controller.setAnimation(new AnimationBuilder().addAnimation("hyperspeed"));
                }
            }
        }

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

            List<Player> playerList = level.getEntitiesOfClass(Player.class, new AABB(pos, pos.east().south().above()));
            if(!playerList.isEmpty() && !fountain.hasPlayerAbove){
                level.playSound(null, pos, SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON, SoundSource.BLOCKS, 0.2f, 0.6f);
                fountain.hasPlayerAbove = true;
                level.sendBlockUpdated(pos, state, state, 2);
            }
            else if(playerList.isEmpty() && fountain.hasPlayerAbove){
                level.playSound(null, pos, SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_OFF, SoundSource.BLOCKS, 0.2f, 0.2f);
                fountain.hasPlayerAbove = false;
                level.sendBlockUpdated(pos, state, state, 2);
            }

            if(boundEntity instanceof ExperienceObeliskEntity obelisk
                    && !level.isClientSide
                    && obelisk.getFluidAmount() > 0
                    && (level.hasNeighborSignal(pos) || fountain.hasPlayerAbove)){

                int value = 4;
                int interval = 20;

                switch (fountain.getActivityState()) {
                    case 1 -> { //40xp/s
                        value = 20;
                        interval = 10;
                    }
                    case 2 -> { //400xp/s
                        value = 100;
                        interval = 5;
                    }
                    case 3 -> { //4000xp/s
                        value = 400;
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
    public boolean hasPlayerAbove = false;

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

    public String toName(int state){
        switch(state){
            case 0 -> {
                return("slow");
            }
            case 1 -> {
                return("moderate");
            }
            case 2 -> {
                return("fast");
            }
            case 3 -> {
                return("hyperspeed");
            }
        }
        return null;
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

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this, BlockEntity::getUpdateTag);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag tag = pkt.getTag();
        if(tag != null){
            this.activityState = tag.getInt("ActivityState");
            this.hasPlayerAbove = tag.getBoolean("PlayerAbove");
        }
        super.onDataPacket(net, pkt);
    }


}
