package com.cyanogen.cognition.recipe.jei.info;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractInformationalCategory implements IRecipeCategory<AbstractInformationalRecipe> {

    public final IGuiHelper guiHelper;
    public final ResourceLocation texture = ResourceLocation.parse("cognition:textures/gui/recipes/information_jei.png");
    public final IDrawableAnimated arrow;
    public final IDrawable counterArrow;

    public AbstractInformationalCategory(IRecipeCategoryRegistration registration){
        this.guiHelper = registration.getJeiHelpers().getGuiHelper();
        this.arrow = guiHelper.drawableBuilder(texture,0,66,41,7)
                .buildAnimated(100, IDrawableAnimated.StartDirection.LEFT, false);
        this.counterArrow = guiHelper.drawableBuilder(texture, 0,73,11,9).build();
    }

    @Override
    public int getWidth() {
        return 136;
    }

    @Override
    public int getHeight() {
        return 66;
    }

    @Override
    public void draw(AbstractInformationalRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiHelper.createDrawable(texture, 0, 0, 136, 66).draw(guiGraphics);
        arrow.draw(guiGraphics, 49, 21);
        IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
    }

    public Component getProcessTooltip(){
        return Component.empty();
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltipBuilder, AbstractInformationalRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {

        Component tooltip = getProcessTooltip();

        int x1 = 49;
        int x2 = x1 + 43;
        int y1 = 21;
        int y2 = y1 + 11;

        if(mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y2){
            tooltipBuilder.add(tooltip);
        }
        IRecipeCategory.super.getTooltip(tooltipBuilder, recipe, recipeSlotsView, mouseX, mouseY);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AbstractInformationalRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 18,18).setSlotName("input").addIngredients(recipe.getInput());
        builder.addSlot(RecipeIngredientRole.CATALYST, 61,34).setSlotName("catalyst").addIngredients(recipe.getCatalyst());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 102,18).setSlotName("output").addItemStack(recipe.getResultItem(null));
    }

}
