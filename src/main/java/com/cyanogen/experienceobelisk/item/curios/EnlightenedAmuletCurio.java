package com.cyanogen.experienceobelisk.item.curios;

import com.cyanogen.experienceobelisk.item.EnlightenedAmuletItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

public class EnlightenedAmuletCurio implements ICurioItem {

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if(stack.getItem() instanceof EnlightenedAmuletItem amulet){
            amulet.inventoryTick(stack, slotContext.entity().level(), slotContext.entity(), 1, false);
        }
    }

    @Override
    public List<Component> getSlotsTooltip(List<Component> tooltips, Item.TooltipContext context, ItemStack stack) {
        return ICurioItem.super.getSlotsTooltip(tooltips, context, stack);
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return slotContext.identifier().equals("necklace");
    }
}
