package com.cyanogen.experienceobelisk.event;

import com.cyanogen.experienceobelisk.item.EnlightenedAmuletItem;
import com.cyanogen.experienceobelisk.item.NeurogelMendingItem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.ItemStackedOnOtherEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import top.theillusivec4.curios.api.event.CurioCanEquipEvent;


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

    public static class CuriosEventHandler{

        @SubscribeEvent
        public void onCurioCanEquip(CurioCanEquipEvent event){
            EnlightenedAmuletItem.canEquip(event);
        }

    }

}
