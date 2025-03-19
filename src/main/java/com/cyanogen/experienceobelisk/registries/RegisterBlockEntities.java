package com.cyanogen.experienceobelisk.registries;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.cyanogen.experienceobelisk.block_entities.*;
import com.cyanogen.experienceobelisk.block_entities.bibliophage.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegisterBlockEntities {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ExperienceObelisk.MOD_ID);

    private static com.mojang.datafixers.types.Type<?> Type;

    public static final RegistryObject<BlockEntityType<ExperienceObeliskEntity>> EXPERIENCE_OBELISK_BE = BLOCK_ENTITIES.register("experienceobelisk_be",
            ()-> BlockEntityType.Builder.of(ExperienceObeliskEntity::new, RegisterBlocks.EXPERIENCE_OBELISK.get()).build(Type));

    public static final RegistryObject<BlockEntityType<ExperienceFountainEntity>> EXPERIENCE_FOUNTAIN_BE = BLOCK_ENTITIES.register("experiencefountain_be",
            ()-> BlockEntityType.Builder.of(ExperienceFountainEntity::new, RegisterBlocks.EXPERIENCE_FOUNTAIN.get()).build(Type));

    public static final RegistryObject<BlockEntityType<PrecisionDispellerEntity>> PRECISION_DISPELLER_BE = BLOCK_ENTITIES.register("precisiondispeller_be",
            ()-> BlockEntityType.Builder.of(PrecisionDispellerEntity::new, RegisterBlocks.PRECISION_DISPELLER.get()).build(Type));

    public static final RegistryObject<BlockEntityType<AcceleratorEntity>> ACCELERATOR_BE = BLOCK_ENTITIES.register("accelerator_be",
            ()-> BlockEntityType.Builder.of(AcceleratorEntity::new, RegisterBlocks.ACCELERATOR.get()).build(Type));

    public static final RegistryObject<BlockEntityType<LinearAcceleratorEntity>> LINEAR_ACCELERATOR_BE = BLOCK_ENTITIES.register("linearaccelerator_be",
            ()-> BlockEntityType.Builder.of(LinearAcceleratorEntity::new, RegisterBlocks.LINEAR_ACCELERATOR.get()).build(Type));

    public static final RegistryObject<BlockEntityType<InfectedBookshelfEntity>> INFECTED_BOOKSHELF_BE = BLOCK_ENTITIES.register("infectedbookshelf_be",
            ()-> BlockEntityType.Builder.of(InfectedBookshelfEntity::new, RegisterBlocks.INFECTED_BOOKSHELF.get()).build(Type));

    public static final RegistryObject<BlockEntityType<InfectedEnchantedBookshelfEntity>> INFECTED_ENCHANTED_BOOKSHELF_BE = BLOCK_ENTITIES.register("infectedenchantedbookshelf_be",
            ()-> BlockEntityType.Builder.of(InfectedEnchantedBookshelfEntity::new, RegisterBlocks.INFECTED_ENCHANTED_BOOKSHELF.get()).build(Type));

    public static final RegistryObject<BlockEntityType<InfectedArchiversBookshelfEntity>> INFECTED_ARCHIVERS_BOOKSHELF_BE = BLOCK_ENTITIES.register("infectedarchiversbookshelf_be",
            ()-> BlockEntityType.Builder.of(InfectedArchiversBookshelfEntity::new, RegisterBlocks.INFECTED_ARCHIVERS_BOOKSHELF.get()).build(Type));

    public static final RegistryObject<BlockEntityType<MolecularMetamorpherEntity>> MOLECULAR_METAMORPHER_BE = BLOCK_ENTITIES.register("molecularmetamorpher_be",
            ()-> BlockEntityType.Builder.of(MolecularMetamorpherEntity::new, RegisterBlocks.MOLECULAR_METAMORPHER.get()).build(Type));

    public static final RegistryObject<BlockEntityType<FluorescentAgarEntity>> FLUORESCENT_AGAR_BE = BLOCK_ENTITIES.register("fluorescentagar_be",
            ()-> BlockEntityType.Builder.of(FluorescentAgarEntity::new, RegisterBlocks.FLUORESCENT_AGAR.get()).build(Type));

    public static final RegistryObject<BlockEntityType<NutrientAgarEntity>> NUTRIENT_AGAR_BE = BLOCK_ENTITIES.register("nutrientagar_be",
            ()-> BlockEntityType.Builder.of(NutrientAgarEntity::new, RegisterBlocks.NUTRIENT_AGAR.get()).build(Type));

    public static final RegistryObject<BlockEntityType<InsightfulAgarEntity>> INSIGHTFUL_AGAR_BE = BLOCK_ENTITIES.register("insightfulagar_be",
            ()-> BlockEntityType.Builder.of(InsightfulAgarEntity::new, RegisterBlocks.INSIGHTFUL_AGAR.get()).build(Type));

    public static final RegistryObject<BlockEntityType<ExtravagantAgarEntity>> EXTRAVAGANT_AGAR_BE = BLOCK_ENTITIES.register("extravagantagar_be",
            ()-> BlockEntityType.Builder.of(ExtravagantAgarEntity::new, RegisterBlocks.EXTRAVAGANT_AGAR.get()).build(Type));

    public static void register(IEventBus eventBus){
        BLOCK_ENTITIES.register(eventBus);
    }

}
