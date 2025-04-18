package com.cyanogen.experienceobelisk.item;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cyanogen.experienceobelisk.item.CognitiveToolset.COGNITIVE_TIER;

public class CognitiveArmorset {

    public static final AttributeModifier ARMOR_RANGE =
            new AttributeModifier(ResourceLocation.fromNamespaceAndPath("experienceobelisk", "armor_range"),
                    0.5, AttributeModifier.Operation.ADD_VALUE);

    public static List<ItemAttributeModifiers.Entry> increasedReach(EquipmentSlotGroup slotGroup){
        List<ItemAttributeModifiers.Entry> list = new ArrayList<>();
        list.add(new ItemAttributeModifiers.Entry(Attributes.BLOCK_INTERACTION_RANGE, ARMOR_RANGE, slotGroup));
        list.add(new ItemAttributeModifiers.Entry(Attributes.ENTITY_INTERACTION_RANGE, ARMOR_RANGE, slotGroup));
        return list;
    }

    public static Map<ArmorItem.Type, Integer> getDefenseForSlot(){
        HashMap<ArmorItem.Type, Integer> map = new HashMap<>();
        map.put(ArmorItem.Type.HELMET, 3);
        map.put(ArmorItem.Type.CHESTPLATE, 7);
        map.put(ArmorItem.Type.LEGGINGS, 6);
        map.put(ArmorItem.Type.BOOTS, 2);
        return map;
    }

    public static ArmorMaterial COGNITIVE_ARMOR_MATERIAL = new ArmorMaterial(
            getDefenseForSlot(),
            15,
            SoundEvents.ARMOR_EQUIP_NETHERITE,
            () -> COGNITIVE_TIER.getRepairIngredient(),
            List.of(new ArmorMaterial.Layer(ResourceLocation.withDefaultNamespace("cognitive"))),
            1.0f,
            0);

    public static class ExtraAttributeArmorItem extends ArmorItem{

        private final List<ItemAttributeModifiers.Entry> extraModifiers;

        public ExtraAttributeArmorItem(Holder<ArmorMaterial> material, Type type, List<ItemAttributeModifiers.Entry> extraModifiers) {
            super(material, type, new Item.Properties());
            this.extraModifiers = extraModifiers;
        }

        @Override
        public ItemAttributeModifiers getDefaultAttributeModifiers() {
            List<ItemAttributeModifiers.Entry> fromSuper = super.getDefaultAttributeModifiers().modifiers();
            List<ItemAttributeModifiers.Entry> modifierList = new ArrayList<>(fromSuper);

            if(!fromSuper.isEmpty() && !extraModifiers.isEmpty() && !fromSuper.contains(extraModifiers.getFirst())){
                //make sure to check that extra modifiers haven't already been added
                modifierList.addAll(extraModifiers);
            }

            ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
            for(ItemAttributeModifiers.Entry entry : modifierList){
                builder.add(entry.attribute(), entry.modifier(), entry.slot());
            }
            return builder.build();
        }


    }


}
