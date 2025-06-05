package com.cyanogen.cognition.recipe.jei;

import com.cyanogen.cognition.Cognition;
import com.cyanogen.cognition.recipe.InfectingRecipe;
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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InfectingCategory implements IRecipeCategory<InfectingRecipe> {

    IRecipeCategoryRegistration registration;
    IGuiHelper guiHelper;
    public final ResourceLocation infectingBackground = ResourceLocation.parse("cognition:textures/gui/recipes/infecting_jei.png");
    public final IDrawableAnimated arrow;
    public final IDrawable counterArrow;

    private final int arrowWidth = 41;
    private final int arrowHeight = 7;
    private final int arrowOffsetX = 49;
    private final int arrowOffsetY = 21;

    private final int counterArrowWidth = 11;
    private final int counterArrowHeight = 9;
    private final int counterArrowXOffset = 56;
    private final int counterArrowYOffset = 7;

    public static final RecipeType<InfectingRecipe> INFECTING_TYPE =
            RecipeType.create(Cognition.MOD_ID, InfectingRecipe.Type.ID, InfectingRecipe.class);

    public InfectingCategory(IRecipeCategoryRegistration registration){
        this.registration = registration;
        this.guiHelper = registration.getJeiHelpers().getGuiHelper();
        this.arrow = guiHelper.drawableBuilder(infectingBackground,0,66, arrowWidth, arrowHeight)
                .buildAnimated(100, IDrawableAnimated.StartDirection.LEFT, false);
        this.counterArrow = guiHelper.drawableBuilder(infectingBackground, 0,73,11,9).build();
    }

    @Override
    public RecipeType<InfectingRecipe> getRecipeType() {
        return INFECTING_TYPE;
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
        return Component.translatable("jei.cognition.info.infecting.title");
    }

    @Override
    public IDrawable getIcon() {
        ItemStack icon = new ItemStack(RegisterItems.BIBLIOPHAGE.get());
        return guiHelper.createDrawableItemStack(icon);
    }

    @Override
    public void draw(InfectingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiHelper.createDrawable(infectingBackground, 0, 0, 136, 66).draw(guiGraphics);
        arrow.draw(guiGraphics, 49, 21);

        if(recipe.getInfectionCount() > 0){
            counterArrow.draw(guiGraphics, counterArrowXOffset, counterArrowYOffset);
            guiGraphics.drawString(Minecraft.getInstance().font, "x"+recipe.getInfectionCount(),
                    counterArrowXOffset + 15,counterArrowYOffset + 1,
                    0x8b8b8b, false);
        }
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltipBuilder, InfectingRecipe recipe,
                           IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {

        getArrowTooltip(tooltipBuilder, mouseX, mouseY);
        getCounterArrowTooltip(recipe.getInfectionCount(), tooltipBuilder, mouseX, mouseY);
    }

    public void getArrowTooltip(ITooltipBuilder tooltipBuilder, double mouseX, double mouseY){

        Component arrowTooltip = Component.translatable("jei.cognition.info.infecting.tooltip");

        int x1 = arrowOffsetX - 3;
        int x2 = arrowOffsetX + arrowWidth + 3;
        int y1 = arrowOffsetY - 2;
        int y2 = arrowOffsetY + arrowHeight + 2;

        if(mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y2){
            tooltipBuilder.add(arrowTooltip);
        }
    }

    public void getCounterArrowTooltip(int infectionCount, ITooltipBuilder tooltipBuilder, double mouseX, double mouseY){

        Component counterArrowTooltip = Component.translatable("jei.cognition.info.infecting.tooltip_count",
                Component.literal(String.valueOf(infectionCount)).withStyle(ChatFormatting.GREEN));

        int x1 = counterArrowXOffset - 1;
        int x2 = counterArrowXOffset + counterArrowWidth + 19;
        int y1 = counterArrowYOffset - 1;
        int y2 = counterArrowYOffset + counterArrowHeight + 1;

        if(infectionCount > 1 && mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y2){
            tooltipBuilder.add(counterArrowTooltip);
        }
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, InfectingRecipe recipe, IFocusGroup focuses) {

        builder.addSlot(RecipeIngredientRole.INPUT, 18,18).setSlotName("input").addIngredients(recipe.getIngredient());
        builder.addSlot(RecipeIngredientRole.CATALYST, 61,34).setSlotName("catalyst").addItemStacks(getCatalysts());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 102,18).setSlotName("output").addItemStack(recipe.getResultItem(null));
    }

    public List<ItemStack> getCatalysts(){
        List<ItemStack> catalysts = new ArrayList<>();

        catalysts.add(RegisterItems.BIBLIOPHAGE.get().getDefaultInstance());
        catalysts.add(RegisterItems.INFECTED_BOOKSHELF_ITEM.get().getDefaultInstance());
        catalysts.add(RegisterItems.INFECTED_ARCHIVERS_BOOKSHELF_ITEM.get().getDefaultInstance());
        catalysts.add(RegisterItems.INFECTED_ENCHANTED_BOOKSHELF_ITEM.get().getDefaultInstance());
        catalysts.add(RegisterItems.NUTRIENT_AGAR_ITEM.get().getDefaultInstance());
        catalysts.add(RegisterItems.INSIGHTFUL_AGAR_ITEM.get().getDefaultInstance());
        catalysts.add(RegisterItems.EXTRAVAGANT_AGAR_ITEM.get().getDefaultInstance());

        return catalysts;
    }

}
