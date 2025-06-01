package com.cyanogen.experienceobelisk.gui;

import com.cyanogen.experienceobelisk.block_entities.ExperienceObeliskEntity;
import com.cyanogen.experienceobelisk.config.Config;
import com.cyanogen.experienceobelisk.registries.RegisterItems;
import com.cyanogen.experienceobelisk.registries.RegisterMenus;
import com.cyanogen.experienceobelisk.utils.MiscUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ExperienceObeliskMenu extends AbstractContainerMenu {

    BlockPos pos;
    final BlockPos posServer;
    ExperienceObeliskEntity entity;
    Inventory inventory;

    public ExperienceObeliskMenu(int id, Inventory inventory, FriendlyByteBuf data) {
        this(id, null);

        Level level = inventory.player.level();
        this.pos = data.readBlockPos();
        this.entity = (ExperienceObeliskEntity) level.getBlockEntity(pos);
        this.inventory = inventory;
    }

    public ExperienceObeliskMenu(int id, BlockPos pos) {
        super(RegisterMenus.EXPERIENCE_OBELISK_MENU.get(), id);
        this.posServer = pos;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        double distance = 7;
        if(player.getItemInHand(InteractionHand.MAIN_HAND).is(RegisterItems.MEMORY_TABLET.get())) distance = Config.COMMON.bindingRange.get();
        return MiscUtils.straightLineDistance(posServer, player.blockPosition()) <= distance;
    }
}
