package com.cyanogen.experienceobelisk.registries;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RegisterCreativeTab {

    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, ExperienceObelisk.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXPERIENCE_OBELISK_TAB = TABS.register("experienceobelisk_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.experienceobelisk"))
                    .icon(RegisterItems.EXPERIENCE_OBELISK_ITEM.get()::getDefaultInstance)
                    .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                    .displayItems((p_270258_, output) -> {

                        //INGREDIENTS
                        output.accept(RegisterItems.COGNITIVE_FLUX.get());
                        output.accept(RegisterItems.COGNITIVE_AMALGAM.get());
                        output.accept(RegisterItems.COGNITIVE_ALLOY.get());
                        output.accept(RegisterItems.COGNITIVE_CRYSTAL.get());
                        output.accept(RegisterItems.ASTUTE_ASSEMBLY.get());
                        output.accept(RegisterItems.PRIMORDIAL_ASSEMBLY.get());
                        output.accept(RegisterItems.FORGOTTEN_DUST.get());
                        output.accept(RegisterItems.CALCARINE_MATRIX.get());

                        //TOOLSETS & ARMOR
                        output.accept(RegisterItems.COGNITIVE_SWORD.get());
                        output.accept(RegisterItems.COGNITIVE_SHOVEL.get());
                        output.accept(RegisterItems.COGNITIVE_PICKAXE.get());
                        output.accept(RegisterItems.COGNITIVE_AXE.get());
                        output.accept(RegisterItems.COGNITIVE_HOE.get());
                        output.accept(RegisterItems.COGNITIVE_ROD.get());
                        output.accept(RegisterItems.COGNITIVE_SHEARS.get());
                        output.accept(RegisterItems.COGNITIVE_HELMET.get());
                        output.accept(RegisterItems.COGNITIVE_CHESTPLATE.get());
                        output.accept(RegisterItems.COGNITIVE_LEGGINGS.get());
                        output.accept(RegisterItems.COGNITIVE_BOOTS.get());

                        //FUNCTIONAL ITEMS
                        output.accept(RegisterItems.ATTUNEMENT_STAFF.get());
                        output.accept(RegisterItems.ENLIGHTENED_AMULET.get());
                        output.accept(RegisterItems.BIBLIOPHAGE.get());
                        output.accept(RegisterItems.FLUORESCENT_JELLY.get());
                        output.accept(RegisterItems.MENDING_NEUROGEL.get());
                        output.accept(RegisterItems.POSEIDON_FLASK.get());
                        output.accept(RegisterItems.HADES_FLASK.get());
                        output.accept(RegisterItems.CHAOS_FLASK.get());
                        output.accept(RegisterItems.NIGHTMARE_BOTTLE.get());
                        output.accept(RegisterItems.DAYDREAM_BOTTLE.get());
                        output.accept(RegisterItems.TRANSFORMING_FOCUS.get());

                        //FUNCTIONAL BLOCKS
                        output.accept(RegisterItems.EXPERIENCE_OBELISK_ITEM.get());
                        output.accept(RegisterItems.EXPERIENCE_FOUNTAIN_ITEM.get());
                        output.accept(RegisterItems.PRECISION_DISPELLER_ITEM.get());
                        output.accept(RegisterItems.MOLECULAR_METAMORPHER_ITEM.get());
                        output.accept(RegisterItems.ACCELERATOR_ITEM.get());
                        output.accept(RegisterItems.LINEAR_ACCELERATOR_ITEM.get());
                        output.accept(RegisterItems.ARCHIVERS_BOOKSHELF_ITEM.get());
                        output.accept(RegisterItems.ENCHANTED_BOOKSHELF_ITEM.get());
                        output.accept(RegisterItems.INFECTED_BOOKSHELF_ITEM.get());
                        output.accept(RegisterItems.INFECTED_ARCHIVERS_BOOKSHELF_ITEM.get());
                        output.accept(RegisterItems.INFECTED_ENCHANTED_BOOKSHELF_ITEM.get());
                        output.accept(RegisterItems.FLUORESCENT_AGAR_ITEM.get());
                        output.accept(RegisterItems.NUTRIENT_AGAR_ITEM.get());
                        output.accept(RegisterItems.INSIGHTFUL_AGAR_ITEM.get());
                        output.accept(RegisterItems.EXTRAVAGANT_AGAR_ITEM.get());

                        //DECORATIVE / OTHER BLOCKS
                        output.accept(RegisterItems.COGNITIVE_ALLOY_BLOCK_ITEM.get());
                        output.accept(RegisterItems.COGNITIVE_CRYSTAL_BLOCK_ITEM.get());
                        output.accept(RegisterItems.WHISPERGLASS_ITEM.get());
                        output.accept(RegisterItems.FORGOTTEN_DUST_BLOCK_ITEM.get());

                        //MISC
                        output.accept(RegisterItems.COGNITIUM_BUCKET.get());
                    })
                    .build());

    public static void register(IEventBus eventBus){
        TABS.register(eventBus);
    }
}
