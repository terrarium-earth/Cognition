package com.cyanogen.cognition.item;

import com.cyanogen.cognition.registries.RegisterItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.cyanogen.cognition.Cognition.MOD_ID;

public class CognitiveToolset {

    //-----TOOLSET-----//

    public static final AttributeModifier HANDHELD_RANGE =
            new AttributeModifier(ResourceLocation.fromNamespaceAndPath("cognition", "handheld_range"),
                    1.0, AttributeModifier.Operation.ADD_VALUE);

    public static List<ItemAttributeModifiers.Entry> increasedReach(){
        List<ItemAttributeModifiers.Entry> list = new ArrayList<>();
        list.add(new ItemAttributeModifiers.Entry(Attributes.BLOCK_INTERACTION_RANGE, HANDHELD_RANGE, EquipmentSlotGroup.MAINHAND));
        list.add(new ItemAttributeModifiers.Entry(Attributes.ENTITY_INTERACTION_RANGE, HANDHELD_RANGE, EquipmentSlotGroup.MAINHAND));
        return list;
    }

    public static Item.Properties createCustomAttributes(Item.Properties properties, @Nullable ItemAttributeModifiers baseModifiers, List<ItemAttributeModifiers.Entry> extraModifiers){
        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
        if(baseModifiers != null){
            for(ItemAttributeModifiers.Entry entry : baseModifiers.modifiers()) {
                builder.add(entry.attribute(), entry.modifier(), entry.slot());
            }
        }
        for(ItemAttributeModifiers.Entry entry : extraModifiers) {
            builder.add(entry.attribute(), entry.modifier(), entry.slot());
        }
        return properties.attributes(builder.build());
    }

    public static Tier COGNITIVE_TIER = new Tier() {

        @Override
        public TagKey<Block> getIncorrectBlocksForDrops() {
            return BlockTags.INCORRECT_FOR_IRON_TOOL;
        }

        @Override
        public int getUses() {
            return 921;
        }

        @Override
        public float getSpeed() {
            return 7.0F;
        }

        @Override
        public float getAttackDamageBonus() {
            return 3.0F;
        }

        @Override
        public int getEnchantmentValue() {
            return 15;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(RegisterItems.COGNITIVE_ALLOY.get());
        }
    };

    //-----BOW-----//

    public static class CognitiveBowItem extends BowItem{

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

            if(event.getItemStack().getItem() instanceof CognitiveBowItem bow){

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

    //-----FLINT AND COG-----//

    public static class FlintAndCognitiveAlloyItem extends FlintAndSteelItem{

        public FlintAndCognitiveAlloyItem(int maxDamage) {
            super(createCustomAttributes(new Item.Properties(), null, increasedReach()).durability(maxDamage));
        }

        @Override
        public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand usedHand) {
            if(interactionTarget instanceof Creeper creeper){
                creeper.ignite();
                player.level().playSound(player, player.blockPosition(), SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, player.level().getRandom().nextFloat() * 0.4F + 0.8F);
                return InteractionResult.sidedSuccess(player.level().isClientSide);
            }
            return super.interactLivingEntity(stack, player, interactionTarget, usedHand);
        }

        @Override
        public InteractionResult useOn(UseOnContext context) {

            BlockPos pos = context.getClickedPos();
            Level level = context.getLevel();
            BlockState state = level.getBlockState(pos);
            Player player = context.getPlayer();

            if(state.getBlock() instanceof TntBlock tnt){
                tnt.onCaughtFire(state, level, pos, null, player);
                level.removeBlock(pos, false);
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
            return super.useOn(context);
        }

    }


}
