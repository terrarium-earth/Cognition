package com.cyanogen.experienceobelisk.item;

import com.cyanogen.experienceobelisk.block_entities.ExperienceObeliskEntity;
import com.cyanogen.experienceobelisk.block_entities.ExperienceReceivingEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

public class AttunementStaffItem extends Item {

    public AttunementStaffItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return pStack.getOrCreateTag().contains("boundX");
    }

    public void summonOrbs(Level pLevel, Player pPlayer, Vec3 clickLocation) {
        long totalXP = ExperienceObeliskEntity.getTotalXP(pPlayer);
        int value = (int) Math.floor(Math.random() * 7 + 1);    //random value from 1 to 7

        if(totalXP < value){
            value = (int) totalXP;
        }

        if(!pLevel.isClientSide && totalXP != 0){
            ServerLevel server = (ServerLevel) pLevel;
            server.addFreshEntity(new ExperienceOrb(server, clickLocation.x, clickLocation.y, clickLocation.z, value));
            pPlayer.giveExperiencePoints(-value);
        }

    }

    @Override
    public InteractionResult useOn(UseOnContext context) {

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Vec3 clickLocation = context.getClickLocation();
        BlockEntity entity = level.getBlockEntity(pos);
        ItemStack stack = context.getItemInHand();
        Player player = context.getPlayer();

        CompoundTag tag = stack.getOrCreateTag();

        if(player != null && player.isShiftKeyDown()){

            if(entity instanceof ExperienceObeliskEntity){

                if(tag.contains("boundX") && tag.getInt("boundX") == pos.getX()){
                    tag.remove("boundX");
                    tag.remove("boundY");
                    tag.remove("boundZ");

                    player.displayClientMessage(new TranslatableComponent("message.experienceobelisk.binding_wand.unbind_obelisk"), true);
                }
                else{
                    tag.putInt("boundX", pos.getX());
                    tag.putInt("boundY", pos.getY());
                    tag.putInt("boundZ", pos.getZ());

                    player.displayClientMessage(new TranslatableComponent("message.experienceobelisk.binding_wand.bind_obelisk"), true);
                }

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

                return InteractionResult.CONSUME;
            }
        }
        else if(player != null){
            summonOrbs(level, player, clickLocation);
        }
        return super.useOn(context);

    }


}
