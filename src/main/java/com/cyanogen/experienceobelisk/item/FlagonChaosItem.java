package com.cyanogen.experienceobelisk.item;

import com.cyanogen.experienceobelisk.utils.ExperienceUtils;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class FlagonChaosItem extends Item{

    public FlagonChaosItem(Properties p) {
        super(p);
    }

    //-----------BEHAVIOR-----------//

    final int cost = 16; // 2 levels
    final int cooldown = 10;

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        BlockHitResult result = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        BlockPos pos = result.getBlockPos();
        Direction direction = result.getDirection();
        BlockState state = level.getBlockState(pos);
        ItemStack item = player.getItemInHand(hand);
        int k = player.isCreative() ? 0 : 1;

        if((player.isCreative() || ExperienceUtils.getTotalXp(player) >= cost) && !player.getCooldowns().isOnCooldown(this)){

            if(level.mayInteract(player, pos) && player.mayUseItemAt(pos.relative(direction), direction, item)){

                if(state.getBlock() instanceof BucketPickup bucketpickup) {
                    ItemStack test = bucketpickup.pickupBlock(level, pos, state);

                    if(!test.isEmpty()){
                        bucketpickup.getPickupSound(state).ifPresent((p_150709_) -> {
                            player.playSound(p_150709_, 1.0F, 1.0F);
                        });
                        level.gameEvent(player, GameEvent.FLUID_PICKUP, pos);

                        player.getCooldowns().addCooldown(this, cooldown);
                        player.giveExperiencePoints(-cost * k);
                        return InteractionResultHolder.sidedSuccess(item, level.isClientSide());
                    }
                }
                else if(state.is(Blocks.WATER_CAULDRON) || state.is(Blocks.LAVA_CAULDRON) || state.is(Blocks.POWDER_SNOW_CAULDRON)){
                    level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());

                    player.getCooldowns().addCooldown(this, cooldown);
                    player.giveExperiencePoints(-cost * k);
                    return InteractionResultHolder.sidedSuccess(item, level.isClientSide());
                }
            }

            return InteractionResultHolder.fail(item);
        }
        return super.use(level, player, hand);
    }

    /*
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
                if(fluid.isSource()){
                    level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());

                    player.getCooldowns().addCooldown(this, cooldown);
                    player.giveExperiencePoints(-cost * k);
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
                else if (state.getBlock() instanceof BucketPickup pickup) {
                    ItemStack stack = pickup.pickupBlock(level, pos, state);

                    level.gameEvent(player, GameEvent.FLUID_PICKUP, pos);

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

     */



}
