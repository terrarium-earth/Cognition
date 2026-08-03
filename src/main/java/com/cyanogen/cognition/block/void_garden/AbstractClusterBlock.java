package com.cyanogen.cognition.block.void_garden;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractClusterBlock extends Block {

    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 1, 5);

    //1 -- Bud (2 mins)
    //2 -- Small Crystal (2 mins)
    //3 -- Large Crystal (2 mins)
    //4 -- Fully grown (22 mins, 100% effect)
    //5 -- Decaying (2 mins)

    public AbstractClusterBlock() {
        super(Properties.of()
                .strength(2.8f)
                .sound(SoundType.AMETHYST_CLUSTER)
                .destroyTime(1.2f)
                .requiresCorrectToolForDrops()
        );
        this.registerDefaultState(this.stateDefinition.any().setValue(STAGE, 4));
    }

    public static float stageMultiplier(int stage){
        return switch (stage) {
            case 1 -> 0.3f;
            case 2 -> 0.5f;
            case 3 -> 0.8f;
            default -> 1f;
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
        super.createBlockStateDefinition(builder);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(STAGE, 4);
    }
}
