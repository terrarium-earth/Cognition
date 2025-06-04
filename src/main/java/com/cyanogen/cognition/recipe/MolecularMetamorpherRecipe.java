package com.cyanogen.cognition.recipe;

import com.cyanogen.cognition.Cognition;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class MolecularMetamorpherRecipe implements Recipe<RecipeInput> {

    private final ArrayList<Tuple<Ingredient, Integer>> ingredients; //A -- ingredient, B -- count
    private final ItemStack result;
    private final int cost;
    private final int processTime;
    private final ResourceLocation id;

    //for serializer use only
    public final Ingredient ingredient1;
    public final int count1;
    public final Ingredient ingredient2;
    public final int count2;
    public final Ingredient ingredient3;
    public final int count3;

    //for serializer use only
    public MolecularMetamorpherRecipe(Ingredient ingredient1, int count1,
                                      Ingredient ingredient2, int count2,
                                      Ingredient ingredient3, int count3,
                                      ItemStack result, int cost, int processTime, ResourceLocation id){

        this.ingredient1 = ingredient1;
        this.count1 = count1;
        this.ingredient2 = ingredient2;
        this.count2 = count2;
        this.ingredient3 = ingredient3;
        this.count3 = count3;

        this.result = result;
        this.cost = cost;
        this.processTime = processTime;
        this.id = id;

        this.ingredients = assembleIngredients(ingredient1, count1, ingredient2, count2, ingredient3, count3);
    }

    public static ArrayList<Tuple<Ingredient, Integer>> assembleIngredients(Ingredient ingredient1, int count1,
                                                                            Ingredient ingredient2, int count2,
                                                                            Ingredient ingredient3 ,int count3){

        ArrayList<Tuple<Ingredient, Integer>> ingredients = new ArrayList<>(4);
        //skipping index 0 to make things more intuitive
        ingredients.add(0, new Tuple<>(Ingredient.EMPTY, 0));
        ingredients.add(1, new Tuple<>(ingredient1, count1));
        ingredients.add(2, new Tuple<>(ingredient2, count2));
        ingredients.add(3, new Tuple<>(ingredient3, count3));

        return ingredients;
    }

    @Override
    public boolean matches(RecipeInput recipeInput, @Nullable Level level) {

        ArrayList<ItemStack> contents = new ArrayList<>();
        for(int j = 0; j < 3; j++){
            contents.add(recipeInput.getItem(j));
        }
        int tracker = 3;

        for(int k = 1; k <= 3; k++){

            Ingredient ingredient = ingredients.get(k).getA();
            int count = ingredients.get(k).getB();

            if(ingredient.isEmpty() || count <= 0){
                tracker = tracker - 1;
            }
            else{
                if(!contents.isEmpty()){
                    for(ItemStack item : contents){
                        if(ingredient.test(item) && item.getCount() >= count){
                            tracker = tracker - 1;
                            contents.remove(item);
                            break;
                        }
                    }
                }

            }
        }

        return tracker <= 0;
    }

    @Override
    @NotNull
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        for(Tuple<Ingredient, Integer> ingredient : this.ingredients){
            list.add(ingredient.getA());
        }
        return list;
    }

    public ArrayList<Tuple<Ingredient, Integer>> getIngredients(boolean k){
        return this.ingredients;
    }

    @Override
    @NotNull
    public ItemStack assemble(@Nullable RecipeInput recipeInput, @Nullable HolderLookup.Provider provider) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    @NotNull
    public ItemStack getResultItem(@Nullable HolderLookup.Provider provider) {
        return result.copy();
    }

    public int getCost(){
        return cost;
    }

    public int getProcessTime(){
        return processTime;
    }

    public ResourceLocation getId() {
        return id;
    }

    @Override
    public boolean isSpecial() {
        return false;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<MolecularMetamorpherRecipe>{
        public static final Type INSTANCE = new Type();
        public static final String ID = "molecular_metamorphosis";
    }

    public boolean isNameFormatting(){
        return id.getPath().equals("item_name_formatting");
    }

    //----- FOR DEBUGGING USE... -----//

    public String getInfo() {
        return "----- Molecular Metamorphosis ----- \n" +
                "ID: " + id + "\n" +
                "Ingredients: " + "\n" + getIngredientInfoString() +
                "Result: " + result + "\n" +
                "Cost: " + cost + "\n" +
                "Process Time: " + processTime + "\n";
    }

    public String getIngredientInfoString(){

        StringBuilder info = new StringBuilder();

        for(Tuple<Ingredient, Integer> ingredient : ingredients){

            if(!(ingredient.getA().isEmpty() || ingredient.getB() <= 0)){
                ItemStack first = ingredient.getA().getItems()[0];
                int count = ingredient.getB();

                info.append(count).append("- ").append(first.getItem());

                if(ingredient.getA().getItems().length > 1){
                    info.append(" etc...");
                }

                info.append("\n");
            }
        }
        return info.toString();
    }

    //-----SERIALIZER-----//

    public static class Serializer implements RecipeSerializer<MolecularMetamorpherRecipe> {

        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Cognition.MOD_ID, "molecular_metamorphosis");

        public static MolecularMetamorpherRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {

            Ingredient ingredient1 = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            int count1 = buffer.readInt();
            Ingredient ingredient2 = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            int count2 = buffer.readInt();
            Ingredient ingredient3 = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            int count3 = buffer.readInt();

            ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);
            int cost = buffer.readInt();
            int processTime = buffer.readInt();
            ResourceLocation id = ResourceLocation.STREAM_CODEC.decode(buffer);

            return new MolecularMetamorpherRecipe(ingredient1, count1, ingredient2, count2, ingredient3, count3, result, cost, processTime, id);
        }

        public static void toNetwork(RegistryFriendlyByteBuf buffer, MolecularMetamorpherRecipe recipe) {

            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.ingredient1);
            buffer.writeInt(recipe.count1);
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.ingredient2);
            buffer.writeInt(recipe.count2);
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.ingredient3);
            buffer.writeInt(recipe.count3);

            ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
            buffer.writeInt(recipe.cost);
            buffer.writeInt(recipe.processTime);
            ResourceLocation.STREAM_CODEC.encode(buffer, recipe.id);
        }

        private static final MapCodec<MolecularMetamorpherRecipe> CODEC =
                RecordCodecBuilder.mapCodec((recipeInstance) -> recipeInstance.group(
                        Ingredient.CODEC.fieldOf("ingredient1").forGetter((recipe) -> recipe.ingredient1),
                                Codec.INT.fieldOf("count1").forGetter((recipe) -> recipe.count1),
                        Ingredient.CODEC.fieldOf("ingredient2").forGetter((recipe) -> recipe.ingredient2),
                                Codec.INT.fieldOf("count2").forGetter((recipe) -> recipe.count2),
                        Ingredient.CODEC.fieldOf("ingredient3").forGetter((recipe) -> recipe.ingredient3),
                                Codec.INT.fieldOf("count3").forGetter((recipe) -> recipe.count3),

                        ItemStack.CODEC.fieldOf("result").forGetter((recipe) -> recipe.result),
                        Codec.INT.fieldOf("cost").forGetter((recipe) -> recipe.cost),
                        Codec.INT.fieldOf("processTime").forGetter((recipe) -> recipe.processTime),
                        ResourceLocation.CODEC.fieldOf("id").forGetter((recipe) -> recipe.id)
                        )
                        .apply(recipeInstance, MolecularMetamorpherRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, MolecularMetamorpherRecipe> STREAM_CODEC =
                StreamCodec.of(MolecularMetamorpherRecipe.Serializer::toNetwork, MolecularMetamorpherRecipe.Serializer::fromNetwork);

        @Override
        public MapCodec<MolecularMetamorpherRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, MolecularMetamorpherRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }


}
