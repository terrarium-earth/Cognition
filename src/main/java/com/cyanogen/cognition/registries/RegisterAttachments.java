package com.cyanogen.cognition.registries;

import com.cyanogen.cognition.Cognition;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class RegisterAttachments {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Cognition.MOD_ID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<BlockPos>> LINKED_OBELISK_POS =
            ATTACHMENTS.register("linked_obelisk_pos",
            () -> AttachmentType.builder(()-> BlockPos.ZERO).serialize(BlockPos.CODEC).copyOnDeath().build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Long>> EXPERIENCE_UPON_DEATH =
            ATTACHMENTS.register("experience_upon_death",
                    () -> AttachmentType.builder(()-> 0L).serialize(Codec.LONG).copyOnDeath().build());

    public static void register(IEventBus eventBus){
        ATTACHMENTS.register(eventBus);
    }

}
