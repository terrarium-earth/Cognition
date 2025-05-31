package com.cyanogen.experienceobelisk.event;

import com.cyanogen.experienceobelisk.item.CognitiveBowItem;
import com.cyanogen.experienceobelisk.item.FortuitousAmuletItem;
import com.cyanogen.experienceobelisk.item.MemoryTabletItem;
import com.cyanogen.experienceobelisk.item.NeurogelMendingItem;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterItemDecorationsEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.ItemStackedOnOtherEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

}
