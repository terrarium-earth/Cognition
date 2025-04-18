package com.cyanogen.experienceobelisk.gui;

import com.cyanogen.experienceobelisk.block_entities.ExperienceObeliskEntity;
import com.cyanogen.experienceobelisk.network.experience_obelisk.UpdateRadius;
import com.cyanogen.experienceobelisk.network.shared.UpdateRedstone;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class ExperienceObeliskOptionsScreen extends Screen {

    public ExperienceObeliskMenu menu;
    public Level clientLevel;
    public final Inventory inventory;
    public final Component component;

    private final ResourceLocation texture = ResourceLocation.fromNamespaceAndPath("experienceobelisk","textures/gui/screens/experience_obelisk.png");

    protected ExperienceObeliskOptionsScreen(ExperienceObeliskMenu menu, Inventory inventory, Component component) {
        super(Component.literal("Experience Obelisk"));
        this.menu = menu;
        this.clientLevel = menu.level;
        this.inventory = inventory;
        this.component = component;
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

    @Override
    public void render(GuiGraphics gui, int mouseX, int mouseY, float partialTick) {

        renderBackground(gui, mouseX, mouseY, partialTick);

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
        gui.drawString(this.font, Component.translatable("title.experienceobelisk.experience_obelisk.radius"),
                this.width / 2 - 77,this.height / 2 - 56, 0xFFFFFF);
        gui.drawString(this.font, Component.translatable("title.experienceobelisk.experience_obelisk.redstone"),
                this.width / 2 - 77,this.height / 2 - 10, 0xFFFFFF);

        double radius = 2.5;
        boolean isRedstoneEnabled = false;

        BlockPos pos = menu.getBlockPos();
        if(pos != null && clientLevel.getBlockEntity(pos) instanceof ExperienceObeliskEntity obelisk){
            radius = obelisk.getRadius();
            isRedstoneEnabled = obelisk.isRedstoneEnabled();
        }

        //render widgets
        clearWidgets();
        buttons.get(2).setMessage(Component.literal(String.valueOf(radius)));
        if(isRedstoneEnabled){
            buttons.get(4).setMessage(Component.translatable("button.experienceobelisk.experience_obelisk.enabled"));
        }
        else{
            buttons.get(4).setMessage(Component.translatable("button.experienceobelisk.experience_obelisk.ignored"));
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

    private void updateRadius(double change){
        BlockPos pos = menu.getBlockPos();
        if(pos != null){
            PacketDistributor.sendToServer(new UpdateRadius(pos, change));
        }
    }

    private void toggleRedstone(){
        BlockPos pos = menu.getBlockPos();
        if(pos != null && clientLevel.getBlockEntity(pos) instanceof ExperienceObeliskEntity obelisk){
            PacketDistributor.sendToServer(new UpdateRedstone(menu.getBlockPos(), !obelisk.isRedstoneEnabled()));
        }
    }

    private final List<Button> buttons = new ArrayList<>();
    private void setupWidgetElements(){

        buttons.clear();

        int w = 50; //width (divisible by 2)
        int h = 20; //height
        int s = 2; //spacing
        int y1 = 43;
        int y2 = -3;

        double radius = 2.5;
        boolean isRedstoneEnabled;

        BlockPos pos = menu.getBlockPos();
        if(pos != null && clientLevel.getBlockEntity(pos) instanceof ExperienceObeliskEntity obelisk){
            radius = obelisk.getRadius();
            isRedstoneEnabled = obelisk.isRedstoneEnabled();
        } else {
            isRedstoneEnabled = false;
        }

        Style green = Style.EMPTY.withColor(0x45FF5B);
        Style red = Style.EMPTY.withColor(0xFF454B);

        MutableComponent status;
        if(isRedstoneEnabled){
            status = Component.translatable("button.experienceobelisk.experience_obelisk.enabled");
        }
        else{
            status = Component.translatable("button.experienceobelisk.experience_obelisk.ignored");
        }

        Button back = Button.builder(Component.translatable("button.experienceobelisk.experience_obelisk.back"),
                        (onPress) -> Minecraft.getInstance().setScreen(new ExperienceObeliskScreen(menu, inventory, component)))
                .size(20,20)
                .pos(this.width / 2 + 91, this.height / 2 - 78)
                .tooltip(Tooltip.create(Component.translatable("tooltip.experienceobelisk.experience_obelisk.back")))
                .build();

        Button decreaseRadius = Button.builder(Component.literal("-").setStyle(red),
                        (onPress) -> updateRadius(-0.5))
                .size(26, h)
                .pos(this.width / 2 - 56, this.height / 2 - y1)
                .build();

        Button resetRadius = Button.builder(Component.literal(String.valueOf(radius)),
                        (onPress) -> updateRadius(0))
                .size(50, h)
                .pos(this.width / 2 - 25, this.height / 2 - y1)
                .tooltip(Tooltip.create(Component.translatable("tooltip.experienceobelisk.experience_obelisk.radius")))
                .build();

        Button increaseRadius = Button.builder(Component.literal("+").setStyle(green),
                        (onPress) -> updateRadius(0.5))
                .size(26, h)
                .pos(this.width / 2 + 30, this.height / 2 - y1)
                .build();

        Button toggleRedstone = Button.builder(status,
                        (onPress) -> toggleRedstone())
                .size(w, h)
                .pos(this.width / 2 - 25, this.height / 2 - y2)
                .build();

        buttons.add(back);
        buttons.add(decreaseRadius);
        buttons.add(resetRadius);
        buttons.add(increaseRadius);
        buttons.add(toggleRedstone);
    }

}
