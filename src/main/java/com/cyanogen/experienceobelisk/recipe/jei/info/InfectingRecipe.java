package com.cyanogen.experienceobelisk.recipe.jei.info;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;

public class InfectingRecipe extends AbstractInformationalRecipe {

    public InfectingRecipe(Ingredient input, Ingredient catalyst, ItemStack output, String id) {
        super(input, catalyst, output, id, 0);
    }

    public InfectingRecipe(Ingredient input, Ingredient catalyst, ItemStack output, String id, int count) {
        super(input, catalyst, output, id, count);
    }

    public RecipeType<?> getType() {
        return InfectingRecipe.Type.INSTANCE;
    }

    public static class Type implements RecipeType<InfectingRecipe>{
        public static final InfectingRecipe.Type INSTANCE = new InfectingRecipe.Type();
        public static final String ID = "infecting";
    }

    public int getCount(){
        return count;
    }


}
