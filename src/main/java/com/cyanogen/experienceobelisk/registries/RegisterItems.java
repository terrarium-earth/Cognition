package com.cyanogen.experienceobelisk.registries;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.cyanogen.experienceobelisk.item.*;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class RegisterItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ExperienceObelisk.MOD_ID);

    public static Tier COGNITIVE = new Tier() {
        @Override
        public int getUses() {
            return 750;
        }

        @Override
        public float getSpeed() {
            return 7.0F;
        }

        @Override
        public float getAttackDamageBonus() {
            return 2.4F;
        }

        @Override
        public int getLevel() {
            return 2;
        }

        @Override
        public int getEnchantmentValue() {
            return 15;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(COGNITIVE_ALLOY.get());
        }
    };

    public static AttributeModifier range = new AttributeModifier("experienceobelisk:range",1.0, AttributeModifier.Operation.ADDITION);

    public static Item baseItem(){
        return new Item(new Item.Properties());
    }

    //-----CRAFTING INGREDIENTS-----//

    public static final RegistryObject<Item> COGNITIVE_FLUX = ITEMS.register("cognitive_flux", RegisterItems::baseItem);
    public static final RegistryObject<Item> COGNITIVE_AMALGAM = ITEMS.register("cognitive_amalgam", RegisterItems::baseItem);
    public static final RegistryObject<Item> COGNITIVE_ALLOY = ITEMS.register("cognitive_alloy", RegisterItems::baseItem);
    public static final RegistryObject<Item> COGNITIVE_CRYSTAL = ITEMS.register("cognitive_crystal", RegisterItems::baseItem);
    public static final RegistryObject<Item> ASTUTE_ASSEMBLY = ITEMS.register("astute_assembly", RegisterItems::baseItem);

    //-----COGNITIVE TOOLSET-----//

    public static final RegistryObject<Item> COGNITIVE_SWORD = ITEMS.register("cognitive_sword",
            () -> new SwordItem(COGNITIVE, 3, -2.4f, new Item.Properties()){
                @Override
                public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot slot) {
                    return addRangeAttributeModifier(super.getDefaultAttributeModifiers(slot), slot);
                }
            });

    public static final RegistryObject<Item> COGNITIVE_SHOVEL = ITEMS.register("cognitive_shovel",
            () -> new ShovelItem(COGNITIVE, 1.5f, -3f, new Item.Properties()){
                @Override
                public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot slot) {
                    return addRangeAttributeModifier(super.getDefaultAttributeModifiers(slot), slot);
                }
            });

    public static final RegistryObject<Item> COGNITIVE_PICKAXE = ITEMS.register("cognitive_pickaxe",
            () -> new PickaxeItem(COGNITIVE, 1, -2.8f, new Item.Properties()){
                @Override
                public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot slot) {
                    return addRangeAttributeModifier(super.getDefaultAttributeModifiers(slot), slot);
                }
            });

    public static final RegistryObject<Item> COGNITIVE_AXE = ITEMS.register("cognitive_axe",
            () -> new AxeItem(COGNITIVE, 6, -3.1f, new Item.Properties()){
                @Override
                public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot slot) {
                    return addRangeAttributeModifier(super.getDefaultAttributeModifiers(slot), slot);
                }
            });

    public static final RegistryObject<Item> COGNITIVE_HOE = ITEMS.register("cognitive_hoe",
            () -> new HoeItem(COGNITIVE, -2, -1, new Item.Properties()){
                @Override
                public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot slot) {
                    return addRangeAttributeModifier(super.getDefaultAttributeModifiers(slot), slot);
                }
            });

    public static Multimap<Attribute, AttributeModifier> addRangeAttributeModifier(Multimap<Attribute, AttributeModifier> multimap, EquipmentSlot slot){

        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(multimap);
        if(slot.equals(EquipmentSlot.MAINHAND) && !multimap.containsValue(range)){
            builder.put(ForgeMod.BLOCK_REACH.get(), range);
            builder.put(ForgeMod.ENTITY_REACH.get(), range);
        }
        return builder.build();
    }

    //-----FUNCTIONAL ITEMS-----//

    public static final RegistryObject<Item> ATTUNEMENT_STAFF = ITEMS.register("attunement_staff",
            () -> new AttunementStaffItem(new Item.Properties()));

    public static final RegistryObject<Item> ENLIGHTENED_AMULET = ITEMS.register("enlightened_amulet",
            () -> new EnlightenedAmuletItem(new Item.Properties()));

    public static final RegistryObject<BucketItem> COGNITIUM_BUCKET = ITEMS.register("cognitium_bucket",
            () -> new BucketItem(RegisterFluids.COGNITIUM, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    //-----FUNCTIONAL BLOCK ITEMS-----//

    public static final RegistryObject<Item> EXPERIENCE_OBELISK_ITEM = ITEMS.register("experience_obelisk",
            () -> new ExperienceObeliskItem(RegisterBlocks.EXPERIENCE_OBELISK.get(), new Item.Properties()));

    public static final RegistryObject<Item> EXPERIENCE_FOUNTAIN_ITEM = ITEMS.register("experience_fountain",
            () -> new ExperienceFountainItem(RegisterBlocks.EXPERIENCE_FOUNTAIN.get(), new Item.Properties()));

    public static final RegistryObject<Item> PRECISION_DISPELLER_ITEM = ITEMS.register("precision_dispeller",
            () -> new PrecisionDispellerItem(RegisterBlocks.PRECISION_DISPELLER.get(), new Item.Properties()));

    //-----BLOCK ITEMS-----//

    public static final RegistryObject<Item> COGNITIVE_ALLOY_BLOCK_ITEM = ITEMS.register("cognitive_alloy_block",
            () -> new BlockItem(RegisterBlocks.COGNITIVE_ALLOY_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<Item> COGNITIVE_CRYSTAL_BLOCK_ITEM = ITEMS.register("cognitive_crystal_block",
            () -> new BlockItem(RegisterBlocks.COGNITIVE_CRYSTAL_BLOCK.get(), new Item.Properties()));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
