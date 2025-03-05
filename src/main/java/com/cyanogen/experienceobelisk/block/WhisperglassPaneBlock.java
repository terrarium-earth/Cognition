package com.cyanogen.experienceobelisk.block;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;

public class WhisperglassPaneBlock extends IronBarsBlock {
    public WhisperglassPaneBlock() {
        super(Properties.copy(Blocks.GLASS_PANE)
                .mapColor(MapColor.COLOR_LIGHT_BLUE)
                .sound(SoundType.GLASS)
                .strength(2.8f)
                .destroyTime(1.2f)
                .requiresCorrectToolForDrops()
                .friction(1.0f)
                .noOcclusion()
                .isViewBlocking((state,getter,pos)->false));
    }
}
