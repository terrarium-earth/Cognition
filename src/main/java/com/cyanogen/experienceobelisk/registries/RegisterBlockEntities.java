package com.cyanogen.experienceobelisk.registries;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.cyanogen.experienceobelisk.block_entities.ExperienceFountainEntity;
import com.cyanogen.experienceobelisk.block_entities.ExperienceObeliskEntity;
import com.cyanogen.experienceobelisk.block_entities.PrecisionDispellerEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegisterBlockEntities {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, ExperienceObelisk.MOD_ID);

    private static com.mojang.datafixers.types.Type<?> Type;

    public static final RegistryObject<BlockEntityType<ExperienceObeliskEntity>> EXPERIENCEOBELISK_BE = BLOCK_ENTITIES.register("experienceobelisk_be",
            ()-> BlockEntityType.Builder.of(ExperienceObeliskEntity::new, RegisterBlocks.EXPERIENCE_OBELISK.get()).build(Type));

    public static final RegistryObject<BlockEntityType<ExperienceFountainEntity>> EXPERIENCEFOUNTAIN_BE = BLOCK_ENTITIES.register("experiencefountain_be",
            ()-> BlockEntityType.Builder.of(ExperienceFountainEntity::new, RegisterBlocks.EXPERIENCE_FOUNTAIN.get()).build(Type));

    public static final RegistryObject<BlockEntityType<PrecisionDispellerEntity>> PRECISIONDISPELLER_BE = BLOCK_ENTITIES.register("precisiondispeller_be",
            ()-> BlockEntityType.Builder.of(PrecisionDispellerEntity::new, RegisterBlocks.PRECISION_DISPELLER.get()).build(Type));


    public static void register(IEventBus eventBus){
        BLOCK_ENTITIES.register(eventBus);
    }

}
