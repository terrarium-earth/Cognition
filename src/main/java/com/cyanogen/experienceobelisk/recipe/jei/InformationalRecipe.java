package com.cyanogen.experienceobelisk.recipe.jei;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class InformationalRecipe implements Recipe<SimpleContainer> {

    //this is a dummy recipe used to display in-game mechanics in JEI

    private final Ingredient input;
    private final Ingredient catalyst;
    private final ItemStack output;
    private final String actionType;
    private final String id;

    public InformationalRecipe(Ingredient input, Ingredient catalyst, ItemStack output, String actionType, String id){
        this.input = input;
        this.catalyst = catalyst;
        this.output = output;
        this.actionType = actionType;
        this.id = id;
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

    public String getActionType() {
        return actionType;
    }

    @Override
    public ResourceLocation getId() {
        return new ResourceLocation(id);
    }

    @Override
    public RecipeType<?> getType() {
        return InformationalRecipe.Type.INSTANCE;
    }

    public static class Type implements RecipeType<InformationalRecipe>{
        public static final InformationalRecipe.Type INSTANCE = new InformationalRecipe.Type();
        public static final String ID = "informational";
    }

    //-----UNUSED-----//

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
