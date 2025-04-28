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

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<BlockPos>> RECOLLECTION_FOCUS_OBELISK_LOCATION =
            ATTACHMENTS.register("recollection_focus_obelisk_location",
            () -> AttachmentType.builder(()->new BlockPos(0,0,0)).serialize(BlockPos.CODEC).copyOnDeath().build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> PLAYER_EXPERIENCE_LEVELS_ON_DEATH =
            ATTACHMENTS.register("player_experience_on_death",
                    () -> AttachmentType.builder(()-> 0).serialize(Codec.INT).copyOnDeath().build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Float>> PLAYER_EXPERIENCE_PROGRESS_ON_DEATH =
            ATTACHMENTS.register("player_experience_on_death",
                    () -> AttachmentType.builder(()-> 0.0f).serialize(Codec.FLOAT).copyOnDeath().build());

    public static void register(IEventBus eventBus){
        ATTACHMENTS.register(eventBus);
    }

}
