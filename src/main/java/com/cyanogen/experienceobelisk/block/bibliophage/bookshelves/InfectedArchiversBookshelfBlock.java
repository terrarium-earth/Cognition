package com.cyanogen.experienceobelisk.block.bibliophage.bookshelves;

import com.cyanogen.experienceobelisk.block_entities.bibliophage.bookshelves.InfectedArchiversBookshelfEntity;
import com.cyanogen.experienceobelisk.registries.RegisterBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class InfectedArchiversBookshelfBlock extends InfectedBookshelfBlock implements EntityBlock {

    public InfectedArchiversBookshelfBlock() {
        super(1.5f);
    }

    //-----BLOCK ENTITY-----//

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return blockEntityType == RegisterBlockEntities.INFECTED_ARCHIVERS_BOOKSHELF_BE.get() ? InfectedArchiversBookshelfEntity::tick : null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return RegisterBlockEntities.INFECTED_ARCHIVERS_BOOKSHELF_BE.get().create(pos, state);
    }

}
