package com.cyanogen.experienceobelisk.gui;

import com.cyanogen.experienceobelisk.registries.RegisterMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class ExperienceObeliskMenu extends AbstractContainerMenu {

    private ContainerData blockPositionData;
    public Level level;

    //constructor used by client
    public ExperienceObeliskMenu(int id, Inventory inventory) {
        this(id, inventory, null);

        this.blockPositionData = new SimpleContainerData(3);
        this.addDataSlots(blockPositionData);
        this.level = inventory.player.level();
    }

    //constructor used by server
    public ExperienceObeliskMenu(int id, Inventory inventory, @Nullable BlockPos pos) {
        super(RegisterMenus.EXPERIENCE_OBELISK_MENU.get(), id);

        //data slots (server)
        if(pos != null){
            blockPositionData = new SimpleContainerData(3);
            blockPositionData.set(0, pos.getX());
            blockPositionData.set(1, pos.getY());
            blockPositionData.set(2, pos.getZ());
            this.addDataSlots(blockPositionData);

            sendAllDataToRemote();
        }
        else{
            blockPositionData = new SimpleContainerData(3);
        }
        this.level = inventory.player.level();
    }

    public BlockPos getBlockPos(){
        return new BlockPos(blockPositionData.get(0), blockPositionData.get(1), blockPositionData.get(2));
    }

    @Override
    @Nullable
    public ItemStack quickMoveStack(Player player, int i) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.position().distanceTo(Vec3.atCenterOf(getBlockPos())) <= 7;
    }

}
