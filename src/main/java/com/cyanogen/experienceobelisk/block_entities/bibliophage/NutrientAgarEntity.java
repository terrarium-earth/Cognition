package com.cyanogen.experienceobelisk.block_entities.bibliophage;

import com.cyanogen.experienceobelisk.registries.RegisterBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class NutrientAgarEntity extends AbstractAgarEntity {

    public NutrientAgarEntity(BlockPos pos, BlockState state) {
        super(RegisterBlockEntities.NUTRIENT_AGAR_BE.get(), pos, state);
    }

}
