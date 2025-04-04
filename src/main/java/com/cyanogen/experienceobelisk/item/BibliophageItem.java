package com.cyanogen.experienceobelisk.item;

import com.cyanogen.experienceobelisk.block_entities.bibliophage.agar.FluorescentAgarEntity;
import com.cyanogen.experienceobelisk.registries.RegisterBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class BibliophageItem extends Item {

    public BibliophageItem(Properties p) {
        super(p);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        if(getValidBlocksForInfection().contains(block)){
            infectBlock(level, pos, block);

            if(player != null && !player.isCreative()){
                stack.shrink(1);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return super.useOn(context);
    }

    public static List<Block> getValidBlocksForInfection(){
        List<Block> list = new ArrayList<>();
        list.add(Blocks.BOOKSHELF);
        list.add(RegisterBlocks.ENCHANTED_BOOKSHELF.get());
        list.add(RegisterBlocks.ARCHIVERS_BOOKSHELF.get());
        list.add(RegisterBlocks.FLUORESCENT_AGAR.get());

        return list;
    }

    public static void infectBlock(Level level, BlockPos pos, Block block){

        BlockState state = null;

        if(!level.isClientSide){
            if(block.equals(RegisterBlocks.FLUORESCENT_AGAR.get())){
                if(level.getBlockEntity(pos) instanceof FluorescentAgarEntity agarEntity){
                    agarEntity.incrementInfectionProgress();
                }
                return;
            }

            if(block.equals(Blocks.BOOKSHELF)){
                state = RegisterBlocks.INFECTED_BOOKSHELF.get().defaultBlockState();
            }
            else if(block.equals(RegisterBlocks.ENCHANTED_BOOKSHELF.get())){
                state = RegisterBlocks.INFECTED_ENCHANTED_BOOKSHELF.get().defaultBlockState();
            }
            else if(block.equals(RegisterBlocks.ARCHIVERS_BOOKSHELF.get())){
                state = RegisterBlocks.INFECTED_ARCHIVERS_BOOKSHELF.get().defaultBlockState();
            }

            if(state != null){
                boolean success = level.setBlockAndUpdate(pos, state);

                if(success){
                    level.playSound(null, pos, SoundEvents.WART_BLOCK_BREAK, SoundSource.BLOCKS, 1f,1f);
                    level.levelEvent(null, 2001, pos, Block.getId(state));
                }
            }
        }

    }

}
