package com.cyanogen.experienceobelisk.registries;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.cyanogen.experienceobelisk.item.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

import static com.cyanogen.experienceobelisk.item.CognitiveToolset.createCustomAttributes;
import static com.cyanogen.experienceobelisk.item.CognitiveToolset.increasedReach;

public class RegisterItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, ExperienceObelisk.MOD_ID);

    public static final Tier COGNITIVE_TIER = CognitiveToolset.COGNITIVE_TIER;
    public static Item baseItem(){
        return new Item(new Item.Properties());
    }

    //-----CRAFTING INGREDIENTS-----//

    public static final DeferredHolder<Item, Item> COGNITIVE_FLUX = ITEMS.register("cognitive_flux", RegisterItems::baseItem);
    public static final DeferredHolder<Item, Item> COGNITIVE_AMALGAM = ITEMS.register("cognitive_amalgam", RegisterItems::baseItem);
    public static final DeferredHolder<Item, Item> COGNITIVE_ALLOY = ITEMS.register("cognitive_alloy", RegisterItems::baseItem);
    public static final DeferredHolder<Item, Item> COGNITIVE_CRYSTAL = ITEMS.register("cognitive_crystal", RegisterItems::baseItem);
    public static final DeferredHolder<Item, Item> ASTUTE_ASSEMBLY = ITEMS.register("astute_assembly", RegisterItems::baseItem);
    public static final DeferredHolder<Item, Item> PRIMORDIAL_ASSEMBLY = ITEMS.register("primordial_assembly", RegisterItems::baseItem);
    public static final DeferredHolder<Item, Item> FORGOTTEN_DUST = ITEMS.register("forgotten_dust", () -> new Item(new Item.Properties()){
        @Override
        public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
            return 100;
        }
    });
    public static final DeferredHolder<Item, Item> CALCARINE_MATRIX = ITEMS.register("calcarine_matrix", RegisterItems::baseItem);

    //-----COGNITIVE TOOLSET-----//

    public static final DeferredHolder<Item, SwordItem> COGNITIVE_SWORD = ITEMS.register("cognitive_sword",
            () -> new SwordItem(COGNITIVE_TIER, createCustomAttributes(
                    new Item.Properties(), SwordItem.createAttributes(COGNITIVE_TIER, 3, -2.4f), increasedReach())));

    public static final DeferredHolder<Item, ShovelItem> COGNITIVE_SHOVEL = ITEMS.register("cognitive_shovel",
            () -> new ShovelItem(COGNITIVE_TIER, createCustomAttributes(
                    new Item.Properties(), ShovelItem.createAttributes(COGNITIVE_TIER, 1.5f, -3f), increasedReach())));

    public static final DeferredHolder<Item, PickaxeItem> COGNITIVE_PICKAXE = ITEMS.register("cognitive_pickaxe",
            () -> new PickaxeItem(COGNITIVE_TIER, createCustomAttributes(
                    new Item.Properties(), PickaxeItem.createAttributes(COGNITIVE_TIER, 1f, -2.8f), increasedReach())));

    public static final DeferredHolder<Item, AxeItem> COGNITIVE_AXE = ITEMS.register("cognitive_axe",
            () -> new AxeItem(COGNITIVE_TIER, createCustomAttributes(
                    new Item.Properties(), AxeItem.createAttributes(COGNITIVE_TIER, 6f, -3.1f), increasedReach())));

    public static final DeferredHolder<Item, HoeItem> COGNITIVE_HOE = ITEMS.register("cognitive_hoe",
            () -> new HoeItem(COGNITIVE_TIER, createCustomAttributes(
                    new Item.Properties(), HoeItem.createAttributes(COGNITIVE_TIER, -2f, -1f), increasedReach())));

    public static final DeferredHolder<Item, ArmorItem> COGNITIVE_HELMET = ITEMS.register("cognitive_helmet",
            () -> new CognitiveArmorset.ExtraAttributeArmorItem(RegisterTiers.COGNITIVE_ARMOR_MATERIAL, ArmorItem.Type.HELMET,
                    CognitiveArmorset.increasedReach(EquipmentSlotGroup.HEAD)));

    public static final DeferredHolder<Item, ArmorItem> COGNITIVE_CHESTPLATE = ITEMS.register("cognitive_chestplate",
            () -> new CognitiveArmorset.ExtraAttributeArmorItem(RegisterTiers.COGNITIVE_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE,
                    CognitiveArmorset.increasedReach(EquipmentSlotGroup.CHEST)));

    public static final DeferredHolder<Item, ArmorItem> COGNITIVE_LEGGINGS = ITEMS.register("cognitive_leggings",
            () -> new CognitiveArmorset.ExtraAttributeArmorItem(RegisterTiers.COGNITIVE_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS,
                    CognitiveArmorset.increasedReach(EquipmentSlotGroup.LEGS)));

    public static final DeferredHolder<Item, ArmorItem> COGNITIVE_BOOTS = ITEMS.register("cognitive_boots",
            () -> new CognitiveArmorset.ExtraAttributeArmorItem(RegisterTiers.COGNITIVE_ARMOR_MATERIAL, ArmorItem.Type.BOOTS,
                    CognitiveArmorset.increasedReach(EquipmentSlotGroup.FEET)));

    public static final DeferredHolder<Item, FishingRodItem> COGNITIVE_ROD = ITEMS.register("cognitive_rod",
            () -> new FishingRodItem(createCustomAttributes(new Item.Properties(), null, increasedReach()).durability(2200)));

    public static final DeferredHolder<Item, ShearsItem> COGNITIVE_SHEARS = ITEMS.register("cognitive_shears",
            () -> new ShearsItem(createCustomAttributes(new Item.Properties(), null, increasedReach()).durability(2200)));

    //-----FUNCTIONAL ITEMS-----//

    public static final DeferredHolder<Item, AttunementStaffItem> ATTUNEMENT_STAFF = ITEMS.register("attunement_staff",
            () -> new AttunementStaffItem(new Item.Properties()));

    public static final DeferredHolder<Item, EnlightenedAmuletItem> ENLIGHTENED_AMULET = ITEMS.register("enlightened_amulet",
            () -> new EnlightenedAmuletItem(new Item.Properties()));

    public static final DeferredHolder<Item, BucketItem> COGNITIUM_BUCKET = ITEMS.register("cognitium_bucket",
            () -> new BucketItem(RegisterFluids.COGNITIUM_SOURCE.get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    public static final DeferredHolder<Item, BottleNightmareItem> NIGHTMARE_BOTTLE = ITEMS.register("nightmare_bottle",
            () -> new BottleNightmareItem(new Item.Properties().stacksTo(64)));

    public static final DeferredHolder<Item, BottleDaydreamItem> DAYDREAM_BOTTLE = ITEMS.register("daydream_bottle",
            () -> new BottleDaydreamItem(new Item.Properties().stacksTo(64)));

    public static final DeferredHolder<Item, BibliophageItem> BIBLIOPHAGE = ITEMS.register("bibliophage",
            () -> new BibliophageItem(new Item.Properties()));

    public static final DeferredHolder<Item, FluorescentJellyItem> FLUORESCENT_JELLY = ITEMS.register("fluorescent_jelly",
            () -> new FluorescentJellyItem(new Item.Properties()));

    public static final DeferredHolder<Item, NeurogelMendingItem> MENDING_NEUROGEL = ITEMS.register("mending_neurogel",
            () -> new NeurogelMendingItem(new Item.Properties()));

    public static final DeferredHolder<Item, FlaskPoseidonItem> POSEIDON_FLASK = ITEMS.register("flask_of_poseidon",
            () -> new FlaskPoseidonItem(new Item.Properties()));

    public static final DeferredHolder<Item, FlaskHadesItem> HADES_FLASK = ITEMS.register("flask_of_hades",
            () -> new FlaskHadesItem(new Item.Properties()));

    public static final DeferredHolder<Item, FlaskChaosItem> CHAOS_FLASK = ITEMS.register("flask_of_chaos",
            () -> new FlaskChaosItem(new Item.Properties()));

    public static final DeferredHolder<Item, TransformingFocusItem> TRANSFORMING_FOCUS = ITEMS.register("transforming_focus",
            () -> new TransformingFocusItem(new Item.Properties().durability(TransformingFocusItem.durability)));

    //-----FUNCTIONAL BLOCK ITEMS-----//

    public static final DeferredHolder<Item, ExperienceObeliskItem> EXPERIENCE_OBELISK_ITEM = ITEMS.register("experience_obelisk",
            () -> new ExperienceObeliskItem(RegisterBlocks.EXPERIENCE_OBELISK.get(), new Item.Properties()));

    public static final DeferredHolder<Item, ExperienceFountainItem> EXPERIENCE_FOUNTAIN_ITEM = ITEMS.register("experience_fountain",
            () -> new ExperienceFountainItem(RegisterBlocks.EXPERIENCE_FOUNTAIN.get(), new Item.Properties()));

    public static final DeferredHolder<Item, PrecisionDispellerItem> PRECISION_DISPELLER_ITEM = ITEMS.register("precision_dispeller",
            () -> new PrecisionDispellerItem(RegisterBlocks.PRECISION_DISPELLER.get(), new Item.Properties()));

    public static final DeferredHolder<Item, MolecularMetamorpherItem> MOLECULAR_METAMORPHER_ITEM = ITEMS.register("molecular_metamorpher",
            () -> new MolecularMetamorpherItem(RegisterBlocks.MOLECULAR_METAMORPHER.get(), new Item.Properties()));

    public static final DeferredHolder<Item, BlockItem> ACCELERATOR_ITEM = ITEMS.register("accelerator",
            () -> new BlockItem(RegisterBlocks.ACCELERATOR.get(), new Item.Properties()));

    public static final DeferredHolder<Item, BlockItem> LINEAR_ACCELERATOR_ITEM = ITEMS.register("linear_accelerator",
            () -> new BlockItem(RegisterBlocks.LINEAR_ACCELERATOR.get(), new Item.Properties()));

    public static final DeferredHolder<Item, BlockItem> ENCHANTED_BOOKSHELF_ITEM = ITEMS.register("enchanted_bookshelf",
            () -> new BlockItem(RegisterBlocks.ENCHANTED_BOOKSHELF.get(), new Item.Properties()));

    public static final DeferredHolder<Item, BlockItem> ARCHIVERS_BOOKSHELF_ITEM = ITEMS.register("archivers_bookshelf",
            () -> new BlockItem(RegisterBlocks.ARCHIVERS_BOOKSHELF.get(), new Item.Properties()));

    public static final DeferredHolder<Item, BlockItem> INFECTED_BOOKSHELF_ITEM = ITEMS.register("infected_bookshelf",
            () -> new BlockItem(RegisterBlocks.INFECTED_BOOKSHELF.get(), new Item.Properties()));

    public static final DeferredHolder<Item, BlockItem> INFECTED_ENCHANTED_BOOKSHELF_ITEM = ITEMS.register("infected_enchanted_bookshelf",
            () -> new BlockItem(RegisterBlocks.INFECTED_ENCHANTED_BOOKSHELF.get(), new Item.Properties()));

    public static final DeferredHolder<Item, BlockItem> INFECTED_ARCHIVERS_BOOKSHELF_ITEM = ITEMS.register("infected_archivers_bookshelf",
            () -> new BlockItem(RegisterBlocks.INFECTED_ARCHIVERS_BOOKSHELF.get(), new Item.Properties()));

    public static final DeferredHolder<Item, BlockItem> FLUORESCENT_AGAR_ITEM = ITEMS.register("fluorescent_agar",
            () -> new BlockItem(RegisterBlocks.FLUORESCENT_AGAR.get(), new Item.Properties()));

    public static final DeferredHolder<Item, BlockItem> NUTRIENT_AGAR_ITEM = ITEMS.register("nutrient_agar",
            () -> new BlockItem(RegisterBlocks.NUTRIENT_AGAR.get(), new Item.Properties()));

    public static final DeferredHolder<Item, BlockItem> INSIGHTFUL_AGAR_ITEM = ITEMS.register("insightful_agar",
            () -> new BlockItem(RegisterBlocks.INSIGHTFUL_AGAR.get(), new Item.Properties()));

    public static final DeferredHolder<Item, BlockItem> EXTRAVAGANT_AGAR_ITEM = ITEMS.register("extravagant_agar",
            () -> new BlockItem(RegisterBlocks.EXTRAVAGANT_AGAR.get(), new Item.Properties()));

    //-----BLOCK ITEMS-----//

    public static final DeferredHolder<Item, BlockItem> COGNITIVE_ALLOY_BLOCK_ITEM = ITEMS.register("cognitive_alloy_block",
            () -> new BlockItem(RegisterBlocks.COGNITIVE_ALLOY_BLOCK.get(), new Item.Properties()));

    public static final DeferredHolder<Item, BlockItem> COGNITIVE_CRYSTAL_BLOCK_ITEM = ITEMS.register("cognitive_crystal_block",
            () -> new BlockItem(RegisterBlocks.COGNITIVE_CRYSTAL_BLOCK.get(), new Item.Properties()));

    public static final DeferredHolder<Item, BlockItem> WHISPERGLASS_ITEM = ITEMS.register("whisperglass",
            () -> new BlockItem(RegisterBlocks.WHISPERGLASS_BLOCK.get(), new Item.Properties()));

    public static final DeferredHolder<Item, BlockItem> FORGOTTEN_DUST_BLOCK_ITEM = ITEMS.register("forgotten_dust_block",
            () -> new BlockItem(RegisterBlocks.FORGOTTEN_DUST_BLOCK.get(), new Item.Properties()){
                @Override
                public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
                    return 400;
                }
            });

    //-----DUMMY ITEM-----//

    public static final DeferredHolder<Item, Item> DUMMY_SWORD = ITEMS.register("dummy_sword", RegisterItems::baseItem);

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
