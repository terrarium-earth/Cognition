package com.cyanogen.cognition.recipe.jei;

import com.cyanogen.cognition.Cognition;
import com.cyanogen.cognition.recipe.FillingRecipe;
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

public class FillingCategory implements IRecipeCategory<FillingRecipe> {

    IRecipeCategoryRegistration registration;
    IGuiHelper guiHelper;
    public final ResourceLocation fillingBackground = ResourceLocation.parse("cognition:textures/gui/recipes/filling_jei.png");
    public final ResourceLocation cognitiumTexture = ResourceLocation.parse("cognition:textures/block/cognitium_still.png");
    public final IDrawableAnimated arrow;
    public final IDrawable cognitiumStack;

    private final int arrowWidth = 41;
    private final int arrowHeight = 7;
    private final int arrowOffsetX = 59;
    private final int arrowOffsetY = 21;

    private final int cognitiumOffsetX = 34;
    private final int cognitiumOffsetY = 18;

    public static final RecipeType<FillingRecipe> FILLING_TYPE =
            RecipeType.create(Cognition.MOD_ID, FillingRecipe.Type.ID, FillingRecipe.class);

    public FillingCategory(IRecipeCategoryRegistration registration){
        this.registration = registration;
        this.guiHelper = registration.getJeiHelpers().getGuiHelper();
        this.arrow = guiHelper.drawableBuilder(fillingBackground,0,66, arrowWidth, arrowHeight)
                .buildAnimated(100, IDrawableAnimated.StartDirection.LEFT, false);
        this.cognitiumStack = guiHelper.drawableBuilder(cognitiumTexture, 0,0,16,16).build();
    }

    @Override
    public RecipeType<FillingRecipe> getRecipeType() {
        return FILLING_TYPE;
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
        return Component.translatable("jei.cognition.info.filling.title");
    }

    @Override
    public IDrawable getIcon() {
        ItemStack icon = new ItemStack(RegisterItems.EXPERIENCE_FOUNTAIN_ITEM.get());
        return guiHelper.createDrawableItemStack(icon);
    }

    @Override
    public void draw(FillingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiHelper.createDrawable(fillingBackground, 0, 0, 136, 66).draw(guiGraphics);
        arrow.draw(guiGraphics, arrowOffsetX, arrowOffsetY);

        if(recipe.getCognitiumCost() > 0){
            cognitiumStack.draw(guiGraphics, cognitiumOffsetX, cognitiumOffsetY);
        }
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltipBuilder, FillingRecipe recipe,
                           IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {

        getArrowTooltip(tooltipBuilder, mouseX, mouseY);
        getCognitiumTooltip(recipe.getCognitiumCost(), tooltipBuilder, mouseX, mouseY);
    }

    public void getArrowTooltip(ITooltipBuilder tooltipBuilder, double mouseX, double mouseY){

        Component arrowTooltip = Component.translatable("jei.cognition.info.filling.tooltip");

        int x1 = arrowOffsetX - 3;
        int x2 = arrowOffsetX + arrowWidth + 3;
        int y1 = arrowOffsetY - 2;
        int y2 = arrowOffsetY + arrowHeight + 2;

        if(mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y2){
            tooltipBuilder.add(arrowTooltip);
        }
    }

    public void getCognitiumTooltip(int cognitiumCost, ITooltipBuilder tooltipBuilder, double mouseX, double mouseY){

        int x1 = cognitiumOffsetX;
        int x2 = x1 + 16;
        int y1 = cognitiumOffsetY;
        int y2 = y1 + 16;

        if(cognitiumCost > 0 && mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y2){

            Component amount = Component.translatable("jei.cognition.info.filling.cognitium_amount",
                    Component.literal(String.valueOf(cognitiumCost)).withStyle(ChatFormatting.GREEN));

            //todo: tooltip should give the following info:
            // 1. cognitium volume --- e.g. [106900] mB Cognitium
            // 2. levels --- e.g. [50] levels
            // 3. xp --- e.g. [5345] XP
            // format [] with ChatFormatting.GREEN

            tooltipBuilder.add(amount);
        }
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FillingRecipe recipe, IFocusGroup focuses) {

        ItemStack catalyst = RegisterItems.EXPERIENCE_FOUNTAIN_ITEM.get().getDefaultInstance();

        builder.addSlot(RecipeIngredientRole.INPUT, 10,18).setSlotName("input").addIngredients(recipe.getIngredient());
        builder.addSlot(RecipeIngredientRole.CATALYST, 71,34).setSlotName("catalyst").addItemStack(catalyst);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 86,18).setSlotName("output").addItemStack(recipe.getResultItem(null));
    }
}
