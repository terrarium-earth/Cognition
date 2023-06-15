package com.cyanogen.experienceobelisk.gui;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PrecisionDispellerMenu extends AbstractContainerMenu {

    SimpleContainer container = new SimpleContainer(2);

    public PrecisionDispellerMenu(int id, Inventory inventory, FriendlyByteBuf data) {
        this(id, inventory, inventory.player);
    }

    public PrecisionDispellerMenu(int id, Inventory inventory, Player player) {
        super(ModMenusInit.PRECISION_DISPELLER_MENU.get(), id);

        this.addSlot(new Slot(this.container, 0, 17, 18));
        this.addSlot(new Slot(this.container, 1, 17, 52){
            @Override
            public boolean mayPlace(@NotNull ItemStack p_40231_) {
                return false;
            }

            @Override
            public void onTake(Player pPlayer, ItemStack pStack) {
                this.container.setItem(0, ItemStack.EMPTY);
                setChanged();
                super.onTake(pPlayer, pStack);
            }
        });

        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);
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
