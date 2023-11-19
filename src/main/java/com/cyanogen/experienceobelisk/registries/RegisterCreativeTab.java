package com.cyanogen.experienceobelisk.registries;

import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;

public class RegisterCreativeTab {
    public static final CreativeModeTab MOD_TAB = new CreativeModeTab("experience_obelisk") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(RegisterBlocks.EXPERIENCE_OBELISK.get());
        }

        @Override
        public void fillItemList(NonNullList<ItemStack> items) {

            ArrayList<Item> itemList = new ArrayList<>();

            //Crafting Materials
            itemList.add(RegisterItems.COGNITIVE_FLUX.get());
            itemList.add(RegisterItems.COGNITIVE_AMALGAM.get());
            itemList.add(RegisterItems.COGNITIVE_ALLOY.get());
            itemList.add(RegisterItems.COGNITIVE_CRYSTAL.get());
            itemList.add(RegisterItems.ASTUTE_ASSEMBLAGE.get());

            //Cognitive Toolset
            itemList.add(RegisterItems.COGNITIVE_SWORD.get());
            itemList.add(RegisterItems.COGNITIVE_SHOVEL.get());
            itemList.add(RegisterItems.COGNITIVE_PICKAXE.get());
            itemList.add(RegisterItems.COGNITIVE_AXE.get());
            itemList.add(RegisterItems.COGNITIVE_HOE.get());

            //Functional Items
            itemList.add(RegisterItems.ATTUNEMENT_STAFF.get());
            itemList.add(RegisterItems.ENLIGHTENED_AMULET.get());
            itemList.add(RegisterItems.COGNITIUM_BUCKET.get());

            //Block Items
            itemList.add(RegisterItems.EXPERIENCE_OBELISK_ITEM.get());
            itemList.add(RegisterItems.EXPERIENCE_FOUNTAIN_ITEM.get());
            itemList.add(RegisterItems.PRECISION_DISPELLER_ITEM.get());

            //Blocks
            itemList.add(RegisterBlocks.COGNITIVE_ALLOY_BLOCK.get().asItem());
            itemList.add(RegisterBlocks.COGNITIVE_CRYSTAL_BLOCK.get().asItem());

            for(Item item : itemList) {
                item.fillItemCategory(this, items);
            }
        }
    };
}
