package com.cyanogen.cognition.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;

public class FortuitousAmuletItem extends EnlightenedAmuletItem{

    public static final float xpBoostForSmall = 1.7f;
    public static final float xpBoostForMed = 1.4f;
    public static final float xpBoostForLarge = 1.2f;

    public FortuitousAmuletItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {

        if(entity instanceof Player player && isActive(stack) && !level.isClientSide && level.getGameTime() % 20 == 0){
            player.forceAddEffect(new MobEffectInstance(MobEffects.LUCK, 21, 1, false, false), null);
        }

        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }

    public static void handleExperience(LivingExperienceDropEvent event) {
        int xp = event.getOriginalExperience();
        Player player = event.getAttackingPlayer();

        if(player != null && player.getInventory().contains((stack) -> stack.getItem() instanceof FortuitousAmuletItem amulet &&
                amulet.isActive(stack))){

            float xpBoost;
            if(xp <= 25){
                xpBoost = xpBoostForSmall;
            }
            else if(xp <= 100){
                xpBoost = xpBoostForMed;
            }
            else{
                xpBoost = xpBoostForLarge;
            }

            event.setDroppedExperience((int) (xp * xpBoost));
        }
    }


}
