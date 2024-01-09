package com.cyanogen.experienceobelisk.registries;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class RegisterCreativeTab {
    public static final CreativeModeTab MOD_TAB = new CreativeModeTab("experienceobelisk") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(RegisterBlocks.EXPERIENCE_OBELISK.get());
        }

    };
}
