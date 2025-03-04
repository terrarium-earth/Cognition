package com.cyanogen.experienceobelisk.item;

import com.cyanogen.experienceobelisk.utils.ExperienceUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class FlaskHadesItem extends Item{

    public FlaskHadesItem(Properties p) {
        super(p);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    //-----------BEHAVIOR-----------//

    public static final int cost = 160; // 10 levels
    final int cooldown = 80;

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos clickedPos = context.getClickedPos(); //the position of the block that was clicked
        BlockPos replacePos = context.getClickedPos().relative(context.getClickedFace(), 1); //the position adjacent to the clicked block
        Level level = context.getLevel();
        Player player = context.getPlayer();

        if(player != null && (player.isCreative() || ExperienceUtils.getTotalXp(player) >= cost) && !player.getCooldowns().isOnCooldown(this)){

            boolean canModifyClicked = level.mayInteract(player, clickedPos) && player.mayUseItemAt(clickedPos, context.getClickedFace(), player.getItemInHand(context.getHand()));
            boolean canPlace = level.mayInteract(player, replacePos) && player.mayUseItemAt(replacePos, context.getClickedFace(), player.getItemInHand(context.getHand()));
            boolean edit = !player.isShiftKeyDown() && canModifyClicked;

            BlockState clickedState = level.getBlockState(clickedPos);
            BlockState stateToReplace = level.getBlockState(replacePos);

            if(clickedState.getBlock() instanceof AbstractCauldronBlock && edit){ //cauldrons

                if(clickedState.getBlock().equals(Blocks.CAULDRON)){
                    level.setBlockAndUpdate(clickedPos, Blocks.LAVA_CAULDRON.defaultBlockState());
                    return handlePlayer(player, level);
                }
                else{
                    return InteractionResult.FAIL;
                }
            }
            else if(clickedState.hasBlockEntity() && edit){ //fluid containers

                BlockEntity entity = level.getBlockEntity(clickedPos);
                assert entity != null;
                if(entity.getCapability(ForgeCapabilities.FLUID_HANDLER, context.getClickedFace()).resolve().isPresent()){
                    IFluidHandler handler = entity.getCapability(ForgeCapabilities.FLUID_HANDLER, context.getClickedFace()).resolve().get();

                    int fillAmount = handler.fill(new FluidStack(Fluids.LAVA.getSource(), 1000), IFluidHandler.FluidAction.SIMULATE);
                    handler.fill(new FluidStack(Fluids.WATER.getSource(), fillAmount), IFluidHandler.FluidAction.EXECUTE);

                    return handlePlayer(player, level);
                }
            }
            else if((stateToReplace.isAir() || stateToReplace.canBeReplaced(Fluids.LAVA)) && canPlace){ //air or replaceable block
                level.setBlockAndUpdate(replacePos, Blocks.LAVA.defaultBlockState());

                return handlePlayer(player, level);
            }

            return InteractionResult.FAIL;
        }

        return super.useOn(context);
    }

    public InteractionResult handlePlayer(Player player, Level level){

        int k = player.isCreative() ? 0 : 1;
        player.getCooldowns().addCooldown(this, cooldown);
        player.giveExperiencePoints(-cost * k);
        player.playSound(SoundEvents.BUCKET_EMPTY_LAVA, 1f, 1f);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

}
