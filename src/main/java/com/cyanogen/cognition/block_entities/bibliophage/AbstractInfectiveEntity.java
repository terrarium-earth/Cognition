package com.cyanogen.cognition.block_entities.bibliophage;

import com.cyanogen.cognition.recipe.InfectingRecipe;
import com.cyanogen.cognition.registries.RegisterTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cyanogen.cognition.item.BibliophageItem.infectBlock;

public abstract class AbstractInfectiveEntity extends BlockEntity {

    public AbstractInfectiveEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void infectAdjacent(Level level, BlockPos pos){

        Map<BlockPos, BlockState> adjacentMap = new HashMap<>();

        for(BlockPos adjacentPos : getAdjacents(pos)){

            BlockState adjacentState = level.getBlockState(adjacentPos);

            if(adjacentState.isAir() || adjacentState.is(RegisterTags.Blocks.INFECTIVE_BLOCKS)){
                continue;
            }

            BlockState infectedState = InfectingRecipe.getInfectedBlockState(level, adjacentState);
            if(infectedState != null){
                adjacentMap.put(adjacentPos, infectedState);
            }
        }

        if(!adjacentMap.isEmpty()){

            int index = (int) Math.floor(Math.random() * adjacentMap.size());
            BlockPos posToInfect = (BlockPos) adjacentMap.keySet().toArray()[index];
            BlockState newBlock = adjacentMap.getOrDefault(posToInfect, null);

            if(newBlock != null){
                infectBlock(level, posToInfect, newBlock);
            }
        }
    }

    public List<BlockPos> getAdjacents(BlockPos pos){
        List<BlockPos> list = new ArrayList<>();
        list.add(pos.above());
        list.add(pos.below());
        list.add(pos.north());
        list.add(pos.south());
        list.add(pos.east());
        list.add(pos.west());

        return list;
    }

    public List<BlockPos> getEdgeBlocks(BlockPos pos){
        List<BlockPos> list = new ArrayList<>();

        BlockPos above = pos.above();
        BlockPos below = pos.below();

        list.add(above.north());
        list.add(above.south());
        list.add(above.east());
        list.add(above.west());
        list.add(below.north());
        list.add(below.south());
        list.add(below.east());
        list.add(below.west());
        list.add(pos.north().west());
        list.add(pos.north().east());
        list.add(pos.south().west());
        list.add(pos.south().east());

        return list;
    }

    public List<BlockPos> getVertexBlocks(BlockPos pos){
        List<BlockPos> list = new ArrayList<>();

        BlockPos above = pos.above();
        BlockPos below = pos.below();

        list.add(above.north().west());
        list.add(above.north().east());
        list.add(above.south().west());
        list.add(above.south().east());
        list.add(below.north().west());
        list.add(below.north().east());
        list.add(below.south().west());
        list.add(below.south().east());

        return list;
    }

}
