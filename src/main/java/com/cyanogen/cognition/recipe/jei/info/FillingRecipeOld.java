package com.cyanogen.cognition.recipe.jei.info;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;

public class FillingRecipeOld extends AbstractInformationalRecipe {

    private final float xpCost;

    public FillingRecipeOld(Ingredient input, Ingredient catalyst, ItemStack output, String id, float xp) {
        super(input, catalyst, output, id, 0);
        this.xpCost = xp;
    }

    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<FillingRecipeOld>{
        public static final Type INSTANCE = new Type();
        public static final String ID = "filling";
    }

    public float getXpCost(){
        return xpCost;
    }

    public int getIntegerXpCost(){
        return (int) getXpCost();
    }

}
