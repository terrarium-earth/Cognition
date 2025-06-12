package com.cyanogen.experienceobelisk.recipe;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.cyanogen.experienceobelisk.registries.RegisterRecipes;
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

public class EmptyingRecipe implements Recipe<SimpleContainer> {

    private final Ingredient ingredient;
    private final ItemStack result;
    private final boolean hasResultStack;
    private final int mBgain;
    private final ResourceLocation id;

    public EmptyingRecipe(Ingredient ingredient, ItemStack result, boolean hasResultStack, int mBgain, ResourceLocation id){
        this.ingredient = ingredient;
        this.result = result;
        this.hasResultStack = hasResultStack;
        this.mBgain = mBgain;
        this.id = id;
    }

    public static @Nullable EmptyingRecipe getRecipe(Level level, ItemStack stack){

        List<EmptyingRecipe> recipeList =
                level.getRecipeManager().getAllRecipesFor(RegisterRecipes.EMPTYING_TYPE.get());
        EmptyingRecipe infectingRecipe = null;

        for(EmptyingRecipe recipe : recipeList){
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

    public boolean hasResultStack(){
        return hasResultStack;
    }

    public int getCognitiumGain() {
        return mBgain;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<EmptyingRecipe>{
        public static final Type INSTANCE = new Type();
        public static final String ID = "emptying";
    }

    public static class Serializer implements RecipeSerializer<EmptyingRecipe>{

        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(ExperienceObelisk.MOD_ID, "emptying");

        @Override
        public EmptyingRecipe fromJson(ResourceLocation id, JsonObject recipe) {

            Ingredient ingredient = Ingredient.fromJson(GsonHelper.getNonNull(recipe, "ingredient"));
            ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(recipe, "result"));
            boolean hasResultStack = GsonHelper.getAsBoolean(recipe, "hasResultStack");
            int mBgain = GsonHelper.getAsInt(recipe, "gain_mB");

            return new EmptyingRecipe(ingredient, result, hasResultStack, mBgain, id);
        }

        @Override
        public @Nullable EmptyingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            boolean hasResultStack = buffer.readBoolean();
            int mBgain = buffer.readInt();

            return new EmptyingRecipe(ingredient, result, hasResultStack, mBgain, id);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, EmptyingRecipe recipe) {

            recipe.getIngredient().toNetwork(buffer);
            buffer.writeItemStack(recipe.getResultItem(null), false);
            buffer.writeBoolean(recipe.hasResultStack);
            buffer.writeInt(recipe.mBgain);
        }

    }
}
