package com.cyanogen.experienceobelisk.item;

import com.cyanogen.experienceobelisk.utils.ExperienceUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class FlagonPoseidonItem extends Item{

    public FlagonPoseidonItem(Properties p) {
        super(p);
    }

    //-----------BEHAVIOR-----------//

    final int cost = 16; // 2 levels
    final int cooldown = 10;

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos pos = context.getClickedPos().relative(context.getClickedFace(), 1);
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockState state = level.getBlockState(pos);

        if(player != null && (player.isCreative() || ExperienceUtils.getTotalXp(player) >= cost) && !player.getCooldowns().isOnCooldown(this)){

            if(level.mayInteract(player, pos) && player.mayUseItemAt(pos, context.getClickedFace(), player.getItemInHand(context.getHand()))){

                if(state.isAir() || state.canBeReplaced(Fluids.WATER)){ //air or replaceable block
                    if(level.dimensionType().ultraWarm()){
                        Fluids.WATER.getFluidType().onVaporize(player, level, pos, null);
                    }
                    else{
                        level.setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState());
                    }

                    return handlePlayer(player, level);
                }
                else if(state.getBlock().equals(Blocks.CAULDRON)){ //cauldrons
                    level.setBlockAndUpdate(pos, Blocks.WATER_CAULDRON.defaultBlockState());

                    return handlePlayer(player, level);
                }
                else if(state.hasBlockEntity()){ //fluid containers

                    BlockEntity entity = level.getBlockEntity(pos);
                    assert entity != null;
                    if(entity.getCapability(ForgeCapabilities.FLUID_HANDLER).resolve().isPresent()){
                        IFluidHandler handler = entity.getCapability(ForgeCapabilities.FLUID_HANDLER).resolve().get();

                        int fillAmount = handler.fill(new FluidStack(Fluids.WATER.getSource(), 1000), IFluidHandler.FluidAction.SIMULATE);
                        handler.fill(new FluidStack(Fluids.WATER.getSource(), fillAmount), IFluidHandler.FluidAction.EXECUTE);

                        return handlePlayer(player, level);
                    }
                }
            }

            return InteractionResult.FAIL;
        }

        return super.useOn(context);
    }

    public InteractionResult handlePlayer(Player player, Level level){

        int k = player.isCreative() ? 0 : 1;
        player.getCooldowns().addCooldown(this, cooldown);
        player.giveExperiencePoints(-cost * k);
        player.playSound(SoundEvents.BUCKET_EMPTY, 1f, 1f);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

}
