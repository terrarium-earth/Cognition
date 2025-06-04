package com.cyanogen.cognition.recipe.jei.info;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;

public class InfectingRecipeOld extends AbstractInformationalRecipe {

    public InfectingRecipeOld(Ingredient input, Ingredient catalyst, ItemStack output, String id) {
        super(input, catalyst, output, id, 0);
    }

    public InfectingRecipeOld(Ingredient input, Ingredient catalyst, ItemStack output, String id, int count) {
        super(input, catalyst, output, id, count);
    }

    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<InfectingRecipeOld>{
        public static final Type INSTANCE = new Type();
        public static final String ID = "infecting";
    }

    public int getCount(){
        return count;
    }


}
