package com.cyanogen.experienceobelisk.block.bibliophage.bookshelves;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class BookshelfBlock extends Block {

    public final float enchantPowerBonus;

    public BookshelfBlock(float enchantPowerBonus) {
        super(Properties.ofFullCopy(Blocks.BOOKSHELF));
        this.enchantPowerBonus = enchantPowerBonus;
    }

    @Override
    public float getEnchantPowerBonus(BlockState state, LevelReader level, BlockPos pos) {
        return enchantPowerBonus;
    }


}
