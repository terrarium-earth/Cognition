package com.cyanogen.experienceobelisk.block_entities;

import com.cyanogen.experienceobelisk.registries.RegisterBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class PrecisionDispellerEntity extends BlockEntity implements GeoBlockEntity{

    public boolean pendingAnimation = false;

    public PrecisionDispellerEntity(BlockPos pos, BlockState state) {
        super(RegisterBlockEntities.PRECISIONDISPELLER_BE.get(), pos, state);
    }

    //-----------ANIMATIONS-----------//

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    protected static final RawAnimation USE = RawAnimation.begin().thenPlay("use").thenLoop("static");

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, this::controller));
    }

    protected <E extends PrecisionDispellerEntity> PlayState controller(final AnimationState<E> state){

        AnimationController<E> controller = state.getController();
        controller.transitionLength(0);

        if(pendingAnimation){
            controller.stop();
            controller.setAnimation(USE);
            pendingAnimation = false;
        }

        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    //-----------NBT-----------//

    //sends CompoundTag out with nbt data
    @Override
    public CompoundTag getUpdateTag()
    {
        CompoundTag tag = super.getUpdateTag();
        tag.putBoolean("PendingAnimation", pendingAnimation);
        return tag;
    }

    //gets packet to send to client
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this, BlockEntity::getUpdateTag);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag tag = pkt.getTag();
        if(tag != null){
            this.pendingAnimation = tag.getBoolean("PendingAnimation");
        }
        super.onDataPacket(net, pkt);
    }
}
