package com.cyanogen.experienceobelisk.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeMod;

import static com.cyanogen.experienceobelisk.registries.RegisterItems.COGNITIVE;

public class CognitiveToolset {

    public static class CognitiveSwordItem extends SwordItem{

        AttributeModifier range = new AttributeModifier("experienceobelisk:range",1.0, AttributeModifier.Operation.ADDITION);

        public CognitiveSwordItem(Properties pProperties) {
            super(COGNITIVE, 3, -2.4f, pProperties);
        }

        @Override
        public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {

            Multimap<Attribute, AttributeModifier> multimap = super.getDefaultAttributeModifiers(slot);

            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(multimap);
            if(slot.equals(EquipmentSlot.MAINHAND) && !multimap.containsValue(range)){
                builder.put(ForgeMod.REACH_DISTANCE.get(), range);
            }
            return builder.build();
        }
    }

    public class CognitivePickaxeItem extends PickaxeItem {

        public CognitivePickaxeItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
            super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
        }
    }

    public class CognitiveShovelItem extends ShovelItem{

        public CognitiveShovelItem(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
            super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
        }
    }

    public class CognitiveAxeItem extends AxeItem{

        public CognitiveAxeItem(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
            super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
        }
    }

    public class CognitiveHoeItem extends HoeItem{

        public CognitiveHoeItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
            super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
        }
    }
}
