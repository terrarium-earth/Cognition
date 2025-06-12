package com.cyanogen.experienceobelisk.recipe.jei.info;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;

public class _FillingRecipe extends _AbstractInformationalRecipe {

    public _FillingRecipe(Ingredient input, Ingredient catalyst, ItemStack output, String id) {
        super(input, catalyst, output, id, 0);
    }

    public RecipeType<?> getType() {
        return _FillingRecipe.Type.INSTANCE;
    }

    public static class Type implements RecipeType<_FillingRecipe>{
        public static final _FillingRecipe.Type INSTANCE = new _FillingRecipe.Type();
        public static final String ID = "filling";
    }


}
