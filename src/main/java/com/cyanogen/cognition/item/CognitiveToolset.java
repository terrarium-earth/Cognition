package com.cyanogen.cognition.item;

import com.cyanogen.cognition.registries.RegisterItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

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
            return 835;
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

        @Override
        protected void shootProjectile(LivingEntity shooter, Projectile projectile, int index, float velocity, float inaccuracy, float angle, @Nullable LivingEntity target) {
            super.shootProjectile(shooter, projectile, index, velocity * velocityMultiplier, inaccuracy / accuracyMultiplier, angle, target);
        }

        public String getPercentageString(float multiplier){
            return (int) Math.floor((multiplier - 1) * 100) + "%";
        }

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
            if(tooltipComponents.size() == 1){
                tooltipComponents.add(Component.literal(""));
                tooltipComponents.add(Component.translatable("item.modifiers.mainhand").withStyle(ChatFormatting.GRAY));
            }

            tooltipComponents.add(Component.translatable("tooltip.cognition.cognitive_bow.velocity_multiplier",
                    Component.literal(getPercentageString(velocityMultiplier)).withStyle(ChatFormatting.BLUE)));

            tooltipComponents.add(Component.translatable("tooltip.cognition.cognitive_bow.accuracy_multiplier",
                    Component.literal(getPercentageString(accuracyMultiplier)).withStyle(ChatFormatting.BLUE)));

            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
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

    }


}
