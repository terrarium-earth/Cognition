package com.cyanogen.experienceobelisk.item;

import com.cyanogen.experienceobelisk.registries.RegisterItems;
import com.cyanogen.experienceobelisk.registries.RegisterSounds;
import com.cyanogen.experienceobelisk.utils.MiscUtils;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ItemStackedOnOtherEvent;

public class NeurogelMendingItem extends Item {

    public NeurogelMendingItem(Properties p) {
        super(p);
    }

    final static int repairAmount = 200;

    public static void handleItem(ItemStackedOnOtherEvent event){
        ItemStack itemToRepair = event.getCarriedItem();
        ItemStack stackedOn = event.getStackedOnItem();
        Player player = event.getPlayer();

        if(stackedOn.is(RegisterItems.MENDING_NEUROGEL.get()) && itemToRepair.isDamaged()){
            int damage = itemToRepair.getDamageValue();
            stackedOn.shrink(1);
            itemToRepair.setDamageValue(Math.max(damage - repairAmount, 0));
            player.playSound(RegisterSounds.NEUROGEL_APPLY.get(), 0.5f, MiscUtils.randomInRange(0.8f, 1.2f)); //volume, pitch

            event.setCanceled(true);
        }

    }

    //Repairs items by 250 durability points when applied
    //Can be applied to items you wouldn't be able to otherwise mend, such as those with conflicting enchants, or those that don't accept mending
}
