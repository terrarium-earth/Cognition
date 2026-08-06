package com.cyanogen.cognition.block.void_garden;

import com.cyanogen.cognition.block_entities.void_garden.VoidAltarEntity;
import com.cyanogen.cognition.registries.RegisterBlockEntities;
import com.cyanogen.cognition.registries.RegisterItems;
import com.cyanogen.cognition.utils.MiscUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class VoidAltarBlock extends Block implements EntityBlock {
    public VoidAltarBlock() {
        super(Properties.of()
                .strength(9f)
                .destroyTime(1.2f)
                .explosionResistance(9f)
                .noOcclusion()
                .lightLevel(value -> 7)
                .sound(SoundType.METAL)
                .requiresCorrectToolForDrops());
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return blockEntityType == RegisterBlockEntities.VOID_ALTAR.get() ? VoidAltarEntity::tick : null;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return RegisterBlockEntities.VOID_ALTAR.get().create(pos, state);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        //todo: attunement staff interaction (in the attunement staff class)

        if(level.isClientSide){
            return ItemInteractionResult.sidedSuccess(true);
        }
        else if(level.getBlockEntity(pos) instanceof VoidAltarEntity altar ){

            ItemStack altarHeldItem = altar.getHeldItem();
            if(!player.addItem(altarHeldItem)){
                player.drop(altarHeldItem, false);
            }
            altar.setHeldItem(ItemStack.EMPTY);

            if(stack.getItem().equals(RegisterItems.EMERALDINE_CORE.get()) || stack.getItem().equals(RegisterItems.TELLURITE_CORE.get())){

                if(altar.getCooldown() == 0){
                    altar.setHeldItem(stack);
                    player.setItemInHand(hand, ItemStack.EMPTY);
                    altar.setCooldown(300);
                }
                else{
                    player.displayClientMessage(Component.translatable("message.cognition.void_altar.on_cooldown",
                            Component.literal(String.valueOf(altar.getCooldown())).withStyle(ChatFormatting.GREEN)), true);
                }

            }

            return ItemInteractionResult.CONSUME;
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {

        if(level.isClientSide){
            return InteractionResult.sidedSuccess(true);
        }
        else if(level.getBlockEntity(pos) instanceof VoidAltarEntity altar){

            ItemStack altarHeldItem = altar.getHeldItem();
            if(!player.addItem(altarHeldItem)){
                player.drop(altarHeldItem, false);
            }
            altar.setHeldItem(ItemStack.EMPTY);

            return InteractionResult.CONSUME;
        }

        for(BlockPos position : MiscUtils.get2DAreaOfEffect(pos.below(), 4, 4.15f)){
            level.setBlockAndUpdate(position, Blocks.OBSIDIAN.defaultBlockState());
        } //todo: rmb to remove

        return super.useWithoutItem(state, level, pos, player, hitResult);
    }


}
