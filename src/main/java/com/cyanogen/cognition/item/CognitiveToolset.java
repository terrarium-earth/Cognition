package com.cyanogen.cognition.item;

import com.cyanogen.cognition.registries.RegisterItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CognitiveToolset {

    //-----TOOLSET-----//

    public static final AttributeModifier HANDHELD_RANGE =
            new AttributeModifier(ResourceLocation.fromNamespaceAndPath("cognition", "handheld_range"),
                    1.0, AttributeModifier.Operation.ADD_VALUE);

    public static List<ItemAttributeModifiers.Entry> increasedReach(){
        List<ItemAttributeModifiers.Entry> list = new ArrayList<>();
        list.add(new ItemAttributeModifiers.Entry(Attributes.BLOCK_INTERACTION_RANGE, HANDHELD_RANGE, EquipmentSlotGroup.MAINHAND));
        list.add(new ItemAttributeModifiers.Entry(Attributes.ENTITY_INTERACTION_RANGE, HANDHELD_RANGE, EquipmentSlotGroup.MAINHAND));
        return list;
    }

    public static Item.Properties createCustomAttributes(Item.Properties properties, @Nullable ItemAttributeModifiers baseModifiers, List<ItemAttributeModifiers.Entry> extraModifiers){
        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
        if(baseModifiers != null){
            for(ItemAttributeModifiers.Entry entry : baseModifiers.modifiers()) {
                builder.add(entry.attribute(), entry.modifier(), entry.slot());
            }
        }
        for(ItemAttributeModifiers.Entry entry : extraModifiers) {
            builder.add(entry.attribute(), entry.modifier(), entry.slot());
        }
        return properties.attributes(builder.build());
    }

    public static Tier COGNITIVE_TIER = new Tier() {

        @Override
        public TagKey<Block> getIncorrectBlocksForDrops() {
            return BlockTags.INCORRECT_FOR_IRON_TOOL;
        }

        @Override
        public int getUses() {
            return 921;
        }

        @Override
        public float getSpeed() {
            return 7.0F;
        }

        @Override
        public float getAttackDamageBonus() {
            return 3.0F;
        }

        @Override
        public int getEnchantmentValue() {
            return 15;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(RegisterItems.COGNITIVE_ALLOY.get());
        }
    };

}
