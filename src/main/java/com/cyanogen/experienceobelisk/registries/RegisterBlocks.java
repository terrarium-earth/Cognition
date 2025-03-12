package com.cyanogen.experienceobelisk.registries;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.cyanogen.experienceobelisk.block.*;
import com.cyanogen.experienceobelisk.block.bibliophage.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class RegisterBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, ExperienceObelisk.MOD_ID);

    //-----FUNCTIONAL BLOCKS-----//

    public static final RegistryObject<Block> EXPERIENCE_OBELISK = BLOCKS.register("experience_obelisk", ExperienceObeliskBlock::new);
    public static final RegistryObject<Block> EXPERIENCE_FOUNTAIN = BLOCKS.register("experience_fountain", ExperienceFountainBlock::new);
    public static final RegistryObject<Block> PRECISION_DISPELLER = BLOCKS.register("precision_dispeller", PrecisionDispellerBlock::new);
    public static final RegistryObject<Block> ACCELERATOR = BLOCKS.register("accelerator", AcceleratorBlock::new);
    public static final RegistryObject<Block> LINEAR_ACCELERATOR = BLOCKS.register("linear_accelerator", LinearAcceleratorBlock::new);
    public static final RegistryObject<Block> INFECTED_BOOKSHELF = BLOCKS.register("infected_bookshelf", () -> new InfectedBookshelfBlock(1.0f));
    public static final RegistryObject<Block> INFECTED_ENCHANTED_BOOKSHELF = BLOCKS.register("infected_enchanted_bookshelf", InfectedEnchantedBookshelfBlock::new);
    public static final RegistryObject<Block> INFECTED_ARCHIVERS_BOOKSHELF = BLOCKS.register("infected_archivers_bookshelf", InfectedArchiversBookshelfBlock::new);
    public static final RegistryObject<Block> MOLECULAR_METAMORPHER = BLOCKS.register("molecular_metamorpher", MolecularMetamorpherBlock::new);
    public static final RegistryObject<Block> FLUORESCENT_AGAR = BLOCKS.register("fluorescent_agar", FluorescentAgarBlock::new);
    public static final RegistryObject<Block> NUTRIENT_AGAR = BLOCKS.register("nutrient_agar", NutrientAgarBlock::new);
    public static final RegistryObject<Block> INSIGHTFUL_AGAR = BLOCKS.register("insightful_agar", InsightfulAgarBlock::new);
    public static final RegistryObject<Block> EXTRAVAGANT_AGAR = BLOCKS.register("extravagant_agar", ExtravagantAgarBlock::new);

    //-----DECORATIVE / CRAFTING-----//

    public static final RegistryObject<Block> COGNITIVE_ALLOY_BLOCK = BLOCKS.register("cognitive_alloy_block", CognitiveAlloyBlock::new);
    public static final RegistryObject<Block> COGNITIVE_CRYSTAL_BLOCK = BLOCKS.register("cognitive_crystal_block", CognitiveCrystalBlock::new);
    public static final RegistryObject<Block> WHISPERGLASS_BLOCK = BLOCKS.register("whisperglass", WhisperglassBlock::new);
    public static final RegistryObject<Block> FORGOTTEN_DUST_BLOCK = BLOCKS.register("forgotten_dust_block", ForgottenDustBlock::new);
    public static final RegistryObject<Block> ENCHANTED_BOOKSHELF = BLOCKS.register("enchanted_bookshelf", () -> new BookshelfBlock(2.0f));
    public static final RegistryObject<Block> ARCHIVERS_BOOKSHELF = BLOCKS.register("archivers_bookshelf", () -> new BookshelfBlock(1.5f));

    //-----FLUID BLOCKS-----//

    public static final RegistryObject<LiquidBlock> COGNITIUM = BLOCKS.register("cognitium",
            () -> new LiquidBlock(RegisterFluids.COGNITIUM_FLOWING, BlockBehaviour.Properties.copy(Blocks.WATER)
                    .liquid()
                    .lightLevel(value -> 10)
                    .emissiveRendering((p_61036_, p_61037_, p_61038_) -> true)
            ));


    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }

}
