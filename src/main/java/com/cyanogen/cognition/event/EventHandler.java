package com.cyanogen.cognition.event;

import com.cyanogen.cognition.item.FortuitousAmuletItem;
import com.cyanogen.cognition.item.NeurogelMendingItem;
import com.cyanogen.cognition.item.MemoryTabletItem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.ItemStackedOnOtherEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;
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

    @SubscribeEvent
    public void onLivingDropExperience(LivingExperienceDropEvent event){
        MemoryTabletItem.handleExperience(event);
        FortuitousAmuletItem.handleExperience(event);
    }

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event){
        MemoryTabletItem.handleDeath(event);
    }

}
