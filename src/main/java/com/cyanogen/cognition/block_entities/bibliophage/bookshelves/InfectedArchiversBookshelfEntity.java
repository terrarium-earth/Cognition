package com.cyanogen.cognition.block_entities.bibliophage.bookshelves;

import com.cyanogen.cognition.config.Config;
import com.cyanogen.cognition.registries.RegisterBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class InfectedArchiversBookshelfEntity extends AbstractInfectedBookshelfEntity {

    public InfectedArchiversBookshelfEntity(BlockPos pos, BlockState state) {
        super(RegisterBlockEntities.INFECTED_ARCHIVERS_BOOKSHELF.get(), pos, state,
                Config.COMMON.archiversSpawnDelayMin.get(),
                Config.COMMON.archiversSpawnDelayMax.get(),
                Config.COMMON.archiversOrbValue.get(),
                Config.COMMON.archiversSpawns.get());

    }

  
}
