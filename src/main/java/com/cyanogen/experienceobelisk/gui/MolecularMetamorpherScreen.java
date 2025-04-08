package com.cyanogen.experienceobelisk.gui;

import com.cyanogen.experienceobelisk.block_entities.MolecularMetamorpherEntity;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MolecularMetamorpherScreen extends AbstractContainerScreen<MolecularMetamorpherMenu>{

    private final ResourceLocation texture = new ResourceLocation("experienceobelisk:textures/gui/screens/molecular_metamorpher.png");
    public MolecularMetamorpherEntity metamorpher;
    private final Component title = Component.translatable("title.experienceobelisk.molecular_metamorpher");
    private final Component inventoryTitle = Component.translatable("title.experienceobelisk.precision_dispeller.inventory");
    private final MolecularMetamorpherMenu menu;

    public MolecularMetamorpherScreen(MolecularMetamorpherMenu menu, Inventory inventory, Component component) {
        super(menu, menu.inventory, menu.component);
        this.metamorpher = menu.metamorpherClient;
        this.menu = menu;
    }

    @Override
    protected void renderBg(GuiGraphics gui, float f, int a, int b) {

    }

    @Override
    protected void init() {
        setupWidgetElements();
        super.init();
    }

    @Override
    protected void renderLabels(GuiGraphics gui, int mouseX, int mouseY) {
        gui.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0xFFFFFF);
        gui.drawString(this.font, this.inventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 0xFFFFFF);
    }

    @Override
    public void render(GuiGraphics gui, int mouseX, int mouseY, float partialTick) {

        renderBackground(gui);

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, texture);

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        int arrowWidth = 26;
        double completion = 0;
        if(metamorpher.getProcessTime() != 0){
            completion = metamorpher.getProcessProgress() / (float) metamorpher.getProcessTime();
        }

        //render background texture
        gui.blit(texture, x, y, 0, 0, 176, 166);

        //render recipe progress
        gui.blit(texture, this.width / 2 + 109 - 88, this.height / 2 + 48 - 83, 0, 175, (int) (arrowWidth * completion), 4);

        //render xp bar
        int xpBarLength = 61;
        int levels;
        int points;
        double progress;

        if(metamorpher.obeliskStillExists){
            levels = metamorpher.obeliskLevels;
            points = metamorpher.obeliskPoints;
            progress = metamorpher.obeliskProgress;

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

            tooltipList.add(Component.translatable("tooltip.experienceobelisk.molecular_metamorpher.bound"));

            tooltipList.add(Component.translatable("tooltip.experienceobelisk.molecular_metamorpher.xp",
                    Component.literal(String.valueOf(points)).withStyle(ChatFormatting.GREEN)));

            if(mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y2){
                gui.renderTooltip(this.font, tooltipList, Optional.empty(), mouseX, mouseY);
            }
        }

        clearWidgets();
        loadWidgetElements();

        //render settings button
        for(Renderable widget : this.renderables){
            widget.render(gui, mouseX, mouseY, partialTick);
        }

        super.render(gui, mouseX, mouseY, partialTick);
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

        Button settings = Button.builder(Component.translatable("button.experienceobelisk.experience_obelisk.settings"),
                        (onPress) -> Minecraft.getInstance().setScreen(new MolecularMetamorpherOptionsScreen(menu)))
                .size(20,20)
                .pos(this.width / 2 + 91, this.height / 2 - 78)
                .tooltip(Tooltip.create(Component.translatable("tooltip.experienceobelisk.experience_obelisk.settings")))
                .build();

        buttons.add(settings);

    }

}
