package com.cyanogen.experienceobelisk.event;

import com.cyanogen.experienceobelisk.enchantment.AthenaBlessingEnchant;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EventHandler {

    @SubscribeEvent
    public void playerDestroyItemEvent(PlayerDestroyItemEvent event){
        AthenaBlessingEnchant.itemDestroyed(event);
    }


}
