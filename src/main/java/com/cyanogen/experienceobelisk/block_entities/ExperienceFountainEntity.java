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
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class ExperienceFountainEntity extends ExperienceReceivingEntity implements GeoBlockEntity{

    public ExperienceFountainEntity(BlockPos pos, BlockState state) {
        super(RegisterBlockEntities.EXPERIENCEFOUNTAIN_BE.get(), pos, state);
    }

    //-----------ANIMATIONS-----------//

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    protected static final RawAnimation ACTIVE_SLOW = RawAnimation.begin().thenPlay("active-slow");
    protected static final RawAnimation ACTIVE_MODERATE = RawAnimation.begin().thenPlay("active-moderate");
    protected static final RawAnimation ACTIVE_FAST = RawAnimation.begin().thenPlay("active-fast");
    protected static final RawAnimation ACTIVE_HYPER = RawAnimation.begin().thenPlay("active-hyperspeed");

    protected static final RawAnimation SLOW = RawAnimation.begin().thenPlay("slow");
    protected static final RawAnimation MODERATE = RawAnimation.begin().thenPlay("moderate");
    protected static final RawAnimation FAST = RawAnimation.begin().thenPlay("fast");
    protected static final RawAnimation HYPER = RawAnimation.begin().thenPlay("hyperspeed");

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, this::controller));
    }

    protected <E extends ExperienceFountainEntity> PlayState controller(final AnimationState<E> state) {

        BlockEntity entity = state.getAnimatable();
        AnimationController<E> controller = state.getController();
        RawAnimation animation = controller.getCurrentRawAnimation();
        RawAnimation animationToPlay = SLOW;

        if(level != null && entity instanceof ExperienceFountainEntity fountain){

            boolean hasNeighborSignal = level.hasNeighborSignal(fountain.getBlockPos());
            boolean isActive = fountain.isBound && (hasNeighborSignal || fountain.hasPlayerAbove);

            switch(fountain.activityState){
                case 0 -> {
                    if(isActive){
                        animationToPlay = ACTIVE_SLOW;
                    }
                }
                case 1 -> {
                    if(isActive){
                        animationToPlay = ACTIVE_MODERATE;
                    }
                    else{
                        animationToPlay = MODERATE;
                    }
                }
                case 2 -> {
                    if(isActive){
                        animationToPlay = ACTIVE_FAST;
                    }
                    else{
                        animationToPlay = FAST;
                    }
                }
                case 3 -> {
                    if(isActive){
                        animationToPlay = ACTIVE_HYPER;
                    }
                    else{
                        animationToPlay = HYPER;
                    }
                }
            }
        }

        if(animation == null || !animation.equals(animationToPlay)){
            controller.setAnimation(animationToPlay);
        }

        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    //-----------PASSIVE BEHAVIOR-----------//

    public static <T> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {

        if(blockEntity instanceof ExperienceFountainEntity fountain && fountain.isBound){

            BlockEntity boundEntity = level.getBlockEntity(fountain.getBoundPos());

            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();

            List<Player> playerList = level.getEntitiesOfClass(Player.class, new AABB(
                    x + 0.25,y + 0.5,z + 0.25,x + 0.75,y + 1.065,z + 0.75));

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
                    orb.setDeltaMovement(0,0.20 + 0.10 * Math.random(),0);
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

    public String toName(int activityState, boolean isActive){

        switch(activityState){
            case 0 -> {
                return isActive ? "active-slow" : "slow";
            }
            case 1 -> {
                return isActive ? "active-moderate" : "moderate";
            }
            case 2 -> {
                return isActive ? "active-fast" : "fast";
            }
            case 3 -> {
                return isActive ? "active-hyperspeed" : "hyperspeed";
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
