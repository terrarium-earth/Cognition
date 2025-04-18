package com.cyanogen.experienceobelisk.block_entities;

import com.cyanogen.experienceobelisk.block.LinearAcceleratorBlock;
import com.cyanogen.experienceobelisk.registries.RegisterBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static com.cyanogen.experienceobelisk.block.LinearAcceleratorBlock.ACTIVE;

public class LinearAcceleratorEntity extends AbstractAcceleratorEntity{

    public LinearAcceleratorEntity(BlockPos pos, BlockState state) {
        super(RegisterBlockEntities.LINEAR_ACCELERATOR.get(), pos, state);
    }

    static double orbSpeed = 1.5;
    static double entitySpeed = 0.6;

    public static <T> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {

        if(blockEntity instanceof LinearAcceleratorEntity accelerator){

            if(!accelerator.redstoneEnabled || level.hasNeighborSignal(pos)){

                if(!state.getValue(ACTIVE)){
                    state = state.setValue(ACTIVE, true);
                    level.setBlockAndUpdate(pos, state);
                }

                Direction facing = state.getValue(LinearAcceleratorBlock.FACING);
                int x = 0;
                int z = 0;

                switch (facing){
                    case NORTH -> z = -1;
                    case SOUTH -> z = 1;
                    case EAST -> x = 1;
                    case WEST -> x = -1;
                }

                AABB area = new AABB(
                        pos.getX(),
                        pos.getY() + 1,
                        pos.getZ(),
                        pos.getX() + 1,
                        pos.getY() + 2,
                        pos.getZ() + 1);

                List<Entity> list = level.getEntities(null, area);

                if(!list.isEmpty()) for(Entity entity : list){
                    boolean isShiftPlayer = entity instanceof Player player && player.isShiftKeyDown();

                    if(entity instanceof ExperienceOrb orb){
                        orb.addDeltaMovement(new Vec3(orbSpeed * x,0,orbSpeed * z));
                    }
                    else if(!isShiftPlayer){
                        entity.addDeltaMovement(new Vec3(entitySpeed * x,0,entitySpeed * z));
                    }

                }
            }
            else if(state.getValue(ACTIVE)){
                state = state.setValue(ACTIVE, false);
                level.setBlockAndUpdate(pos, state);
            }

        }

    }
}
