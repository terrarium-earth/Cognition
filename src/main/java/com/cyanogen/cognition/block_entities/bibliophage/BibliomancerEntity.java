package com.cyanogen.cognition.block_entities.bibliophage;

import com.cyanogen.cognition.block_entities.ExperienceReceivingEntity;
import com.cyanogen.cognition.registries.RegisterBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BibliomancerEntity extends ExperienceReceivingEntity {

    public BibliomancerEntity(BlockPos pos, BlockState state) {
        super(RegisterBlockEntities.BIBLIOMANCER.get(), pos, state);
    }

}
