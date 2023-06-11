package com.cyanogen.experienceobelisk.item;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.cyanogen.experienceobelisk.ModCreativeModeTab;
import com.cyanogen.experienceobelisk.block.ModBlocksInit;
import com.cyanogen.experienceobelisk.fluid.ModFluidsInit;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItemsInit {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ExperienceObelisk.MOD_ID);

    //-----BLOCK ITEMS-----//

    public static final RegistryObject<Item> EXPERIENCE_OBELISK_ITEM = ITEMS.register("experience_obelisk",
            () -> new ExperienceObeliskItem(ModBlocksInit.EXPERIENCE_OBELISK.get(), new Item.Properties().tab(ModCreativeModeTab.MOD_TAB)));

    public static final RegistryObject<Item> EXPERIENCE_FOUNTAIN_ITEM = ITEMS.register("experience_fountain",
            () -> new ExperienceFountainItem(ModBlocksInit.EXPERIENCE_FOUNTAIN.get(), new Item.Properties().tab(ModCreativeModeTab.MOD_TAB)));

    //-----FUNCTIONAL ITEMS-----//

    public static final RegistryObject<Item> COGNITIVE_CRYSTAL = ITEMS.register("cognitive_crystal",
            () -> new CognitiveCrystalItem(new Item.Properties().tab(ModCreativeModeTab.MOD_TAB)));

    public static final RegistryObject<Item> BINDING_WAND = ITEMS.register("binding_wand",
            () -> new BindingWandItem(new Item.Properties().tab(ModCreativeModeTab.MOD_TAB)));

    //-----RECIPE INGREDIENTS, EVERYTHING ELSE-----//

    public static final RegistryObject<BucketItem> COGNITIUM_BUCKET = ITEMS.register("cognitium_bucket",
            () -> new BucketItem(ModFluidsInit.COGNITIUM, new Item.Properties().tab(ModCreativeModeTab.MOD_TAB).rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> COGNITIVE_FLUX = ITEMS.register("cognitive_flux",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MOD_TAB)));

    public static final RegistryObject<Item> COGNITIVE_AMALGAM = ITEMS.register("cognitive_amalgam",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MOD_TAB)));

    public static final RegistryObject<Item> COGNITIVE_ALLOY = ITEMS.register("cognitive_alloy",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MOD_TAB)));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
