package com.cyanogen.experienceobelisk.item;

import com.cyanogen.experienceobelisk.block_entities.ExperienceObeliskEntity;
import com.cyanogen.experienceobelisk.saved_data.MemoryTabletData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;

import static com.cyanogen.experienceobelisk.saved_data.MemoryTabletData.getFromStorage;
import static com.cyanogen.experienceobelisk.saved_data.MemoryTabletData.createAndSaveToStorage;

public class MemoryTabletItem extends Item {

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
            MemoryTabletData data = getFromStorage(player);

            if(data != null){
                if(data.hasLinkedObelisk()){
                    player.displayClientMessage(Component.translatable("message.cognition.memory_tablet.query"), true);
                }
                else{
                    player.displayClientMessage(Component.translatable("message.cognition.memory_tablet.query_fail"), true);
                }
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
                MemoryTabletData data = getFromStorage(player);

                if(obelisk.hasBeenMemorized(player)){
                    obelisk.removeFromObelisk(player);
                    if(data != null){
                        data.setLinkedObelisk(false);
                    }

                    player.displayClientMessage(Component.translatable("message.cognition.memory_tablet.unlink",
                            Component.literal(pos.toShortString()).withStyle(ChatFormatting.GREEN)), true);
                    //level.playSound(null, player.blockPosition(), RegisterSounds.MEMORY_TABLET_UNLINK.get(), SoundSource.PLAYERS, 0.2f, 0.8f);
                }
                else {
                    boolean success = obelisk.saveToObelisk(player);
                    if (success) {
                        data = new MemoryTabletData();
                        data.setLinkedObelisk(true);
                        createAndSaveToStorage(player, data);

                        player.displayClientMessage(Component.translatable("message.cognition.memory_tablet.link",
                                Component.literal(pos.toShortString()).withStyle(ChatFormatting.GREEN)), true);
                        //level.playSound(null, player.blockPosition(), RegisterSounds.MEMORY_TABLET_LINK.get(), SoundSource.PLAYERS, 0.2f, 1f);
                    }
                    else {
                        player.displayClientMessage(Component.translatable("message.cognition.memory_tablet.link_failed",
                                Component.literal(pos.toShortString()).withStyle(ChatFormatting.RED)), true);
                    }
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return super.useOn(context);
    }

    public static void handleDeath(LivingDeathEvent event){

        Level level = event.getEntity().level();
        boolean keepInventory = level.getGameRules().getRule(GameRules.RULE_KEEPINVENTORY).get();

        if(event.getEntity() instanceof Player player && !keepInventory){

            MemoryTabletData data = getFromStorage(player);

            if(data != null && data.hasLinkedObelisk()){
                int levels = player.experienceLevel;
                float progress = player.experienceProgress;

                data.set(true, levels, progress);
            }
        }

    }

    public static void handleExperience(LivingExperienceDropEvent event) {
        if(event.getEntity() instanceof Player player){

            MemoryTabletData data = getFromStorage(player);
            if(data != null && data.hasLinkedObelisk()){
                event.setDroppedExperience(0);
                event.setCanceled(true);
            }
        }
    }

}

