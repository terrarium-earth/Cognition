package com.cyanogen.cognition.item;

import com.cyanogen.cognition.registries.RegisterSounds;
import com.cyanogen.cognition.utils.ExperienceUtils;
import com.cyanogen.cognition.utils.MiscUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class FlaskChaosItem extends BucketItem {

    public FlaskChaosItem(Properties p) {
        super(Fluids.EMPTY, p);
    }

    //-----------BEHAVIOR-----------//

    public static final int cost = 1; // <1 level
    final int cooldown = 8;

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        ItemStack itemstack = player.getItemInHand(hand);
        BlockHitResult blockhitresult = getPlayerPOVHitResult(
                level, player, this.content == Fluids.EMPTY ? ClipContext.Fluid.SOURCE_ONLY : ClipContext.Fluid.NONE
        );

        if(blockhitresult.getType() == HitResult.Type.MISS){
            return InteractionResultHolder.pass(itemstack);
        }
        else if(blockhitresult.getType() != HitResult.Type.BLOCK){
            return InteractionResultHolder.pass(itemstack);
        }
        else{
            BlockPos blockpos = blockhitresult.getBlockPos();
            Direction direction = blockhitresult.getDirection();
            BlockPos blockpos1 = blockpos.relative(direction);

            if (level.mayInteract(player, blockpos) && player.mayUseItemAt(blockpos1, direction, itemstack)&& canUse(player)) {
                BlockState blockState = level.getBlockState(blockpos);
                Block block = blockState.getBlock();

                if (block instanceof BucketPickup bucketpickup) {
                    ItemStack test = bucketpickup.pickupBlock(player, level, blockpos, blockState);
                    if(!test.isEmpty()){
                        handlePlayer(player);
                        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
                    }
                }
            }
            return InteractionResultHolder.fail(itemstack);
        }
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {

        Level level = context.getLevel();
        Player player = context.getPlayer();
        InteractionHand hand = context.getHand();

        if(player != null && canUse(player)){

            BlockHitResult result = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
            BlockPos pos = result.getBlockPos();
            Direction direction = result.getDirection();
            BlockState state = level.getBlockState(pos);

            if(state.getBlock() instanceof AbstractCauldronBlock block
                && (block.equals(Blocks.LAVA_CAULDRON) || block.equals(Blocks.WATER_CAULDRON))) { //cauldrons

                    level.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());
                    handlePlayer(player);
                    return InteractionResult.sidedSuccess(level.isClientSide);
            }
            else if(state.hasBlockEntity()){ // block entities

                BlockEntity entity = level.getBlockEntity(pos);
                assert entity != null;

                IFluidHandler handler = level.getCapability(Capabilities.FluidHandler.BLOCK, pos, direction);

                if (handler != null) {
                    int drainAmount = handler.drain(1000, IFluidHandler.FluidAction.SIMULATE).getAmount();
                    if (drainAmount != 0) {
                        handler.drain(1000, IFluidHandler.FluidAction.EXECUTE);
                        handlePlayer(player);
                        return InteractionResult.sidedSuccess(level.isClientSide);
                    }
                }
            }
        }
        return super.onItemUseFirst(stack, context);
    }

    public boolean canUse(Player player){
        return (player.isCreative() || ExperienceUtils.getTotalXP(player) >= cost) && !player.getCooldowns().isOnCooldown(this);
    }

    public void handlePlayer(Player player){
        int k = player.isCreative() ? 0 : 1;
        player.getCooldowns().addCooldown(this, cooldown);
        player.giveExperiencePoints(-cost * k);
        player.playSound(RegisterSounds.FLASK_FILL_VOID.get(), MiscUtils.randomInRange(1.0f, 1.2f), MiscUtils.randomInRange(0.8f, 1.2f));
    }

}
