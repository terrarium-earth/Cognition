package com.cyanogen.experienceobelisk.item;

import com.cyanogen.experienceobelisk.registries.RegisterSounds;
import com.cyanogen.experienceobelisk.utils.ExperienceUtils;
import com.cyanogen.experienceobelisk.utils.MiscUtils;
import net.minecraft.core.BlockPos;
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

    public static final int cost = 55; // 5 levels
    final int cooldown = 100;
    private final FluidStack fluidStack = new FluidStack(Fluids.LAVA.getSource(), 1000);

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {

        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        BlockPos placePos = context.getClickedPos().relative(context.getClickedFace());

        if(player != null && (player.isCreative() || ExperienceUtils.getTotalXp(player) >= cost) && !player.getCooldowns().isOnCooldown(this)){

            boolean canModifyClicked = level.mayInteract(player, clickedPos) && player.mayUseItemAt(clickedPos, context.getClickedFace(), player.getItemInHand(context.getHand()));
            boolean canPlace = level.mayInteract(player, placePos) && player.mayUseItemAt(placePos, context.getClickedFace(), player.getItemInHand(context.getHand()));
            boolean edit = !player.isShiftKeyDown() && canModifyClicked;

            BlockState clickedState = level.getBlockState(clickedPos);
            BlockState stateToReplace = level.getBlockState(placePos);

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
                if(entity.getCapability(ForgeCapabilities.FLUID_HANDLER).resolve().isPresent()){
                    IFluidHandler handler = entity.getCapability(ForgeCapabilities.FLUID_HANDLER).resolve().get();

                    int drainAmount = handler.fill(fluidStack, IFluidHandler.FluidAction.SIMULATE);

                    if(drainAmount != 0){
                        handler.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                        return handlePlayer(player, level);
                    }
                }
            }
            else if((stateToReplace.isAir() || stateToReplace.canBeReplaced(Fluids.LAVA)) && canPlace){ //air or replaceable block
                level.setBlockAndUpdate(placePos, Blocks.LAVA.defaultBlockState());

                return handlePlayer(player, level);
            }
        }

        return super.onItemUseFirst(stack, context);
    }

    public InteractionResult handlePlayer(Player player, Level level){

        int k = player.isCreative() ? 0 : 1;
        player.getCooldowns().addCooldown(this, cooldown);
        player.giveExperiencePoints(-cost * k);
        player.playSound(RegisterSounds.FLASK_EMPTY_LAVA.get(), MiscUtils.randomInRange(0.8f, 1.0f), MiscUtils.randomInRange(0.8f, 1.0f));
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

}
