package com.cyanogen.experienceobelisk.item;

import com.cyanogen.experienceobelisk.registries.RegisterItems;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;

public class FortuitousAmuletItem extends EnlightenedAmuletItem{

    public static final float xpBoostForSmall = 1.6f;
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

        float xpBoost;
        if(xp <= 20){
            xpBoost = xpBoostForSmall;
        }
        else if(xp <= 50){
            xpBoost = xpBoostForMed;
        }
        else{
            xpBoost = xpBoostForLarge;
        }

        ItemStack amulet = RegisterItems.FORTUITOUS_AMULET.get().getDefaultInstance();
        amulet.getOrCreateTag().putBoolean("isActive", true);

        if(player != null && player.getInventory().contains(amulet)){

            System.out.println("xp changed");
            event.setDroppedExperience((int) (xp * xpBoost));
        }
    }

}
