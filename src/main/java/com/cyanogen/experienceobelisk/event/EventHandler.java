package com.cyanogen.experienceobelisk.event;

import com.cyanogen.experienceobelisk.block.ExperienceFountainBlock;
import com.cyanogen.experienceobelisk.item.CognitiveBowItem;
import com.cyanogen.experienceobelisk.item.FortuitousAmuletItem;
import com.cyanogen.experienceobelisk.item.MemoryTabletItem;
import com.cyanogen.experienceobelisk.item.NeurogelMendingItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ItemStackedOnOtherEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

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
