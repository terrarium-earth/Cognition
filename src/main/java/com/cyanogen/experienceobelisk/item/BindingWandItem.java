package com.cyanogen.experienceobelisk.item;

import com.cyanogen.experienceobelisk.block_entities.ExperienceObeliskEntity;
import com.cyanogen.experienceobelisk.block_entities.ExperienceReceivingEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class BindingWandItem extends Item {

    public BindingWandItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public boolean isFoil(ItemStack p_41453_) {
        return true;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, net.minecraftforge.common.ToolAction toolAction) {
        return net.minecraftforge.common.ToolActions.DEFAULT_SWORD_ACTIONS.contains(toolAction);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockEntity entity = level.getBlockEntity(pos);
        ItemStack stack = context.getItemInHand();
        Player player = context.getPlayer();

        CompoundTag tag = stack.getOrCreateTag();

        if(player != null && player.isShiftKeyDown()){

            if(entity instanceof ExperienceObeliskEntity){

                tag.putInt("boundX", pos.getX());
                tag.putInt("boundY", pos.getY());
                tag.putInt("boundZ", pos.getZ());

                player.displayClientMessage(new TranslatableComponent("message.experienceobelisk.binding_wand.select_obelisk"), true);

                return InteractionResult.CONSUME;
            }
            else if(entity instanceof ExperienceReceivingEntity receivingEntity){

                if(receivingEntity.isBound){
                    receivingEntity.setUnbound();
                    player.displayClientMessage(new TranslatableComponent("message.experienceobelisk.binding_wand.unbind_target"), true);
                }
                else if(tag.contains("boundX")){     //check if wand has an obelisk stored

                    BlockPos obeliskPos = new BlockPos(tag.getInt("boundX"), tag.getInt("boundY"), tag.getInt("boundZ"));

                    if(level.getBlockEntity(obeliskPos) instanceof ExperienceObeliskEntity){       //check if obelisk at location still exists

                        if(pos.distSqr(obeliskPos) <= 320){     //check if obelisk is within the effective radius
                            receivingEntity.setBoundPos(obeliskPos);
                            receivingEntity.setBound();

                            player.displayClientMessage(new TranslatableComponent("message.experienceobelisk.binding_wand.bind_target",
                                    new TextComponent(obeliskPos.toShortString()).withStyle(ChatFormatting.GREEN)), true);
                        }
                        else{
                            player.displayClientMessage(new TranslatableComponent("message.experienceobelisk.binding_wand.obelisk_too_far"), true);
                        }
                    }
                    else{
                        player.displayClientMessage(new TranslatableComponent("message.experienceobelisk.binding_wand.obelisk_doesnt_exist",
                                new TextComponent(obeliskPos.toShortString())).withStyle(ChatFormatting.RED), true);
                    }
                }
                else{
                    player.displayClientMessage(new TranslatableComponent("message.experienceobelisk.binding_wand.obelisk_not_selected"), true);
                }

                return InteractionResult.CONSUME;
            }
        }
        return super.useOn(context);

    }


}
