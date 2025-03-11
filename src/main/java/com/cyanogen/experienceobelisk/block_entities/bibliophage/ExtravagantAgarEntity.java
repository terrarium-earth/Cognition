package com.cyanogen.experienceobelisk.block_entities.bibliophage;

import com.cyanogen.experienceobelisk.registries.RegisterBlockEntities;
import com.cyanogen.experienceobelisk.utils.MiscUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class ExtravagantAgarEntity extends AbstractAgarEntity {

    public ExtravagantAgarEntity(BlockPos pos, BlockState state) {
        super(RegisterBlockEntities.EXTRAVAGANT_AGAR_BE.get(), pos, state);
        infectivity = 0.02;
    }

    public static <T> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {

        if(level.getGameTime() % 2 == 0){
            Vec3 particlePos = MiscUtils.generateRandomBlockSurfacePos(pos, 0.55f);

            level.addAlwaysVisibleParticle(
                    ParticleTypes.ENCHANT, true, particlePos.x, particlePos.y, particlePos.z,
                    MiscUtils.randomInRange(-0.5f, 0.5f),
                    MiscUtils.randomInRange(-0.5f, 0.5f),
                    MiscUtils.randomInRange(-0.5f, 0.5f));
        }
    }

    // Increases the orb value of adjacent bookshelves by 1.25x
    // Inherits properties of nutrient agar

}
