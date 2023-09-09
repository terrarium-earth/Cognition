package com.cyanogen.experienceobelisk.event;

import com.cyanogen.experienceobelisk.enchantment.AthenaBlessingEnchant;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandler {

    public AttributeModifier reachModifier =  new AttributeModifier("experienceobelisk:reach_modifier",1, AttributeModifier.Operation.ADDITION);

    @SubscribeEvent
    public void playerDestroyItemEvent(PlayerDestroyItemEvent event){
        AthenaBlessingEnchant.itemDestroyed(event);
    }

    @SubscribeEvent
    public void itemAttributeModifierEvent(ItemAttributeModifierEvent event){
        ItemStack stack = event.getItemStack();
        EquipmentSlot slot = event.getSlotType();
        String registryName = stack.getItem().getRegistryName().toString();
        boolean isCognitiveItem = stack.isDamageableItem() && registryName.contains("experienceobelisk:cognitive_");

        if(isCognitiveItem && slot.equals(EquipmentSlot.MAINHAND) && event.getOriginalModifiers().containsKey(Attributes.ATTACK_SPEED)){
            event.addModifier(ForgeMod.REACH_DISTANCE.get(), reachModifier);
        }
    }

}
