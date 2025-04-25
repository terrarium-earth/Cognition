package com.cyanogen.cognition.registries;

import com.cyanogen.cognition.Cognition;
import com.cyanogen.cognition.item.CognitiveArmorset;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ArmorMaterial;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RegisterTiers {

    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(BuiltInRegistries.ARMOR_MATERIAL, Cognition.MOD_ID);
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> COGNITIVE_ARMOR_MATERIAL =
            ARMOR_MATERIALS.register("cognitive", () -> CognitiveArmorset.COGNITIVE_ARMOR_MATERIAL);

    public static void register(IEventBus eventBus){
        ARMOR_MATERIALS.register(eventBus);
    }
}
