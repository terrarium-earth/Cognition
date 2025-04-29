package com.cyanogen.cognition.gui;

import com.cyanogen.cognition.block_entities.MolecularMetamorpherEntity;
import com.cyanogen.cognition.utils.ExperienceUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MolecularMetamorpherScreen extends AbstractContainerScreen<MolecularMetamorpherMenu>{

    private final ResourceLocation texture = ResourceLocation.parse("cognition:textures/gui/screens/molecular_metamorpher.png");
    private final Component title = Component.translatable("title.cognition.molecular_metamorpher");
    private final Component inventoryTitle = Component.translatable("title.cognition.precision_dispeller.inventory");
    private final Level clientLevel;
    public final Inventory inventory;
    public final Component component;
    private final int[] inputSlotsX = {19, 50, 70};
    private final int[] inputSlotsY = {35, 52, 18};

    public MolecularMetamorpherScreen(MolecularMetamorpherMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
        this.clientLevel = inventory.player.level();
        this.inventory = inventory;
        this.component = component;
    }

    @Override
    protected void renderBg(GuiGraphics gui, float f, int a, int b) {
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    }

    @Override
    protected void renderLabels(GuiGraphics gui, int mouseX, int mouseY) {
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    protected void init() {
        setupWidgetElements();
        super.init();
    }

    protected void renderTitles(GuiGraphics gui, int titleX, int titleY, int inventoryX, int inventoryY){
        gui.drawString(this.font, this.title, titleX, titleY, 0xFFFFFF);
        gui.drawString(this.font, this.inventoryTitle, inventoryX, inventoryY, 0xFFFFFF);
    }

    @Override
    public void render(GuiGraphics gui, int mouseX, int mouseY, float partialTick) {

        //render background shading
        renderTransparentBackground(gui);

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, texture);

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        int arrowWidth = 26;
        double completion = 0;

        BlockPos pos = menu.getBlockPos();
        if(pos != null && clientLevel.getBlockEntity(pos) instanceof MolecularMetamorpherEntity metamorpher && metamorpher.getProcessTime() != 0){
            completion = metamorpher.getProcessProgress() / (float) metamorpher.getProcessTime();
        }

        //render background texture
        gui.blit(texture, x, y, 0, 0, 176, 166);

        //render selection highlights
        super.render(gui, mouseX, mouseY, partialTick);

        //render recipe progress
        gui.blit(texture, this.width / 2 + 109 - 88, this.height / 2 + 48 - 83, 0, 175, (int) (arrowWidth * completion), 4);

        //render xp bar
        int xpBarLength = 61;
        int levels;
        int points;
        double progress;

        if(pos != null && clientLevel.getBlockEntity(pos) instanceof MolecularMetamorpherEntity metamorpher && metamorpher.getBoundObelisk() != null){

            //obelisk info
            if(metamorpher.getBoundObelisk() != null){
                levels = metamorpher.getBoundObelisk().getLevels();
                points = metamorpher.getBoundObelisk().getExperiencePoints();
                progress = ExperienceUtils.getProgressToNextLevel(points, levels);

                //render xp bar
                gui.blit(texture, this.width / 2 + 105 - 88, this.height / 2 + 70 - 83, 0, 179, 64, 11);
                gui.blit(texture, this.width / 2 + 107 - 88, this.height / 2 + 71 - 83, 0, 166, (int) (xpBarLength * progress), 9);

                //render level counter
                gui.drawCenteredString(this.font, Component.literal(String.valueOf(levels)).withStyle(ChatFormatting.GREEN),
                        this.width / 2 + 52,this.height / 2 - 11, 0xFFFFFF);

                //render XP tooltip
                int x1 = this.width / 2 + 19;
                int y1 = this.height / 2 - 12;
                int x2 = x1 + xpBarLength;
                int y2 = y1 + 9;

                List<Component> tooltipList = new ArrayList<>();
                tooltipList.add(Component.translatable("tooltip.cognition.molecular_metamorpher.bound"));
                tooltipList.add(Component.translatable("tooltip.cognition.molecular_metamorpher.xp",
                        Component.literal(String.valueOf(points)).withStyle(ChatFormatting.GREEN)));

                if(mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y2){
                    gui.renderTooltip(this.font, tooltipList, Optional.empty(), mouseX, mouseY);
                }
            }

            //render fake items
            if(metamorpher.inputsAreLocked()){
                for(int i = 0; i < 3; i++){
                    ItemStack stack = metamorpher.getSavedInputs().getStackInSlot(i);
                    gui.renderFakeItem(stack, inputSlotsX[i], inputSlotsY[i]);
                    System.out.println("rendering fake item: " + stack);
                    //todo: i can try to include some translucent layer over the items to make them appear faded out
                }
            }
        }

        clearWidgets();
        loadWidgetElements();

        //render settings button
        for(Renderable widget : this.renderables){
            widget.render(gui, mouseX, mouseY, partialTick);
        }

        this.renderTitles(gui, x + 8, y + 6, x + 8, y + 72);
        this.renderTooltip(gui, mouseX, mouseY);
    }

    private void loadWidgetElements(){
        if(!this.buttons.isEmpty()){
            for(Button b : this.buttons){
                b.setFocused(false);
                addRenderableWidget(b);
            }
        }
    }

    private final List<Button> buttons = new ArrayList<>();
    private void setupWidgetElements() {

        buttons.clear();

        Button settings = Button.builder(Component.translatable("button.cognition.experience_obelisk.settings"),
                        (onPress) -> Minecraft.getInstance().setScreen(new MolecularMetamorpherOptionsScreen(menu, clientLevel)))
                .size(20,20)
                .pos(this.width / 2 + 91, this.height / 2 - 78)
                .tooltip(Tooltip.create(Component.translatable("tooltip.cognition.experience_obelisk.settings")))
                .build();

        buttons.add(settings);

    }

}
