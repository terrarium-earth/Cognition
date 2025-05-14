package com.cyanogen.cognition.item;

import com.cyanogen.cognition.registries.RegisterSounds;
import com.cyanogen.cognition.utils.ExperienceUtils;
import com.cyanogen.cognition.utils.MiscUtils;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class FlaskPoseidonItem extends BucketItem{

    public FlaskPoseidonItem(Properties p) {
        super(Fluids.WATER.getSource(), p);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    //-----------BEHAVIOR-----------//

    public static final int cost = 7; // 1 level
    final int cooldown = 10;
    private final FluidStack fluidStack = new FluidStack(Fluids.WATER.getSource(), 1000);

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) { //vanilla bucket behavior

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

            if(!level.mayInteract(player, blockpos) || !player.mayUseItemAt(blockpos1, direction, itemstack)){
                return InteractionResultHolder.fail(itemstack);
            }
            else if(canUse(player)){
                BlockState blockstate = level.getBlockState(blockpos);
                BlockPos blockpos2 = canBlockContainFluid(player, level, blockpos, blockstate) ? blockpos : blockpos1;

                if(this.emptyContents(player, level, blockpos2, blockhitresult, itemstack)){
                    this.checkExtraContent(player, level, itemstack, blockpos2);
                    if(player instanceof ServerPlayer){
                        CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, blockpos2, itemstack);
                    }

                    player.awardStat(Stats.ITEM_USED.get(this));
                    handlePlayer(player);

                    return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
                }
                else{
                    return InteractionResultHolder.fail(itemstack);
                }
            }
            return InteractionResultHolder.fail(itemstack);
        }
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {

        BlockPos clickedPos = context.getClickedPos();
        Level level = context.getLevel();
        Player player = context.getPlayer();
        Direction direction = context.getClickedFace();

        if(player != null && canUse(player)){
            boolean canModifyClicked = level.mayInteract(player, clickedPos) && player.mayUseItemAt(clickedPos, context.getClickedFace(), player.getItemInHand(context.getHand()));
            boolean edit = !player.isShiftKeyDown() && canModifyClicked;
            BlockState clickedState = level.getBlockState(clickedPos);

            if(clickedState.getBlock() instanceof AbstractCauldronBlock && edit){ //cauldrons

                if(clickedState.getBlock().equals(Blocks.CAULDRON)){
                    level.setBlockAndUpdate(clickedPos, Blocks.WATER_CAULDRON.defaultBlockState().trySetValue(BlockStateProperties.LEVEL_CAULDRON, 3));
                    handlePlayer(player);
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
                else{
                    return InteractionResult.FAIL;
                }
            }
            else if(clickedState.hasBlockEntity() && edit){ //fluid containers

                BlockEntity entity = level.getBlockEntity(clickedPos);
                assert entity != null;

                IFluidHandler handler = level.getCapability(Capabilities.FluidHandler.BLOCK, clickedPos, direction);

                if(handler != null){
                    int drainAmount = handler.fill(fluidStack, IFluidHandler.FluidAction.SIMULATE);

                    if(drainAmount != 0){
                        handler.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                        handlePlayer(player);
                        return InteractionResult.sidedSuccess(level.isClientSide);
                    }
                }
            }
        }

        return super.useOn(context);
    }

    public boolean canUse(Player player){
        return (player.isCreative() || ExperienceUtils.getTotalXP(player) >= cost) && !player.getCooldowns().isOnCooldown(this);
    }

    public void handlePlayer(Player player){
        int k = player.isCreative() ? 0 : 1;
        player.getCooldowns().addCooldown(this, cooldown);
        player.giveExperiencePoints(-cost * k);
        player.playSound(RegisterSounds.FLASK_EMPTY_WATER.get(), MiscUtils.randomInRange(0.8f, 1.0f), MiscUtils.randomInRange(0.8f, 1.0f));
    }

}
