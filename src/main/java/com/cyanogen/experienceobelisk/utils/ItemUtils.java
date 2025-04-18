package com.cyanogen.experienceobelisk.utils;

import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public class ItemUtils {

    public static CompoundTag getCustomDataTag(ItemStack stack){
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.of(new CompoundTag())).copyTag();
    }

    public static void saveCustomDataTag(ItemStack stack, CompoundTag tag){
        DataComponentPatch patch = DataComponentPatch.builder().set(DataComponents.CUSTOM_DATA, CustomData.of(tag)).build();
        stack.applyComponents(patch);
    }

    public static CompoundTag getBlockEntityTag(ItemStack stack){
        return stack.getOrDefault(DataComponents.BLOCK_ENTITY_DATA, CustomData.EMPTY).copyTag();
    }

}
