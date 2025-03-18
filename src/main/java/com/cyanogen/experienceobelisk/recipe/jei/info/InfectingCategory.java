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
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InfectingCategory implements IRecipeCategory<InfectingRecipe>{

    IRecipeCategoryRegistration registration;
    IGuiHelper guiHelper;
    private final ResourceLocation texture = new ResourceLocation("experienceobelisk:textures/gui/recipes/information_jei.png");
    private final IDrawableAnimated arrow;
    private final IDrawable counterArrow;

    public static final RecipeType<InfectingRecipe> infectingType =
            RecipeType.create(ExperienceObelisk.MOD_ID, InfectingRecipe.Type.ID, InfectingRecipe.class);

    public InfectingCategory(IRecipeCategoryRegistration registration){
        this.registration = registration;
        this.guiHelper = registration.getJeiHelpers().getGuiHelper();

        this.arrow = guiHelper.drawableBuilder(texture,0,66,41,7)
                .buildAnimated(100, IDrawableAnimated.StartDirection.LEFT, false);
        this.counterArrow = guiHelper.drawableBuilder(texture, 0,73,11,9).build();
    }

    @Override
    public RecipeType<InfectingRecipe> getRecipeType() {
        return infectingType;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.experienceobelisk.info.infecting.title");
    }

    @Override
    public IDrawable getIcon() {
        ItemStack icon = new ItemStack(RegisterItems.BIBLIOPHAGE.get());
        return guiHelper.createDrawableItemStack(icon);
    }

    @Override
    public void draw(InfectingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {

        if(recipe.count > 0){
            counterArrow.draw(guiGraphics, 52, 8);
            guiGraphics.drawString(Minecraft.getInstance().font, "x"+recipe.count,67,8,0xFFFFFF);
        }

        arrow.draw(guiGraphics, 46, 22);
        IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
    }

    @Override
    public List<Component> getTooltipStrings(InfectingRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {

        List<Component> tooltipList = new ArrayList<>();
        Component desc = Component.translatable("jei.experienceobelisk.info.infecting.tooltip");
        Component requiredCount = Component.translatable("jei.experienceobelisk.info.infecting.tooltip_count",
                Component.literal(String.valueOf(recipe.count)).withStyle(ChatFormatting.GREEN));

        int arrow_x1 = 46;
        int arrow_x2 = arrow_x1 + 43;
        int arrow_y1 = 22;
        int arrow_y2 = arrow_y1 + 11;

        int counterarrow_x1 = 52;
        int counterarrow_x2 = counterarrow_x1 + 30;
        int counterarrow_y1 = 8;
        int counterarrow_y2 = counterarrow_y1 + 9;

        if(mouseX >= arrow_x1 && mouseX <= arrow_x2 && mouseY >= arrow_y1 && mouseY <= arrow_y2){
            tooltipList.add(desc);
            return tooltipList;
        }
        else if(recipe.getCount() > 0 && mouseX >= counterarrow_x1 && mouseX <= counterarrow_x2 && mouseY >= counterarrow_y1 && mouseY <= counterarrow_y2){
            tooltipList.add(requiredCount);
            return tooltipList;
        }

        return IRecipeCategory.super.getTooltipStrings(recipe, recipeSlotsView, mouseX, mouseY);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, InfectingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 15,19).setSlotName("input").addIngredients(recipe.getInput());
        builder.addSlot(RecipeIngredientRole.CATALYST, 58,35).setSlotName("catalyst").addIngredients(recipe.getCatalyst());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 99,19).setSlotName("output").addItemStack(recipe.getResultItem(null));
    }

    @Override
    public IDrawable getBackground() {
        return guiHelper.createDrawable(texture, 0, 0, 136, 66);
    }


}
