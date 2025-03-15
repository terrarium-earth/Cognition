package com.cyanogen.experienceobelisk.recipe.jei;

import com.cyanogen.experienceobelisk.registries.RegisterItems;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class InformationalRecipes {

    public static List<InformationalRecipe> populate(){
        List<InformationalRecipe> recipes = new ArrayList<>();

        recipes.add(new InformationalRecipe(
                Ingredient.of(Items.GLASS_BOTTLE),
                Ingredient.of(RegisterItems.EXPERIENCE_FOUNTAIN_ITEM.get()),
                Items.EXPERIENCE_BOTTLE.getDefaultInstance(),
                "Filling",
                "experience_bottle_filling"));

        recipes.add(new InformationalRecipe(
                Ingredient.of(Items.BOOKSHELF),
                Ingredient.of(RegisterItems.BIBLIOPHAGE.get(), RegisterItems.INFECTED_BOOKSHELF_ITEM.get(), RegisterItems.NUTRIENT_AGAR_ITEM.get(),
                        RegisterItems.INSIGHTFUL_AGAR_ITEM.get(), RegisterItems.EXTRAVAGANT_AGAR_ITEM.get()),
                RegisterItems.INFECTED_BOOKSHELF_ITEM.get().getDefaultInstance(),
                "Infecting",
                "infected_bookshelf_infecting"));

        recipes.add(new InformationalRecipe(
                Ingredient.of(RegisterItems.ARCHIVERS_BOOKSHELF_ITEM.get()),
                Ingredient.of(RegisterItems.BIBLIOPHAGE.get(), RegisterItems.INFECTED_BOOKSHELF_ITEM.get(), RegisterItems.NUTRIENT_AGAR_ITEM.get(),
                        RegisterItems.INSIGHTFUL_AGAR_ITEM.get(), RegisterItems.EXTRAVAGANT_AGAR_ITEM.get()),
                RegisterItems.INFECTED_ARCHIVERS_BOOKSHELF_ITEM.get().getDefaultInstance(),
                "Infecting",
                "infected_archivers_bookshelf_infecting"));

        recipes.add(new InformationalRecipe(
                Ingredient.of(RegisterItems.ENCHANTED_BOOKSHELF_ITEM.get()),
                Ingredient.of(RegisterItems.BIBLIOPHAGE.get(), RegisterItems.INFECTED_BOOKSHELF_ITEM.get(), RegisterItems.NUTRIENT_AGAR_ITEM.get(),
                        RegisterItems.INSIGHTFUL_AGAR_ITEM.get(), RegisterItems.EXTRAVAGANT_AGAR_ITEM.get()),
                RegisterItems.INFECTED_ENCHANTED_BOOKSHELF_ITEM.get().getDefaultInstance(),
                "Infecting",
                "infected_enchanted_bookshelf_infecting"));

        recipes.add(new InformationalRecipe(
                Ingredient.of(RegisterItems.FLUORESCENT_AGAR_ITEM.get()),
                Ingredient.of(RegisterItems.BIBLIOPHAGE.get(), RegisterItems.INFECTED_BOOKSHELF_ITEM.get(), RegisterItems.NUTRIENT_AGAR_ITEM.get(),
                        RegisterItems.INSIGHTFUL_AGAR_ITEM.get(), RegisterItems.EXTRAVAGANT_AGAR_ITEM.get()),
                RegisterItems.NUTRIENT_AGAR_ITEM.get().getDefaultInstance(),
                "Infecting",
                "nutrient_agar_infecting"));

        return recipes;
    }

}
