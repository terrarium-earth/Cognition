package com.cyanogen.experienceobelisk;

import com.cyanogen.experienceobelisk.block.ModBlocksInit;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTab {
    public static final CreativeModeTab MOD_TAB = new CreativeModeTab("experience_obelisk") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModBlocksInit.EXPERIENCE_OBELISK.get());
        }
    };
}
