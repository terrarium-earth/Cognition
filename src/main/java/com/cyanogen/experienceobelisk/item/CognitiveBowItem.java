package com.cyanogen.experienceobelisk.item;

import com.cyanogen.experienceobelisk.config.Config;
import com.cyanogen.experienceobelisk.registries.RegisterItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.cyanogen.experienceobelisk.ExperienceObelisk.MOD_ID;

public class CognitiveBowItem extends BowItem {

    private final float velocityMultiplier;
    private final float accuracyMultiplier;

    public CognitiveBowItem(int maxDamage, float velocityMultiplier, float accuracyMultiplier) {
        super(new Item.Properties().durability(maxDamage));
        this.velocityMultiplier = velocityMultiplier;
        this.accuracyMultiplier = accuracyMultiplier;
    }

    public static void registerProperties(){
        ResourceLocation pulling = new ResourceLocation(MOD_ID + ":pulling");
        ResourceLocation pull = new ResourceLocation(MOD_ID + ":pull");

        ItemProperties.register(RegisterItems.COGNITIVE_BOW.get(), pulling,
                (stack,level,entity,seed) -> getPull(stack, entity, true));
        ItemProperties.register(RegisterItems.COGNITIVE_BOW.get(), pull,
                (stack,level,entity,seed) -> getPull(stack, entity, false));
    }

    public static float getPull(ItemStack stack, Entity entity, boolean binary){
        float pull = 0.0f;
        if(entity instanceof Player player && stack.getItem() instanceof CognitiveBowItem && Objects.equals(player.getUseItem(), stack)){
            pull = binary ? 1.0f : (float) player.getTicksUsingItem() / 20;
        }
        return pull;
    }

    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (entity instanceof Player player) {
            boolean flag = player.getAbilities().instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0;
            ItemStack itemstack = player.getProjectile(stack);

            int i = this.getUseDuration(stack) - timeLeft;
            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, level, player, i, !itemstack.isEmpty() || flag);
            if (i < 0) return;

            if (!itemstack.isEmpty() || flag) {
                if (itemstack.isEmpty()) {
                    itemstack = new ItemStack(Items.ARROW);
                }

                float f = getPowerForTime(i);
                //ADDITIONS
                float velocity = f * 3.0f * velocityMultiplier;
                float inaccuracy = 1 / accuracyMultiplier;

                if (!((double)f < 0.1D)) {
                    boolean flag1 = player.getAbilities().instabuild || (itemstack.getItem() instanceof ArrowItem && ((ArrowItem)itemstack.getItem()).isInfinite(itemstack, stack, player));
                    if (!level.isClientSide) {
                        ArrowItem arrowitem = (ArrowItem)(itemstack.getItem() instanceof ArrowItem ? itemstack.getItem() : Items.ARROW);
                        AbstractArrow abstractarrow = arrowitem.createArrow(level, itemstack, player);
                        abstractarrow = customArrow(abstractarrow);
                        abstractarrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, velocity, inaccuracy);
                        if (f == 1.0F) {
                            abstractarrow.setCritArrow(true);
                        }

                        int j = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack);
                        if (j > 0) {
                            abstractarrow.setBaseDamage(abstractarrow.getBaseDamage() + (double)j * 0.5D + 0.5D);
                        }

                        int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack);
                        if (k > 0) {
                            abstractarrow.setKnockback(k);
                        }

                        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack) > 0) {
                            abstractarrow.setSecondsOnFire(100);
                        }

                        stack.hurtAndBreak(1, player, (p_289501_) -> {
                            p_289501_.broadcastBreakEvent(player.getUsedItemHand());
                        });
                        if (flag1 || player.getAbilities().instabuild && (itemstack.is(Items.SPECTRAL_ARROW) || itemstack.is(Items.TIPPED_ARROW))) {
                            abstractarrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                        }

                        level.addFreshEntity(abstractarrow);
                    }

                    level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                    if (!flag1 && !player.getAbilities().instabuild) {
                        itemstack.shrink(1);
                        if (itemstack.isEmpty()) {
                            player.getInventory().removeItem(itemstack);
                        }
                    }

                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

    public String getPercentageString(float multiplier){
        return Math.round((multiplier - 1) * 100) + "%";
    }

    public static void handleTooltip(ItemTooltipEvent event){
        List<Component> tooltipList = event.getToolTip();

        if(event.getItemStack().getItem() instanceof CognitiveBowItem bow && Config.COMMON.showAdditionalBowInfo.get()){

            List<Component> tooltips = new ArrayList<>();
            tooltips.add(Component.literal(""));
            tooltips.add(Component.translatable("tooltip.experienceobelisk.cognitive_bow.firing").withStyle(ChatFormatting.GRAY));

            tooltips.add(Component.translatable("tooltip.experienceobelisk.cognitive_bow.velocity_multiplier",
                    Component.literal(bow.getPercentageString(bow.velocityMultiplier)).withStyle(ChatFormatting.BLUE)));
            tooltips.add(Component.translatable("tooltip.experienceobelisk.cognitive_bow.accuracy_multiplier",
                    Component.literal(bow.getPercentageString(bow.accuracyMultiplier)).withStyle(ChatFormatting.BLUE)));

            if(event.getFlags().isAdvanced()){
                tooltipList.addAll(tooltipList.size() - 2, tooltips);
            }
            else{
                tooltipList.addAll(tooltips);
            }
        }
    }



}
