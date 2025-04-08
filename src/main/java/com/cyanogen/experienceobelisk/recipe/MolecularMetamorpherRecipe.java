package com.cyanogen.experienceobelisk.recipe;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Tuple;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class MolecularMetamorpherRecipe implements Recipe<SimpleContainer> {

    private final ArrayList<Tuple<Ingredient, Integer>> ingredients; //A -- ingredient, B -- count
    private final ItemStack output;
    private final int cost;
    private final int processTime;
    private final ResourceLocation id;

    public MolecularMetamorpherRecipe(ArrayList<Tuple<Ingredient, Integer>> ingredients, ItemStack output, int cost, int processTime, ResourceLocation id){
        this.ingredients = ingredients;
        this.output = output;
        this.cost = cost;
        this.processTime = processTime;
        this.id = id;
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
    public boolean matches(SimpleContainer container, @Nullable Level level) {

        ArrayList<ItemStack> contents = new ArrayList<>();
        for(int j = 0; j < 3; j++){
            contents.add(container.getItem(j));
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
    public ItemStack assemble(SimpleContainer container, @Nullable RegistryAccess access) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(@Nullable RegistryAccess p_267052_) {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    public int getCost(){
        return cost;
    }

    public int getProcessTime(){
        return processTime;
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

    public String getInfo() {
        return "----- Molecular Metamorphosis ----- \n" +
                "ID: " + id + "\n" +
                "Ingredients: " + "\n" + getIngredientInfoString() +
                "Output: " + output + "\n" +
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
        public static final ResourceLocation ID = new ResourceLocation(ExperienceObelisk.MOD_ID, "molecular_metamorphosis");

        @Override
        public MolecularMetamorpherRecipe fromJson(ResourceLocation id, JsonObject recipe) {

            Ingredient ingredient1 = Ingredient.fromJson(GsonHelper.getNonNull(recipe, "ingredient1"));
            Ingredient ingredient2 = Ingredient.fromJson(GsonHelper.getNonNull(recipe, "ingredient2"));
            Ingredient ingredient3 = Ingredient.fromJson(GsonHelper.getNonNull(recipe, "ingredient3"));
            int count1 = GsonHelper.getAsInt(recipe, "count1");
            int count2 = GsonHelper.getAsInt(recipe, "count2");
            int count3 = GsonHelper.getAsInt(recipe, "count3");

            ArrayList<Tuple<Ingredient, Integer>> ingredients = assembleIngredients(ingredient1, count1, ingredient2, count2, ingredient3, count3);

            ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(recipe, "result"));
            int cost = GsonHelper.getAsInt(recipe, "cost");
            int processTime = GsonHelper.getAsInt(recipe, "processTime");

            return new MolecularMetamorpherRecipe(ingredients, result, cost, processTime, id);
        }

        @Override
        public @Nullable MolecularMetamorpherRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {

            Ingredient ingredient1 = Ingredient.fromNetwork(buffer);
            int count1 = buffer.readInt();
            Ingredient ingredient2 = Ingredient.fromNetwork(buffer);
            int count2 = buffer.readInt();
            Ingredient ingredient3 = Ingredient.fromNetwork(buffer);
            int count3 = buffer.readInt();

            ArrayList<Tuple<Ingredient, Integer>> ingredients = assembleIngredients(ingredient1, count1, ingredient2, count2, ingredient3, count3);

            ItemStack result = buffer.readItem();
            int cost = buffer.readInt();
            int processTime = buffer.readInt();

            return new MolecularMetamorpherRecipe(ingredients, result, cost, processTime, id);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, MolecularMetamorpherRecipe recipe) {

            for(Tuple<Ingredient, Integer> ingredientWithCount : recipe.ingredients){
                ingredientWithCount.getA().toNetwork(buffer);
                buffer.writeInt(ingredientWithCount.getB());
            }

            buffer.writeItemStack(recipe.getResultItem(null), false);
            buffer.writeInt(recipe.cost);
            buffer.writeInt(recipe.processTime);
        }
    }


}
