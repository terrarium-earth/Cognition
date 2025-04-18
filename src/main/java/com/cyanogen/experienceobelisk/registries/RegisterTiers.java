package com.cyanogen.experienceobelisk.registries;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.cyanogen.experienceobelisk.item.CognitiveArmorset;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ArmorMaterial;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RegisterTiers {

    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(BuiltInRegistries.ARMOR_MATERIAL, ExperienceObelisk.MOD_ID);
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> COGNITIVE_ARMOR_MATERIAL =
            ARMOR_MATERIALS.register("cognitive", () -> CognitiveArmorset.COGNITIVE_ARMOR_MATERIAL);

    public static void register(IEventBus eventBus){
        ARMOR_MATERIALS.register(eventBus);
    }
}
