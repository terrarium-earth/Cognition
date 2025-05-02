package com.cyanogen.cognition.item;

import com.cyanogen.cognition.block_entities.ExperienceObeliskEntity;
import com.cyanogen.cognition.registries.RegisterAttachments;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

public class MemoryTabletItem extends Item {

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<BlockPos>> obeliskLocation =
            RegisterAttachments.MEMORY_TABLET_OBELISK_LOCATION;

    public MemoryTabletItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {

        if(!level.isClientSide){
            if(player.hasData(obeliskLocation)){

                BlockPos pos = player.getData(obeliskLocation);
                player.displayClientMessage(Component.translatable("message.cognition.memory_tablet.query",
                        Component.literal(pos.toShortString()).withStyle(ChatFormatting.GREEN)), true);

            }
            else{
                player.displayClientMessage(Component.translatable("message.cognition.memory_tablet.query_fail"), true);
            }
        }

        return InteractionResultHolder.sidedSuccess(player.getItemInHand(usedHand), level.isClientSide);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();

        if(level.getBlockEntity(pos) instanceof ExperienceObeliskEntity obelisk && player != null && player.isShiftKeyDown()){

            if(!level.isClientSide){

                if(obelisk.hasBeenMemorized(player)){
                    player.removeData(obeliskLocation);
                    player.displayClientMessage(Component.translatable("message.cognition.memory_tablet.unlink",
                            Component.literal(pos.toShortString()).withStyle(ChatFormatting.GREEN)), true);

                    level.playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.2f, 0.8f);
                }
                else{
                    player.setData(obeliskLocation, pos);
                    player.displayClientMessage(Component.translatable("message.cognition.memory_tablet.link",
                            Component.literal(pos.toShortString()).withStyle(ChatFormatting.GREEN)), true);

                    level.playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.2f, 1f);
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return super.useOn(context);
    }

    public static void handleDeath(LivingDeathEvent event){

        boolean keepInventory = event.getEntity().level().getGameRules().getRule(GameRules.RULE_KEEPINVENTORY).get();

        if(event.getEntity() instanceof Player player && player.hasData(obeliskLocation) && !keepInventory){
            int levels = player.experienceLevel;
            float progress = player.experienceProgress;
            //this roundabout method is because serialize() does not accept Codec.LONG
            player.setData(RegisterAttachments.PLAYER_EXPERIENCE_LEVELS_ON_DEATH, levels);
            player.setData(RegisterAttachments.PLAYER_EXPERIENCE_PROGRESS_ON_DEATH, progress);
        }

    }

    public static void handleExperience(LivingExperienceDropEvent event) {
        if(event.getEntity() instanceof Player player && player.hasData(obeliskLocation)){
            event.setDroppedExperience(0);
            event.setCanceled(true);
        }
        //todo: check interactions with gravestone mods
    }

}
