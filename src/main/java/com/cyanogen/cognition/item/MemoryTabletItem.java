package com.cyanogen.cognition.item;

import com.cyanogen.cognition.block.ExperienceObeliskBlock;
import com.cyanogen.cognition.block_entities.ExperienceObeliskEntity;
import com.cyanogen.cognition.registries.RegisterSounds;
import com.cyanogen.cognition.saved_data.MemoryTabletData;
import com.cyanogen.cognition.utils.ExperienceUtils;
import com.cyanogen.cognition.utils.MiscUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
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
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;

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

        if(!player.isShiftKeyDown() && !level.isClientSide){
            MemoryTabletData data = MemoryTabletData.getFromStorage(player);
            if(data != null && data.hasLinkedObelisk()){

                BlockPos pos = data.getLinkedObelisk();
                BlockState state = level.getBlockState(pos);

                if(state.getBlock() instanceof ExperienceObeliskBlock obeliskBlock &&
                        MiscUtils.straightLineDistance(player.blockPosition(), pos) <= 48){

                    player.openMenu(obeliskBlock.getMenuProvider(state, level, pos));
                }
                else{
                    player.displayClientMessage(Component.translatable("message.cognition.memory_tablet.out_of_range"), true);
                }
            }
            else{
                player.displayClientMessage(Component.translatable("message.cognition.memory_tablet.query_fail"), true);
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

            MemoryTabletData data = MemoryTabletData.getFromStorage(player);

            if(obelisk.getSavedPlayer().isEmpty()){
                //new player or player switching obelisks
                obelisk.remember(player, data);

                player.displayClientMessage(Component.translatable("message.cognition.memory_tablet.link",
                        Component.literal(pos.toShortString()).withStyle(ChatFormatting.GREEN)), true);
                level.playSound(null, player.blockPosition(), RegisterSounds.MEMORY_TABLET_LINK.get(),
                        SoundSource.PLAYERS, 0.2f, 1f);
            }
            else if(obelisk.getSavedPlayer().equals(player.getStringUUID())){
                //player unlinking from linked obelisk
                obelisk.forget(player, data);

                player.displayClientMessage(Component.translatable("message.cognition.memory_tablet.unlink",
                        Component.literal(pos.toShortString()).withStyle(ChatFormatting.GREEN)), true);
                level.playSound(null, player.blockPosition(), RegisterSounds.MEMORY_TABLET_UNLINK.get(),
                        SoundSource.PLAYERS, 0.2f, 0.8f);
            }
            else{
                player.displayClientMessage(Component.translatable("message.cognition.memory_tablet.obelisk_in_use"), true);
            }

            return InteractionResult.sidedSuccess(false);
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
