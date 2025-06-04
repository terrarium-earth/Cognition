package com.cyanogen.cognition.recipe.jei;

import com.cyanogen.cognition.Cognition;
import com.cyanogen.cognition.registries.RegisterItems;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class InfectingCategory extends AbstractInformationalCategory {

    IRecipeCategoryRegistration registration;
    private final int counterArrowXOffset = 56;
    private final int counterArrowYOffset = 7;

    public static final RecipeType<AbstractInformationalRecipe> infectingType =
            RecipeType.create(Cognition.MOD_ID, InfectingRecipeOld.Type.ID, InfectingRecipeOld.class);

    public InfectingCategory(IRecipeCategoryRegistration registration){
        super(registration);
        this.registration = registration;
    }

    @Override
    public RecipeType<AbstractInformationalRecipe> getRecipeType() {
        return infectingType;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.cognition.info.infecting.title");
    }

    @Override
    public IDrawable getIcon() {
        ItemStack icon = new ItemStack(RegisterItems.BIBLIOPHAGE.get());
        return guiHelper.createDrawableItemStack(icon);
    }

    @Override
    public void draw(AbstractInformationalRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);

        if(recipe.count > 0){
            counterArrow.draw(guiGraphics, counterArrowXOffset, counterArrowYOffset);
            guiGraphics.drawString(Minecraft.getInstance().font, "x"+recipe.count,counterArrowXOffset + 15,counterArrowYOffset + 1,
                    0x8b8b8b, false);
        }
    }

    @Override
    public Component getProcessTooltip() {
        return Component.translatable("jei.cognition.info.infecting.tooltip");
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltipBuilder, AbstractInformationalRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {

        Component requiredCount = Component.translatable("jei.cognition.info.infecting.tooltip_count",
                Component.literal(String.valueOf(recipe.count)).withStyle(ChatFormatting.GREEN));

        int counterArrow_x2 = counterArrowXOffset + 30;
        int counterArrow_y2 = counterArrowYOffset + 9;

        if(recipe instanceof InfectingRecipeOld infectingRecipe && infectingRecipe.getCount() > 0
                && mouseX >= counterArrowXOffset && mouseX <= counterArrow_x2 && mouseY >= counterArrowYOffset && mouseY <= counterArrow_y2){

            tooltipBuilder.add(requiredCount);
        }

        super.getTooltip(tooltipBuilder, recipe, recipeSlotsView, mouseX, mouseY);
    }

}
