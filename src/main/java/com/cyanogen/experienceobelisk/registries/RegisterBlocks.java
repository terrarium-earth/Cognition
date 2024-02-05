package com.cyanogen.experienceobelisk.registries;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.cyanogen.experienceobelisk.block.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
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
    public static final RegistryObject<Block> ETERNAL_ANVIL = BLOCKS.register("eternal_anvil", EternalAnvilBlock::new);

    //-----DECORATIVE / CRAFTING-----//

    public static final RegistryObject<Block> COGNITIVE_ALLOY_BLOCK = BLOCKS.register("cognitive_alloy_block", CognitiveAlloyBlock::new);
    public static final RegistryObject<Block> COGNITIVE_CRYSTAL_BLOCK = BLOCKS.register("cognitive_crystal_block", CognitiveCrystalBlock::new);

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
