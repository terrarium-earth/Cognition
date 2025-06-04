package com.cyanogen.cognition.recipe.jei.info;

import com.cyanogen.cognition.config.Config;
import com.cyanogen.cognition.registries.RegisterItems;
import com.cyanogen.cognition.utils.MiscUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PopulateInformationalRecipes {

    public static List<AbstractInformationalRecipe> populateFillingRecipes(){
        List<AbstractInformationalRecipe> recipes = new ArrayList<>();

        recipes.add(new FillingRecipe(
                Ingredient.of(Items.GLASS_BOTTLE),
                Ingredient.of(RegisterItems.EXPERIENCE_FOUNTAIN_ITEM.get()),
                Items.EXPERIENCE_BOTTLE.getDefaultInstance(),
                "cognition:experience_bottle_filling",
                12.5f));

        recipes.add(new FillingRecipe(
                Ingredient.of(Items.BUCKET),
                Ingredient.of(RegisterItems.EXPERIENCE_FOUNTAIN_ITEM.get()),
                RegisterItems.COGNITIUM_BUCKET.get().getDefaultInstance(),
                "cognition:cognitium_bucket_filling",
                50f));

        return recipes;
    }

    public static List<AbstractInformationalRecipe> populateEmptyingRecipes(){
        List<AbstractInformationalRecipe> recipes = new ArrayList<>();

        recipes.add(new EmptyingRecipe(
                Ingredient.of(Items.EXPERIENCE_BOTTLE),
                Ingredient.of(RegisterItems.EXPERIENCE_FOUNTAIN_ITEM.get()),
                Items.GLASS_BOTTLE.getDefaultInstance(),
                "cognition:experience_bottle_emptying",
                12.5f));

        recipes.add(new EmptyingRecipe(
                Ingredient.of(RegisterItems.COGNITIUM_BUCKET.get()),
                Ingredient.of(RegisterItems.EXPERIENCE_FOUNTAIN_ITEM.get()),
                Items.BUCKET.getDefaultInstance(),
                "cognition:cognitium_bucket_emptying",
                50f));

        recipes.addAll(populateEmptyingRecipesFromConfig());

        return recipes;
    }

    public static List<AbstractInformationalRecipe> populateEmptyingRecipesFromConfig(){
        List<AbstractInformationalRecipe> recipes = new ArrayList<>();
        Map<String, Float> allowedItemsMap = MiscUtils.getExperienceItemMapFromList(Config.COMMON.allowedExperienceItems.get());

        for(Map.Entry<String, Float> entry : allowedItemsMap.entrySet()){
            ResourceLocation itemResource = ResourceLocation.bySeparator(entry.getKey(), ':');

            if(BuiltInRegistries.ITEM.containsKey(itemResource)){
                Item item = BuiltInRegistries.ITEM.get(itemResource);
                recipes.add(new EmptyingRecipe(
                        Ingredient.of(item),
                        Ingredient.of(RegisterItems.EXPERIENCE_FOUNTAIN_ITEM.get()),
                        ItemStack.EMPTY,
                        "cognition:" + itemResource.getPath() + "_emptying",
                        entry.getValue()));
            }
        }

        return recipes;
    }

    public static List<AbstractInformationalRecipe> populateInfectingRecipes(){

        List<AbstractInformationalRecipe> recipes = new ArrayList<>();
        Ingredient catalysts = Ingredient.of(RegisterItems.BIBLIOPHAGE.get(), RegisterItems.INFECTED_BOOKSHELF_ITEM.get(), RegisterItems.NUTRIENT_AGAR_ITEM.get(),
                RegisterItems.INSIGHTFUL_AGAR_ITEM.get(), RegisterItems.EXTRAVAGANT_AGAR_ITEM.get());

        recipes.add(new InfectingRecipeOld(
                Ingredient.of(Items.BOOKSHELF),
                catalysts,
                RegisterItems.INFECTED_BOOKSHELF_ITEM.get().getDefaultInstance(),
                "cognition:infected_bookshelf_infecting"));

        recipes.add(new InfectingRecipeOld(
                Ingredient.of(RegisterItems.ARCHIVERS_BOOKSHELF_ITEM.get()),
                catalysts,
                RegisterItems.INFECTED_ARCHIVERS_BOOKSHELF_ITEM.get().getDefaultInstance(),
                "cognition:infected_archivers_bookshelf_infecting"));

        recipes.add(new InfectingRecipeOld(
                Ingredient.of(RegisterItems.ENCHANTED_BOOKSHELF_ITEM.get()),
                catalysts,
                RegisterItems.INFECTED_ENCHANTED_BOOKSHELF_ITEM.get().getDefaultInstance(),
                "cognition:infected_enchanted_bookshelf_infecting"));

        recipes.add(new InfectingRecipeOld(
                Ingredient.of(RegisterItems.FLUORESCENT_AGAR_ITEM.get()),
                catalysts,
                RegisterItems.NUTRIENT_AGAR_ITEM.get().getDefaultInstance(),
                "cognition:nutrient_agar_infecting", 4));

        return recipes;
    }

}
