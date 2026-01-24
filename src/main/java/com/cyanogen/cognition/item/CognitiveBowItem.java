package com.cyanogen.cognition.item;

import com.cyanogen.cognition.config.Config;
import com.cyanogen.cognition.registries.RegisterItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.cyanogen.cognition.Cognition.MOD_ID;

public class CognitiveBowItem extends BowItem {

    private final float velocityMultiplier;
    private final float accuracyMultiplier;

    public CognitiveBowItem(int maxDamage, float velocityMultiplier, float accuracyMultiplier) {
        super(new Item.Properties().durability(maxDamage));
        this.velocityMultiplier = velocityMultiplier;
        this.accuracyMultiplier = accuracyMultiplier;
    }

    public static void registerProperties(){
        ItemProperties.register(RegisterItems.COGNITIVE_BOW.get(), ResourceLocation.fromNamespaceAndPath(MOD_ID, "pulling"),
                (stack,level,entity,seed) -> getPull(stack, entity, true));
        ItemProperties.register(RegisterItems.COGNITIVE_BOW.get(), ResourceLocation.fromNamespaceAndPath(MOD_ID, "pull"),
                (stack,level,entity,seed) -> getPull(stack, entity, false));
    }

    public static float getPull(ItemStack stack, Entity entity, boolean binary){
        float pull = 0.0f;
        if(entity instanceof Player player && stack.getItem() instanceof CognitiveBowItem && Objects.equals(player.getUseItem(), stack)){
            pull = binary ? 1.0f : (float) player.getTicksUsingItem() / 20;
        }
        return pull;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player player) {
            ItemStack itemstack = player.getProjectile(stack);
            if (!itemstack.isEmpty()) {
                int i = this.getUseDuration(stack, entityLiving) - timeLeft;
                i = EventHooks.onArrowLoose(stack, level, player, i, !itemstack.isEmpty());
                if (i < 0) {
                    return;
                }

                float f = getPowerForTime(i);
                float velocity = f * 3.0f * velocityMultiplier;
                float inaccuracy = 1 / accuracyMultiplier;
                if (!((double)f < 0.1)) {
                    List<ItemStack> list = draw(stack, itemstack, player);
                    if (level instanceof ServerLevel serverlevel) {
                        if (!list.isEmpty()) {
                            this.shoot(serverlevel, player, player.getUsedItemHand(), stack, list, velocity, inaccuracy, f == 1.0F, null);
                        }
                    }
                    level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
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
            tooltips.add(Component.translatable("tooltip.cognition.cognitive_bow.firing").withStyle(ChatFormatting.GRAY));

            tooltips.add(Component.translatable("tooltip.cognition.cognitive_bow.velocity_multiplier",
                    Component.literal(bow.getPercentageString(bow.velocityMultiplier)).withStyle(ChatFormatting.BLUE)));
            tooltips.add(Component.translatable("tooltip.cognition.cognitive_bow.accuracy_multiplier",
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