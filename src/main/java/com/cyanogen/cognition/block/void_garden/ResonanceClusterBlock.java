package com.cyanogen.cognition.block.void_garden;

import com.cyanogen.cognition.block_entities.void_garden.ResonanceClusterEntity;
import com.cyanogen.cognition.registries.RegisterBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ResonanceClusterBlock extends AbstractClusterBlock implements EntityBlock {

    public ResonanceClusterBlock() {
        super();
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return RegisterBlockEntities.RESONANCE_CLUSTER.get().create(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return blockEntityType == RegisterBlockEntities.RESONANCE_CLUSTER.get() ? ResonanceClusterEntity::tick : null;
    }
}
