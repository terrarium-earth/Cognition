package com.cyanogen.cognition.item;

import com.cyanogen.cognition.utils.ItemUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class ActivatableItem extends Item {

    public ActivatableItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {

        ItemStack stack = new ItemStack(this);
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("isActive", false);

        ItemUtils.saveCustomDataTag(stack, tag);

        return stack;
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return ItemUtils.getCustomDataTag(stack).getBoolean("isActive");
    }

    public void playActivationSound(Player player){

    }

    public void playDeactivationSound(Player player){

    }

    public boolean isActive(ItemStack stack){
        CompoundTag tag = ItemUtils.getCustomDataTag(stack);
        return tag.getBoolean("isActive");
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);
        CompoundTag tag = ItemUtils.getCustomDataTag(stack);

        if(player.isShiftKeyDown()){
            boolean isActive = tag.getBoolean("isActive");
            if(isActive){
                tag.putBoolean("isActive", false);
                playDeactivationSound(player);
            }
            else{
                tag.putBoolean("isActive", true);
                playActivationSound(player);
            }
            ItemUtils.saveCustomDataTag(stack, tag);
        }

        return super.use(level, player, hand);
    }

    //-----CUSTOM HOVER TEXT-----//

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

        boolean isActive = ItemUtils.getCustomDataTag(stack).getBoolean("isActive");

        if(isActive){
            tooltipComponents.add(Component.translatable("tooltip.cognition.activatable_item.active"));
        }
        else{
            tooltipComponents.add(Component.translatable("tooltip.cognition.activatable_item.inactive"));
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

}
