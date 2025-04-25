package com.cyanogen.cognition.block_entities.bibliophage.bookshelves;

import com.cyanogen.cognition.config.Config;
import com.cyanogen.cognition.registries.RegisterBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class InfectedBookshelfEntity extends AbstractInfectedBookshelfEntity {

    public InfectedBookshelfEntity(BlockPos pos, BlockState state) {
        super(RegisterBlockEntities.INFECTED_BOOKSHELF.get(), pos, state,
                Config.COMMON.infectedSpawnDelayMin.get(),
                Config.COMMON.infectedSpawnDelayMax.get(),
                Config.COMMON.infectedOrbValue.get(),
                Config.COMMON.infectedSpawns.get());
    }

}
