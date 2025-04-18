package com.cyanogen.experienceobelisk.item;

import com.cyanogen.experienceobelisk.renderer.ExperienceObeliskItemRenderer;
import com.cyanogen.experienceobelisk.utils.ItemUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;

import static com.cyanogen.experienceobelisk.utils.ExperienceUtils.xpToLevels;


public class ExperienceObeliskItem extends BlockItem implements GeoItem{

    public ExperienceObeliskItem(Block block, Properties p) {
        super(block, p);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    //-----ANIMATIONS-----//

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    protected static final RawAnimation IDLE = RawAnimation.begin().thenPlay("idle");

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, this::controller));
    }

    protected <E extends ExperienceObeliskItem> PlayState controller(final AnimationState<E> state) {

        AnimationController<E> controller = state.getController();
        controller.setAnimation(IDLE);

        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {

        consumer.accept(new GeoRenderProvider() {
            private final BlockEntityWithoutLevelRenderer renderer = new ExperienceObeliskItemRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                return renderer;
            }
        });
    }

    //-----CUSTOM HOVER TEXT-----//


    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

        CompoundTag tag = ItemUtils.getBlockEntityTag(stack);

        if(tag.contains("Amount")){
            int amount = tag.getInt("Amount");
            int levels = xpToLevels(amount / 20);

            tooltipComponents.add(Component.translatable("tooltip.experienceobelisk.experience_obelisk.item_levels",
                    Component.literal(String.valueOf(levels)).withStyle(ChatFormatting.GREEN)));

            tooltipComponents.add(Component.translatable("tooltip.experienceobelisk.experience_obelisk.item_points",
                    Component.literal(String.valueOf(amount / 20)).withStyle(ChatFormatting.GREEN)));
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

}
