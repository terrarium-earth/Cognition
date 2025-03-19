package com.cyanogen.experienceobelisk.registries;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class RegisterCreativeTab {
    
    public static final CreativeModeTab MOD_TAB = new CreativeModeTab("experienceobelisk"){
        
        @Override
        public ItemStack makeIcon() {
            return RegisterItems.EXPERIENCE_OBELISK_ITEM.get().getDefaultInstance();
        }

        @Override
        public void fillItemList(NonNullList<ItemStack> itemList) {

            //BASIC INGREDIENTS
            RegisterItems.COGNITIVE_FLUX.get().fillItemCategory(this, itemList);
            RegisterItems.COGNITIVE_AMALGAM.get().fillItemCategory(this, itemList);
            RegisterItems.COGNITIVE_ALLOY.get().fillItemCategory(this, itemList);
            RegisterItems.COGNITIVE_CRYSTAL.get().fillItemCategory(this, itemList);
            RegisterItems.ASTUTE_ASSEMBLY.get().fillItemCategory(this, itemList);
            RegisterItems.PRIMORDIAL_ASSEMBLY.get().fillItemCategory(this, itemList);
            RegisterItems.FORGOTTEN_DUST.get().fillItemCategory(this, itemList);
            RegisterItems.CALCARINE_MATRIX.get().fillItemCategory(this, itemList);

            //TOOLSETS
            RegisterItems.COGNITIVE_SWORD.get().fillItemCategory(this, itemList);
            RegisterItems.COGNITIVE_SHOVEL.get().fillItemCategory(this, itemList);
            RegisterItems.COGNITIVE_PICKAXE.get().fillItemCategory(this, itemList);
            RegisterItems.COGNITIVE_AXE.get().fillItemCategory(this, itemList);
            RegisterItems.COGNITIVE_HOE.get().fillItemCategory(this, itemList);
            RegisterItems.COGNITIVE_ROD.get().fillItemCategory(this, itemList);
            RegisterItems.COGNITIVE_SHEARS.get().fillItemCategory(this, itemList);
            RegisterItems.COGNITIVE_HELMET.get().fillItemCategory(this, itemList);
            RegisterItems.COGNITIVE_CHESTPLATE.get().fillItemCategory(this, itemList);
            RegisterItems.COGNITIVE_LEGGINGS.get().fillItemCategory(this, itemList);
            RegisterItems.COGNITIVE_BOOTS.get().fillItemCategory(this, itemList);

            //FUNCTIONAL ITEMS
            RegisterItems.ATTUNEMENT_STAFF.get().fillItemCategory(this, itemList);
            RegisterItems.ENLIGHTENED_AMULET.get().fillItemCategory(this, itemList);
            RegisterItems.BIBLIOPHAGE.get().fillItemCategory(this, itemList);
            RegisterItems.EXPERIENCE_JELLY.get().fillItemCategory(this, itemList);
            RegisterItems.MENDING_NEUROGEL.get().fillItemCategory(this, itemList);
            RegisterItems.POSEIDON_FLASK.get().fillItemCategory(this, itemList);
            RegisterItems.HADES_FLASK.get().fillItemCategory(this, itemList);
            RegisterItems.CHAOS_FLASK.get().fillItemCategory(this, itemList);
            RegisterItems.NIGHTMARE_BOTTLE.get().fillItemCategory(this, itemList);
            RegisterItems.DAYDREAM_BOTTLE.get().fillItemCategory(this, itemList);
            RegisterItems.TRANSFORMING_FOCUS.get().fillItemCategory(this, itemList);

            //FUNCTIONAL BLOCKS
            RegisterItems.EXPERIENCE_OBELISK_ITEM.get().fillItemCategory(this, itemList);
            RegisterItems.EXPERIENCE_FOUNTAIN_ITEM.get().fillItemCategory(this, itemList);
            RegisterItems.PRECISION_DISPELLER_ITEM.get().fillItemCategory(this, itemList);
            RegisterItems.MOLECULAR_METAMORPHER_ITEM.get().fillItemCategory(this, itemList);
            RegisterItems.ACCELERATOR_ITEM.get().fillItemCategory(this, itemList);
            RegisterItems.LINEAR_ACCELERATOR_ITEM.get().fillItemCategory(this, itemList);
            RegisterItems.ARCHIVERS_BOOKSHELF_ITEM.get().fillItemCategory(this, itemList);
            RegisterItems.ENCHANTED_BOOKSHELF_ITEM.get().fillItemCategory(this, itemList);
            RegisterItems.INFECTED_BOOKSHELF_ITEM.get().fillItemCategory(this, itemList);
            RegisterItems.INFECTED_ARCHIVERS_BOOKSHELF_ITEM.get().fillItemCategory(this, itemList);
            RegisterItems.INFECTED_ENCHANTED_BOOKSHELF_ITEM.get().fillItemCategory(this, itemList);
            RegisterItems.FLUORESCENT_AGAR_ITEM.get().fillItemCategory(this, itemList);
            RegisterItems.NUTRIENT_AGAR_ITEM.get().fillItemCategory(this, itemList);
            RegisterItems.INSIGHTFUL_AGAR_ITEM.get().fillItemCategory(this, itemList);
            RegisterItems.EXTRAVAGANT_AGAR_ITEM.get().fillItemCategory(this, itemList);

            //DECORATIVE / OTHER BLOCKS
            RegisterItems.COGNITIVE_ALLOY_BLOCK_ITEM.get().fillItemCategory(this, itemList);
            RegisterItems.COGNITIVE_CRYSTAL_BLOCK_ITEM.get().fillItemCategory(this, itemList);
            RegisterItems.WHISPERGLASS_ITEM.get().fillItemCategory(this, itemList);
            RegisterItems.FORGOTTEN_DUST_BLOCK_ITEM.get().fillItemCategory(this, itemList);

            //MISC
            RegisterItems.COGNITIUM_BUCKET.get().fillItemCategory(this, itemList);
        }
    };
}
