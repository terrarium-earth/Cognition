package com.cyanogen.experienceobelisk.recipe.jei;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
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
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.ArrayList;
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
        assert Minecraft.getInstance().level != null;
        RecipeManager manager = Minecraft.getInstance().level.getRecipeManager();
        List<MolecularMetamorpherRecipe> metamorpherRecipes = manager.getAllRecipesFor(MolecularMetamorpherRecipe.Type.INSTANCE);
        List<InfectingRecipe> infectingRecipes = manager.getAllRecipesFor(InfectingRecipe.Type.INSTANCE);
        List<FillingRecipe> fillingRecipes = manager.getAllRecipesFor(FillingRecipe.Type.INSTANCE);
        List<EmptyingRecipe> emptyingRecipes = manager.getAllRecipesFor(EmptyingRecipe.Type.INSTANCE);

        List<MolecularMetamorpherRecipe> extra = new ArrayList<>();
        extra.add(RecipeUtils.getEmptyNameFormattingRecipe());

        registration.addRecipes(METAMORPHER_TYPE, metamorpherRecipes);
        registration.addRecipes(METAMORPHER_TYPE, extra);
        registration.addRecipes(INFECTING_TYPE, infectingRecipes);
        registration.addRecipes(FILLING_TYPE, fillingRecipes);
        registration.addRecipes(EMPTYING_TYPE, emptyingRecipes);

        System.out.println("Metamorphosis: Added " + metamorpherRecipes.size() + " recipes -----------------------------");
        System.out.println("Infecting: Added " + infectingRecipes.size() + " recipes -----------------------------");
        System.out.println("Filling: Added " + fillingRecipes.size() + " recipes -----------------------------");
        System.out.println("Emptying: Added " + emptyingRecipes.size() + " recipes -----------------------------");

        //INFO
        ItemStack forgottenDust = new ItemStack(RegisterItems.FORGOTTEN_DUST.get());
        registration.addIngredientInfo(forgottenDust, VanillaTypes.ITEM_STACK, Component.translatable("jei.experienceobelisk.description.forgotten_dust"));

        //HIDE FROM VIEWER
        List<ItemStack> hidden = new ArrayList<>();
        hidden.add(RegisterItems.DUMMY_SWORD.get().getDefaultInstance());
        registration.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, hidden);

        IModPlugin.super.registerRecipes(registration);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {

        registration.addRecipeCatalyst(RegisterItems.MOLECULAR_METAMORPHER_ITEM.get().getDefaultInstance(), METAMORPHER_TYPE);
        registration.addRecipeCatalyst(RegisterItems.EXPERIENCE_FOUNTAIN_ITEM.get().getDefaultInstance(), FILLING_TYPE);
        registration.addRecipeCatalyst(RegisterItems.EXPERIENCE_FOUNTAIN_ITEM.get().getDefaultInstance(), EMPTYING_TYPE);

        for(ItemStack catalyst : RecipeUtils.getCatalysts()){
            registration.addRecipeCatalyst(catalyst, INFECTING_TYPE);
        }

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
        return new ResourceLocation(ExperienceObelisk.MOD_ID, "jei_plugin");
    }

}
