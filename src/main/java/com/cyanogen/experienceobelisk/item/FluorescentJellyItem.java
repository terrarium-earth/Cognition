package com.cyanogen.experienceobelisk.item;

import com.cyanogen.experienceobelisk.config.Config;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class FluorescentJellyItem extends Item {

    public FluorescentJellyItem(Properties properties) {
        super(properties);
    }

    public int getNutrition(){
        return Config.COMMON.jellyNutrition.get();
    }

    public float getSaturation(){
        double saturation = Config.COMMON.jellySaturation.get();
        return (float) saturation;
    }

    public boolean canBeEaten(){
        return Config.COMMON.jellyNutrition.get() > 0;
    }

    @Override
    public @Nullable FoodProperties getFoodProperties(ItemStack stack, @Nullable LivingEntity entity) {
        return new FoodProperties.Builder().nutrition(getNutrition()).saturationModifier(getSaturation()).build();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);
        FoodData data = player.getFoodData();

        if((data.needsFood() || player.isCreative()) && canBeEaten()){

            data.eat(getNutrition(), getSaturation());
            player.playSound(SoundEvents.GENERIC_EAT);

            if(!player.isCreative()){
                stack.shrink(1);
            }

            return InteractionResultHolder.consume(stack);
        }
        else{
            return super.use(level, player, hand);
        }
    }

}
