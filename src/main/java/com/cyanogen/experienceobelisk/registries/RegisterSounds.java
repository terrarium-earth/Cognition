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

    public static final RegistryObject<SoundEvent> ENLIGHTENED_AMULET_ACTIVATE = registerSound("enlightened_amulet_activate");

    public static final RegistryObject<SoundEvent> ENLIGHTENED_AMULET_DEACTIVATE = registerSound("enlightened_amulet_deactivate");

    public static RegistryObject<SoundEvent> registerSound(String soundName){
        return SOUNDS.register(soundName, () -> SoundEvent.createFixedRangeEvent(new ResourceLocation(ExperienceObelisk.MOD_ID, soundName), 4));
    }

    public static void register(IEventBus eventBus){
        SOUNDS.register(eventBus);
    }
}
