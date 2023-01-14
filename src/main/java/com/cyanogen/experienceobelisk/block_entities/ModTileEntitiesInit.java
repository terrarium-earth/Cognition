package com.cyanogen.experienceobelisk.block_entities;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.cyanogen.experienceobelisk.block.ModBlocksInit;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModTileEntitiesInit {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, ExperienceObelisk.MOD_ID);

    //registering a new block entity, binding it to EXPERIENCE_OBELISK

    public static final RegistryObject<BlockEntityType<XPObeliskEntity>> XPOBELISK_BE = BLOCK_ENTITIES.register("experienceobelisk_be",
            ()-> BlockEntityType.Builder.of(XPObeliskEntity::new, ModBlocksInit.EXPERIENCE_OBELISK.get()).build(null));


    public static void register(IEventBus eventBus){
        BLOCK_ENTITIES.register(eventBus);
    }


}
