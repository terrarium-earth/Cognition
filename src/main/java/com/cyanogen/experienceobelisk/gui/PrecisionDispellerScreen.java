package com.cyanogen.experienceobelisk.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
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

    private class SelectablePanel{
        public Enchantment enchantment;
        public int level;
        public int x1;
        public int x2;
        public int y1;
        public int y2;
        public Status status;

        private SelectablePanel(int x1, int x2, int y1, int y2){
        }

        public void setEnchantment(Enchantment e){
            this.enchantment = e;
        }

        public void setEnchLevel(int level){
            this.level = level;
        }

        public void setPosition(int x1, int x2, int y1, int y2){
            this.x1 = x1; this.x2 = x2; this.y1 = y1; this.y2 = y2;
        }

        public boolean isWithin(double mouseX, double mouseY){
            return mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y2;
        }

        public void setStatus(Status status){
            this.status = status;
        }

        public void render(PoseStack posestack, Status status){
            switch(status){
                case UNHOVERED -> blit(posestack, x1, y1, 0, 177, 102, 17, 256, 256);
                case HOVERED -> blit(posestack, x1, y1, 0, 211, 102, 17, 256, 256);
                case SELECTED -> blit(posestack, x1, y1, 0, 194, 102, 17, 256, 256);
            }

        }
    }

    public ArrayList<SelectablePanel> selectablePanels;

    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {

        renderBackground(pPoseStack);

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, texture);

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        //render background texture
        this.blit(pPoseStack, x, y, 0, 0, 176, 166);

        ItemStack inputStack = menu.container.getItem(0);
        scrollEnabled = EnchantmentHelper.getEnchantments(inputStack).size() > 3;

        //render scroll button
        if(inputStack.isEnchanted() && scrollEnabled){
            blit(pPoseStack, x + 153, y + scrollButtonPos, 177, 0, 9, 13, 256, 256);
        }
        else{
            scrollButtonPos = 18;
            blit(pPoseStack, x + 153, y + scrollButtonPos, 187, 0, 9, 13, 256, 256);
        }

        //render selection buttons
        if(!inputStack.isEmpty() && inputStack.isEnchanted()){

            Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(inputStack);

            //rows - number of enchantments, so correspondingly, number of buttons
            //columns - x1, y1, x2 and y2 of each button


            for(int i = 0; i <= map.size() - 1; i++){
                int x1 = x + 49;
                int y1 = y + 18 + 17*i;
                int x2 = x1 + 102;
                int y2 = y1 + 17;

                if(pMouseX >= x1 && pMouseX <= x2 && pMouseY >= y1 && pMouseY <= y2){
                    //render hover texture
                    blit(pPoseStack, x1, y1, 0, 211, 102, 17, 256, 256);
                }
                else{
                    //render default texture
                    blit(pPoseStack, x1, y1, 0, 177, 102, 17, 256, 256);
                }

                selectablePanels.add(new SelectablePanel(x1,x2,y1,y2));
            }

            int index = 0;
            for(Map.Entry<Enchantment, Integer> entry : map.entrySet()){
                Component fullName = entry.getKey().getFullname(entry.getValue());

                drawString(pPoseStack, this.font, fullName.copy().withStyle(ChatFormatting.WHITE), x + 53, y + 22 + 17 * index, 0xFFFFFF);
                selectablePanels.get(index).setEnchantment(entry.getKey());
                selectablePanels.get(index).setEnchLevel(entry.getValue());
                index++;
            }
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
    int clickedDelta = 99999;

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
        clickedDelta = 99999;

        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {

        int y = (this.height - this.imageHeight) / 2;

        if(scrollClicked && clickedDelta != 99999 && scrollEnabled){
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

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        //mouse clicked on scrollbutton
        if(pMouseX >= x + 152 && pMouseX <= x + 162 && pMouseY >= y + scrollButtonPos && pMouseY <= y + scrollButtonPos + 13 && scrollEnabled){

            scrollClicked = true;
            clickedDelta = (int) pMouseY - (y + scrollButtonPos);
        }
        //mouse clicked on selection panel
        else if(pMouseX >= x + 49 && pMouseX <= x + 102 && pMouseY >= y + 18 && pMouseY <= 69){
            mouseClickedOnSelector(pMouseY, y);
        }
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    public void mouseClickedOnSelector(double mouseY, int y){

    }




}
