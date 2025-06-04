package com.cyanogen.cognition.recipe.jei.info;

import com.cyanogen.cognition.Cognition;
import com.cyanogen.cognition.registries.RegisterItems;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class EmptyingCategory extends AbstractInformationalCategory{

    public static final RecipeType<AbstractInformationalRecipe> emptyingType =
            RecipeType.create(Cognition.MOD_ID, EmptyingRecipeOld.Type.ID, EmptyingRecipeOld.class);

    public EmptyingCategory(IRecipeCategoryRegistration registration){
        super(registration);
    }

    @Override
    public RecipeType<AbstractInformationalRecipe> getRecipeType() {
        return emptyingType;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.cognition.info.emptying.title");
    }

    @Override
    public IDrawable getIcon() {
        ItemStack icon = new ItemStack(RegisterItems.EXPERIENCE_FOUNTAIN_ITEM.get());
        return guiHelper.createDrawableItemStack(icon);
    }

    @Override
    public Component getProcessTooltip() {
        return Component.translatable("jei.cognition.info.emptying.tooltip");
    }

    @Override
    public void draw(AbstractInformationalRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiHelper.createDrawable(emptyingBackground, 0, 0, 136, 66).draw(guiGraphics);
        arrow.draw(guiGraphics, 35, 21);
        Font font = Minecraft.getInstance().font;

        if(recipe instanceof EmptyingRecipeOld emptyingRecipe){

            if(emptyingRecipe.getCognitiumAmount() > 0){
                cognitiumStack.draw(guiGraphics, 110, 18);
            }

            String gain = "+" + emptyingRecipe.getIntegerXpGain() + " XP";
            guiGraphics.drawString(Minecraft.getInstance().font, gain,
                    getWidth() - font.width(gain) - 1,getHeight() - 7, grey, false);
        }
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltipBuilder, AbstractInformationalRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {

        int x1 = 110;
        int x2 = x1 + 16;
        int y1 = 18;
        int y2 = y1 + 16;

        if(recipe instanceof EmptyingRecipeOld emptyingRecipe && emptyingRecipe.getCognitiumAmount() > 0
                && mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y2){

            Component amount = Component.translatable("jei.cognition.info.emptying.cognitium_amount",
                    Component.literal(String.valueOf(emptyingRecipe.getCognitiumAmount())).withStyle(ChatFormatting.GREEN));

            tooltipBuilder.add(amount);
        }

        super.getTooltip(tooltipBuilder, recipe, recipeSlotsView, mouseX + 14, mouseY);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AbstractInformationalRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 10,18).setSlotName("input").addIngredients(recipe.getInput());
        builder.addSlot(RecipeIngredientRole.CATALYST, 47,34).setSlotName("catalyst").addIngredients(recipe.getCatalyst());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 86,18).setSlotName("output").addItemStack(recipe.getResultItem());
    }
}
