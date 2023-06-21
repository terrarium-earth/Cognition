package com.cyanogen.experienceobelisk.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.extensions.IForgeBlock;

public class CognitiveAlloyBlock extends Block implements IForgeBlock {

    public CognitiveAlloyBlock() {
        super(BlockBehaviour.Properties.of(Material.METAL)
                .strength(9f)
                .destroyTime(1.2f)
                .requiresCorrectToolForDrops()
                .explosionResistance(1200f)
        );


    }



}
