package com.cyanogen.cognition.item;

import com.cyanogen.cognition.block_entities.ExperienceObeliskEntity;
import com.cyanogen.cognition.registries.RegisterAttachments;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
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
    public InteractionResult useOn(UseOnContext context) {

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();

        if(level.getBlockEntity(pos) instanceof ExperienceObeliskEntity && player != null && player.isShiftKeyDown()){

            player.setData(obeliskLocation, pos);
            player.setItemInHand(context.getHand(), ItemStack.EMPTY);
            //todo: play sound and particle
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
