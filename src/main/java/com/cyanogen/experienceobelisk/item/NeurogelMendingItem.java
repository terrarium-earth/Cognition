package com.cyanogen.experienceobelisk.item;

import com.cyanogen.experienceobelisk.registries.RegisterItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ItemStackedOnOtherEvent;

public class NeurogelMendingItem extends Item {

    public NeurogelMendingItem(Properties p) {
        super(p);
    }

    final static int repairAmount = 250;

    public static void handleItem(ItemStackedOnOtherEvent event){
        ItemStack itemToRepair = event.getCarriedItem();
        ItemStack stackedOn = event.getStackedOnItem();

        if(stackedOn.is(RegisterItems.MENDING_NEUROGEL.get()) && itemToRepair.isDamaged()){
            int damage = itemToRepair.getDamageValue();
            stackedOn.shrink(1);
            itemToRepair.setDamageValue(Math.max(damage - repairAmount, 0));

            event.setCanceled(true);
        }

    }

    //Repairs items by 250 durability points when applied
    //Can be applied to items you wouldn't be able to otherwise mend, such as those with conflicting enchants, or those that don't accept mending
}
