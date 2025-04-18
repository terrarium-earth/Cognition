package com.cyanogen.experienceobelisk.recipe.jei.info;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;

public class FillingRecipe extends AbstractInformationalRecipe {

    public FillingRecipe(Ingredient input, Ingredient catalyst, ItemStack output, String id) {
        super(input, catalyst, output, id, 0);
    }

    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<FillingRecipe>{
        public static final Type INSTANCE = new Type();
        public static final String ID = "filling";
    }


}
