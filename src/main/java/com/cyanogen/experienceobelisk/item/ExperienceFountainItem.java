package com.cyanogen.experienceobelisk.item;

import com.cyanogen.experienceobelisk.renderer.ExperienceFountainItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class ExperienceFountainItem extends BlockItem implements GeoItem{

    public ExperienceFountainItem(Block block, Properties properties) {
        super(block, properties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    //-----ANIMATIONS-----//

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    protected static final RawAnimation CYCLE = RawAnimation.begin().thenPlay("cycle");

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, this::controller));
    }

    protected <E extends ExperienceFountainItem> PlayState controller(final AnimationState<E> state) {

        AnimationController<E> controller = state.getController();
        controller.setAnimation(CYCLE);

        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            private final BlockEntityWithoutLevelRenderer renderer = new ExperienceFountainItemRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }
        });
    }

}
