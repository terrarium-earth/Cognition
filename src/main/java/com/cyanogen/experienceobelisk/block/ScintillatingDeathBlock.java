package com.cyanogen.experienceobelisk.block;

import com.cyanogen.experienceobelisk.block_entities.ScintillatingDeathEntity;
import com.cyanogen.experienceobelisk.registries.RegisterBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ScintillatingDeathBlock extends Block implements EntityBlock {

    public ScintillatingDeathBlock() {
        super(Properties.of()
                .strength(9f)
                .destroyTime(1.2f)
                .requiresCorrectToolForDrops()
                .explosionResistance(9f)
                .noOcclusion()
                .lightLevel(value -> 7)
                .sound(SoundType.GLASS)
                .noCollission()
                .noOcclusion()
                .isSuffocating((state, getter, pos) -> false)
                .isValidSpawn((state, getter, pos, A) -> false)
        );
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return RegisterBlockEntities.SCINTILLATINGDEATH_BE.get().create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return blockEntityType == RegisterBlockEntities.SCINTILLATINGDEATH_BE.get() ? ScintillatingDeathEntity::tick : null;
    }
}
