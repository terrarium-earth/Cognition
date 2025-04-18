package com.cyanogen.experienceobelisk.registries;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.cyanogen.experienceobelisk.block_entities.*;
import com.cyanogen.experienceobelisk.block_entities.bibliophage.agar.ExtravagantAgarEntity;
import com.cyanogen.experienceobelisk.block_entities.bibliophage.agar.FluorescentAgarEntity;
import com.cyanogen.experienceobelisk.block_entities.bibliophage.agar.InsightfulAgarEntity;
import com.cyanogen.experienceobelisk.block_entities.bibliophage.agar.NutrientAgarEntity;
import com.cyanogen.experienceobelisk.block_entities.bibliophage.bookshelves.InfectedArchiversBookshelfEntity;
import com.cyanogen.experienceobelisk.block_entities.bibliophage.bookshelves.InfectedBookshelfEntity;
import com.cyanogen.experienceobelisk.block_entities.bibliophage.bookshelves.InfectedEnchantedBookshelfEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RegisterBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, ExperienceObelisk.MOD_ID);

    private static com.mojang.datafixers.types.Type<?> Type;

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ExperienceObeliskEntity>> EXPERIENCE_OBELISK =
            BLOCK_ENTITIES.register("experience_obelisk",
            ()-> BlockEntityType.Builder.of(ExperienceObeliskEntity::new, RegisterBlocks.EXPERIENCE_OBELISK.get()).build(Type));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ExperienceFountainEntity>> EXPERIENCE_FOUNTAIN =
            BLOCK_ENTITIES.register("experience_fountain",
            ()-> BlockEntityType.Builder.of(ExperienceFountainEntity::new, RegisterBlocks.EXPERIENCE_FOUNTAIN.get()).build(Type));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PrecisionDispellerEntity>> PRECISION_DISPELLER =
            BLOCK_ENTITIES.register("precision_dispeller",
            ()-> BlockEntityType.Builder.of(PrecisionDispellerEntity::new, RegisterBlocks.PRECISION_DISPELLER.get()).build(Type));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AcceleratorEntity>> ACCELERATOR =
            BLOCK_ENTITIES.register("accelerator",
            ()-> BlockEntityType.Builder.of(AcceleratorEntity::new, RegisterBlocks.ACCELERATOR.get()).build(Type));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LinearAcceleratorEntity>> LINEAR_ACCELERATOR =
            BLOCK_ENTITIES.register("linear_accelerator",
            ()-> BlockEntityType.Builder.of(LinearAcceleratorEntity::new, RegisterBlocks.LINEAR_ACCELERATOR.get()).build(Type));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<InfectedBookshelfEntity>> INFECTED_BOOKSHELF =
            BLOCK_ENTITIES.register("infected_bookshelf",
            ()-> BlockEntityType.Builder.of(InfectedBookshelfEntity::new, RegisterBlocks.INFECTED_BOOKSHELF.get()).build(Type));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<InfectedEnchantedBookshelfEntity>> INFECTED_ENCHANTED_BOOKSHELF =
            BLOCK_ENTITIES.register("infected_enchanted_bookshelf",
            ()-> BlockEntityType.Builder.of(InfectedEnchantedBookshelfEntity::new, RegisterBlocks.INFECTED_ENCHANTED_BOOKSHELF.get()).build(Type));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<InfectedArchiversBookshelfEntity>> INFECTED_ARCHIVERS_BOOKSHELF =
            BLOCK_ENTITIES.register("infected_archivers_bookshelf",
            ()-> BlockEntityType.Builder.of(InfectedArchiversBookshelfEntity::new, RegisterBlocks.INFECTED_ARCHIVERS_BOOKSHELF.get()).build(Type));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MolecularMetamorpherEntity>> MOLECULAR_METAMORPHER =
            BLOCK_ENTITIES.register("molecular_metamorpher",
            ()-> BlockEntityType.Builder.of(MolecularMetamorpherEntity::new, RegisterBlocks.MOLECULAR_METAMORPHER.get()).build(Type));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FluorescentAgarEntity>> FLUORESCENT_AGAR =
            BLOCK_ENTITIES.register("fluorescent_agar",
            ()-> BlockEntityType.Builder.of(FluorescentAgarEntity::new, RegisterBlocks.FLUORESCENT_AGAR.get()).build(Type));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<NutrientAgarEntity>> NUTRIENT_AGAR =
            BLOCK_ENTITIES.register("nutrient_agar",
            ()-> BlockEntityType.Builder.of(NutrientAgarEntity::new, RegisterBlocks.NUTRIENT_AGAR.get()).build(Type));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<InsightfulAgarEntity>> INSIGHTFUL_AGAR =
            BLOCK_ENTITIES.register("insightful_agar",
            ()-> BlockEntityType.Builder.of(InsightfulAgarEntity::new, RegisterBlocks.INSIGHTFUL_AGAR.get()).build(Type));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ExtravagantAgarEntity>> EXTRAVAGANT_AGAR =
            BLOCK_ENTITIES.register("extravagant_agar",
            ()-> BlockEntityType.Builder.of(ExtravagantAgarEntity::new, RegisterBlocks.EXTRAVAGANT_AGAR.get()).build(Type));

    public static void register(IEventBus eventBus){
        BLOCK_ENTITIES.register(eventBus);
    }

}
