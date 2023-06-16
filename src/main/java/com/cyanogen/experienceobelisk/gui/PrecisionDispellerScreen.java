package com.cyanogen.experienceobelisk.gui;

import com.cyanogen.experienceobelisk.network.PacketHandler;
import com.cyanogen.experienceobelisk.network.precision_dispeller.UpdateSlots;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.ArrayList;
import java.util.Map;

public class PrecisionDispellerScreen extends AbstractContainerScreen<PrecisionDispellerMenu> {

    private final ResourceLocation texture = new ResourceLocation("experienceobelisk:textures/gui/screens/precision_dispeller.png");
    private final TranslatableComponent title = new TranslatableComponent("title.experienceobelisk.precision_dispeller");
    private final TranslatableComponent inventoryTitle = new TranslatableComponent("title.experienceobelisk.precision_dispeller.inventory");

    public PrecisionDispellerScreen(PrecisionDispellerMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    public Component getTitle() {
        return super.getTitle();
    }

    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        this.font.draw(pPoseStack, this.title, (float)this.titleLabelX, (float)this.titleLabelY, 0xFFFFFF);
        this.font.draw(pPoseStack, this.inventoryTitle, (float)this.inventoryLabelX, (float)this.inventoryLabelY, 0xFFFFFF);
    }

    enum Status{
        UNHOVERED,
        HOVERED,
        SELECTED
    }

    private static class SelectablePanel{
        public Enchantment enchantment;
        public MutableComponent displayComponent;
        public int x1;
        public int x2;
        public int y1;
        public int y2;
        public Status status;

        private SelectablePanel(int x1, int y1, Enchantment e, MutableComponent c, Status s){
            this.enchantment = e;
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x1 + 102;
            this.y2 = y1 + 17;
            this.displayComponent = c;
            this.status = s;
        }

        public void setPosition(int x1, int x2, int y1, int y2){
            this.x1 = x1; this.x2 = x2; this.y1 = y1; this.y2 = y2;
        }

        public boolean isHovered(double mouseX, double mouseY){
            return mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y2;
        }

        public void setStatus(Status status){
            this.status = status;
        }

        public void renderPanel(PoseStack posestack){
            switch(status){
                case UNHOVERED -> blit(posestack, x1, y1, 0, 177, 102, 17, 256, 256);
                case HOVERED -> blit(posestack, x1, y1, 0, 211, 102, 17, 256, 256);
                case SELECTED -> blit(posestack, x1, y1, 0, 194, 102, 17, 256, 256);
            }
        }

        public void renderText(PoseStack posestack, Font font){
            drawString(posestack, font, displayComponent, x1 + 4, y1 + 4, 0xFFFFFF);
        }
    }

    public ArrayList<SelectablePanel> selectablePanels = new ArrayList<>();
    public int selectedIndex = -1;

    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {

        selectablePanels.clear();
        renderBackground(pPoseStack);

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, texture);

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        //render background texture
        this.blit(pPoseStack, x, y, 0, 0, 176, 166);

        ItemStack inputStack = menu.container.getItem(0);
        Map<Enchantment,Integer> enchantmentMap = EnchantmentHelper.getEnchantments(inputStack);
        scrollEnabled = enchantmentMap.size() > 3;

        //render scroll button
        if(inputStack.isEnchanted() && scrollEnabled){
            blit(pPoseStack, x + 153, y + scrollButtonPos, 177, 0, 9, 13, 256, 256);
        }
        else{
            scrollButtonPos = 18;
            blit(pPoseStack, x + 153, y + scrollButtonPos, 187, 0, 9, 13, 256, 256);
        }

        if(inputStack.isEnchanted()){
            int index = 0;

            //populating selectablePanels
            for(Map.Entry<Enchantment, Integer> entry : enchantmentMap.entrySet()){

                Component fullName = entry.getKey().getFullname(entry.getValue());
                MutableComponent displayName;

                if(entry.getKey().isCurse()){
                    displayName = fullName.copy().withStyle(ChatFormatting.RED);
                }
                else{
                    displayName = fullName.copy().withStyle(ChatFormatting.WHITE);
                }

                selectablePanels.add(new SelectablePanel(x + 49, y + 18 + 17 * index, entry.getKey(), displayName, Status.UNHOVERED));

                index++;
            }

            //rendering selectablePanels
            for(SelectablePanel panel : selectablePanels){

                if(selectablePanels.indexOf(panel) == selectedIndex){
                    panel.setStatus(Status.SELECTED);
                }
                else if(panel.isHovered(pMouseX, pMouseY)){
                    panel.setStatus(Status.HOVERED);
                }
                panel.renderPanel(pPoseStack);
                //why does putting blit and drawstring in the same for loop make all this zalgo text
            }

            for(SelectablePanel panel : selectablePanels){
                panel.renderText(pPoseStack, font);
                //i hate this
            }
        }
        else{
            selectedIndex = -1;
        }

        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pPoseStack, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {

    }

    int scrollButtonPos = 18;
    boolean scrollClicked = false;
    boolean scrollEnabled = false;
    int clickedDelta = -1;

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        if(pMouseX >= x + 48 && pMouseX <= x + 162 && pMouseY >= y + 17 && pMouseY <= y + 69 && scrollEnabled){
            scrollButtonPos = scrollButtonPos - 3 * (int) pDelta;

            if(scrollButtonPos > 56){
                scrollButtonPos = 56;
            }
            else if(scrollButtonPos < 18){
                scrollButtonPos = 18;
            }
        }
        return super.mouseScrolled(pMouseX, pMouseY, pDelta);
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {

        //mouse released anywhere on screen
        scrollClicked = false;
        clickedDelta = -1;

        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {

        int y = (this.height - this.imageHeight) / 2;

        if(scrollClicked && clickedDelta != -1 && scrollEnabled){
            scrollButtonPos = (int) pMouseY - y - clickedDelta;
        }

        if(scrollButtonPos > 56){
            scrollButtonPos = 56;
        }
        else if(scrollButtonPos < 18){
            scrollButtonPos = 18;
        }

        return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {

        super.mouseClicked(pMouseX, pMouseY, pButton);

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        if(pMouseX >= x + 152 && pMouseX <= x + 162 && pMouseY >= y + scrollButtonPos && pMouseY <= y + scrollButtonPos + 13 && scrollEnabled){

            scrollClicked = true;
            clickedDelta = (int) pMouseY - (y + scrollButtonPos);
        }
        else{
            for(SelectablePanel panel : selectablePanels){
                if(panel.isHovered(pMouseX, pMouseY)){
                    selectedIndex = selectablePanels.indexOf(panel);

                    ItemStack inputItem = menu.container.getItem(0);
                    Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(inputItem);
                    map.remove(panel.enchantment);
                    ItemStack outputItem = inputItem.copy();
                    EnchantmentHelper.setEnchantments(map, outputItem);

                    PacketHandler.INSTANCE.sendToServer(new UpdateSlots(this.menu.containerId, 1, outputItem));
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                }
            }
        }
        return true;
    }

}
