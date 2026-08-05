package com.cyanogen.cognition.block.void_garden;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractClusterBlock extends Block {

    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 1, 5);

    //1 -- Bud
    //2 -- Small Crystal
    //3 -- Large Crystal
    //4 -- Fully grown (100% effect)
    //5 -- Decaying
    // stages 1,2,3 and 5 each account for 5% of total lifespan

    public AbstractClusterBlock() {
        super(Properties.of()
                .strength(2.8f)
                .sound(SoundType.AMETHYST_CLUSTER)
                .destroyTime(1.2f)
                .requiresCorrectToolForDrops()
                .pushReaction(PushReaction.DESTROY)
        );
        this.registerDefaultState(this.stateDefinition.any().setValue(STAGE, 4));
    }

    public static float stageMultiplier(int stage, boolean isResonance){

        if(isResonance){
            return switch (stage) {
                case 1 -> 1.1f;
                case 2 -> 1.2f;
                case 3 -> 1.35f;
                default -> 1.5f;
            };
        }else{
            return switch (stage) {
                case 1 -> 0.9f;
                case 2 -> 0.7f;
                case 3 -> 0.6f;
                default -> 0.5f;
            };
        }
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
