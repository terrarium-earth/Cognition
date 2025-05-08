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

public class InformationalRecipes {

    public static List<AbstractInformationalRecipe> populateFillingRecipes(){
        List<AbstractInformationalRecipe> recipes = new ArrayList<>();

        recipes.add(new FillingRecipe(
                Ingredient.of(Items.GLASS_BOTTLE),
                Ingredient.of(RegisterItems.EXPERIENCE_FOUNTAIN_ITEM.get()),
                Items.EXPERIENCE_BOTTLE.getDefaultInstance(),
                "cognition:experience_bottle_filling"));

        recipes.add(new FillingRecipe(
                Ingredient.of(Items.BUCKET),
                Ingredient.of(RegisterItems.EXPERIENCE_FOUNTAIN_ITEM.get()),
                RegisterItems.COGNITIUM_BUCKET.get().getDefaultInstance(),
                "cognition:cognitium_bucket_filling"));

        return recipes;
    }

    public static List<FillingRecipe> populateEmptyingRecipesFromConfig(){
        List<FillingRecipe> recipes = new ArrayList<>();
        Map<String, Float> allowedItemsMap = MiscUtils.getMapFromStringList(Config.COMMON.allowedExperienceItems.get());

        for(Map.Entry<String, Float> entry : allowedItemsMap.entrySet()){
            ResourceLocation itemResource = ResourceLocation.bySeparator(entry.getKey(), ':');

            if(BuiltInRegistries.ITEM.containsKey(itemResource)){
                Item item = BuiltInRegistries.ITEM.get(itemResource);
                recipes.add(new FillingRecipe(
                        Ingredient.of(item),
                        Ingredient.of(RegisterItems.EXPERIENCE_FOUNTAIN_ITEM.get()),
                        ItemStack.EMPTY,
                        "cognition:" + itemResource.getPath() + "_filling"));
            }
        }

        return recipes;
    }

    public static List<AbstractInformationalRecipe> populateInfectingRecipes(){

        List<AbstractInformationalRecipe> recipes = new ArrayList<>();
        Ingredient catalysts = Ingredient.of(RegisterItems.BIBLIOPHAGE.get(), RegisterItems.INFECTED_BOOKSHELF_ITEM.get(), RegisterItems.NUTRIENT_AGAR_ITEM.get(),
                RegisterItems.INSIGHTFUL_AGAR_ITEM.get(), RegisterItems.EXTRAVAGANT_AGAR_ITEM.get());

        recipes.add(new InfectingRecipe(
                Ingredient.of(Items.BOOKSHELF),
                catalysts,
                RegisterItems.INFECTED_BOOKSHELF_ITEM.get().getDefaultInstance(),
                "cognition:infected_bookshelf_infecting"));

        recipes.add(new InfectingRecipe(
                Ingredient.of(RegisterItems.ARCHIVERS_BOOKSHELF_ITEM.get()),
                catalysts,
                RegisterItems.INFECTED_ARCHIVERS_BOOKSHELF_ITEM.get().getDefaultInstance(),
                "cognition:infected_archivers_bookshelf_infecting"));

        recipes.add(new InfectingRecipe(
                Ingredient.of(RegisterItems.ENCHANTED_BOOKSHELF_ITEM.get()),
                catalysts,
                RegisterItems.INFECTED_ENCHANTED_BOOKSHELF_ITEM.get().getDefaultInstance(),
                "cognition:infected_enchanted_bookshelf_infecting"));

        recipes.add(new InfectingRecipe(
                Ingredient.of(RegisterItems.FLUORESCENT_AGAR_ITEM.get()),
                catalysts,
                RegisterItems.NUTRIENT_AGAR_ITEM.get().getDefaultInstance(),
                "cognition:nutrient_agar_infecting", 4));

        return recipes;
    }

}
