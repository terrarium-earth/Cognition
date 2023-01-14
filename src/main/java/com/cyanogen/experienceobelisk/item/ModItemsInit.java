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

import static net.minecraft.world.item.Items.BUCKET;

public class ModItemsInit {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ExperienceObelisk.MOD_ID);

    public static final RegistryObject<Item> EXPERIENCE_OBELISK_ITEM = ITEMS.register("experience_obelisk",
            () -> new ExperienceObeliskItem(ModBlocksInit.EXPERIENCE_OBELISK.get(), new Item.Properties().tab(ModCreativeModeTab.MOD_TAB)));

    //bucket of experience
    public static final RegistryObject<BucketItem> RAW_EXPERIENCE_BUCKET = ITEMS.register("raw_experience_bucket",
            () -> new BucketItem(ModFluidsInit.RAW_EXPERIENCE, new Item.Properties().tab(ModCreativeModeTab.MOD_TAB).rarity(Rarity.UNCOMMON)));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
