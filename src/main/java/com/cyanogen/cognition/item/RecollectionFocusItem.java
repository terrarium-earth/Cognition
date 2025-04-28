package com.cyanogen.cognition.item;

import com.cyanogen.cognition.block_entities.ExperienceObeliskEntity;
import com.cyanogen.cognition.registries.RegisterAttachments;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

public class RecollectionFocusItem extends Item {

    public RecollectionFocusItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();

        if(level.getBlockEntity(pos) instanceof ExperienceObeliskEntity && player != null){
            player.setData(RegisterAttachments.RECOLLECTION_FOCUS_OBELISK_LOCATION, pos);
            player.getItemInHand(context.getHand()).shrink(1);
        }

        return super.useOn(context);
    }

    public static void handleDeath(LivingDeathEvent event){
        if(event.getEntity() instanceof Player player && player.hasData(RegisterAttachments.RECOLLECTION_FOCUS_OBELISK_LOCATION)){
            int levels = player.experienceLevel;
            float progress = player.experienceProgress;
            //this roundabout method is because serialize() does not accept Codec.LONG
            player.setData(RegisterAttachments.PLAYER_EXPERIENCE_LEVELS_ON_DEATH, levels);
            player.setData(RegisterAttachments.PLAYER_EXPERIENCE_PROGRESS_ON_DEATH, progress);
        }

    }

}
