package com.cyanogen.cognition.item;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.neoforged.neoforge.event.ItemStackedOnOtherEvent;

public class NeurogelMendingBlobItem extends NeurogelMendingItem{

    public NeurogelMendingBlobItem(Properties p) {
        super(p);
    }

    @Override
    public int maxRepairPoints() {
        return super.maxRepairPoints() / 4;
    }

    @Override
    public double maxRepairPercentage() {
        return super.maxRepairPercentage() / 4;
    }

    @Override
    public void handleAnvilItem(Slot slot, Player player, ItemStack stackedOn, ItemStack itemToRepair, ItemStackedOnOtherEvent event) {
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        return InteractionResult.PASS;
    }

}
