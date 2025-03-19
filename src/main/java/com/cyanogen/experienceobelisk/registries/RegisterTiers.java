package com.cyanogen.experienceobelisk.registries;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public class RegisterTiers {

    public static Tier COGNITIVE_TIER = new Tier() {
        @Override
        public int getUses() {
            return 835;
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
        public int getLevel() {
            return 2;
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

    public static ArmorMaterial COGNITIVE_ARMOR_MATERIAL = new ArmorMaterial() {

        @Override
        public int getDurabilityForSlot(EquipmentSlot slot) {
            return switch (slot.getName()){
                case "head" -> 280;
                case "chest" -> 420;
                case "legs" -> 370;
                case "feet" -> 250;
                default -> 0;
            };
        }

        @Override
        public int getDefenseForSlot(EquipmentSlot slot) {
            return switch (slot.getName()){
                case "head" -> 3;
                case "chest" -> 7;
                case "legs" -> 6;
                case "feet" -> 2;
                default -> 0;
            };
        }

        @Override
        public int getEnchantmentValue() {
            return 15;
        }

        @Override
        public SoundEvent getEquipSound() {
            return SoundEvents.ARMOR_EQUIP_NETHERITE;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(RegisterItems.COGNITIVE_ALLOY.get());
        }

        @Override
        public String getName() {
            return "cognitive";
        }

        @Override
        public float getToughness() {
            return 1.0f;
        }

        @Override
        public float getKnockbackResistance() {
            return 0;
        }
    };

}
