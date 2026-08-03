package com.cyanogen.cognition.block_entities.void_garden;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractClusterEntity extends BlockEntity {

    public final boolean isResonance;

    public AbstractClusterEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, boolean isResonance) {
        super(type, pos, blockState);
        this.isResonance = isResonance;
    }

    public static <T> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {


    }
}
