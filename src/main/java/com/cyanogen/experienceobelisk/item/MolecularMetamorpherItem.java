package com.cyanogen.experienceobelisk.item;

import com.cyanogen.experienceobelisk.renderer.MolecularMetamorpherItemRenderer;
import com.cyanogen.experienceobelisk.utils.ItemUtils;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;

public class MolecularMetamorpherItem extends BlockItem implements GeoItem {

    public MolecularMetamorpherItem(Block block, Properties properties) {
        super(block, properties);
    }

    //-----ANIMATIONS-----//

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    protected static final RawAnimation IDLE = RawAnimation.begin().thenPlay("idle");

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, this::controller));

    }

    protected <E extends MolecularMetamorpherItem> PlayState controller(final AnimationState<E> state) {

        AnimationController<E> controller = state.getController();
        RawAnimation animation = controller.getCurrentRawAnimation();

        if(animation == null){
            controller.setAnimation(IDLE);
        }

        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {

        consumer.accept(new GeoRenderProvider() {
            private final BlockEntityWithoutLevelRenderer renderer = new MolecularMetamorpherItemRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                return renderer;
            }
        });
    }

    //-----CUSTOM HOVER TEXT-----//

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

        CompoundTag inputs = ItemUtils.getBlockEntityTag(stack).getCompound("Inputs");
        CompoundTag outputs = ItemUtils.getBlockEntityTag(stack).getCompound("Outputs");

        boolean isEmpty = inputs.getList("Items", 9).isEmpty() && outputs.getList("Items",9).isEmpty();

        if(!isEmpty){
            tooltipComponents.add(Component.translatable("tooltip.experienceobelisk.molecular_metamorpher.has_contents"));
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

}
