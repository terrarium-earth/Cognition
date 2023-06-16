package com.cyanogen.experienceobelisk.gui;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

import java.util.Map;

public class PrecisionDispellerMenu extends AbstractContainerMenu {

    SimpleContainer container = new SimpleContainer(2);
    Player player;

    public PrecisionDispellerMenu(int id, Inventory inventory, FriendlyByteBuf data) {
        this(id, inventory, inventory.player);
    }

    public PrecisionDispellerMenu(int id, Inventory inventory, Player player) {

        super(ModMenusInit.PRECISION_DISPELLER_MENU.get(), id);

        this.player = player;

        this.addSlot(new Slot(this.container, 0, 17, 18){
            @Override
            public void onTake(Player pPlayer, ItemStack pStack) {
                container.setItem(1, ItemStack.EMPTY);
                super.onTake(pPlayer, pStack);
            }
        });
        this.addSlot(new Slot(this.container, 1, 17, 52){
            @Override
            public boolean mayPlace(ItemStack pStack) {
                return false;
            }

            @Override
            public void onTake(Player pPlayer, ItemStack pStack) {
                handleExperience(container.getItem(0), pStack, player.level);
                container.setItem(0, ItemStack.EMPTY);

                pPlayer.playSound(SoundEvents.GRINDSTONE_USE, 1, 1);
                super.onTake(pPlayer, pStack);
            }
        });

        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);

    }

    public void handleExperience(ItemStack inputItem, ItemStack outputItem, Level level){
        if(!level.isClientSide){
            ServerLevel server = (ServerLevel) level;
            Enchantment removed = null;
            int enchLevel = 0;

            Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(inputItem);
            for(Map.Entry<Enchantment, Integer> entry : map.entrySet()){
                if(EnchantmentHelper.getItemEnchantmentLevel(entry.getKey(), outputItem) == 0){
                    removed = entry.getKey();
                    enchLevel = entry.getValue();
                    break;
                }
            }

            if(removed != null){
                if(removed.isCurse()){
                    player.giveExperiencePoints(-100); //adjust value later
                }
                else{
                    int points = rarityToInt(removed.getRarity()) * enchLevel * 10; //adjust value later
                    ExperienceOrb orb = new ExperienceOrb(server, player.getX(), player.getY(), player.getZ(), points);
                    server.addFreshEntity(orb);

                }
            }


        }
    }

    public int rarityToInt(Enchantment.Rarity rarity){

        int output = 1;
        switch(rarity){
            case UNCOMMON -> output = 2;
            case RARE -> output = 3;
            case VERY_RARE -> output = 4;
        }
        return output;
    }

    @Override
    public void addSlotListener(ContainerListener pListener) {
        super.addSlotListener(pListener);
    }

    @Override
    public void removed(Player player) {

        ItemStack item = container.getItem(0);

        if(!player.addItem(item)){
            player.drop(item, false);
        }
        super.removed(player);
    }


    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }


}
