package com.cyanogen.experienceobelisk.registries;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.cyanogen.experienceobelisk.loot_modifiers.AddSingleItem;
import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegisterLootModifiers {

    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> GLMS =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, ExperienceObelisk.MOD_ID);

    public static final RegistryObject<Codec<AddSingleItem>> ADD_SINGLE_ITEM = GLMS.register("add_single_item", () -> AddSingleItem.CODEC);

    public static void register(IEventBus eventBus){
        GLMS.register(eventBus);
    }

}
