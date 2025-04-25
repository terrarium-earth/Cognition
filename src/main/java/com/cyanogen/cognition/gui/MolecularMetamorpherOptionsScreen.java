package com.cyanogen.cognition.gui;

import com.cyanogen.cognition.block_entities.MolecularMetamorpherEntity;
import com.cyanogen.cognition.network.shared.UpdateRedstone;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class MolecularMetamorpherOptionsScreen extends Screen{

    private final ResourceLocation texture = ResourceLocation.parse("experienceobelisk:textures/gui/screens/experience_obelisk.png");
    private final MolecularMetamorpherMenu menu;
    private final Level clientLevel;

    public MolecularMetamorpherOptionsScreen(MolecularMetamorpherMenu menu, Level level) {
        super(menu.component);
        this.menu = menu;
        clientLevel = level;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public Component getTitle() {
        return Component.empty();
    }

    @Override
    protected void init() {
        setupWidgetElements();
        super.init();
    }

    @Override
    public void render(GuiGraphics gui, int mouseX, int mouseY, float partialTick) {

        renderTransparentBackground(gui);

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, texture);

        int textureWidth = 256;
        int textureHeight = 256;
        int x = this.width / 2 - 176 / 2;
        int y = this.height / 2 - 166 / 2;

        //render gui texture
        gui.blit(texture, x, y, 0, 0, 176, 166, textureWidth, textureHeight);

        //descriptors & info
        gui.drawCenteredString(this.font, Component.translatable("title.experienceobelisk.experience_obelisk.settings"),
                this.width / 2,this.height / 2 - 76, 0xFFFFFF);
        gui.drawString(this.font, Component.translatable("title.experienceobelisk.experience_obelisk.redstone"),
                this.width / 2 - 77,this.height / 2 - 56, 0xFFFFFF);

        //render widgets
        clearWidgets();

        BlockPos pos = menu.getBlockPos();
        if(pos != null && clientLevel.getBlockEntity(pos) instanceof MolecularMetamorpherEntity metamorpher){
            if(metamorpher.isRedstoneEnabled()){
                buttons.get(1).setMessage(Component.translatable("button.experienceobelisk.experience_obelisk.enabled"));
            }
            else{
                buttons.get(1).setMessage(Component.translatable("button.experienceobelisk.experience_obelisk.ignored"));
            }
        }
        else{
            buttons.get(1).setMessage(Component.translatable("button.experienceobelisk.experience_obelisk.ignored"));
        }

        loadWidgetElements();


        for(Renderable widget : this.renderables) {
            widget.render(gui, mouseX, mouseY, partialTick);
        }
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
    private void setupWidgetElements(){

        buttons.clear();

        int w = 50;
        int h = 20;
        int y1 = 43;

        Button back = Button.builder(Component.translatable("button.experienceobelisk.experience_obelisk.back"),
                        (onPress) -> Minecraft.getInstance().setScreen(new MolecularMetamorpherScreen(menu, menu.inventory, menu.component)))
                .size(20,20)
                .pos(this.width / 2 + 91, this.height / 2 - 78)
                .tooltip(Tooltip.create(Component.translatable("tooltip.experienceobelisk.experience_obelisk.back")))
                .build();

        Button toggleRedstone = Button.builder(Component.empty(),
                        (onPress) -> toggleRedstone())
                .size(w, h)
                .pos(this.width / 2 - 25, this.height / 2 - y1)
                .build();

        buttons.add(back);
        buttons.add(toggleRedstone);
    }

    private void toggleRedstone(){
        BlockPos pos = menu.getBlockPos();
        if(pos != null && clientLevel.getBlockEntity(pos) instanceof MolecularMetamorpherEntity metamorpher){
            PacketDistributor.sendToServer(new UpdateRedstone(menu.getBlockPos(), !metamorpher.isRedstoneEnabled()));
        }
    }

}
