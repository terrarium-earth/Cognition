package com.cyanogen.experienceobelisk.gui;

import com.cyanogen.experienceobelisk.block_entities.ExperienceObeliskEntity;
import com.cyanogen.experienceobelisk.registries.RegisterMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ExperienceObeliskMenu extends AbstractContainerMenu {

    BlockPos pos;
    ExperienceObeliskEntity entity;
    Inventory inventory;

    public ExperienceObeliskMenu(int id, Inventory inventory, FriendlyByteBuf data) {
        this(id, inventory, inventory.player);

        Level level = inventory.player.level;
        this.pos = data.readBlockPos();
        this.entity = (ExperienceObeliskEntity) level.getBlockEntity(pos);
        this.inventory = inventory;

        this.sendAllDataToRemote(); //ok lets try sync

        System.out.println("blockpos: " + pos);
        System.out.println("entity: " + entity);
    }

    public ExperienceObeliskMenu(int id, Inventory inventory, Player player) {
        super(RegisterMenus.EXPERIENCE_OBELISK_MENU.get(), id);
    }


    @Override
    public boolean stillValid(Player player) {
        if(player.containerMenu instanceof ExperienceObeliskMenu menu){
            return player.position().distanceTo(Vec3.atCenterOf(menu.pos)) < 7;
        }
        else{
            return false;
        }
    }
}
