package com.cyanogen.cognition.block_entities.bibliophage.agar;

import com.cyanogen.cognition.config.Config;
import com.cyanogen.cognition.registries.RegisterBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class NutrientAgarEntity extends AbstractAgarEntity {

    public NutrientAgarEntity(BlockPos pos, BlockState state) {
        super(RegisterBlockEntities.NUTRIENT_AGAR.get(), pos, state,
                Config.COMMON.insightfulAgarInfectivity.get());
    }

}
