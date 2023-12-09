package com.cyanogen.experienceobelisk.block_entities;

import com.cyanogen.experienceobelisk.registries.RegisterBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class PrecisionDispellerEntity extends ExperienceReceivingEntity implements GeoBlockEntity{

    public PrecisionDispellerEntity(BlockPos pos, BlockState state) {
        super(RegisterBlockEntities.PRECISIONDISPELLER_BE.get(), pos, state);
    }

    //-----------ANIMATIONS-----------//

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    protected static final RawAnimation USE = RawAnimation.begin().thenPlay("use").thenPlay("static");

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", state -> PlayState.CONTINUE).triggerableAnim("use", USE));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public void triggerUseAnimation(){
        triggerAnim("controller", "use");
    }

}
