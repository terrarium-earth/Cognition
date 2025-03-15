package com.cyanogen.experienceobelisk.recipe.jei;

import com.cyanogen.experienceobelisk.registries.RegisterItems;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import static com.cyanogen.experienceobelisk.recipe.jei.CognitionJeiPlugin.informationalType;

public class InformationalRecipeCategory implements IRecipeCategory<InformationalRecipe>{

    //this is a dummy recipe used to display in-game mechanics in JEI

    IRecipeCategoryRegistration registration;
    IGuiHelper guiHelper;
    private final ResourceLocation texture = new ResourceLocation("");
    private Component title = null;
    private final Component defaultTitle = Component.literal("Information");

    public InformationalRecipeCategory(IRecipeCategoryRegistration registration){
        this.registration = registration;
        this.guiHelper = registration.getJeiHelpers().getGuiHelper();
    }

    @Override
    public RecipeType<InformationalRecipe> getRecipeType() {
        return informationalType;
    }

    @Override
    public Component getTitle() {
        return title == null ? defaultTitle : title;
    }

    @Override
    public IDrawable getBackground() {
        return guiHelper.createDrawable(texture, 0, 0, 176, 87);
    }

    @Override
    public IDrawable getIcon() {
        ItemStack icon = new ItemStack(RegisterItems.EXPERIENCE_OBELISK_ITEM.get());
        return guiHelper.createDrawableItemStack(icon);
        //td: design a simple symbol
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, InformationalRecipe recipe, IFocusGroup focuses) {
        title = Component.literal(recipe.getActionType());
        builder.addSlot(RecipeIngredientRole.INPUT, 30, 50).setSlotName("input").addIngredients(recipe.getInput());
        builder.addSlot(RecipeIngredientRole.CATALYST, 60, 50).setSlotName("catalyst").addIngredients(recipe.getCatalyst());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 90, 50).setSlotName("output").addItemStack(recipe.getResultItem(null));

    }


}
