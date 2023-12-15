package com.cyanogen.experienceobelisk.gui;

import com.cyanogen.experienceobelisk.block_entities.ExperienceObeliskEntity;
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
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.cyanogen.experienceobelisk.gui.ExperienceObeliskScreen.levelsToXP;
import static com.cyanogen.experienceobelisk.gui.ExperienceObeliskScreen.xpToLevels;

public class PrecisionDispellerScreen extends AbstractContainerScreen<PrecisionDispellerMenu> {

    private final ResourceLocation texture = new ResourceLocation("experienceobelisk:textures/gui/screens/precision_dispeller.png");
    private final TranslatableComponent title = new TranslatableComponent("title.experienceobelisk.precision_dispeller");
    private final TranslatableComponent inventoryTitle = new TranslatableComponent("title.experienceobelisk.precision_dispeller.inventory");

    public ExperienceObeliskEntity xpobelisk;

    public PrecisionDispellerScreen(PrecisionDispellerMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
        this.xpobelisk = menu.obeliskClient;
    }

    //-----SELECTABLE PANEL-----//

    enum Status{
        UNHOVERED,
        HOVERED,
        SELECTED
    }

    private static class SelectablePanel{
        public Enchantment enchantment;
        public int level;
        public int x1;
        public int x2;
        public int y1;
        public int y2;
        public Status status;
        boolean isVisible;

        private SelectablePanel(int x1, int y1, Enchantment e, int level, Status s, boolean isVisible){
            this.enchantment = e;
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x1 + 102;
            this.y2 = y1 + 17;
            this.level = level;
            this.status = s;
            this.isVisible = isVisible;
        }

        public boolean isHovered(double mouseX, double mouseY){
            return mouseX > x1 && mouseX < x2 && mouseY > y1 && mouseY < y2;
        }

        public void renderPanel(PoseStack posestack){
            switch(status){
                case UNHOVERED -> blit(posestack, x1, y1, 0, 177, 102, 17, 256, 256);
                case HOVERED -> blit(posestack, x1, y1, 0, 211, 102, 17, 256, 256);
                case SELECTED -> blit(posestack, x1, y1, 0, 194, 102, 17, 256, 256);
            }
        }

        public String getFullName(){
            Component fullName = enchantment.getFullname(level);
            return fullName.copy().getString();
        }

        public void renderText(PoseStack posestack, Font font){
            String text = getFullName();

            if(font.width(text) > 90){
                while(font.width(text) > 90){
                    text = text.substring(0, text.length() - 1);
                }
                text = text + "...";
            }

            if(enchantment.isCurse()){
                font.draw(posestack, text, x1 + 4, y1 + 4, 0xFC5454);
            }
            else{
                font.draw(posestack, text, x1 + 4, y1 + 4, 0xFFFFFF);
            }

        }
    }

    public ArrayList<SelectablePanel> selectablePanels = new ArrayList<>();
    public int selectedIndex = -1;

