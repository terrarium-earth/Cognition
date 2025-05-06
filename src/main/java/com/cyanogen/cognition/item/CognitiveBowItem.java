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

    private final float velocityMultiplier = 1.25f;
    private final float accuracyMultiplier = 1.20f;

    public CognitiveBowItem(Properties properties) {
        super(properties.durability(835));
    }

    @Override
    protected void shootProjectile(LivingEntity shooter, Projectile projectile, int index, float velocity, float inaccuracy, float angle, @Nullable LivingEntity target) {
        super.shootProjectile(shooter, projectile, index, velocity * velocityMultiplier, inaccuracy / accuracyMultiplier, angle, target);
    }

    public int convertToPercentage(float multiplier){
        return (int) Math.floor((multiplier - 1) * 100);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if(tooltipComponents.size() == 1){
            tooltipComponents.add(Component.literal(""));
            tooltipComponents.add(Component.translatable("item.modifiers.mainhand").withStyle(ChatFormatting.GRAY));
        }

        tooltipComponents.add(Component.literal("+" + convertToPercentage(velocityMultiplier) + "% Projectile Velocitye").withStyle(ChatFormatting.BLUE));
        tooltipComponents.add(Component.literal("+" + convertToPercentage(accuracyMultiplier) + "% Projectile Accuracy").withStyle(ChatFormatting.BLUE));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
