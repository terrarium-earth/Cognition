package com.cyanogen.experienceobelisk.event;

import com.cyanogen.experienceobelisk.enchantment.AthenaBlessingEnchant;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandler {

    @SubscribeEvent
    public void playerDestroyItemEvent(PlayerDestroyItemEvent event){
        AthenaBlessingEnchant.itemDestroyed(event);
    }



}
