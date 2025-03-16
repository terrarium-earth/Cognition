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

public class FillingCategory implements IRecipeCategory<FillingRecipe>{

    IRecipeCategoryRegistration registration;
    IGuiHelper guiHelper;
    private final ResourceLocation texture = new ResourceLocation("");
    private final Component title = Component.literal("Filling"); //todo: en_us

    public static final RecipeType<FillingRecipe> fillingType =
            RecipeType.create(ExperienceObelisk.MOD_ID, FillingRecipe.Type.ID, FillingRecipe.class);

    public FillingCategory(IRecipeCategoryRegistration registration){
        this.registration = registration;
        this.guiHelper = registration.getJeiHelpers().getGuiHelper();
    }

    @Override
    public RecipeType<FillingRecipe> getRecipeType() {
        return fillingType;
    }

    @Override
    public Component getTitle() {
        return title;
    }

    @Override
    public IDrawable getIcon() {
        ItemStack icon = new ItemStack(RegisterItems.EXPERIENCE_FOUNTAIN_ITEM.get());
        return guiHelper.createDrawableItemStack(icon);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FillingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 60,35).setSlotName("input").addIngredients(recipe.getInput());
        builder.addSlot(RecipeIngredientRole.CATALYST, 100,35).setSlotName("catalyst").addIngredients(recipe.getCatalyst());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 140,35).setSlotName("output").addItemStack(recipe.getResultItem(null));
    }

    @Override
    public IDrawable getBackground() {
        return guiHelper.createDrawable(texture, 0, 0, 176, 87);
    }


}
