package com.cyanogen.experienceobelisk.enchantment;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEnchantmentsInit {

    public static final DeferredRegister<Enchantment> ENCHANTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, ExperienceObelisk.MOD_ID);

    public static final RegistryObject<Enchantment> ATHENA_BLESSING = ENCHANTS.register("athena_blessing", () -> new AthenaBlessingEnchant(
            Enchantment.Rarity.VERY_RARE,
            EnchantmentCategory.BREAKABLE,
            new EquipmentSlot[]{ EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND }
    ));

    public static void register(IEventBus eventBus){
        ENCHANTS.register(eventBus);
    }
}
