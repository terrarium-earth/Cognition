package com.cyanogen.cognition.registries;

import com.cyanogen.cognition.Cognition;
import com.cyanogen.cognition.loot_modifiers.AddSingleItem;
import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class RegisterLootModifiers {

    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> GLMS =
            DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Cognition.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<AddSingleItem>> ADD_SINGLE_ITEM
            = GLMS.register("add_single_item", () -> AddSingleItem.CODEC);

    public static void register(IEventBus eventBus){
        GLMS.register(eventBus);
    }

}
