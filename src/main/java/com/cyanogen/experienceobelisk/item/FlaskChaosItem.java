package com.cyanogen.experienceobelisk.item;

import com.cyanogen.experienceobelisk.utils.ExperienceUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

public class FlaskChaosItem extends Item{

    public FlaskChaosItem(Properties p) {
        super(p);
    }

    //-----------BEHAVIOR-----------//

    final int cost = 7; // 1 level
    final int cooldown = 8;

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        BlockHitResult result = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        BlockPos pos = result.getBlockPos();
        Direction direction = result.getDirection();
        BlockState state = level.getBlockState(pos);
        ItemStack item = player.getItemInHand(hand);

        if((player.isCreative() || ExperienceUtils.getTotalXp(player) >= cost) && !player.getCooldowns().isOnCooldown(this)){

            if(level.mayInteract(player, pos) && player.mayUseItemAt(pos.relative(direction), direction, item)){

                if(state.getBlock() instanceof BucketPickup bucketpickup) { //fluid sources & waterlogged blocks
                    ItemStack test = bucketpickup.pickupBlock(level, pos, state);

                    if(!test.isEmpty()){
                        bucketpickup.getPickupSound(state).ifPresent((event) -> {
                            player.playSound(event, 1.0F, 1.0F);
                        });
                        level.gameEvent(player, GameEvent.FLUID_PICKUP, pos);

                        return handlePlayer(player, item, level);
                    }
                }
                else if(state.getBlock() instanceof AbstractCauldronBlock block && block.isFull(state)){ //cauldrons
                    level.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());

                    return handlePlayer(player, item, level);
                }
            }

            return InteractionResultHolder.fail(item);
        }
        return super.use(level, player, hand);
    }

    public InteractionResultHolder<ItemStack> handlePlayer(Player player, ItemStack item, Level level){

        int k = player.isCreative() ? 0 : 1;
        player.getCooldowns().addCooldown(this, cooldown);
        player.giveExperiencePoints(-cost * k);
        player.playSound(SoundEvents.BUCKET_FILL, 1f, 1f);
        return InteractionResultHolder.sidedSuccess(item, level.isClientSide);
    }

}
