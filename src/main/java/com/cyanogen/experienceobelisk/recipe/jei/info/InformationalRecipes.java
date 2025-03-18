package com.cyanogen.experienceobelisk.recipe.jei.info;

import com.cyanogen.experienceobelisk.registries.RegisterItems;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class InformationalRecipes {

    public static List<FillingRecipe> populateFillingRecipes(){
        List<FillingRecipe> recipes = new ArrayList<>();

        recipes.add(new FillingRecipe(
                Ingredient.of(Items.GLASS_BOTTLE),
                Ingredient.of(RegisterItems.EXPERIENCE_FOUNTAIN_ITEM.get()),
                Items.EXPERIENCE_BOTTLE.getDefaultInstance(),
                "experienceobelisk:experience_bottle_filling"));

        recipes.add(new FillingRecipe(
                Ingredient.of(Items.BUCKET),
                Ingredient.of(RegisterItems.EXPERIENCE_FOUNTAIN_ITEM.get()),
                RegisterItems.COGNITIUM_BUCKET.get().getDefaultInstance(),
                "experienceobelisk:cognitium_bucket_filling"));

        return recipes;
    }

    public static List<InfectingRecipe> populateInfectingRecipes(){

        List<InfectingRecipe> recipes = new ArrayList<>();
        Ingredient catalysts = Ingredient.of(RegisterItems.BIBLIOPHAGE.get(), RegisterItems.INFECTED_BOOKSHELF_ITEM.get(), RegisterItems.NUTRIENT_AGAR_ITEM.get(),
                RegisterItems.INSIGHTFUL_AGAR_ITEM.get(), RegisterItems.EXTRAVAGANT_AGAR_ITEM.get());

        recipes.add(new InfectingRecipe(
                Ingredient.of(Items.BOOKSHELF),
                catalysts,
                RegisterItems.INFECTED_BOOKSHELF_ITEM.get().getDefaultInstance(),
                "experienceobelisk:infected_bookshelf_infecting"));

        recipes.add(new InfectingRecipe(
                Ingredient.of(RegisterItems.ARCHIVERS_BOOKSHELF_ITEM.get()),
                catalysts,
                RegisterItems.INFECTED_ARCHIVERS_BOOKSHELF_ITEM.get().getDefaultInstance(),
                "experienceobelisk:infected_archivers_bookshelf_infecting"));

        recipes.add(new InfectingRecipe(
                Ingredient.of(RegisterItems.ENCHANTED_BOOKSHELF_ITEM.get()),
                catalysts,
                RegisterItems.INFECTED_ENCHANTED_BOOKSHELF_ITEM.get().getDefaultInstance(),
                "experienceobelisk:infected_enchanted_bookshelf_infecting"));

        recipes.add(new InfectingRecipe(
                Ingredient.of(RegisterItems.FLUORESCENT_AGAR_ITEM.get()),
                catalysts,
                RegisterItems.NUTRIENT_AGAR_ITEM.get().getDefaultInstance(),
                "experienceobelisk:nutrient_agar_infecting", 4));

        return recipes;
    }

}
