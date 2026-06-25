package com.cyanogen.cognition.block.void_garden;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class ResonanceClusterBlock extends Block {
    public ResonanceClusterBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(SIZE, 1));
    }

    public static final IntegerProperty SIZE = IntegerProperty.create("size", 1, 5);

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SIZE);
        super.createBlockStateDefinition(builder);
    }
}
