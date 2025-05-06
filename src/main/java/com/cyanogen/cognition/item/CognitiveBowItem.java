package com.cyanogen.cognition.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CognitiveBowItem extends BowItem {

    public CognitiveBowItem(Properties properties) {
        super(properties.durability(835));
    }

    @Override
    protected void shootProjectile(LivingEntity shooter, Projectile projectile, int index, float velocity, float inaccuracy, float angle, @Nullable LivingEntity target) {
        super.shootProjectile(shooter, projectile, index, velocity * 1.25f, 0.91f * inaccuracy, angle, target);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if(tooltipComponents.size() == 1){
            tooltipComponents.add(Component.literal(""));
            tooltipComponents.add(Component.literal("When in Main Hand:"));
        }

        tooltipComponents.add(Component.literal("+25% Projectile Speed").withStyle(ChatFormatting.BLUE));
        tooltipComponents.add(Component.literal("+10% Projectile Accuracy").withStyle(ChatFormatting.BLUE));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
