package com.cyanogen.cognition.block_entities.void_garden;

import com.cyanogen.cognition.registries.RegisterBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class InterferenceClusterEntity extends AbstractClusterEntity {

    public InterferenceClusterEntity(BlockPos pos, BlockState blockState) {
        super(RegisterBlockEntities.INTERFERENCE_CLUSTER.get(), pos, blockState, false);
    }

}
