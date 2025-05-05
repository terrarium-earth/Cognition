package com.cyanogen.cognition.registries;

import com.cyanogen.cognition.Cognition;
import com.cyanogen.cognition.block.*;
import com.cyanogen.cognition.block.bibliophage.BibliomancerBlock;
import com.cyanogen.cognition.block.bibliophage.agar.ExtravagantAgarBlock;
import com.cyanogen.cognition.block.bibliophage.agar.FluorescentAgarBlock;
import com.cyanogen.cognition.block.bibliophage.agar.InsightfulAgarBlock;
import com.cyanogen.cognition.block.bibliophage.agar.NutrientAgarBlock;
import com.cyanogen.cognition.block.bibliophage.bookshelves.BookshelfBlock;
import com.cyanogen.cognition.block.bibliophage.bookshelves.InfectedArchiversBookshelfBlock;
import com.cyanogen.cognition.block.bibliophage.bookshelves.InfectedBookshelfBlock;
import com.cyanogen.cognition.block.bibliophage.bookshelves.InfectedEnchantedBookshelfBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RegisterBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, Cognition.MOD_ID);

    //-----FUNCTIONAL BLOCKS-----//

    public static final DeferredHolder<Block, ExperienceObeliskBlock> EXPERIENCE_OBELISK = BLOCKS.register("experience_obelisk", ExperienceObeliskBlock::new);
    public static final DeferredHolder<Block, ExperienceFountainBlock> EXPERIENCE_FOUNTAIN = BLOCKS.register("experience_fountain", ExperienceFountainBlock::new);
    public static final DeferredHolder<Block, PrecisionDispellerBlock> PRECISION_DISPELLER = BLOCKS.register("precision_dispeller", PrecisionDispellerBlock::new);
    public static final DeferredHolder<Block, AcceleratorBlock> ACCELERATOR = BLOCKS.register("accelerator", AcceleratorBlock::new);
    public static final DeferredHolder<Block, LinearAcceleratorBlock> LINEAR_ACCELERATOR = BLOCKS.register("linear_accelerator", LinearAcceleratorBlock::new);
    public static final DeferredHolder<Block, InfectedBookshelfBlock> INFECTED_BOOKSHELF = BLOCKS.register("infected_bookshelf", () -> new InfectedBookshelfBlock(1.0f));
    public static final DeferredHolder<Block, InfectedEnchantedBookshelfBlock> INFECTED_ENCHANTED_BOOKSHELF = BLOCKS.register("infected_enchanted_bookshelf", InfectedEnchantedBookshelfBlock::new);
    public static final DeferredHolder<Block, InfectedArchiversBookshelfBlock> INFECTED_ARCHIVERS_BOOKSHELF = BLOCKS.register("infected_archivers_bookshelf", InfectedArchiversBookshelfBlock::new);
    public static final DeferredHolder<Block, MolecularMetamorpherBlock> MOLECULAR_METAMORPHER = BLOCKS.register("molecular_metamorpher", MolecularMetamorpherBlock::new);
    public static final DeferredHolder<Block, FluorescentAgarBlock> FLUORESCENT_AGAR = BLOCKS.register("fluorescent_agar", FluorescentAgarBlock::new);
    public static final DeferredHolder<Block, NutrientAgarBlock> NUTRIENT_AGAR = BLOCKS.register("nutrient_agar", NutrientAgarBlock::new);
    public static final DeferredHolder<Block, InsightfulAgarBlock> INSIGHTFUL_AGAR = BLOCKS.register("insightful_agar", InsightfulAgarBlock::new);
    public static final DeferredHolder<Block, ExtravagantAgarBlock> EXTRAVAGANT_AGAR = BLOCKS.register("extravagant_agar", ExtravagantAgarBlock::new);
    public static final DeferredHolder<Block, BibliomancerBlock> BIBLIOMANCER = BLOCKS.register("bibliomancer", BibliomancerBlock::new);

    //-----DECORATIVE / CRAFTING-----//

    public static final DeferredHolder<Block, CognitiveAlloyBlock> COGNITIVE_ALLOY_BLOCK = BLOCKS.register("cognitive_alloy_block", CognitiveAlloyBlock::new);
    public static final DeferredHolder<Block, CognitiveCrystalBlock> COGNITIVE_CRYSTAL_BLOCK = BLOCKS.register("cognitive_crystal_block", CognitiveCrystalBlock::new);
    public static final DeferredHolder<Block, WhisperglassBlock> WHISPERGLASS_BLOCK = BLOCKS.register("whisperglass", WhisperglassBlock::new);
    public static final DeferredHolder<Block, ForgottenDustBlock> FORGOTTEN_DUST_BLOCK = BLOCKS.register("forgotten_dust_block", ForgottenDustBlock::new);
    public static final DeferredHolder<Block, BookshelfBlock> ENCHANTED_BOOKSHELF = BLOCKS.register("enchanted_bookshelf", () -> new BookshelfBlock(2.0f));
    public static final DeferredHolder<Block, BookshelfBlock> ARCHIVERS_BOOKSHELF = BLOCKS.register("archivers_bookshelf", () -> new BookshelfBlock(1.5f));

    //-----FLUID BLOCKS-----//

    public static final DeferredHolder<Block, LiquidBlock> COGNITIUM = BLOCKS.register("cognitium",
            () -> new LiquidBlock(RegisterFluids.COGNITIUM_SOURCE.get(), BlockBehaviour.Properties.of()
                    .liquid()
                    .lightLevel((state) -> 10)
                    .replaceable()
                    .emissiveRendering((state, getter, pos) -> true)));

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }

}
