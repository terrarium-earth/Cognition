package com.cyanogen.experienceobelisk.block_entities.bibliophage;

import com.cyanogen.experienceobelisk.config.Config;
import com.cyanogen.experienceobelisk.registries.RegisterBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class InfectedArchiversBookshelfEntity extends AbstractInfectedBookshelfEntity {

    public InfectedArchiversBookshelfEntity(BlockPos pos, BlockState state) {
        super(RegisterBlockEntities.INFECTED_ARCHIVERS_BOOKSHELF_BE.get(), pos, state);

        super.spawnDelayMin = 180;
        super.spawnDelayMax = 220;
        super.orbValue = 6;
        super.spawns = 200;
    }

    @Override
    public void onLoad() {
        super.spawnDelayMin = Config.COMMON.archiversSpawnDelayMin.get();
        super.spawnDelayMax = Config.COMMON.archiversSpawnDelayMax.get();
        super.orbValue = Config.COMMON.archiversOrbValue.get();
        super.spawns = Config.COMMON.archiversSpawns.get();
    }
  
}
