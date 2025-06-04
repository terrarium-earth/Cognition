package com.cyanogen.cognition.recipe.jei;

import com.cyanogen.cognition.Cognition;
import com.cyanogen.cognition.registries.RegisterItems;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class FillingCategory extends AbstractInformationalCategory {

    public static final RecipeType<AbstractInformationalRecipe> fillingType =
            RecipeType.create(Cognition.MOD_ID, FillingRecipeOld.Type.ID, FillingRecipeOld.class);

    public FillingCategory(IRecipeCategoryRegistration registration){
        super(registration);
    }

    @Override
    public RecipeType<AbstractInformationalRecipe> getRecipeType() {
        return fillingType;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.cognition.info.filling.title");
    }

    @Override
    public IDrawable getIcon() {
        ItemStack icon = new ItemStack(RegisterItems.EXPERIENCE_FOUNTAIN_ITEM.get());
        return guiHelper.createDrawableItemStack(icon);
    }

    @Override
    public Component getProcessTooltip() {
        return Component.translatable("jei.cognition.info.filling.tooltip");
    }

    @Override
    public void draw(AbstractInformationalRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
        Font font = Minecraft.getInstance().font;

        if(recipe instanceof FillingRecipeOld fillingRecipe){
            String cost = "-" + fillingRecipe.getIntegerXpCost() + " XP";

            guiGraphics.drawString(Minecraft.getInstance().font, cost,
                    getWidth() - font.width(cost) - 1,getHeight() - 7, grey, false);
        }

    }
}
