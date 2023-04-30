package com.cyanogen.experienceobelisk.gui;

import com.cyanogen.experienceobelisk.block_entities.XPObeliskEntity;
import com.cyanogen.experienceobelisk.network.PacketHandler;
import com.cyanogen.experienceobelisk.network.experienceobelisk.XPObeliskUpdateRadius;
import com.cyanogen.experienceobelisk.network.experienceobelisk.XPObeliskUpdateRedstone;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Objects;

public class ExperienceObeliskOptionsScreen extends Screen {

    public Level level;
    public Player player;
    public BlockPos pos;
    public XPObeliskEntity xpobelisk;

    private Button increaseRadius;
    private Button decreaseRadius;
    private Button currentRadius;
    private Button toggleRedstone;
    private Button lockFluid;
    private Button back;

    private final ResourceLocation texture = new ResourceLocation("experienceobelisk:textures/gui/container/dark_bg2.png");

    protected ExperienceObeliskOptionsScreen(Level level, Player player, BlockPos pos, ExperienceObeliskScreen screen) {
        super(new TextComponent("Experience Obelisk"));
        this.level = level;
        this.player = player;
        this.pos = pos;
        this.xpobelisk = screen.xpobelisk;
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (pKeyCode == 256 || pKeyCode == 69) {
            this.onClose();
            return true;
        } else {
            return super.keyPressed(pKeyCode, pScanCode, pModifiers);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public Minecraft getMinecraft() {
        return this.minecraft;
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, texture);

        int textureWidth = 256;
        int textureHeight = 256;
        int x = this.width / 2 - 176 / 2;
        int y = this.height / 2 - 166 / 2;


        //render gui texture
        blit(pPoseStack, x, y, 0, 0, 176, 166, textureWidth, textureHeight);

        //widgets
        setupWidgetElements();

        //descriptors & info
        drawCenteredString(new PoseStack(), this.font, "Settings",
                this.width / 2,this.height / 2 - 76, 0xFFFFFF);
        drawString(new PoseStack(), this.font, "Radius",
                this.width / 2 - 77,this.height / 2 - 56, 0xFFFFFF);
        drawString(new PoseStack(), this.font, "Redstone",
                this.width / 2 - 77,this.height / 2 - 10, 0xFFFFFF);


        //render widgets
        for(Widget widget : this.renderables) {
            widget.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        }

    }


    private void setupWidgetElements(){

        clearWidgets();
        int w = 50; //width (divisible by 2)
        int h = 20; //height
        int s = 2; //spacing
        int y1 = 43;
        int y2 = -3;
        int y3 = -49;

        Style green = Style.EMPTY.withColor(0x45FF5B);
        Style red = Style.EMPTY.withColor(0xFF454B);
        double radius = xpobelisk.getRadius();

        String status;
        Style styleStatus;
        if(xpobelisk.isRedstoneEnabled()){
            status = "Enabled";
            styleStatus = green;
        }
        else{
            status = "Ignored";
            styleStatus = red;
        }


        decreaseRadius = addRenderableWidget(new Button(this.width / 2 - 56, this.height / 2 - y1, 26, h, new TextComponent("-")
                .setStyle(red), (onPress) -> {

            PacketHandler.INSTANCE.sendToServer(new XPObeliskUpdateRadius(pos, -0.5));


        }));


        currentRadius = addRenderableWidget(new Button(this.width / 2 - 25, this.height / 2 - y1, 50, h, new TextComponent(String.valueOf(radius))
                , (onPress) -> {

            PacketHandler.INSTANCE.sendToServer(new XPObeliskUpdateRadius(pos, 0));

        },
                new Button.OnTooltip() {
                    @Override
                    public void onTooltip(Button pButton, PoseStack pPoseStack, int pMouseX, int pMouseY) {
                        renderTooltip(pPoseStack, new TranslatableComponent("tooltip.experienceobelisk.experience_obelisk.radius"), pMouseX, pMouseY);
                    }
                }
        ));

        increaseRadius = addRenderableWidget(new Button((int) (this.width / 2 + 30), this.height / 2 - y1, 26, h, new TextComponent("+")
                .setStyle(green), (onPress) -> {

            PacketHandler.INSTANCE.sendToServer(new XPObeliskUpdateRadius(pos, 0.5));

        }));

        toggleRedstone = addRenderableWidget(new Button(this.width / 2 - 25, this.height / 2 - y2, w, h, new TextComponent(status).setStyle(styleStatus), onPress -> {

            //send packet

            if(Objects.equals(status, "Ignored")){
                PacketHandler.INSTANCE.sendToServer(new XPObeliskUpdateRedstone(pos, true));
            }
            else{
                PacketHandler.INSTANCE.sendToServer(new XPObeliskUpdateRedstone(pos, false));
            }

        }));

        back = addRenderableWidget(new Button(this.width / 2 + 86, this.height / 2 - 77, 14, 20,
                new TranslatableComponent("button.experienceobelisk.experience_obelisk.settings"), (onPress) -> {

                this.onClose();

        },new Button.OnTooltip(){

            @Override
            public void onTooltip(Button pButton, PoseStack pPoseStack, int pMouseX, int pMouseY) {
                renderTooltip(pPoseStack, new TranslatableComponent("tooltip.experienceobelisk.experience_obelisk.back"), pMouseX, pMouseY);
            }
        }));


    }

}
