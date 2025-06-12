package com.cyanogen.experienceobelisk.recipe;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;

public class InfectingRecipe implements Recipe<SimpleContainer> {

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

    public static @Nullable InfectingRecipe getRecipe(Level level, ItemStack stack){

        List<InfectingRecipe> recipeList =
                level.getRecipeManager().getAllRecipesFor(com.cyanogen.experienceobelisk.registries.RegisterRecipes.INFECTING_TYPE.get());
        InfectingRecipe infectingRecipe = null;

        for(InfectingRecipe recipe : recipeList){
            if(recipe.matches(stack)){
                infectingRecipe = recipe;
                break;
            }
        }
        return infectingRecipe;
    }

    public static @Nullable BlockState getInfectedBlockState(Level level, BlockState ingredientBlockState){

        ItemStack block = ingredientBlockState.getBlock().asItem().getDefaultInstance();
        InfectingRecipe recipe = InfectingRecipe.getRecipe(level, block);

        if(recipe != null){
            ItemStack result = recipe.assemble(block);
            if(result.getItem() instanceof BlockItem blockItem){
                return blockItem.getBlock().defaultBlockState();
            }
        }
        return null;
    }

    public boolean matches(ItemStack stack){
        return matches(new SimpleContainer(stack), null);
    }

    @Override
    public boolean matches(SimpleContainer container, @Nullable Level level) {
        return ingredientBlock.test(container.getItem(0));
    }

    public ItemStack assemble(ItemStack stack){
        return assemble(new SimpleContainer(stack), null);
    }

    @Override
    public ItemStack assemble(@Nullable SimpleContainer container, @Nullable RegistryAccess access) {
        return resultBlock.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem(@Nullable RegistryAccess access) {
        return resultBlock.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
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
        public static final Type INSTANCE = new Type();
        public static final String ID = "infecting";
    }

    public static class Serializer implements RecipeSerializer<InfectingRecipe>{

        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(ExperienceObelisk.MOD_ID, "infecting");

        @Override
        public InfectingRecipe fromJson(ResourceLocation id, JsonObject recipe) {

            Ingredient inputBlock = Ingredient.fromJson(GsonHelper.getNonNull(recipe, "ingredient"));
            ItemStack outputBlock = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(recipe, "result"));
            int infectionCount = GsonHelper.getAsInt(recipe, "infectionCount");

            return new InfectingRecipe(inputBlock, outputBlock, infectionCount, id);
        }

        @Override
        public @Nullable InfectingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {

            Ingredient inputBlock = Ingredient.fromNetwork(buffer);
            ItemStack outputBlock = buffer.readItem();
            int infectionCount = buffer.readInt();

            return new InfectingRecipe(inputBlock, outputBlock, infectionCount, id);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, InfectingRecipe recipe) {

            recipe.getIngredient().toNetwork(buffer);
            buffer.writeItemStack(recipe.getResultItem(null), false);
            buffer.writeInt(recipe.infectionCount);
        }

    }

}
