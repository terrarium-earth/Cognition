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

    int interval = 0;

    public static <T> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {

        if(blockEntity instanceof NutrientAgarEntity agar){

            if(!level.isClientSide && level.getGameTime() % (120 + agar.interval) == 0){
                ServerLevel server = (ServerLevel) level;
                double x = 0;
                double y = 0;
                double z = 0;
                int count = 1;
                double dx = 0.2;
                double dy = 0.2;
                double dz = 0.2;
                double idk = 0;
                //i want to make it occasionally emit some particle
                //insightful_agar_outer
                //insightful_agar_inner
                server.sendParticles(ParticleTypes.BUBBLE, x, y, z, count, dx, dy, dz, idk);
                agar.interval = level.random.nextIntBetweenInclusive(0,120);
            }

        }
    }

}
