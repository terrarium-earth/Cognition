package com.cyanogen.experienceobelisk.gui;

import com.cyanogen.experienceobelisk.block_entities.XPObeliskEntity;
import com.cyanogen.experienceobelisk.network.PacketHandler;
import com.cyanogen.experienceobelisk.network.experienceobelisk.UpdateToServer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import static com.cyanogen.experienceobelisk.network.experienceobelisk.UpdateToServer.Request.*;


public class ExperienceObeliskScreen extends Screen{

    public Level level;
    public Player player;
    public BlockPos pos;
    public XPObeliskEntity xpobelisk;
    private Button add1;
    private Button add10;
    private Button addAll;
    private Button drain1;
    private Button drain10;
    private Button drainAll;
    private Button settings;

    private final ResourceLocation texture = new ResourceLocation("experienceobelisk:textures/gui/container/dark_bg2.png");

    public ExperienceObeliskScreen(Level level, Player player, BlockPos pos) {
        super(new TextComponent("Experience Obelisk"));
        this.level = level;
        this.player = player;
        this.pos = pos;
        this.xpobelisk = (XPObeliskEntity) level.getBlockEntity(pos);
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

    public static int levelsToXP(int levels){
        if (levels <= 16) {
            return (int) (Math.pow(levels, 2) + 6 * levels);
        } else if (levels >= 17 && levels <= 31) {
            return (int) (2.5 * Math.pow(levels, 2) - 40.5 * levels + 360);
        } else if (levels >= 32) {
            return (int) (4.5 * Math.pow(levels, 2) - 162.5 * levels + 2220);
        }
        return 0;
    }

    public static int xpToLevels(long xp){
        if (xp < 394) {
            return (int) (Math.sqrt(xp + 9) - 3);
        } else if (xp >= 394 && xp < 1628) {
            return (int) ((Math.sqrt(40 * xp - 7839) + 81) * 0.1);
        } else if (xp >= 1628) {
            return (int) ((Math.sqrt(72 * xp - 54215) + 325) / 18); //when xp >~2980k, breaks int value limit
        }
        return 0;
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pPoseStack);

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, texture);

        int textureWidth = 256;
        int textureHeight = 256;
        int x = this.width / 2 - 176 / 2;
        int y = this.height / 2 - 166 / 2;

        //breaks around 2980000 mB for some reason

        int fluidAmount = xpobelisk.getFluidAmount();

        int n = fluidAmount - levelsToXP(xpToLevels(fluidAmount)); //remaining xp
        int m = levelsToXP(xpToLevels(fluidAmount) + 1) - levelsToXP(xpToLevels(fluidAmount)); //xp for next level
        int p = n * 138 / m;

        //render gui texture
        blit(pPoseStack, x, y, 0, 0, 176, 166, textureWidth, textureHeight);

        //render xp bar
        blit(pPoseStack, this.width / 2 - 138 / 2, this.height / 2 + 50, 0, 169, 138, 5, textureWidth, textureHeight);
        blit(pPoseStack, this.width / 2 - 138 / 2, this.height / 2 + 50, 0, 173, p, 5, textureWidth, textureHeight);

        //descriptors & info
        drawCenteredString(new PoseStack(), this.font, "Experience Obelisk",
                this.width / 2,this.height / 2 - 76, 0xFFFFFF);
        drawString(new PoseStack(), this.font, "Store",
                this.width / 2 - 77,this.height / 2 - 56, 0xFFFFFF);
        drawString(new PoseStack(), this.font, "Retrieve",
                this.width / 2 - 77,this.height / 2 - 10, 0xFFFFFF);
        drawCenteredString(new PoseStack(), this.font, fluidAmount + " mB",
                this.width / 2,this.height / 2 + 35, 0xFFFFFF);
        drawCenteredString(new PoseStack(), this.font, String.valueOf(xpToLevels(fluidAmount)),
                this.width / 2,this.height / 2 + 60, 0x4DFF12);

        //widgets
        setupWidgetElements();

