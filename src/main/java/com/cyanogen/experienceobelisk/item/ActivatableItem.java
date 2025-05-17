package com.cyanogen.experienceobelisk.item;

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
        stack.getOrCreateTag().putBoolean("isActive", false);
        return stack;
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("isActive");
    }

    public void playActivationSound(Player player){

    }

    public void playDeactivationSound(Player player){

    }

    public boolean isActive(ItemStack stack){
        return stack.getOrCreateTag().getBoolean("isActive");
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);
        CompoundTag tag = stack.getOrCreateTag();

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
        }

        return super.use(level, player, hand);
    }

    //-----CUSTOM HOVER TEXT-----//

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {

        boolean isActive = stack.getOrCreateTag().getBoolean("isActive");

        if(isActive){
            tooltip.add(Component.translatable("tooltip.experienceobelisk.activatable_item.active"));
        }
        else{
            tooltip.add(Component.translatable("tooltip.experienceobelisk.activatable_item.inactive"));
        }

        super.appendHoverText(stack, level, tooltip, flag);

    }

}
