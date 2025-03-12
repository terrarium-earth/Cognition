package com.cyanogen.experienceobelisk.block_entities.bibliophage;

import com.cyanogen.experienceobelisk.registries.RegisterBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class InsightfulAgarEntity extends AbstractAgarEntity {

    public InsightfulAgarEntity(BlockPos pos, BlockState state) {
        super(RegisterBlockEntities.INSIGHTFUL_AGAR_BE.get(), pos, state);
        infectivity = 0.01;
    }

}
