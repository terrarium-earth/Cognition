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

public class EmptyingRecipe implements Recipe<RecipeInput> {

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

        List<RecipeHolder<EmptyingRecipe>> recipeList =
                level.getRecipeManager().getAllRecipesFor(RegisterRecipes.EMPTYING_TYPE.get());
        EmptyingRecipe infectingRecipe = null;

        for(RecipeHolder<EmptyingRecipe> holder : recipeList){
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
        public static final EmptyingRecipe.Type INSTANCE = new EmptyingRecipe.Type();
        public static final String ID = "filling";
    }

    public static class Serializer implements RecipeSerializer<EmptyingRecipe>{

        public static final EmptyingRecipe.Serializer INSTANCE = new EmptyingRecipe.Serializer();
        public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Cognition.MOD_ID, "filling");

        public static EmptyingRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {

            Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);
            boolean hasResultStack = buffer.readBoolean();
            int mBcost = buffer.readInt();
            ResourceLocation id = ResourceLocation.STREAM_CODEC.decode(buffer);

            return new EmptyingRecipe(ingredient, result, hasResultStack, mBcost, id);
        }

        public static void toNetwork(RegistryFriendlyByteBuf buffer, EmptyingRecipe recipe) {

            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.ingredient);
            ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
            buffer.writeBoolean(recipe.hasResultStack);
            buffer.writeInt(recipe.mBgain);
            ResourceLocation.STREAM_CODEC.encode(buffer, recipe.id);
        }

        private static final MapCodec<EmptyingRecipe> CODEC =
                RecordCodecBuilder.mapCodec((recipeInstance) -> recipeInstance.group(
                                Ingredient.CODEC.fieldOf("ingredient").forGetter((recipe) -> recipe.ingredient),
                                ItemStack.CODEC.fieldOf("result").forGetter((recipe) -> recipe.result),
                                Codec.BOOL.fieldOf("hasResultStack").forGetter((recipe) -> recipe.hasResultStack),
                                Codec.INT.fieldOf("gain_mB").forGetter((recipe) -> recipe.mBgain),
                                ResourceLocation.CODEC.fieldOf("id").forGetter((recipe) -> recipe.id)
                        )
                        .apply(recipeInstance, EmptyingRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, EmptyingRecipe> STREAM_CODEC =
                StreamCodec.of(EmptyingRecipe.Serializer::toNetwork, EmptyingRecipe.Serializer::fromNetwork);

        @Override
        public MapCodec<EmptyingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, EmptyingRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
