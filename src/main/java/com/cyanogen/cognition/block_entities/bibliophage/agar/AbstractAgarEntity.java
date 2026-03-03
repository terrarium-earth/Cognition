package com.cyanogen.cognition.block_entities.bibliophage.agar;

import com.cyanogen.cognition.block_entities.bibliophage.AbstractInfectiveEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class AbstractAgarEntity extends AbstractInfectiveEntity {

    public AbstractAgarEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, double infectivity) {
        super(type, pos, state);

        this.infectivity = infectivity;
    }

    private final double infectivity;

    public static <T> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {

        if(blockEntity instanceof AbstractAgarEntity agar){

            if(level.getGameTime() % 20 == 0 && Math.random() <= agar.infectivity){
                agar.infectAdjacent(level, pos);
            }

        }
    }

}
