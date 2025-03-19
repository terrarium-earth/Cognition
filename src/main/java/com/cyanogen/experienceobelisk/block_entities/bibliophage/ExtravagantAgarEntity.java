package com.cyanogen.experienceobelisk.block_entities.bibliophage;

import com.cyanogen.experienceobelisk.registries.RegisterBlockEntities;
import com.cyanogen.experienceobelisk.utils.MiscUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class ExtravagantAgarEntity extends AbstractAgarEntity {

    public ExtravagantAgarEntity(BlockPos pos, BlockState state) {
        super(RegisterBlockEntities.EXTRAVAGANT_AGAR_BE.get(), pos, state);
        infectivity = 0.02;
    }

    public static <T> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {

        AbstractAgarEntity.tick(level, pos, state, blockEntity);

        if(level.getGameTime() % 2 == 0){
            Vec3 particlePos = MiscUtils.generateRandomBlockSurfacePos(pos, 0.55f);

            level.addParticle(
                    ParticleTypes.ENCHANT, false, particlePos.x, particlePos.y, particlePos.z,
                    MiscUtils.randomInRange(-0.5f, 0.5f),
                    MiscUtils.randomInRange(-0.5f, 0.5f),
                    MiscUtils.randomInRange(-0.5f, 0.5f));
        }
    }

}
