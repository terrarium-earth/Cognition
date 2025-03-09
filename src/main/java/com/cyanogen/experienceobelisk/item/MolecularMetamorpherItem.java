package com.cyanogen.experienceobelisk.item;

import com.cyanogen.experienceobelisk.renderer.MolecularMetamorpherItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
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
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            private final BlockEntityWithoutLevelRenderer renderer = new MolecularMetamorpherItemRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }
        });
    }

    //-----CUSTOM HOVER TEXT-----//


    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {

        CompoundTag inputs = stack.getOrCreateTag().getCompound("BlockEntityTag").getCompound("Inputs");
        CompoundTag outputs = stack.getOrCreateTag().getCompound("BlockEntityTag").getCompound("Outputs");

        boolean isEmpty = inputs.getList("Items", 10).isEmpty() && outputs.getList("Items",10).isEmpty();
        //Each itemstack is a compound, which has an ID of 10. See net.minecraft.nbt.Tag

        if(!isEmpty){
            tooltip.add(Component.translatable("tooltip.experienceobelisk.molecular_metamorpher.has_contents"));
        }

        super.appendHoverText(stack, level, tooltip, flag);
    }
}