    //-----RENDERING-----//

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        this.font.draw(poseStack, this.title, (float)this.titleLabelX, (float)this.titleLabelY, 0xFFFFFF);
        this.font.draw(poseStack, this.inventoryTitle, (float)this.inventoryLabelX, (float)this.inventoryLabelY, 0xFFFFFF);
    }

    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {

        selectablePanels.clear();
        renderBackground(poseStack);

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, texture);

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        //render background texture
        this.blit(poseStack, x, y, 0, 0, 176, 166);

        ItemStack inputStack = menu.container.getItem(0);
        Map<Enchantment,Integer> enchantmentMap = EnchantmentHelper.getEnchantments(inputStack);
        scrollEnabled = enchantmentMap.size() > 3;

        //render scroll button
        if((inputStack.isEnchanted() || inputStack.is(Items.ENCHANTED_BOOK)) && scrollEnabled){
            blit(poseStack, x + 153, y + scrollButtonPos, 177, 0, 9, 13, 256, 256);
        }
        else{
            scrollButtonPos = 18;
            blit(poseStack, x + 153, y + 18, 187, 0, 9, 13, 256, 256);
        }

        if(inputStack.isEnchanted() || inputStack.is(Items.ENCHANTED_BOOK)){
            int index = 0;

            //populating selectablePanels
            for(Map.Entry<Enchantment, Integer> entry : enchantmentMap.entrySet()){

                int n = enchantmentMap.size() - 3;
                int b = scrollButtonPos - 18;

                if(n > 0){
                    offset = -b * 17 * n / 38;
                }

                int xpos = x + 49;
                int ypos = y + 18 + 17 * index + offset;
                boolean visibility = ypos > y + 1 && ypos < y + 69;

                selectablePanels.add(new SelectablePanel(xpos, ypos, entry.getKey(), entry.getValue(), Status.UNHOVERED, visibility));
                index++;
            }

            //rendering selectablePanels
            for(SelectablePanel panel : selectablePanels){

                if(selectablePanels.indexOf(panel) == selectedIndex){
                    panel.status = Status.SELECTED;
                }
                else if(panel.isHovered(mouseX, mouseY)){
                    panel.status = Status.HOVERED;
                }

                if(panel.isVisible){
                    panel.renderPanel(poseStack);
                }
            }

            //rendering labels
            for(SelectablePanel panel : selectablePanels){
                if(panel.isVisible){
                    panel.renderText(poseStack, font);
                }
            }

            //covering up
            RenderSystem.setShaderTexture(0, texture);
            blit(poseStack, x + 49, y + 1, 49, 1, 102, 17, 256, 256);
            blit(poseStack, x + 49, y + 69, 49, 69, 102, 17, 256, 256);
        }
        else{
            selectedIndex = -1;
            offset = 0;
        }

        super.render(poseStack, mouseX, mouseY, partialTick);
        this.renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(PoseStack poseStack, int x, int y) {

        long playerXP = levelsToXP(menu.player.experienceLevel) + Math.round(menu.player.experienceProgress * menu.player.getXpNeededForNextLevel());

        for(SelectablePanel panel : selectablePanels){
            if(panel.isHovered(x, y) && panel.isVisible && !panel.status.equals(Status.SELECTED)){

                List<Component> tooltipList = new ArrayList<>();
                tooltipList.add(new TextComponent(panel.getFullName()));

                if(panel.enchantment.isCurse()){

                    tooltipList.add(new TranslatableComponent("tooltip.experienceobelisk.precision_dispeller.curse"));

                    if(xpobelisk == null && playerXP < 1395){
                        tooltipList.add(new TranslatableComponent("tooltip.experienceobelisk.precision_dispeller.insufficient_xp"));
                    }
                    else if(xpobelisk != null && xpobelisk.getFluidAmount() / 20 + playerXP < 1395){
                        tooltipList.add(new TranslatableComponent("tooltip.experienceobelisk.precision_dispeller.insufficient_xp"));
                    }
                }
                else{
                    int points = panel.enchantment.getMinCost(panel.level);
                    int levels = xpToLevels(points);

                    MutableComponent pts = new TextComponent(String.valueOf(points)).withStyle(ChatFormatting.GREEN);
                    MutableComponent lvls = new TextComponent(String.valueOf(levels)).withStyle(ChatFormatting.GREEN);
                    tooltipList.add(new TranslatableComponent("tooltip.experienceobelisk.precision_dispeller.enchantment", pts, lvls));
                }
                this.renderTooltip(poseStack, tooltipList, Optional.empty(), x, y);
            }
        }

        super.renderTooltip(poseStack, x, y);
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {

    }

    int scrollButtonPos = 18;
    int offset = 0;
    boolean scrollClicked = false;
    boolean scrollEnabled = false;
    int clickedDelta = -1;

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        if(mouseX >= x + 48 && mouseX <= x + 162 && mouseY >= y + 17 && mouseY <= y + 69 && scrollEnabled){
            scrollButtonPos = scrollButtonPos - 4 * (int) delta;
        }

        if(scrollButtonPos > 56){
            scrollButtonPos = 56;
        }
        else if(scrollButtonPos < 18){
            scrollButtonPos = 18;
        }

        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {

        //mouse released anywhere on screen
        scrollClicked = false;
        clickedDelta = -1;

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {

        int y = (this.height - this.imageHeight) / 2;

        if(scrollClicked && clickedDelta != -1 && scrollEnabled){
            scrollButtonPos = (int) mouseY - y - clickedDelta;
        }

        if(scrollButtonPos > 56){
            scrollButtonPos = 56;
        }
        else if(scrollButtonPos < 18){
            scrollButtonPos = 18;
        }

        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        super.mouseClicked(mouseX, mouseY, button);

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        if(mouseX >= x + 152 && mouseX <= x + 162 && mouseY >= y + scrollButtonPos && mouseY <= y + scrollButtonPos + 13 && scrollEnabled){

            scrollClicked = true;
            clickedDelta = (int) mouseY - (y + scrollButtonPos);
        }
        else{
           mouseClickedOnPanel(mouseX, mouseY);
        }
        return true;
    }


    //----HANDLE SELECTION-----//

    public void mouseClickedOnPanel(double mouseX, double mouseY){
        
        long playerXP = levelsToXP(menu.player.experienceLevel) + Math.round(menu.player.experienceProgress * menu.player.getXpNeededForNextLevel());

        for(SelectablePanel panel : selectablePanels){

            boolean invalid;

            if(menu.player.isCreative() || !panel.enchantment.isCurse()){
                invalid = false;
            }
            else if(xpobelisk == null){
                invalid = playerXP < 1395;
            }
            else{
                invalid = playerXP + xpobelisk.getFluidAmount() / 20 < 1395;
            }

            if(panel.isHovered(mouseX, mouseY) && panel.isVisible && !invalid){

                if(selectedIndex == selectablePanels.indexOf(panel)){
                    selectedIndex = -1;
                    PacketHandler.INSTANCE.sendToServer(new UpdateSlots(this.menu.containerId, 1, ItemStack.EMPTY));
                }
                else{
                    selectedIndex = selectablePanels.indexOf(panel);

                    ItemStack inputItem = menu.container.getItem(0);
                    Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(inputItem);
                    map.remove(panel.enchantment);
                    ItemStack outputItem;

                    if(inputItem.is(Items.ENCHANTED_BOOK)){
                        if(map.isEmpty()){
                            outputItem = new ItemStack(Items.BOOK, 1);
                        }
                        else{
                            outputItem = new ItemStack(Items.ENCHANTED_BOOK,1);
                            for(Map.Entry<Enchantment,Integer> entry : map.entrySet()){
                                EnchantedBookItem.addEnchantment(outputItem, new EnchantmentInstance(entry.getKey(), entry.getValue()));
                            }
                        }
                    }
                    else{
                        outputItem = inputItem.copy();
                        EnchantmentHelper.setEnchantments(map, outputItem);

                        int repairCost = outputItem.getBaseRepairCost();
                        repairCost = (repairCost - 1) / 2;

                        if(repairCost < 1 || !outputItem.isEnchanted()){
                            repairCost = 0;
                        }

                        outputItem.setRepairCost(repairCost);
                    }

                    PacketHandler.INSTANCE.sendToServer(new UpdateSlots(this.menu.containerId, 1, outputItem));
                }

                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            }
        }
    }

}
