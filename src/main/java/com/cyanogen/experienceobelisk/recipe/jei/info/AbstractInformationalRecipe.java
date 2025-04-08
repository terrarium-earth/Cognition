package com.cyanogen.experienceobelisk.recipe.jei.info;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractInformationalRecipe implements Recipe<SimpleContainer> {

    //this is a dummy recipe used to display in-game mechanics in JEI

    private final Ingredient input;
    private final Ingredient catalyst;
    private final ItemStack output;
    private final String id;
    public final int count;

    public AbstractInformationalRecipe(Ingredient input, Ingredient catalyst, ItemStack output, String id, int count){
        this.input = input;
        this.catalyst = catalyst;
        this.output = output;
        this.id = id;
        this.count = count;
    }

    @Override
    public ItemStack assemble(SimpleContainer container, RegistryAccess access) {
        return output.copy();
    }

    public Ingredient getInput() {
        return input;
    }

    public Ingredient getCatalyst() {
        return catalyst;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(input);
        list.add(catalyst);
        return list;
    }

    @Override
    public ItemStack getResultItem(@Nullable RegistryAccess access) {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return new ResourceLocation(id);
    }

    //-----UNUSED-----//

    @Nullable
    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int d1, int d2) {
        return false;
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        return false;
    }
}
