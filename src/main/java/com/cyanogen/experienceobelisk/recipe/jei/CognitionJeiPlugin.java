package com.cyanogen.experienceobelisk.recipe.jei;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.cyanogen.experienceobelisk.gui.MolecularMetamorpherScreen;
import com.cyanogen.experienceobelisk.recipe.MolecularMetamorpherRecipe;
import com.cyanogen.experienceobelisk.recipe.jei.info.FillingCategory;
import com.cyanogen.experienceobelisk.recipe.jei.info.InfectingCategory;
import com.cyanogen.experienceobelisk.recipe.jei.info.InformationalRecipes;
import com.cyanogen.experienceobelisk.registries.RegisterItems;
import com.cyanogen.experienceobelisk.utils.RecipeUtils;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;

import java.util.ArrayList;
import java.util.List;

import static com.cyanogen.experienceobelisk.recipe.jei.MolecularMetamorpherCategory.metamorpherType;

@mezz.jei.api.JeiPlugin
public class CognitionJeiPlugin implements IModPlugin {

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {

        registration.addRecipeCategories(new MolecularMetamorpherCategory(registration));
        registration.addRecipeCategories(new FillingCategory(registration));
        registration.addRecipeCategories(new InfectingCategory(registration));
        IModPlugin.super.registerCategories(registration);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {

        //RECIPES
        List<MolecularMetamorpherRecipe> metamorpherRecipes = new ArrayList<>();

        for(Recipe<?> recipe : Minecraft.getInstance().level.getRecipeManager().getRecipes()){
            if(recipe instanceof MolecularMetamorpherRecipe metamorpherRecipe){
                metamorpherRecipes.add(metamorpherRecipe);
            }
        }
        metamorpherRecipes.addAll(RecipeUtils.getNameFormattingRecipesForJEI());
        registration.addRecipes(metamorpherType, metamorpherRecipes);

        //INFO V2
        registration.addRecipes(FillingCategory.fillingType, InformationalRecipes.populateFillingRecipes());
        registration.addRecipes(InfectingCategory.infectingType, InformationalRecipes.populateInfectingRecipes());

        //INFO
        ItemStack FORGOTTEN_DUST = new ItemStack(RegisterItems.FORGOTTEN_DUST.get());
        registration.addIngredientInfo(FORGOTTEN_DUST, VanillaTypes.ITEM_STACK, Component.translatable("jei.experienceobelisk.description.forgotten_dust"));

        //HIDE FROM VIEWER
        List<ItemStack> hidden = new ArrayList<>();
        hidden.add(new ItemStack(RegisterItems.DUMMY_SWORD.get(), 1));
        registration.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, hidden);

        IModPlugin.super.registerRecipes(registration);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {

        ItemStack stack = new ItemStack(RegisterItems.MOLECULAR_METAMORPHER_ITEM.get());
        registration.addRecipeCatalyst(stack, metamorpherType);

        IModPlugin.super.registerRecipeCatalysts(registration);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {

        registration.addRecipeTransferHandler(new MolecularMetamorpherTransferHandler(registration), metamorpherType);

        IModPlugin.super.registerRecipeTransferHandlers(registration);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {

        registration.addRecipeClickArea(MolecularMetamorpherScreen.class,107,45,32,10, metamorpherType);

        IModPlugin.super.registerGuiHandlers(registration);
    }

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(ExperienceObelisk.MOD_ID, "jei_plugin");
    }

}
