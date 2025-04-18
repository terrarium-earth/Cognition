package com.cyanogen.experienceobelisk.block_entities.bibliophage.agar;

import com.cyanogen.experienceobelisk.registries.RegisterBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class InsightfulAgarEntity extends AbstractAgarEntity {

    public InsightfulAgarEntity(BlockPos pos, BlockState state) {
        super(RegisterBlockEntities.INSIGHTFUL_AGAR.get(), pos, state);
        infectivity = 0.01;
    }

}
