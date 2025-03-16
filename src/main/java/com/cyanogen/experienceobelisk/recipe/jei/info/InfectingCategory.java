package com.cyanogen.experienceobelisk.recipe.jei.info;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
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

public class InfectingCategory implements IRecipeCategory<InfectingRecipe>{

    IRecipeCategoryRegistration registration;
    IGuiHelper guiHelper;
    private final ResourceLocation texture = new ResourceLocation("");
    private final Component title = Component.literal("Infecting"); //todo: en_us

    public static final RecipeType<InfectingRecipe> infectingType =
            RecipeType.create(ExperienceObelisk.MOD_ID, InfectingRecipe.Type.ID, InfectingRecipe.class);

    public InfectingCategory(IRecipeCategoryRegistration registration){
        this.registration = registration;
        this.guiHelper = registration.getJeiHelpers().getGuiHelper();
    }

    @Override
    public RecipeType<InfectingRecipe> getRecipeType() {
        return infectingType;
    }

    @Override
    public Component getTitle() {
        return title;
    }

    @Override
    public IDrawable getIcon() {
        ItemStack icon = new ItemStack(RegisterItems.BIBLIOPHAGE.get());
        return guiHelper.createDrawableItemStack(icon);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, InfectingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 60,35).setSlotName("input").addIngredients(recipe.getInput());
        builder.addSlot(RecipeIngredientRole.CATALYST, 100,35).setSlotName("catalyst").addIngredients(recipe.getCatalyst());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 140,35).setSlotName("output").addItemStack(recipe.getResultItem(null));
    }

    @Override
    public IDrawable getBackground() {
        return guiHelper.createDrawable(texture, 0, 0, 176, 87);
    }


}
