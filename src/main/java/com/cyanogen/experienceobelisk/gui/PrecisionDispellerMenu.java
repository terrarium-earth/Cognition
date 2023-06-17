package com.cyanogen.experienceobelisk.gui;

import net.minecraft.core.BlockPos;
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
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class PrecisionDispellerMenu extends AbstractContainerMenu {

    SimpleContainer container = new SimpleContainer(2);
    Player player;
    BlockPos pos;

    public PrecisionDispellerMenu(int id, Inventory inventory, FriendlyByteBuf data) {
        this(id, inventory, inventory.player, new BlockPos(0,0,0));
    }

    //-----SLOTS-----//

    public PrecisionDispellerMenu(int id, Inventory inventory, Player player, BlockPos pos) {

        super(ModMenusInit.PRECISION_DISPELLER_MENU.get(), id);

        this.player = player;
        this.pos = pos;

        this.addSlot(new Slot(this.container, 0, 17, 18){

            @Override
            public void onTake(Player pPlayer, ItemStack pStack) {
                container.setItem(1, ItemStack.EMPTY);
                super.onTake(pPlayer, pStack);
            }

            @Override
            public int getMaxStackSize() {
                return 1;
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

            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });

        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);
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

    //-----BEHAVIOR-----//

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
                    ExperienceOrb orb = new ExperienceOrb(server, pos.getX(), pos.getY() + 0.5, pos.getZ(), points);
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
    public void removed(Player player) {

        ItemStack item = container.getItem(0);
        if(!player.addItem(item)){
            player.drop(item, false);
        }
        super.removed(player);
    }

    @Override
    public boolean stillValid(Player player) {
        return player.position().distanceTo(Vec3.atCenterOf(this.pos)) < 7;
    }


    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);

        if(slot.hasItem()) {
            if(pIndex == 0){
                container.setItem(1, ItemStack.EMPTY);
            }
            else if(pIndex == 1){
                container.setItem(0, ItemStack.EMPTY);
            }

            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (pIndex < 2) {
                if (!this.moveItemStackTo(itemstack1, 2, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, 2, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemstack;
    }

}
