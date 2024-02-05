package com.cyanogen.experienceobelisk.gui;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;

public class EternalAnvilMenu extends AnvilMenu {

    public EternalAnvilMenu(int id, Inventory inventory, FriendlyByteBuf data) {
        this(id, inventory, inventory.player);
    }

    public EternalAnvilMenu(int id, Inventory inventory, Player player){
        super(id, inventory);
    }
}
