package com.cyanogen.experienceobelisk.recipe.jei;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.cyanogen.experienceobelisk.recipe.MolecularMetamorpherRecipe;
import com.cyanogen.experienceobelisk.registries.RegisterItems;
import com.cyanogen.experienceobelisk.utils.ExperienceUtils;
import com.mojang.blaze3d.vertex.PoseStack;
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
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.cyanogen.experienceobelisk.utils.RecipeUtils.getItemListWithCounts;

public class MolecularMetamorpherCategory implements IRecipeCategory<MolecularMetamorpherRecipe>{

    IRecipeCategoryRegistration registration;
    IGuiHelper guiHelper;
    private final ResourceLocation texture = new ResourceLocation("experienceobelisk:textures/gui/recipes/molecular_metamorpher_jei.png");
    private final IDrawableAnimated arrow;
    private final IDrawable xpBar;

    public static final RecipeType<MolecularMetamorpherRecipe> metamorpherType =
            RecipeType.create(MolecularMetamorpherRecipe.Type.ID, ExperienceObelisk.MOD_ID, MolecularMetamorpherRecipe.class);

    public MolecularMetamorpherCategory(IRecipeCategoryRegistration registration){
        this.registration = registration;
        this.guiHelper = registration.getJeiHelpers().getGuiHelper();

        this.arrow = guiHelper.drawableBuilder(texture,0,87,29,5)
                .buildAnimated(100, IDrawableAnimated.StartDirection.LEFT, false);
        this.xpBar = guiHelper.drawableBuilder(texture, 0, 92, 63, 10).build();
    }

    @Override
    public RecipeType<MolecularMetamorpherRecipe> getRecipeType() {
        return RecipeType.create(MolecularMetamorpherRecipe.Type.ID, ExperienceObelisk.MOD_ID, MolecularMetamorpherRecipe.class);
    }

    @Override
    public Component getTitle() {
        return Component.translatable("title.experienceobelisk.molecular_metamorpher");
    }

    @Override
    public IDrawable getBackground() {
        return guiHelper.createDrawable(texture, 0, 0, 176, 87);
    }

    @Override
    public @Nullable ResourceLocation getRegistryName(MolecularMetamorpherRecipe recipe) {
        return IRecipeCategory.super.getRegistryName(recipe);
    }

    @Override
    public IDrawable getIcon() {

        ItemStack icon = new ItemStack(RegisterItems.MOLECULAR_METAMORPHER_ITEM.get());
        return guiHelper.createDrawableItemStack(icon);
    }

    @Override
    public void draw(MolecularMetamorpherRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack poseStack, double mouseX, double mouseY) {

        arrow.draw(poseStack, 108, 47);
        xpBar.draw(poseStack, getWidth() - 66, getHeight() - 12);

        Font font = Minecraft.getInstance().font;
        int cost = recipe.getCost();
        int levelCost = ExperienceUtils.xpToLevels(cost);

        int offset = font.width(String.valueOf(levelCost)) / 2;

        //render level counter
        font.draw(poseStack, Component.literal(String.valueOf(levelCost)).withStyle(ChatFormatting.GREEN),
                getWidth() - 31 - offset, getHeight() - 11, 0xFFFFF);

        IRecipeCategory.super.draw(recipe, recipeSlotsView, poseStack, mouseX, mouseY);
    }

    @Override
    public List<Component> getTooltipStrings(MolecularMetamorpherRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {

        int cost = recipe.getCost();
        int time = recipe.getProcessTime() / 20;

        Component c2 = Component.literal(String.valueOf(cost)).withStyle(ChatFormatting.GREEN);
        Component c3 = Component.literal(time +"s").withStyle(ChatFormatting.GOLD);

        Component costXP = Component.translatable("jei.experienceobelisk.metamorpher.cost_xp", c2);
        Component processTime = Component.translatable("jei.experienceobelisk.metamorpher.process_time", c3);

        List<Component> tooltip = new ArrayList<>();

        int x1 = getWidth() - 67;
        int x2 = getWidth();
        int y1 = getHeight() - 14;
        int y2 = getHeight();

        if(mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y2){
            tooltip.add(costXP);
            tooltip.add(processTime);

            return tooltip;
        }

        return IRecipeCategory.super.getTooltipStrings(recipe, recipeSlotsView, mouseX, mouseY);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, MolecularMetamorpherRecipe recipe, IFocusGroup focuses) {

        ItemStack result = recipe.getResultItem();
        int[] x = {19,50,70};
        int[] y = {35,52,18};

        for(Map.Entry<Ingredient, Tuple<Integer, Integer>> entry : recipe.getIngredientMapNoFiller().entrySet()){

            int position = entry.getValue().getA() - 1;
            int count = entry.getValue().getB();
            Ingredient ingredient = entry.getKey();

            builder.addSlot(RecipeIngredientRole.INPUT, x[position], y[position])
                    .setSlotName("input" + position)
                    .addItemStacks(getItemListWithCounts(ingredient, count));
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 140,35).setSlotName("output").addItemStack(result);

        if(!recipe.getId().equals(new ResourceLocation(ExperienceObelisk.MOD_ID, "item_name_formatting"))){
            builder.setShapeless();
        }

    }


}
