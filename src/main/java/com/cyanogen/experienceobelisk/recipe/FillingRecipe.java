package com.cyanogen.experienceobelisk.recipe;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class FillingRecipe implements Recipe<SimpleContainer> {

    private final Ingredient ingredient;
    private final ItemStack result;
    private final int mBcost;
    private final ResourceLocation id;

    public FillingRecipe(Ingredient ingredient, ItemStack result, int mBcost, ResourceLocation id){
        this.ingredient = ingredient;
        this.result = result;
        this.mBcost = mBcost;
        this.id = id;
    }

    public static @Nullable FillingRecipe getRecipe(Level level, ItemStack stack){

        List<FillingRecipe> recipeList =
                level.getRecipeManager().getAllRecipesFor(Type.INSTANCE);
        FillingRecipe infectingRecipe = null;

        for(FillingRecipe recipe : recipeList){
            if(recipe.matches(stack)){
                infectingRecipe = recipe;
                break;
            }
        }
        return infectingRecipe;
    }

    public boolean matches(ItemStack stack){
        return matches(new SimpleContainer(stack), null);
    }

    @Override
    public boolean matches(SimpleContainer container, @Nullable Level level) {
        return ingredient.test(container.getItem(0));
    }

    @Override
    public ItemStack assemble(@Nullable SimpleContainer container, @Nullable RegistryAccess access) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem(@Nullable RegistryAccess access) {
        return result.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    public Ingredient getIngredient(){
        return ingredient;
    }

    public int getCognitiumCost() {
        return mBcost;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<FillingRecipe>{
        public static final Type INSTANCE = new Type();
        public static final String ID = "filling";
    }

    public static class Serializer implements RecipeSerializer<FillingRecipe>{

        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(ExperienceObelisk.MOD_ID, "filling");

        @Override
        public FillingRecipe fromJson(ResourceLocation id, JsonObject recipe) {

            Ingredient ingredient = Ingredient.fromJson(GsonHelper.getNonNull(recipe, "ingredient"));
            ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(recipe, "result"));
            int mBcost = GsonHelper.getAsInt(recipe, "cost_mB");

            return new FillingRecipe(ingredient, result, mBcost, id);
        }

        @Override
        public @Nullable FillingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            int mBcost = buffer.readInt();

            return new FillingRecipe(ingredient, result, mBcost, id);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, FillingRecipe recipe) {

            recipe.getIngredient().toNetwork(buffer);
            buffer.writeItemStack(recipe.getResultItem(null), false);
            buffer.writeInt(recipe.mBcost);
        }

    }
}
