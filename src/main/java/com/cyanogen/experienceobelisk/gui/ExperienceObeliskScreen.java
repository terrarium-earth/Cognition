package com.cyanogen.experienceobelisk.gui;

import com.cyanogen.experienceobelisk.block_entities.ExperienceObeliskEntity;
import com.cyanogen.experienceobelisk.network.experience_obelisk.UpdateContents;
import com.cyanogen.experienceobelisk.utils.ExperienceUtils;
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
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.cyanogen.experienceobelisk.network.experience_obelisk.UpdateContents.*;

public class ExperienceObeliskScreen extends AbstractContainerScreen<ExperienceObeliskMenu> {

    private final ResourceLocation texture = ResourceLocation.parse("experienceobelisk:textures/gui/screens/experience_obelisk.png");
    private final Level clientLevel;
    public final Inventory inventory;
    public final Component component;

    public ExperienceObeliskScreen(ExperienceObeliskMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
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

        int x = this.width / 2 - 176 / 2;
        int y = this.height / 2 - 166 / 2;

        int experiencePoints = 0, levels = 0, fluidAmount = 0, progress = 0;

        BlockPos pos = menu.getBlockPos();
        if(pos != null && clientLevel.getBlockEntity(pos) instanceof ExperienceObeliskEntity obelisk){
            experiencePoints = obelisk.getExperiencePoints();
            levels = obelisk.getLevels();
            fluidAmount = obelisk.getFluidAmount();
            progress = (int) (ExperienceUtils.getProgressToNextLevel(experiencePoints, levels) * 138);
        }

        //render gui texture
        gui.blit(texture, x, y, 0, 0, 176, 166);

        //render xp bar
        gui.blit(texture, this.width / 2 - 138 / 2, this.height / 2 + 50, 0, 169, 138, 5);
        gui.blit(texture, this.width / 2 - 138 / 2, this.height / 2 + 50, 0, 173, progress, 5);

        //descriptors & info
        gui.drawCenteredString(this.font, Component.translatable("title.experienceobelisk.experience_obelisk"),
                this.width / 2,this.height / 2 - 76, 0xFFFFFF);
        gui.drawString(this.font, Component.translatable("title.experienceobelisk.experience_obelisk.store"),
                this.width / 2 - 77,this.height / 2 - 56, 0xFFFFFF);
        gui.drawString(this.font, Component.translatable("title.experienceobelisk.experience_obelisk.retrieve"),
                this.width / 2 - 77,this.height / 2 - 10, 0xFFFFFF);
        gui.drawCenteredString(this.font, fluidAmount + " mB",
                this.width / 2,this.height / 2 + 35, 0xFFFFFF);
        gui.drawCenteredString(this.font, String.valueOf(levels),
                this.width / 2,this.height / 2 + 60, 0x4DFF12);

        clearWidgets();
        loadWidgetElements();

        //render widgets
        for(Renderable widget : this.renderables){
            widget.render(gui, mouseX, mouseY, partialTick);
        }

        //render XP tooltip
        int infoX = this.width / 2;
        int infoY = this.height / 2 + 55;

        int hoverAreaX = 80;
        int hoverAreaY = 14;

        List<Component> tooltipList = new ArrayList<>();
        Component content = Component.translatable("tooltip.experienceobelisk.experience_obelisk.xp",
                Component.literal(String.valueOf(experiencePoints)).withStyle(ChatFormatting.GREEN));
        tooltipList.add(content);

        if(mouseX >= infoX - hoverAreaX/2 && mouseX <= infoX + hoverAreaX/2 && mouseY >= infoY - hoverAreaY/2 && mouseY <= infoY + hoverAreaY/2){
            gui.renderTooltip(this.font, tooltipList, Optional.empty(), mouseX, mouseY);
        }


    }

    @Override
    protected void renderBg(GuiGraphics gui, float f, int a, int b) {

    }

    public static void updateContents(ExperienceObeliskMenu menu, int levels, String request){
        BlockPos pos = menu.getBlockPos();
        if(pos != null){
            PacketDistributor.sendToServer(new UpdateContents(pos, levels, request));
        }
    }

