package com.cyanogen.experienceobelisk.item;

import com.cyanogen.experienceobelisk.registries.RegisterSounds;
import com.cyanogen.experienceobelisk.utils.ExperienceUtils;
import com.cyanogen.experienceobelisk.utils.MiscUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class FlaskChaosItem extends Item{

    public FlaskChaosItem(Properties p) {
        super(p);
    }

    //-----------BEHAVIOR-----------//

    public static final int cost = 1; // <1 level
    final int cooldown = 8;

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {

        Level level = context.getLevel();
        Player player = context.getPlayer();
        InteractionHand hand = context.getHand();

        if(player != null && (player.isCreative() || ExperienceUtils.getTotalXp(player) >= cost) && !player.getCooldowns().isOnCooldown(this)){

            BlockHitResult result = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
            BlockPos pos = result.getBlockPos();
            Direction direction = result.getDirection();
            BlockState state = level.getBlockState(pos);
            ItemStack item = player.getItemInHand(hand);

            if(level.mayInteract(player, pos) && player.mayUseItemAt(pos.relative(direction), direction, item)){

                if(state.getBlock() instanceof BucketPickup bucketpickup) { //fluid sources & waterlogged blocks
                    ItemStack test = bucketpickup.pickupBlock(level, pos, state);

                    if(!test.isEmpty()){
                        level.gameEvent(player, GameEvent.FLUID_PICKUP, pos);

                        return handlePlayer(player, level);
                    }
                }
                else if(state.getBlock() instanceof AbstractCauldronBlock block && block.isFull(state)){ //cauldrons
                    level.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());

                    return handlePlayer(player, level);
                }
                else if(state.hasBlockEntity()){ // block entities

                    BlockEntity entity = level.getBlockEntity(pos);
                    assert entity != null;
                    if(entity.getCapability(ForgeCapabilities.FLUID_HANDLER).resolve().isPresent()){
                        IFluidHandler handler = entity.getCapability(ForgeCapabilities.FLUID_HANDLER).resolve().get();

                        int drainAmount = handler.drain(1000, IFluidHandler.FluidAction.SIMULATE).getAmount();

                        if(drainAmount != 0){
                            handler.drain(1000, IFluidHandler.FluidAction.EXECUTE);
                            return handlePlayer(player, level);
                        }
                    }
                }
            }
        }

        return super.onItemUseFirst(stack, context);
    }

    public InteractionResult handlePlayer(Player player, Level level){

        int k = player.isCreative() ? 0 : 1;
        player.getCooldowns().addCooldown(this, cooldown);
        player.giveExperiencePoints(-cost * k);
        player.playSound(RegisterSounds.FLASK_FILL_VOID.get(), MiscUtils.randomInRange(1.0f, 1.2f), MiscUtils.randomInRange(0.8f, 1.2f));
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

}
