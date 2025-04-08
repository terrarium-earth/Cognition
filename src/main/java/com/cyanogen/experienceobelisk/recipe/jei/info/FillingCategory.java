package com.cyanogen.experienceobelisk.recipe.jei.info;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.cyanogen.experienceobelisk.registries.RegisterItems;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FillingCategory implements IRecipeCategory<FillingRecipe>{

    private final IGuiHelper guiHelper;
    private final ResourceLocation texture = new ResourceLocation("experienceobelisk:textures/gui/recipes/information_jei.png");
    private final IDrawableAnimated arrow;

    public static final RecipeType<FillingRecipe> fillingType =
            RecipeType.create(ExperienceObelisk.MOD_ID, FillingRecipe.Type.ID, FillingRecipe.class);

    public FillingCategory(IRecipeCategoryRegistration registration){
        this.guiHelper = registration.getJeiHelpers().getGuiHelper();

        this.arrow = guiHelper.drawableBuilder(texture,0,66,41,7)
                .buildAnimated(100, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public RecipeType<FillingRecipe> getRecipeType() {
        return fillingType;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.experienceobelisk.info.filling.title");
    }

    @Override
    public IDrawable getIcon() {
        ItemStack icon = new ItemStack(RegisterItems.EXPERIENCE_FOUNTAIN_ITEM.get());
        return guiHelper.createDrawableItemStack(icon);
    }

    @Override
    public void draw(FillingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {

        arrow.draw(guiGraphics, 46, 22);
        IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
    }

    @Override
    public List<Component> getTooltipStrings(FillingRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {

        List<Component> tooltipList = new ArrayList<>();
        Component tooltip = Component.translatable("jei.experienceobelisk.info.filling.tooltip");

        int x1 = 46;
        int x2 = x1 + 43;
        int y1 = 22;
        int y2 = y1 + 11;

        if(mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y2){
            tooltipList.add(tooltip);
            return tooltipList;
        }

        return IRecipeCategory.super.getTooltipStrings(recipe, recipeSlotsView, mouseX, mouseY);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FillingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 15,19).setSlotName("input").addIngredients(recipe.getInput());
        builder.addSlot(RecipeIngredientRole.CATALYST, 58,35).setSlotName("catalyst").addIngredients(recipe.getCatalyst());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 99,19).setSlotName("output").addItemStack(recipe.getResultItem(null));
    }

    @Override
    public IDrawable getBackground() {
        return guiHelper.createDrawable(texture, 0, 0, 136, 66);
    }


}
