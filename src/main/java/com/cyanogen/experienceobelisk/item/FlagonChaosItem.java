package com.cyanogen.experienceobelisk.item;

import com.cyanogen.experienceobelisk.utils.ExperienceUtils;
import com.ibm.icu.text.AlphabeticIndex;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public class FlagonChaosItem extends Item{

    public FlagonChaosItem(Properties p) {
        super(p);
    }

    //-----------BEHAVIOR-----------//

    final int cost = 16; // 2 levels
    final int cooldown = 10;

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos pos = context.getClickedPos().relative(context.getClickedFace(), 1);
        Direction direction = context.getClickedFace();
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockState state = level.getBlockState(pos);

        if(player != null && (player.isCreative() || ExperienceUtils.getTotalXp(player) >= cost) && !player.getCooldowns().isOnCooldown(this)){

            int k = player.isCreative() ? 0 : 1;
            FluidState fluid = state.getFluidState();
            ItemStack item = player.getItemInHand(context.getHand());

            if(level.mayInteract(player, pos) && player.mayUseItemAt(pos, direction, item)){
                if(!fluid.isEmpty()){
                    level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());

                    player.getCooldowns().addCooldown(this, cooldown);
                    player.giveExperiencePoints(-cost * k);
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
                else if(state.getBlock() instanceof LiquidBlockContainer container){
                    container.placeLiquid(level, pos, state, Fluids.EMPTY.defaultFluidState());

                    player.getCooldowns().addCooldown(this, cooldown);
                    player.giveExperiencePoints(-cost * k);
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
                else if(state.hasBlockEntity()){

                    BlockEntity entity = level.getBlockEntity(pos);
                    assert entity != null;
                    if(entity.getCapability(ForgeCapabilities.FLUID_HANDLER).resolve().isPresent()){
                        IFluidHandler handler = entity.getCapability(ForgeCapabilities.FLUID_HANDLER).resolve().get();
                        handler.drain(1000, IFluidHandler.FluidAction.EXECUTE);

                        player.getCooldowns().addCooldown(this, cooldown);
                        player.giveExperiencePoints(-cost * k);
                        return InteractionResult.sidedSuccess(level.isClientSide);
                    }

                }
            }

        }

        return super.useOn(context);
    }

}