    private void loadWidgetElements(){
        if(!this.buttons.isEmpty()){
            for(Button button : this.buttons){
                button.setFocused(false);
                addRenderableWidget(button);
            }
        }
    }

    private final List<Button> buttons = new ArrayList<>();
    private void setupWidgetElements() {

        buttons.clear();

        int buttonWidth = 50; //width (divisible by 2)
        int buttonHeight = 20; //height
        int spacing = 2; //spacing
        int y1 = 43;
        int y2 = -3;

        Button settings = menuSwitchingButton(this.width / 2 + 91, this.height / 2 - 78, 20, 20);

        Button deposit1 = xpHandlingButton((int) (this.width / 2f - 1.5*buttonWidth - spacing), this.height / 2 - y1,
                buttonWidth, buttonHeight, FILL, 1);

        Button deposit10 = xpHandlingButton(this.width / 2 - buttonWidth/2, this.height / 2 - y1,
                buttonWidth, buttonHeight, FILL, 10);

        Button depositAll = xpHandlingButton((int) (this.width / 2f + 0.5*buttonWidth + spacing), this.height / 2 - y1,
                buttonWidth, buttonHeight, FILL_ALL, 0);

        Button withdraw1 = xpHandlingButton((int) (this.width / 2f - 1.5*buttonWidth - spacing), this.height / 2 - y2,
                buttonWidth, buttonHeight, DRAIN, 1);

        Button withdraw10 = xpHandlingButton(this.width / 2 - buttonWidth/2, this.height / 2 - y2,
                buttonWidth, buttonHeight, DRAIN, 10);

        Button withdrawAll = xpHandlingButton((int) (this.width / 2f + 0.5*buttonWidth + spacing), this.height / 2 - y2,
                buttonWidth, buttonHeight, DRAIN_ALL, 0);

        buttons.add(settings);
        buttons.add(deposit1);
        buttons.add(deposit10);
        buttons.add(depositAll);
        buttons.add(withdraw1);
        buttons.add(withdraw10);
        buttons.add(withdrawAll);
    }

    //-----BUTTONS-----//
    public Button xpHandlingButton(int x, int y, int width, int height, String request, int levels){
        Component message = getMessage(request, levels);
        Tooltip tooltip = getTooltip(request, levels);
        return Button.builder(message, (onPress) -> updateContents(menu, levels, request)).bounds(x, y, width, height).tooltip(tooltip).build();
    }

    public Component getMessage(String request, int levels) {
        return switch (request) {
            case FILL -> Component.literal("+" + levels).withStyle(ChatFormatting.GREEN);
            case FILL_ALL -> Component.literal("+All").withStyle(ChatFormatting.GREEN);
            case DRAIN -> Component.literal("-" + levels).withStyle(ChatFormatting.RED);
            case DRAIN_ALL -> Component.literal("-All").withStyle(ChatFormatting.RED);
            default -> Component.empty();
        };
    }

    public @Nullable Tooltip getTooltip(String request, int levels) {
        return switch (request) {
            case FILL -> Tooltip.create(Component.translatable("tooltip.experienceobelisk.experience_obelisk.add" + levels));
            case FILL_ALL -> Tooltip.create(Component.translatable("tooltip.experienceobelisk.experience_obelisk.addAll"));
            case DRAIN -> Tooltip.create(Component.translatable("tooltip.experienceobelisk.experience_obelisk.drain" + levels));
            case DRAIN_ALL -> Tooltip.create(Component.translatable("tooltip.experienceobelisk.experience_obelisk.drainAll"));
            default -> null;
        };
    }

    public Button menuSwitchingButton(int x, int y, int width, int height){
        Component message = Component.translatable("button.experienceobelisk.experience_obelisk.settings");
        Tooltip tooltip = Tooltip.create(Component.translatable("tooltip.experienceobelisk.experience_obelisk.settings"));

        return Button.builder(message,
                (onPress) -> Minecraft.getInstance().setScreen(new ExperienceObeliskOptionsScreen(menu, inventory, component)))
                .bounds(x, y, width, height).tooltip(tooltip).build();
    }

}
