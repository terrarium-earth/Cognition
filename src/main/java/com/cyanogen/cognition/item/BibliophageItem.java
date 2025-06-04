package com.cyanogen.cognition.item;

import com.cyanogen.cognition.block_entities.bibliophage.agar.FluorescentAgarEntity;
import com.cyanogen.cognition.recipe.InfectingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BibliophageItem extends Item {

    public BibliophageItem(Properties p) {
        super(p);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        boolean success = infectBlock(level, pos);

        if(success){
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        else{
            return super.useOn(context);
        }
    }

    public static boolean infectBlock(Level level, BlockPos pos){

        if(level.getBlockEntity(pos) instanceof FluorescentAgarEntity fluorescentAgarEntity){
            fluorescentAgarEntity.incrementInfectionProgress();
            return true;
        }

        InfectingRecipe recipe = InfectingRecipe.getRecipe(level, level.getBlockState(pos).getBlock());
        Block oldBlock = level.getBlockState(pos).getBlock();

        if(recipe != null){
            ItemStack result = recipe.assemble(oldBlock.asItem().getDefaultInstance(), level.registryAccess());

            if(result.getItem() instanceof BlockItem blockItem){
                BlockState newBlockState = blockItem.getBlock().defaultBlockState();
                boolean success = level.setBlockAndUpdate(pos, newBlockState);

                if(success){
                    level.playSound(null, pos, SoundEvents.WART_BLOCK_BREAK, SoundSource.BLOCKS, 1f,1f);
                    level.levelEvent(null, 2001, pos, Block.getId(newBlockState));
                    return true;
                }
            }
            else{
                System.out.println("[Cognition] This infecting recipe does not have a valid block as its result");
            }
        }
        return false;
    }

}
