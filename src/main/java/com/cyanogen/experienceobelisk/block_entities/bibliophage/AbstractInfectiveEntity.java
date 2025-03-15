package com.cyanogen.experienceobelisk.block_entities.bibliophage;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cyanogen.experienceobelisk.item.BibliophageItem.getValidBlocksForInfection;
import static com.cyanogen.experienceobelisk.item.BibliophageItem.infectBlock;

public abstract class AbstractInfectiveEntity extends BlockEntity {

    public AbstractInfectiveEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void infectAdjacent(Level level, BlockPos pos){

        Map<BlockPos, Block> adjacentMap = new HashMap<>();
        List<BlockPos> posList = new ArrayList<>();

        for(BlockPos adjacentPos : getAdjacents(pos)){
            if(getValidBlocksForInfection().contains(level.getBlockState(adjacentPos).getBlock())){

                Block adjacentBlock = level.getBlockState(adjacentPos).getBlock();
                adjacentMap.put(adjacentPos, adjacentBlock);
                posList.add(adjacentPos);
            }
        }

        if(!adjacentMap.isEmpty()){

            int index = (int) Math.floor(Math.random() * posList.size());
            BlockPos posToInfect = posList.get(index);
            Block block = adjacentMap.get(posToInfect);

            infectBlock(level, posToInfect, block);
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
