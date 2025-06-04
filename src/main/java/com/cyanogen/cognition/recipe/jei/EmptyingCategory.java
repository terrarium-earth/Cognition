package com.cyanogen.cognition.recipe.jei;

import com.cyanogen.cognition.Cognition;
import com.cyanogen.cognition.recipe.EmptyingRecipe;
import com.cyanogen.cognition.registries.RegisterItems;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
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
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class EmptyingCategory implements IRecipeCategory<EmptyingRecipe> {

    IRecipeCategoryRegistration registration;
    IGuiHelper guiHelper;
    public final ResourceLocation defaultBackground = ResourceLocation.parse("cognition:textures/gui/recipes/information_jei.png");
    public final ResourceLocation emptyingBackground = ResourceLocation.parse("cognition:textures/gui/recipes/emptying_jei.png");
    public final ResourceLocation cognitiumTexture = ResourceLocation.parse("cognition:textures/block/cognitium_still.png");
    public final IDrawableAnimated arrow;
    public final IDrawable cognitiumStack;

    public static final RecipeType<EmptyingRecipe> emptyingType =
            RecipeType.create(Cognition.MOD_ID, EmptyingRecipe.Type.ID, EmptyingRecipe.class);

    public EmptyingCategory(IRecipeCategoryRegistration registration){
        this.registration = registration;
        this.guiHelper = registration.getJeiHelpers().getGuiHelper();
        this.arrow = guiHelper.drawableBuilder(defaultBackground,0,66,41,7)
                .buildAnimated(100, IDrawableAnimated.StartDirection.LEFT, false);
        this.cognitiumStack = guiHelper.drawableBuilder(cognitiumTexture, 0,0,16,16).build();
    }

    @Override
    public RecipeType<EmptyingRecipe> getRecipeType() {
        return emptyingType;
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
    public Component getTitle() {
        return Component.translatable("jei.cognition.info.emptying.title");
    }


    @Override
    public IDrawable getIcon() {
        ItemStack icon = new ItemStack(RegisterItems.EXPERIENCE_FOUNTAIN_ITEM.get());
        return guiHelper.createDrawableItemStack(icon);
    }

    @Override
    public void draw(EmptyingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiHelper.createDrawable(emptyingBackground, 0, 0, 136, 66).draw(guiGraphics);
        arrow.draw(guiGraphics, 35, 21);

        if(recipe.getCognitiumGain() > 0){
            cognitiumStack.draw(guiGraphics, 110, 18);
        }
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltipBuilder, EmptyingRecipe recipe,
                           IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {

        getArrowTooltip(tooltipBuilder, mouseX, mouseY);
        getCognitiumTooltip(recipe, tooltipBuilder, mouseX, mouseY);
    }

    public void getArrowTooltip(ITooltipBuilder tooltipBuilder, double mouseX, double mouseY){

        Component arrowTooltip = Component.translatable("jei.cognition.info.emptying.tooltip");

        int x1 = 49;
        int x2 = x1 + 43;
        int y1 = 21;
        int y2 = y1 + 11;

        if(mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y2){
            tooltipBuilder.add(arrowTooltip);
        }
    }

    public void getCognitiumTooltip(EmptyingRecipe recipe, ITooltipBuilder tooltipBuilder, double mouseX, double mouseY){

        int x1 = 110;
        int x2 = x1 + 16;
        int y1 = 18;
        int y2 = y1 + 16;

        if(recipe.getCognitiumGain() > 0 && mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y2){

            Component amount = Component.translatable("jei.cognition.info.emptying.cognitium_amount",
                    Component.literal(String.valueOf(recipe.getCognitiumGain())).withStyle(ChatFormatting.GREEN));

            //todo: tooltip should give the following info:
            // 1. cognitium volume --- e.g. [106900] mB Cognitium
            // 2. levels --- e.g. [50] levels
            // 3. xp --- e.g. [5345] XP
            // format [] with ChatFormatting.GREEN

            tooltipBuilder.add(amount);
        }
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, EmptyingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 10,18).setSlotName("input").addIngredients(recipe.getIngredient());
        builder.addSlot(RecipeIngredientRole.CATALYST, 47,34).setSlotName("catalyst")
                .addItemStack(RegisterItems.EXPERIENCE_FOUNTAIN_ITEM.get().getDefaultInstance());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 86,18).setSlotName("output").addItemStack(recipe.getResultItem(null));
    }

}
