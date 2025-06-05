package com.cyanogen.cognition.recipe.jei;

import com.cyanogen.cognition.Cognition;
import com.cyanogen.cognition.recipe.MolecularMetamorpherRecipe;
import com.cyanogen.cognition.registries.RegisterItems;
import com.cyanogen.cognition.utils.ExperienceUtils;
import com.cyanogen.cognition.utils.RecipeUtils;
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
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

import static com.cyanogen.cognition.utils.RecipeUtils.*;

public class MolecularMetamorpherCategory implements IRecipeCategory<MolecularMetamorpherRecipe>{

    IRecipeCategoryRegistration registration;
    IGuiHelper guiHelper;
    private final ResourceLocation texture = ResourceLocation.parse("cognition:textures/gui/recipes/molecular_metamorpher_jei.png");
    private final IDrawableAnimated arrow;

    public static final RecipeType<MolecularMetamorpherRecipe> METAMORPHER_TYPE =
            RecipeType.create(Cognition.MOD_ID, MolecularMetamorpherRecipe.Type.ID, MolecularMetamorpherRecipe.class);

    public MolecularMetamorpherCategory(IRecipeCategoryRegistration registration){
        this.registration = registration;
        this.guiHelper = registration.getJeiHelpers().getGuiHelper();

        this.arrow = guiHelper.drawableBuilder(texture,0,87,29,5)
                .buildAnimated(100, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public RecipeType<MolecularMetamorpherRecipe> getRecipeType() {
        return METAMORPHER_TYPE;
    }

    @Override
    public int getWidth() { //must be overriden or category will not register
        return 176;
    }

    @Override
    public int getHeight() { //must be overriden or category will not register
        return 87;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("title.cognition.molecular_metamorpher");
    }

    @Override
    public IDrawable getIcon() {

        ItemStack icon = new ItemStack(RegisterItems.MOLECULAR_METAMORPHER_ITEM.get());
        return guiHelper.createDrawableItemStack(icon);
    }

    @Override
    public void draw(MolecularMetamorpherRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {

        guiHelper.createDrawable(texture, 0, 0, 176, 87).draw(guiGraphics);
        arrow.draw(guiGraphics, 108, 47);

        Font font = Minecraft.getInstance().font;
        int cost = recipe.getCost();
        int levelCost = ExperienceUtils.xpToLevels(cost);
        int xpBarLength = 61;
        double progress = ExperienceUtils.getProgressToNextLevel(cost, levelCost);
        if(progress == 0) progress = 1;

        //render xp bar
        guiGraphics.blit(texture, getWidth() - 66, getHeight() - 12, 0, 92, (int) (xpBarLength * progress) + 2, 10);
        //render level counter
        guiGraphics.drawCenteredString(font, Component.literal(String.valueOf(levelCost)).withStyle(ChatFormatting.GREEN),
                getWidth() - 31, getHeight() - 11, 0xFFFFFF);

        IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, MolecularMetamorpherRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {

        int cost = recipe.getCost();
        int time = recipe.getProcessTime() / 20;

        Component c2 = Component.literal(String.valueOf(cost)).withStyle(ChatFormatting.GREEN);
        Component c3 = Component.literal(time +"s").withStyle(ChatFormatting.GOLD);

        Component costXP = Component.translatable("jei.cognition.metamorpher.cost_xp", c2);
        Component processTime = Component.translatable("jei.cognition.metamorpher.process_time", c3);

        int x1 = getWidth() - 67;
        int x2 = getWidth();
        int y1 = getHeight() - 14;
        int y2 = getHeight();

        if(mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y2){
            tooltip.add(costXP);
            tooltip.add(processTime);
        }

        IRecipeCategory.super.getTooltip(tooltip, recipe, recipeSlotsView, mouseX, mouseY);
    }


    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, MolecularMetamorpherRecipe recipe, IFocusGroup focuses) {

        if(!recipe.getId().equals(ResourceLocation.fromNamespaceAndPath(Cognition.MOD_ID, "item_name_formatting"))){
            builder.setShapeless();
            setJsonRecipe(builder, recipe);
        }
        else{
            setNameFormattingRecipe(builder);
        }

    }

    public void setJsonRecipe(IRecipeLayoutBuilder builder, MolecularMetamorpherRecipe recipe){

        int[] x = {19,50,70};
        int[] y = {35,52,18};


        for(int i = 1; i <= 3; i++){

            Ingredient ingredient = recipe.getIngredients(true).get(i).getA();
            int count = recipe.getIngredients(true).get(i).getB();

            builder.addSlot(RecipeIngredientRole.INPUT, x[i - 1], y[i - 1]).setSlotName("input" + i).addItemStacks(getItemListWithCounts(ingredient, count));
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 140,35).setSlotName("output").addItemStack(recipe.getResultItem(null));
    }

    public void setNameFormattingRecipe(IRecipeLayoutBuilder builder){

        List<ItemStack> formatItemList = convertItemListToItemStackList(getValidDyes());
        formatItemList.addAll(convertItemListToItemStackList(getValidFormattingItems()));

        ItemStack inputItem = new ItemStack(RegisterItems.DUMMY_SWORD.get(), 1).copy();
        inputItem.set(DataComponents.ITEM_NAME, Component.translatable("jei.cognition.name.any_item"));
        Ingredient formatItems = Ingredient.of(formatItemList.stream());
        List<ItemStack> outputItems = new ArrayList<>();

        for(Item dye : getValidDyes()){

            if(dye instanceof DyeItem dyeItem){
                int dyeColor = dyeItem.getDyeColor().getId();
                ChatFormatting format = ChatFormatting.getById(RecipeUtils.dyeColorToTextColor(dyeColor));

                assert format != null;
                ItemStack outputItem = inputItem.copy();
                outputItem.set(DataComponents.ITEM_NAME, Component.translatable("jei.cognition.name.any_item").withStyle(format));
                outputItems.add(outputItem);
            }
        }
        for(Item formattingItem : getValidFormattingItems()){

            int index = RecipeUtils.getValidFormattingItems().indexOf(formattingItem);
            char code = RecipeUtils.itemToFormat(index);
            ChatFormatting format = ChatFormatting.getByCode(code);

            assert format != null;
            ItemStack outputItem = inputItem.copy();
            outputItem.set(DataComponents.ITEM_NAME, Component.translatable("jei.cognition.name.any_item").withStyle(format));
            outputItems.add(outputItem);
        }

        builder.addSlot(RecipeIngredientRole.INPUT, 19, 35).setSlotName("input1").addItemStack(inputItem);
        builder.addSlot(RecipeIngredientRole.INPUT, 50, 52).setSlotName("input2").addItemStack(Items.NAME_TAG.getDefaultInstance());
        builder.addSlot(RecipeIngredientRole.INPUT, 70, 18).setSlotName("input3").addIngredients(formatItems);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 140, 35).setSlotName("output").addItemStacks(outputItems);
    }

}
