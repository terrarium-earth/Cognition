package com.cyanogen.experienceobelisk.registries;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegisterSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ExperienceObelisk.MOD_ID);

    public static final RegistryObject<SoundEvent> ENLIGHTENED_AMULET_ACTIVATE = registerSound("enlightened_amulet_activate", 4);
    public static final RegistryObject<SoundEvent> ENLIGHTENED_AMULET_DEACTIVATE = registerSound("enlightened_amulet_deactivate", 4);
    public static final RegistryObject<SoundEvent> METAMORPHER_BUSY1 = registerSound("metamorpher_busy1", 3);
    public static final RegistryObject<SoundEvent> METAMORPHER_BUSY2 = registerSound("metamorpher_busy2", 3);
    public static final RegistryObject<SoundEvent> NEUROGEL_APPLY = registerSound("neurogel_apply", 4);
    public static final RegistryObject<SoundEvent> FLUORESCENT_AGAR_INFECT = registerVariableSound("fluorescent_agar_infect");
    public static final RegistryObject<SoundEvent> FLASK_EMPTY_LAVA = registerSound("flask_empty_lava", 4);
    public static final RegistryObject<SoundEvent> FLASK_EMPTY_WATER = registerSound("flask_empty_water", 4);
    public static final RegistryObject<SoundEvent> FLASK_FILL_VOID = registerSound("flask_fill_void", 4);

    public static RegistryObject<SoundEvent> registerSound(String soundName, float range){
        return SOUNDS.register(soundName, () -> SoundEvent.createFixedRangeEvent(new ResourceLocation(ExperienceObelisk.MOD_ID, soundName), range));
    }

    public static RegistryObject<SoundEvent> registerVariableSound(String soundName){
        return SOUNDS.register(soundName, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ExperienceObelisk.MOD_ID, soundName)));
    }

    public static void register(IEventBus eventBus){
        SOUNDS.register(eventBus);
    }
}
