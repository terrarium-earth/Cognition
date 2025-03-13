package com.cyanogen.experienceobelisk.block_entities.bibliophage;

import com.cyanogen.experienceobelisk.config.Config;
import com.cyanogen.experienceobelisk.registries.RegisterBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class InfectedBookshelfEntity extends AbstractInfectedBookshelfEntity {

    public InfectedBookshelfEntity(BlockPos pos, BlockState state) {
        super(RegisterBlockEntities.INFECTED_BOOKSHELF_BE.get(), pos, state,
                Config.COMMON.infectedSpawnDelayMin.get(),
                Config.COMMON.infectedSpawnDelayMax.get(),
                Config.COMMON.infectedOrbValue.get(),
                Config.COMMON.infectedSpawns.get());
    }

}
