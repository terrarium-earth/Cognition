package com.cyanogen.cognition.recipe.jei.info;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;

public class EmptyingRecipe extends AbstractInformationalRecipe {

    private final float xpGain;

    public EmptyingRecipe(Ingredient input, Ingredient catalyst, ItemStack output, String id, float xp) {
        super(input, catalyst, output, id, 0);
        this.xpGain = xp;
    }

    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<EmptyingRecipe>{
        public static final Type INSTANCE = new Type();
        public static final String ID = "emptying";
    }

    public float getXpGain(){
        return xpGain;
    }

    public int getIntegerXpGain(){
        return (int) getXpGain();
    }

    public int getCognitiumAmount(){
        return Math.round(getXpGain() * 20);
    }

}
