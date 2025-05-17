package com.cyanogen.cognition.item;

import com.cyanogen.cognition.block_entities.ExperienceObeliskEntity;
import com.cyanogen.cognition.registries.RegisterAttachments;
import com.cyanogen.cognition.registries.RegisterSounds;
import com.cyanogen.cognition.utils.ExperienceUtils;
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
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

public class MemoryTabletItem extends Item {

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<BlockPos>> LINKED_OBELISK_POS = RegisterAttachments.LINKED_OBELISK_POS;
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Long>> EXPERIENCE_UPON_DEATH = RegisterAttachments.EXPERIENCE_UPON_DEATH;

    public MemoryTabletItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {

        if(!level.isClientSide && !player.isShiftKeyDown()){
            if(player.hasData(LINKED_OBELISK_POS)){

                BlockPos pos = player.getData(LINKED_OBELISK_POS);
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
                    player.removeData(LINKED_OBELISK_POS);

                    player.displayClientMessage(Component.translatable("message.cognition.memory_tablet.unlink",
                            Component.literal(pos.toShortString()).withStyle(ChatFormatting.GREEN)), true);
                    level.playSound(null, player.blockPosition(), RegisterSounds.MEMORY_TABLET_UNLINK.get(), SoundSource.PLAYERS, 0.2f, 0.8f);
                }
                else{
                    player.setData(LINKED_OBELISK_POS, obelisk.getBlockPos());

                    player.displayClientMessage(Component.translatable("message.cognition.memory_tablet.link",
                            Component.literal(pos.toShortString()).withStyle(ChatFormatting.GREEN)), true);
                    level.playSound(null, player.blockPosition(), RegisterSounds.MEMORY_TABLET_LINK.get(), SoundSource.PLAYERS, 0.2f, 1f);
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return super.useOn(context);
    }

    public static void handleDeath(LivingDeathEvent event){

        boolean keepInventory = event.getEntity().level().getGameRules().getRule(GameRules.RULE_KEEPINVENTORY).get();

        if(event.getEntity() instanceof Player player && player.hasData(LINKED_OBELISK_POS) && !keepInventory){
            player.setData(EXPERIENCE_UPON_DEATH, ExperienceUtils.getTotalXP(player));
        }
    }

    public static void handleExperience(LivingExperienceDropEvent event) {
        if(event.getEntity() instanceof Player player && player.hasData(LINKED_OBELISK_POS)){
            event.setDroppedExperience(0);
            event.setCanceled(true);
        }
    }

}
