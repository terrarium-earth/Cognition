package com.cyanogen.experienceobelisk.registries;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RegisterSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, ExperienceObelisk.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> ENLIGHTENED_AMULET_ACTIVATE = registerSound("enlightened_amulet_activate", 4);
    public static final DeferredHolder<SoundEvent, SoundEvent> ENLIGHTENED_AMULET_DEACTIVATE = registerSound("enlightened_amulet_deactivate", 4);
    public static final DeferredHolder<SoundEvent, SoundEvent> METAMORPHER_BUSY1 = registerSound("metamorpher_busy1", 3);
    public static final DeferredHolder<SoundEvent, SoundEvent> METAMORPHER_BUSY2 = registerSound("metamorpher_busy2", 3);
    public static final DeferredHolder<SoundEvent, SoundEvent> NEUROGEL_APPLY = registerSound("neurogel_apply", 4);
    public static final DeferredHolder<SoundEvent, SoundEvent> FLUORESCENT_AGAR_INFECT = registerVariableSound("fluorescent_agar_infect");
    public static final DeferredHolder<SoundEvent, SoundEvent> FLASK_EMPTY_LAVA = registerSound("flask_empty_lava", 4);
    public static final DeferredHolder<SoundEvent, SoundEvent> FLASK_EMPTY_WATER = registerSound("flask_empty_water", 4);
    public static final DeferredHolder<SoundEvent, SoundEvent> FLASK_FILL_VOID = registerSound("flask_fill_void", 4);

    public static DeferredHolder<SoundEvent, SoundEvent> registerSound(String soundName, float range){
        return SOUNDS.register(soundName, () -> SoundEvent.createFixedRangeEvent(ResourceLocation.fromNamespaceAndPath(ExperienceObelisk.MOD_ID, soundName), range));
    }

    public static DeferredHolder<SoundEvent, SoundEvent> registerVariableSound(String soundName){
        return SOUNDS.register(soundName, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ExperienceObelisk.MOD_ID, soundName)));
    }

    public static void register(IEventBus eventBus){
        SOUNDS.register(eventBus);
    }
}
