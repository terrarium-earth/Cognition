package com.cyanogen.cognition.event;

import com.cyanogen.cognition.block.ExperienceFountainBlock;
import com.cyanogen.cognition.item.CognitiveBowItem;
import com.cyanogen.cognition.item.FortuitousAmuletItem;
import com.cyanogen.cognition.item.MemoryTabletItem;
import com.cyanogen.cognition.item.NeurogelMendingItem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.ItemStackedOnOtherEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;


public class EventHandler {

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onTooltip(ItemTooltipEvent event){
        DescriptionTooltips.handleTooltip(event);
        CognitiveBowItem.handleTooltip(event);
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

    @SubscribeEvent
    public void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event){
        ExperienceFountainBlock.handleExperienceItemStack(event);
    }

}
