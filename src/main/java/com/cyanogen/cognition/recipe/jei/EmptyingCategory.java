package com.cyanogen.cognition.recipe.jei;

import com.cyanogen.cognition.Cognition;
import com.cyanogen.cognition.recipe.EmptyingRecipe;
import com.cyanogen.cognition.registries.RegisterItems;
import com.cyanogen.cognition.utils.ExperienceUtils;
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
    public final ResourceLocation emptyingBackground = ResourceLocation.parse("cognition:textures/gui/recipes/emptying_jei.png");
    public final ResourceLocation cognitiumTexture = ResourceLocation.parse("cognition:textures/block/cognitium_still.png");
    public final IDrawableAnimated arrow;
    public final IDrawable cognitiumStack;

    private final int arrowWidth = 41;
    private final int arrowHeight = 7;
    private final int arrowOffsetX = 35;
    private final int arrowOffsetY = 21;

    private final int cognitiumOffsetX = 110;
    private final int cognitiumOffsetY = 18;

    public static final RecipeType<EmptyingRecipe> EMPTYING_TYPE =
            RecipeType.create(Cognition.MOD_ID, EmptyingRecipe.Type.ID, EmptyingRecipe.class);

    public EmptyingCategory(IRecipeCategoryRegistration registration){
        this.registration = registration;
        this.guiHelper = registration.getJeiHelpers().getGuiHelper();
        this.arrow = guiHelper.drawableBuilder(emptyingBackground,0,66,arrowWidth,arrowHeight)
                .buildAnimated(100, IDrawableAnimated.StartDirection.LEFT, false);
        this.cognitiumStack = guiHelper.drawableBuilder(cognitiumTexture, 0,0,16,16).build();
    }

    @Override
    public RecipeType<EmptyingRecipe> getRecipeType() {
        return EMPTYING_TYPE;
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
        return Component.translatable("jei.cognition.emptying.title");
    }


    @Override
    public IDrawable getIcon() {
        ItemStack icon = new ItemStack(RegisterItems.EXPERIENCE_FOUNTAIN_ITEM.get());
        return guiHelper.createDrawableItemStack(icon);
    }

    @Override
    public void draw(EmptyingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiHelper.createDrawable(emptyingBackground, 0, 0, 136, 66).draw(guiGraphics);
        arrow.draw(guiGraphics, arrowOffsetX, arrowOffsetY);

        if(recipe.getCognitiumGain() > 0){
            cognitiumStack.draw(guiGraphics, cognitiumOffsetX, cognitiumOffsetY);
        }
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltipBuilder, EmptyingRecipe recipe,
                           IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {

        getArrowTooltip(tooltipBuilder, mouseX, mouseY);
        getCognitiumTooltip(recipe.getCognitiumGain(), tooltipBuilder, mouseX, mouseY);
    }

    public void getArrowTooltip(ITooltipBuilder tooltipBuilder, double mouseX, double mouseY){

        Component arrowTooltip = Component.translatable("jei.cognition.emptying.tooltip");

        int x1 = arrowOffsetX - 3;
        int x2 = arrowOffsetX + arrowWidth + 3;
        int y1 = arrowOffsetY - 2;
        int y2 = arrowOffsetY + arrowHeight + 2;

        if(mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y2){
            tooltipBuilder.add(arrowTooltip);
        }
    }

    public void getCognitiumTooltip(int cognitiumGain, ITooltipBuilder tooltipBuilder, double mouseX, double mouseY){

        int x1 = cognitiumOffsetX;
        int x2 = x1 + 16;
        int y1 = cognitiumOffsetY;
        int y2 = y1 + 16;

        if(cognitiumGain > 0 && mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y2){

            int xp = cognitiumGain / 20;
            int levels = ExperienceUtils.xpToLevels(xp);

            Component volumeComponent = Component.translatable("jei.cognition.shared.cognitium_amount.mb",
                    Component.literal(String.valueOf(cognitiumGain)).withStyle(ChatFormatting.GREEN));

            Component xpLevelsComponent = Component.translatable("jei.cognition.shared.cognitium_amount.levels_xp",
                    Component.literal(String.valueOf(levels)).withStyle(ChatFormatting.GREEN),
                    Component.literal(String.valueOf(xp)).withStyle(ChatFormatting.GREEN));

            tooltipBuilder.add(volumeComponent);
            tooltipBuilder.add(xpLevelsComponent);
        }
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, EmptyingRecipe recipe, IFocusGroup focuses) {

        ItemStack catalyst = RegisterItems.EXPERIENCE_FOUNTAIN_ITEM.get().getDefaultInstance();
        ItemStack result = ItemStack.EMPTY;
        if(recipe.hasResultStack()){
            result = recipe.getResultItem(null);
        }

        builder.addSlot(RecipeIngredientRole.INPUT, 10,18).setSlotName("input").addIngredients(recipe.getIngredient());
        builder.addSlot(RecipeIngredientRole.CATALYST, 47,34).setSlotName("catalyst").addItemStack(catalyst);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 86,18).setSlotName("output").addItemStack(result);
    }

}
