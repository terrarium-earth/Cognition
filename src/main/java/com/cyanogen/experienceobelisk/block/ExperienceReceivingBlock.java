package com.cyanogen.experienceobelisk.block;

import com.cyanogen.experienceobelisk.block_entities.ExperienceObeliskEntity;
import com.cyanogen.experienceobelisk.block_entities.ExperienceReceivingEntity;
import com.cyanogen.experienceobelisk.registries.RegisterItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class ExperienceReceivingBlock extends Block {

    public ExperienceReceivingBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {

        BlockEntity e = level.getBlockEntity(pos);
        ItemStack heldItem = player.getItemInHand(hand);

        if(e instanceof ExperienceReceivingEntity entity && heldItem.is(RegisterItems.ATTUNEMENT_STAFF.get())){

            if(entity.isBound){

                BlockPos boundPos = entity.getBoundPos();

                if(level.getBlockEntity(boundPos) instanceof ExperienceObeliskEntity){
                    player.displayClientMessage(Component.translatable("message.experienceobelisk.binding_wand.reveal_bound_pos",
                            Component.literal(boundPos.toShortString()).withStyle(ChatFormatting.GREEN)), true);
                }
                else{
                    player.displayClientMessage(Component.translatable("message.experienceobelisk.binding_wand.obelisk_doesnt_exist",
                            Component.literal(boundPos.toShortString())).withStyle(ChatFormatting.RED), true);
                }

            }
            else{
                player.displayClientMessage(Component.translatable("message.experienceobelisk.binding_wand.not_yet_bound"), true);
            }
            return InteractionResult.sidedSuccess(true);
        }

        return InteractionResult.PASS;
    }



}
