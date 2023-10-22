package com.cyanogen.experienceobelisk.gui;

import com.cyanogen.experienceobelisk.block_entities.ExperienceObeliskEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ExperienceObeliskMenu extends AbstractContainerMenu {

    BlockPos pos;
    BlockPos posServer;
    ExperienceObeliskEntity entity;
    Inventory inventory;

    public ExperienceObeliskMenu(int id, Inventory inventory, FriendlyByteBuf data) {
        this(id, null);

        Level level = inventory.player.level;
        this.pos = data.readBlockPos();
        this.entity = (ExperienceObeliskEntity) level.getBlockEntity(pos);
        this.inventory = inventory;
    }

    public ExperienceObeliskMenu(int id, BlockPos pos) {
        super(RegisterMenus.EXPERIENCE_OBELISK_MENU.get(), id);
        this.posServer = pos;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.position().distanceTo(Vec3.atCenterOf(posServer)) <= 7;
    }
}
