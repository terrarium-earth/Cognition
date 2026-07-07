package com.cyanogen.cognition.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class ProtectionSalveItem extends Item {

    public ProtectionSalveItem(Properties properties) {
        super(properties);
    }


    public static void handleEntity(PlayerInteractEvent.EntityInteractSpecific event) {
        Entity entity = event.getTarget();
        Player player = event.getEntity();

        //"Owner" or "Trusted"

       // CompoundTag tag = new CompoundTag();
       // entity.save(tag);

    }
}
