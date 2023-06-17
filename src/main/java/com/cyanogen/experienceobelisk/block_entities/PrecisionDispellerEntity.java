package com.cyanogen.experienceobelisk.block_entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PrecisionDispellerEntity extends BlockEntity {

    public PrecisionDispellerEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModTileEntitiesInit.PRECISIONDISPELLER_BE.get(), pPos, pBlockState);
    }

}
