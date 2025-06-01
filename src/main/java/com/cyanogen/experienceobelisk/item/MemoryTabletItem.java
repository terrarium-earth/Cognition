package com.cyanogen.experienceobelisk.item;

import com.cyanogen.experienceobelisk.block.ExperienceObeliskBlock;
import com.cyanogen.experienceobelisk.block_entities.ExperienceObeliskEntity;
import com.cyanogen.experienceobelisk.config.Config;
import com.cyanogen.experienceobelisk.registries.RegisterSounds;
import com.cyanogen.experienceobelisk.saved_data.MemoryTabletData;
import com.cyanogen.experienceobelisk.utils.ExperienceUtils;
import com.cyanogen.experienceobelisk.utils.MiscUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.network.NetworkHooks;

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
            MemoryTabletData data = MemoryTabletData.getFromStorage(player);
            if(data != null && data.hasLinkedObelisk()){

                BlockPos pos = data.getLinkedObelisk();
                BlockState state = level.getBlockState(pos);

                if(!player.isShiftKeyDown()){
                    if(state.getBlock() instanceof ExperienceObeliskBlock
                            && data.dimensionMatches(level)
                            && MiscUtils.straightLineDistance(player.blockPosition(), pos) <= Config.COMMON.bindingRange.get()){

                        NetworkHooks.openScreen((ServerPlayer) player, state.getMenuProvider(level, pos), pos);
                    }
                    else{
                        player.displayClientMessage(Component.translatable("message.experienceobelisk.memory_tablet.out_of_range"), true);
                    }
                }
                else{
                    player.displayClientMessage(Component.translatable("message.experienceobelisk.memory_tablet.query",
                            Component.literal(pos.toShortString()).withStyle(ChatFormatting.GREEN),
                            Component.literal(data.getDimension()).withStyle(ChatFormatting.GRAY)), true);
                }
            }
            else{
                player.displayClientMessage(Component.translatable("message.experienceobelisk.memory_tablet.query_fail"), true);
            }
        }

        if(player.isShiftKeyDown()){
            return super.use(level, player, usedHand);
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(usedHand), level.isClientSide);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();

        if(level.getBlockEntity(pos) instanceof ExperienceObeliskEntity obelisk && player != null && player.isShiftKeyDown()){

            obelisk.syncFromStorage();
            MemoryTabletData data = MemoryTabletData.getFromStorage(player);

            if(obelisk.getSavedPlayer().isEmpty()){
                //new player or player switching obelisks
                obelisk.remember(player, data);

                player.displayClientMessage(Component.translatable("message.experienceobelisk.memory_tablet.link",
                        Component.literal(pos.toShortString()).withStyle(ChatFormatting.GREEN)), true);
                level.playSound(null, player.blockPosition(), RegisterSounds.MEMORY_TABLET_LINK.get(),
                        SoundSource.PLAYERS, 0.2f, 1f);
            }
            else if(obelisk.getSavedPlayer().equals(player.getStringUUID())){
                //player unlinking from linked obelisk
                obelisk.forget(player, data);

                player.displayClientMessage(Component.translatable("message.experienceobelisk.memory_tablet.unlink",
                        Component.literal(pos.toShortString()).withStyle(ChatFormatting.GREEN)), true);
                level.playSound(null, player.blockPosition(), RegisterSounds.MEMORY_TABLET_UNLINK.get(),
                        SoundSource.PLAYERS, 0.2f, 0.8f);
            }
            else{
                player.displayClientMessage(Component.translatable("message.experienceobelisk.memory_tablet.obelisk_in_use"), true);
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.useOn(context);
    }

    public static void handleDeath(LivingDeathEvent event){

        boolean keepInventory = event.getEntity().level().getGameRules().getRule(GameRules.RULE_KEEPINVENTORY).get();

        if(event.getEntity() instanceof Player player && !keepInventory){
            MemoryTabletData data = MemoryTabletData.getFromStorage(player);

            if(data != null && data.hasLinkedObelisk()){
                data.setExperienceToRecover(ExperienceUtils.getTotalXP(player));
            }
        }
    }

    public static void handleExperience(LivingExperienceDropEvent event) {
        if(event.getEntity() instanceof Player player){
            MemoryTabletData data = MemoryTabletData.getFromStorage(player);

            if(data != null && data.hasLinkedObelisk()){
                event.setDroppedExperience(0);
                event.setCanceled(true);
            }
        }
    }

}