        //render widgets
        for(Widget widget : this.renderables) {
            widget.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        }

    }


    //buttons and whatnot go here
    private void setupWidgetElements() {

        clearWidgets();

        Style style = Style.EMPTY;
        Style green = style.withColor(0x45FF5B);
        Style red = style.withColor(0xFF454B);
        int w = 50; //width (divisible by 2)
        int h = 20; //height
        int s = 2; //spacing
        int y1 = 43;
        int y2 = -3;

        settings = addRenderableWidget(new Button(this.width / 2 + 86, this.height / 2 - 77, 14, 20,
                new TranslatableComponent("button.experienceobelisk.experience_obelisk.settings"), (onPress) -> {

            Minecraft.getInstance().pushGuiLayer(new ExperienceObeliskOptionsScreen(level, player, pos, this));
        },new Button.OnTooltip(){

            @Override
            public void onTooltip(Button pButton, PoseStack pPoseStack, int pMouseX, int pMouseY) {
                renderTooltip(pPoseStack, new TranslatableComponent("tooltip.experienceobelisk.experience_obelisk.settings"), pMouseX, pMouseY);
            }
        }));


        //deposit

        add1 = addRenderableWidget(new Button((int) (this.width / 2 - 1.5*w - s), this.height / 2 - y1, w, h, new TextComponent("+1")
                .setStyle(green), (onPress) -> {

            PacketHandler.INSTANCE.sendToServer(new UpdateToServer(pos, 1, FILL));

        },
                new Button.OnTooltip() {
                    @Override
                    public void onTooltip(Button pButton, PoseStack pPoseStack, int pMouseX, int pMouseY) {
                        renderTooltip(pPoseStack, new TranslatableComponent("tooltip.experienceobelisk.experience_obelisk.add1"), pMouseX, pMouseY);
                    }
                }
        ));


        add10 = addRenderableWidget(new Button(this.width / 2 - w/2, this.height / 2 - y1, w, h, new TextComponent("+10")
                .setStyle(green), (onPress) -> {

            PacketHandler.INSTANCE.sendToServer(new UpdateToServer(pos, 10, FILL));

        },
                new Button.OnTooltip() {
                    @Override
                    public void onTooltip(Button pButton, PoseStack pPoseStack, int pMouseX, int pMouseY) {
                        renderTooltip(pPoseStack, new TranslatableComponent("tooltip.experienceobelisk.experience_obelisk.add10"), pMouseX, pMouseY);
                    }
                }
        ));

        addAll = addRenderableWidget(new Button((int) (this.width / 2 + 0.5*w + s), this.height / 2 - y1, w, h, new TextComponent("+All")
                .setStyle(green), (onPress) -> {

            PacketHandler.INSTANCE.sendToServer(new UpdateToServer(pos, 0, FILL_ALL));

        },
                new Button.OnTooltip() {
                    @Override
                    public void onTooltip(Button pButton, PoseStack pPoseStack, int pMouseX, int pMouseY) {
                        renderTooltip(pPoseStack, new TranslatableComponent("tooltip.experienceobelisk.experience_obelisk.addAll"), pMouseX, pMouseY);
                    }
                }
        ));


        //withdraw
        drain1 = addRenderableWidget(new Button((int) (this.width / 2 - 1.5*w - s), this.height / 2 - y2, w, h, new TextComponent("-1")
                .setStyle(red), (onPress) -> {

            PacketHandler.INSTANCE.sendToServer(new UpdateToServer(pos, 1, DRAIN));

        },
                new Button.OnTooltip() {
                    @Override
                    public void onTooltip(Button pButton, PoseStack pPoseStack, int pMouseX, int pMouseY) {
                        renderTooltip(pPoseStack, new TranslatableComponent("tooltip.experienceobelisk.experience_obelisk.drain1"), pMouseX, pMouseY);
                    }
                }
        ));

        drain10 = addRenderableWidget(new Button(this.width / 2 - w/2, this.height / 2 - y2, w, h, new TextComponent("-10")
                .setStyle(red), (onPress) -> {

            PacketHandler.INSTANCE.sendToServer(new UpdateToServer(pos, 10, DRAIN));

        },
                new Button.OnTooltip() {
                    @Override
                    public void onTooltip(Button pButton, PoseStack pPoseStack, int pMouseX, int pMouseY) {
                        renderTooltip(pPoseStack, new TranslatableComponent("tooltip.experienceobelisk.experience_obelisk.drain10"), pMouseX, pMouseY);
                    }
                }
        ));

        drainAll = addRenderableWidget(new Button((int) (this.width / 2 + 0.5*w + s), this.height / 2 - y2, w, h, new TextComponent("-All")
                .setStyle(red), (onPress) -> {

            PacketHandler.INSTANCE.sendToServer(new UpdateToServer(pos, 0, DRAIN_ALL));

        },
                new Button.OnTooltip() {
                    @Override
                    public void onTooltip(Button pButton, PoseStack pPoseStack, int pMouseX, int pMouseY) {
                        renderTooltip(pPoseStack, new TranslatableComponent("tooltip.experienceobelisk.experience_obelisk.drainAll"), pMouseX, pMouseY);
                    }
                }
        ));


    }

}
