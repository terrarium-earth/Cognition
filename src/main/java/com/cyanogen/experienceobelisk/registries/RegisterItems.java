package com.cyanogen.experienceobelisk.registries;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.cyanogen.experienceobelisk.item.*;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegisterItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ExperienceObelisk.MOD_ID);

    //-----BLOCK ITEMS-----//

    public static final RegistryObject<Item> EXPERIENCE_OBELISK_ITEM = ITEMS.register("experience_obelisk",
            () -> new ExperienceObeliskItem(RegisterBlocks.EXPERIENCE_OBELISK.get(), new Item.Properties().tab(RegisterCreativeTab.MOD_TAB)));

    public static final RegistryObject<Item> EXPERIENCE_FOUNTAIN_ITEM = ITEMS.register("experience_fountain",
            () -> new ExperienceFountainItem(RegisterBlocks.EXPERIENCE_FOUNTAIN.get(), new Item.Properties().tab(RegisterCreativeTab.MOD_TAB)));

    public static final RegistryObject<Item> PRECISION_DISPELLER_ITEM = ITEMS.register("precision_dispeller",
            () -> new PrecisionDispellerItem(RegisterBlocks.PRECISION_DISPELLER.get(), new Item.Properties().tab(RegisterCreativeTab.MOD_TAB)));

    //-----RECIPE INGREDIENTS, EVERYTHING ELSE-----//

    public static final RegistryObject<Item> COGNITIVE_FLUX = ITEMS.register("cognitive_flux",
            () -> new Item(new Item.Properties().tab(RegisterCreativeTab.MOD_TAB)));

    public static final RegistryObject<Item> COGNITIVE_AMALGAM = ITEMS.register("cognitive_amalgam",
            () -> new Item(new Item.Properties().tab(RegisterCreativeTab.MOD_TAB)));

    public static final RegistryObject<Item> COGNITIVE_ALLOY = ITEMS.register("cognitive_alloy",
            () -> new Item(new Item.Properties().tab(RegisterCreativeTab.MOD_TAB)));

    public static final RegistryObject<Item> COGNITIVE_CRYSTAL = ITEMS.register("cognitive_crystal",
            () -> new Item(new Item.Properties().tab(RegisterCreativeTab.MOD_TAB)));

    //-----FUNCTIONAL ITEMS-----//

    public static final RegistryObject<Item> ATTUNEMENT_STAFF = ITEMS.register("attunement_staff",
            () -> new AttunementStaffItem(new Item.Properties().tab(RegisterCreativeTab.MOD_TAB)));

    public static final RegistryObject<Item> ENLIGHTENED_AMULET = ITEMS.register("enlightened_amulet",
            () -> new EnlightenedAmuletItem(new Item.Properties().tab(RegisterCreativeTab.MOD_TAB).rarity(Rarity.RARE)));

    public static final RegistryObject<Item> ASTUTE_ARTIFACT = ITEMS.register("astute_artifact",
            () -> new AstuteArtifactItem(new Item.Properties().tab(RegisterCreativeTab.MOD_TAB).rarity(Rarity.RARE)));

    public static final RegistryObject<BucketItem> COGNITIUM_BUCKET = ITEMS.register("cognitium_bucket",
            () -> new BucketItem(RegisterFluids.COGNITIUM, new Item.Properties().tab(RegisterCreativeTab.MOD_TAB).craftRemainder(Items.BUCKET)));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
