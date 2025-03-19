package com.cyanogen.experienceobelisk.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public class CognitiveAlloyBlock extends Block {

    public CognitiveAlloyBlock() {
        super(BlockBehaviour.Properties.of(Material.METAL)
                .color(MaterialColor.COLOR_BLACK)
                .sound(SoundType.NETHERITE_BLOCK)
                .strength(9f)
                .destroyTime(1.2f)
                .requiresCorrectToolForDrops()
                .explosionResistance(1200f)
        );
    }
}
