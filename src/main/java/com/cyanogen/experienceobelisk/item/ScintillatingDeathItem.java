package com.cyanogen.experienceobelisk.item;

import com.cyanogen.experienceobelisk.block_entities.ScintillatingDeathEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ScintillatingDeathItem extends BlockItem {

    public ScintillatingDeathItem(Block p_40565_, Properties p_40566_) {
        super(p_40565_, p_40566_);
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext context, BlockState state) {

        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if(level.getBlockEntity(pos) instanceof ScintillatingDeathEntity deathCloud){
            deathCloud.setOwner(player);
        }

        return super.placeBlock(context, state);
    }
}
