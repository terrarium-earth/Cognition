package com.cyanogen.experienceobelisk.block_entities.bibliophage.bookshelves;

import com.cyanogen.experienceobelisk.config.Config;
import com.cyanogen.experienceobelisk.registries.RegisterBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class InfectedEnchantedBookshelfEntity extends AbstractInfectedBookshelfEntity {

    public InfectedEnchantedBookshelfEntity(BlockPos pos, BlockState state) {
        super(RegisterBlockEntities.INFECTED_ENCHANTED_BOOKSHELF.get(), pos, state,
                Config.COMMON.enchantedSpawnDelayMin.get(),
                Config.COMMON.enchantedSpawnDelayMax.get(),
                Config.COMMON.enchantedOrbValue.get(),
                Config.COMMON.enchantedSpawns.get());
    }

}
