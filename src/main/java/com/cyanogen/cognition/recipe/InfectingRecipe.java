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
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;

public class InfectingRecipe implements Recipe<RecipeInput> {

    private final Ingredient ingredientBlock;
    private final ItemStack resultBlock;
    private final int infectionCount;
    //This field only exists for convenience in displaying the Nutrient Agar recipe in JEI, and doesn't actually do anything in-game
    private final ResourceLocation id;

    public InfectingRecipe(Ingredient inputBlock, ItemStack resultBlock, int infectionCount, ResourceLocation id){
        this.ingredientBlock = inputBlock;
        this.resultBlock = resultBlock;
        this.infectionCount = infectionCount;
        this.id = id;
    }

    public static @Nullable InfectingRecipe getRecipe(Level level, Block ingredientBlock){

        RecipeInput input = InfectingRecipe.recipeInput(ingredientBlock.asItem().getDefaultInstance());

        List<RecipeHolder<InfectingRecipe>> recipeList =
                level.getRecipeManager().getAllRecipesFor(RegisterRecipes.INFECTING_TYPE.get());
        InfectingRecipe infectingRecipe = null;

        for(RecipeHolder<InfectingRecipe> holder : recipeList){
            if(holder.value().matches(input)){
                infectingRecipe = holder.value();
                break;
            }
        }
        return infectingRecipe;
    }

    public static @Nullable BlockState getInfectedBlockState(Level level, BlockState ingredientBlockState){

        InfectingRecipe recipe = InfectingRecipe.getRecipe(level, ingredientBlockState.getBlock());

        if(recipe != null){
            ItemStack result = recipe.assemble(ingredientBlockState.getBlock().asItem().getDefaultInstance(), level.registryAccess());
            if(result.getItem() instanceof BlockItem blockItem){
                return blockItem.getBlock().defaultBlockState();
            }
        }
        return null;
    }

    private static RecipeInput recipeInput(ItemStack stack){
        return new RecipeInput() {
            @Override
            public ItemStack getItem(int i) {
                return stack;
            }

            @Override
            public int size() {
                return 1;
            }
        };
    }

    public boolean matches(RecipeInput recipeInput){
        return matches(recipeInput, null);
    }

    @Override
    public boolean matches(RecipeInput recipeInput, @Nullable Level level) {
        return ingredientBlock.test(recipeInput.getItem(0));
    }

    public ItemStack assemble(ItemStack stack, HolderLookup.Provider provider){
        return assemble(recipeInput(stack), provider);
    }

    @Override
    public ItemStack assemble(@Nullable RecipeInput recipeInput, @Nullable HolderLookup.Provider provider) {
        return resultBlock.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem(@Nullable HolderLookup.Provider provider) {
        return resultBlock.copy();
    }

    public Ingredient getIngredient(){
        return ingredientBlock;
    }

    public int getInfectionCount() {
        return infectionCount;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<InfectingRecipe>{
        public static final InfectingRecipe.Type INSTANCE = new InfectingRecipe.Type();
        public static final String ID = "infecting";
    }

    public static class Serializer implements RecipeSerializer<InfectingRecipe>{

        public static final InfectingRecipe.Serializer INSTANCE = new InfectingRecipe.Serializer();
        public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Cognition.MOD_ID, "infecting");

        public static InfectingRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {

            Ingredient inputBlock = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            ItemStack outputBlock = ItemStack.STREAM_CODEC.decode(buffer);
            int infectionCount = buffer.readInt();
            ResourceLocation id = ResourceLocation.STREAM_CODEC.decode(buffer);

            return new InfectingRecipe(inputBlock, outputBlock, infectionCount, id);
        }

        public static void toNetwork(RegistryFriendlyByteBuf buffer, InfectingRecipe recipe) {

            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.ingredientBlock);
            ItemStack.STREAM_CODEC.encode(buffer, recipe.resultBlock);
            buffer.writeInt(recipe.infectionCount);
            ResourceLocation.STREAM_CODEC.encode(buffer, recipe.id);
        }

        private static final MapCodec<InfectingRecipe> CODEC =
                RecordCodecBuilder.mapCodec((recipeInstance) -> recipeInstance.group(
                                Ingredient.CODEC.fieldOf("ingredient").forGetter((recipe) -> recipe.ingredientBlock),
                                ItemStack.CODEC.fieldOf("result").forGetter((recipe) -> recipe.resultBlock),
                                Codec.INT.fieldOf("infectionCount").forGetter((recipe) -> recipe.infectionCount),
                                ResourceLocation.CODEC.fieldOf("id").forGetter((recipe) -> recipe.id)
                        )
                        .apply(recipeInstance, InfectingRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, InfectingRecipe> STREAM_CODEC =
                StreamCodec.of(InfectingRecipe.Serializer::toNetwork, InfectingRecipe.Serializer::fromNetwork);

        @Override
        public MapCodec<InfectingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, InfectingRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }

}
