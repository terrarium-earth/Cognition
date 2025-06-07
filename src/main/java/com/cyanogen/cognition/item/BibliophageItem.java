package com.cyanogen.cognition.item;

import com.cyanogen.cognition.block_entities.bibliophage.agar.FluorescentAgarEntity;
import com.cyanogen.cognition.recipe.InfectingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class BibliophageItem extends Item {

    public BibliophageItem(Properties p) {
        super(p);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        InteractionHand hand = context.getHand();

        BlockState newBlock = InfectingRecipe.getInfectedBlockState(level, level.getBlockState(pos));

        if(newBlock != null){
            boolean success = infectBlock(level, pos, newBlock);
            if(success && player != null && !player.isCreative()){
                player.getItemInHand(hand).shrink(1);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        else{
            return super.useOn(context);
        }
    }

    public static boolean infectBlock(Level level, BlockPos pos, @NotNull BlockState newBlock){

        if(level.getBlockEntity(pos) instanceof FluorescentAgarEntity fluorescentAgarEntity){
            fluorescentAgarEntity.incrementInfectionProgress();
            return true;
        }

        if(!level.isClientSide){
            boolean success = level.setBlockAndUpdate(pos, newBlock);
            if(success){
                level.playSound(null, pos, SoundEvents.WART_BLOCK_BREAK, SoundSource.BLOCKS, 1f, 1f);
                level.levelEvent(null, 2001, pos, Block.getId(newBlock));
                return true;
            }
        }
        return false;

    }

}
