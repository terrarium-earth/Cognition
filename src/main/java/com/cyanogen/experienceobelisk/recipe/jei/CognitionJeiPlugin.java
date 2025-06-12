package com.cyanogen.experienceobelisk.recipe.jei;


import com.cyanogen.experienceobelisk.gui.MolecularMetamorpherScreen;
import com.cyanogen.experienceobelisk.recipe.EmptyingRecipe;
import com.cyanogen.experienceobelisk.recipe.FillingRecipe;
import com.cyanogen.experienceobelisk.recipe.InfectingRecipe;
import com.cyanogen.experienceobelisk.recipe.MolecularMetamorpherRecipe;
import com.cyanogen.experienceobelisk.registries.RegisterItems;
import com.cyanogen.experienceobelisk.utils.RecipeUtils;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.cyanogen.experienceobelisk.recipe.jei.EmptyingCategory.EMPTYING_TYPE;
import static com.cyanogen.experienceobelisk.recipe.jei.FillingCategory.FILLING_TYPE;
import static com.cyanogen.experienceobelisk.recipe.jei.InfectingCategory.INFECTING_TYPE;
import static com.cyanogen.experienceobelisk.recipe.jei.MolecularMetamorpherCategory.METAMORPHER_TYPE;

@JeiPlugin
public class CognitionJeiPlugin implements IModPlugin {

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new MolecularMetamorpherCategory(registration));
        registration.addRecipeCategories(new InfectingCategory(registration));
        registration.addRecipeCategories(new FillingCategory(registration));
        registration.addRecipeCategories(new EmptyingCategory(registration));

        IModPlugin.super.registerCategories(registration);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {

        //RECIPES
        List<MolecularMetamorpherRecipe> metamorpherRecipes = new ArrayList<>();
        List<InfectingRecipe> infectingRecipes = new ArrayList<>();
        List<FillingRecipe> fillingRecipes = new ArrayList<>();
        List<EmptyingRecipe> emptyingRecipes = new ArrayList<>();

        assert Minecraft.getInstance().level != null;

        Collection<Recipe<?>> recipes = Minecraft.getInstance().level.getRecipeManager().getRecipes();
        for(Recipe<?> recipe : recipes){
            switch(recipe){
                case MolecularMetamorpherRecipe k -> metamorpherRecipes.add(k);
                case InfectingRecipe j -> infectingRecipes.add(j);
                case FillingRecipe l -> fillingRecipes.add(l);
                case EmptyingRecipe m -> emptyingRecipes.add(m);
                default -> {}
            }
        }
        metamorpherRecipes.add(RecipeUtils.getEmptyNameFormattingRecipe());

        registration.addRecipes(METAMORPHER_TYPE, metamorpherRecipes);
        registration.addRecipes(INFECTING_TYPE, infectingRecipes);
        registration.addRecipes(FILLING_TYPE, fillingRecipes);
        registration.addRecipes(EMPTYING_TYPE, emptyingRecipes);

        //INFO
        ItemStack forgottenDust = new ItemStack(RegisterItems.FORGOTTEN_DUST.get());
        registration.addIngredientInfo(forgottenDust, VanillaTypes.ITEM_STACK, Component.translatable("jei.cognition.description.forgotten_dust"));

        //HIDE FROM VIEWER
        List<ItemStack> hidden = new ArrayList<>();
        hidden.add(RegisterItems.DUMMY_SWORD.get().getDefaultInstance());
        registration.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, hidden);

        IModPlugin.super.registerRecipes(registration);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {

        registration.addRecipeCatalyst(RegisterItems.MOLECULAR_METAMORPHER_ITEM.get(), METAMORPHER_TYPE);
        registration.addRecipeCatalyst(RegisterItems.EXPERIENCE_FOUNTAIN_ITEM.get(), FILLING_TYPE);
        registration.addRecipeCatalyst(RegisterItems.EXPERIENCE_FOUNTAIN_ITEM.get(), EMPTYING_TYPE);
        registration.addRecipeCatalysts(INFECTING_TYPE, VanillaTypes.ITEM_STACK, RecipeUtils.getCatalysts());

        IModPlugin.super.registerRecipeCatalysts(registration);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {

        registration.addRecipeTransferHandler(new MolecularMetamorpherTransferHandler(registration), METAMORPHER_TYPE);

        IModPlugin.super.registerRecipeTransferHandlers(registration);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {

        registration.addRecipeClickArea(MolecularMetamorpherScreen.class,107,45,32,10, METAMORPHER_TYPE);

        IModPlugin.super.registerGuiHandlers(registration);
    }

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(Cognition.MOD_ID, "jei_plugin");
    }

}
