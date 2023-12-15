package com.cyanogen.experienceobelisk.item;

import com.cyanogen.experienceobelisk.gui.ExperienceObeliskScreen;
import com.cyanogen.experienceobelisk.renderer.ExperienceObeliskItemRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;


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
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            private final BlockEntityWithoutLevelRenderer renderer = new ExperienceObeliskItemRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }
        });
    }

    //-----CUSTOM HOVER TEXT-----//

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {

        int amount = stack.getOrCreateTag().getCompound("BlockEntityTag").getInt("Amount");
        int levels = ExperienceObeliskScreen.xpToLevels(amount / 20);

        tooltip.add(Component.translatable("tooltip.experienceobelisk.experience_obelisk.item_fluid_amount",
                Component.literal(amount + " mB").withStyle(ChatFormatting.GOLD)));

        tooltip.add(Component.translatable("tooltip.experienceobelisk.experience_obelisk.item_levels",
                Component.literal(String.valueOf(levels)).withStyle(ChatFormatting.GREEN)));

        super.appendHoverText(stack, level, tooltip, flag);

    }

}
