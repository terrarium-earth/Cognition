package com.cyanogen.cognition.registries;

import com.cyanogen.cognition.Cognition;
import com.mojang.serialization.Codec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class RegisterAttachments {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Cognition.MOD_ID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> HAS_LINKED_OBELISK =
            ATTACHMENTS.register("player_has_linked_obelisk",
            () -> AttachmentType.builder(()->false).serialize(Codec.BOOL).copyOnDeath().build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> PLAYER_EXPERIENCE_LEVELS_UPON_DEATH =
            ATTACHMENTS.register("player_experience_levels_upon_death",
                    () -> AttachmentType.builder(()-> 0).serialize(Codec.INT).copyOnDeath().build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Float>> PLAYER_EXPERIENCE_PROGRESS_UPON_DEATH =
            ATTACHMENTS.register("player_experience_progress_upon_death",
                    () -> AttachmentType.builder(()-> 0.0f).serialize(Codec.FLOAT).copyOnDeath().build());

    public static void register(IEventBus eventBus){
        ATTACHMENTS.register(eventBus);
    }

}
