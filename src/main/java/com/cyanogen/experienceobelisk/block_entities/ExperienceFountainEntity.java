package com.cyanogen.experienceobelisk.block_entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ExperienceFountainEntity extends BlockEntity {

    public ExperienceFountainEntity(BlockPos pPos, BlockState pState) {
        super(ModTileEntitiesInit.EXPERIENCEFOUNTAIN_BE.get(), pPos, pState);
    }
}
