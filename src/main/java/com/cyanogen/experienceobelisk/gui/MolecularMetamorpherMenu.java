package com.cyanogen.experienceobelisk.gui;

import com.cyanogen.experienceobelisk.block_entities.MolecularMetamorpherEntity;
import com.cyanogen.experienceobelisk.config.Config;
import com.cyanogen.experienceobelisk.registries.RegisterItems;
import com.cyanogen.experienceobelisk.registries.RegisterMenus;
import com.cyanogen.experienceobelisk.utils.MiscUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class MolecularMetamorpherMenu extends AbstractContainerMenu {

    SimpleContainer container = new SimpleContainer(5);
    BlockPos pos;
    BlockPos posServer;
    MolecularMetamorpherEntity metamorpherClient;
    Inventory inventory;
    Component component = Component.literal("Molecular Metamorpher");

    public MolecularMetamorpherMenu(int id, Inventory inventory, FriendlyByteBuf data){
        this(id, inventory, null, null, inventory.player, new BlockPos(0,0,0));

        Level level = inventory.player.level();
        this.pos = data.readBlockPos();
        this.metamorpherClient = (MolecularMetamorpherEntity) level.getBlockEntity(pos);
        this.inventory = inventory;
    }

    //-----SLOTS-----//

    public MolecularMetamorpherMenu(int id, Inventory inventoryPlayer, IItemHandler inputs, IItemHandler output, Player player, BlockPos pos){

        super(RegisterMenus.MOLECULAR_METAMORPHER_MENU.get(), id);
        this.posServer = pos;

        if(inputs != null && output != null){
            // INPUT 1
            this.addSlot(new SlotItemHandler(inputs, 0, 19, 35));
            // INPUT 2
            this.addSlot(new SlotItemHandler(inputs, 1, 50, 52));
            // INPUT 3
            this.addSlot(new SlotItemHandler(inputs, 2, 70, 18));
            // OUTPUT 1
            this.addSlot(new SlotItemHandler(output, 0, 140, 35){
                @Override
                public boolean mayPlace(ItemStack p_40231_) {
                    return false;
                }
            });
        }
        else{
            // INPUT 1
            this.addSlot(new Slot(this.container, 0, 19, 35));
            // INPUT 2
            this.addSlot(new Slot(this.container, 1, 50, 52));
            // INPUT 3
            this.addSlot(new Slot(this.container, 2, 70, 18));
            // OUTPUT 1
            this.addSlot(new Slot(this.container, 3, 140, 35){
                @Override
                public boolean mayPlace(ItemStack p_40231_) {
                    return false;
                }
            });
        }

        addPlayerInventory(inventoryPlayer);
        addPlayerHotbar(inventoryPlayer);
    }

    public int put(ItemStack stack, int amount){

        int transferredCount = 0;

        if(stack.getCount() < amount){
            amount = stack.getCount();
        }

        for(int i = 0; i < 3; i++){

            ItemStack copy = stack.copy();
            copy.setCount(amount);
            Slot slot = getSlot(i);

            if(!slot.hasItem()){
                getSlot(i).set(copy);
                stack.shrink(amount);
                return amount;
            }
            else if(ItemStack.isSameItemSameTags(slot.getItem(), copy)){
                int grow = Math.min(amount, slot.getItem().getMaxStackSize() - slot.getItem().getCount());
                slot.getItem().grow(grow);
                stack.shrink(grow);
                return grow;
            }
        }

        return transferredCount; //the amount of items that was successfully transferred
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
    public ItemStack quickMoveStack(Player player, int index) {

        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if(slot.hasItem()) {

            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if(index <= 3){ //moving from menu to player inventory
                if(!this.moveItemStackTo(itemstack1, 4, this.slots.size(), true)){
                    return ItemStack.EMPTY;
                }
            } //moving from player inventory to menu
            else{
                if(!this.moveItemStackTo(itemstack1, 0, 3, true)){
                    return ItemStack.EMPTY;
                }
            }

            if(itemstack1.isEmpty()){
                slot.set(ItemStack.EMPTY);
            }
            else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.position().distanceTo(Vec3.atCenterOf(posServer)) <= 7;
    }
}
