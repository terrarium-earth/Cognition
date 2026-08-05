package com.cyanogen.cognition.block_entities.void_garden;

import com.cyanogen.cognition.registries.RegisterBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class ResonanceClusterEntity extends AbstractClusterEntity {

    public ResonanceClusterEntity(BlockPos pos, BlockState blockState) {
        super(RegisterBlockEntities.RESONANCE_CLUSTER.get(), pos, blockState, true);
    }





}
