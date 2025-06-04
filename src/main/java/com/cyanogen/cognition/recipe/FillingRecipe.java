package com.cyanogen.cognition.recipe;

import com.cyanogen.cognition.Cognition;
import com.cyanogen.cognition.registries.RegisterRecipes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class FillingRecipe implements Recipe<RecipeInput> {

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

        List<RecipeHolder<FillingRecipe>> recipeList =
                level.getRecipeManager().getAllRecipesFor(RegisterRecipes.FILLING_TYPE.get());
        FillingRecipe infectingRecipe = null;

        for(RecipeHolder<FillingRecipe> holder : recipeList){
            if(holder.value().matches(stack)){
                infectingRecipe = holder.value();
                break;
            }
        }
        return infectingRecipe;
    }

    public boolean matches(ItemStack stack){
        return matches(new RecipeInput() {
            @Override
            public ItemStack getItem(int i) {
                return stack;
            }

            @Override
            public int size() {
                return 1;
            }
        }, null);
    }

    @Override
    public boolean matches(RecipeInput recipeInput, @Nullable Level level) {
        return ingredient.test(recipeInput.getItem(0));
    }

    @Override
    public ItemStack assemble(RecipeInput recipeInput, HolderLookup.Provider provider) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem(@Nullable HolderLookup.Provider provider) {
        return result.copy();
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
        public static final FillingRecipe.Type INSTANCE = new FillingRecipe.Type();
        public static final String ID = "filling";
    }

    public static class Serializer implements RecipeSerializer<FillingRecipe>{

        public static final FillingRecipe.Serializer INSTANCE = new FillingRecipe.Serializer();
        public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Cognition.MOD_ID, "filling");

        public static FillingRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {

            Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);
            int mBcost = buffer.readInt();
            ResourceLocation id = ResourceLocation.STREAM_CODEC.decode(buffer);

            return new FillingRecipe(ingredient, result, mBcost, id);
        }

        public static void toNetwork(RegistryFriendlyByteBuf buffer, FillingRecipe recipe) {

            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.ingredient);
            ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
            buffer.writeInt(recipe.mBcost);
            ResourceLocation.STREAM_CODEC.encode(buffer, recipe.id);
        }

        private static final MapCodec<FillingRecipe> CODEC =
                RecordCodecBuilder.mapCodec((recipeInstance) -> recipeInstance.group(
                                Ingredient.CODEC.fieldOf("ingredient").forGetter((recipe) -> recipe.ingredient),
                                ItemStack.CODEC.fieldOf("result").forGetter((recipe) -> recipe.result),
                                Codec.INT.fieldOf("cost_mB").forGetter((recipe) -> recipe.mBcost),
                                ResourceLocation.CODEC.fieldOf("id").forGetter((recipe) -> recipe.id)
                        )
                        .apply(recipeInstance, FillingRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, FillingRecipe> STREAM_CODEC =
                StreamCodec.of(FillingRecipe.Serializer::toNetwork, FillingRecipe.Serializer::fromNetwork);

        @Override
        public MapCodec<FillingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, FillingRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
