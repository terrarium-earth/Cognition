package com.cyanogen.experienceobelisk.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class GuiWrapper {

    public static void openGUI(BlockState state, Level level, BlockPos pos, Player player) {
        Minecraft.getInstance().setScreen(new ExperienceObeliskScreen(level, player, pos));
    }

}
