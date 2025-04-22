package com.cyanogen.experienceobelisk.event;

import com.cyanogen.experienceobelisk.item.NeurogelMendingItem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.ItemStackedOnOtherEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;


public class EventHandler {

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onTooltip(ItemTooltipEvent event){
        DescriptionTooltips.handleTooltip(event);
    }

    @SubscribeEvent
    public void onItemStackedOnOther(ItemStackedOnOtherEvent event){
        NeurogelMendingItem.handleItem(event);
    }

}
