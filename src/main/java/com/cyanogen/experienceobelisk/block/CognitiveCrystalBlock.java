package com.cyanogen.experienceobelisk.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

public class CognitiveCrystalBlock extends Block {

    public CognitiveCrystalBlock() {
        super(BlockBehaviour.Properties.of(Material.AMETHYST)
                .strength(9f)
                .destroyTime(1.2f)
                .requiresCorrectToolForDrops()
                .explosionResistance(1200f)
                .lightLevel((state) -> 12)
        );
    }
}
