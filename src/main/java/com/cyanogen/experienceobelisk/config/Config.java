package com.cyanogen.experienceobelisk.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class Config {

    public static class Common{

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> allowedFluids;
        public final ForgeConfigSpec.ConfigValue<Integer> capacity;
        public final ForgeConfigSpec.ConfigValue<List<String>> allowedExperienceItems;
        public final ForgeConfigSpec.ConfigValue<Double> amuletRange;
        public final ForgeConfigSpec.ConfigValue<Boolean> amuletIgnoresFountainOrbs;
        public final ForgeConfigSpec.ConfigValue<Boolean> amuletIgnoresBookshelfOrbs;
        public final ForgeConfigSpec.ConfigValue<Double> bindingRange;
        public final ForgeConfigSpec.ConfigValue<Boolean> formatting;
        public final ForgeConfigSpec.ConfigValue<Integer> jellyNutrition;
        public final ForgeConfigSpec.ConfigValue<Double> jellySaturation;
        public final ForgeConfigSpec.ConfigValue<Boolean> showAdditionalBowInfo;


        public final ForgeConfigSpec.ConfigValue<Double> dropDustChance;
        public final ForgeConfigSpec.ConfigValue<Boolean> shelvesPermeableToDust;
        public final ForgeConfigSpec.ConfigValue<Boolean> agarPermeableToDust;

        public final ForgeConfigSpec.ConfigValue<Integer> infectedSpawnDelayMin;
        public final ForgeConfigSpec.ConfigValue<Integer> infectedSpawnDelayMax;
        public final ForgeConfigSpec.ConfigValue<Integer> infectedOrbValue;
        public final ForgeConfigSpec.ConfigValue<Integer> infectedSpawns;

        public final ForgeConfigSpec.ConfigValue<Integer> enchantedSpawnDelayMin;
        public final ForgeConfigSpec.ConfigValue<Integer> enchantedSpawnDelayMax;
        public final ForgeConfigSpec.ConfigValue<Integer> enchantedOrbValue;
        public final ForgeConfigSpec.ConfigValue<Integer> enchantedSpawns;

        public final ForgeConfigSpec.ConfigValue<Integer> archiversSpawnDelayMin;
        public final ForgeConfigSpec.ConfigValue<Integer> archiversSpawnDelayMax;
        public final ForgeConfigSpec.ConfigValue<Integer> archiversOrbValue;
        public final ForgeConfigSpec.ConfigValue<Integer> archiversSpawns;

        public final ForgeConfigSpec.ConfigValue<Double> agarFaceBonus;
        public final ForgeConfigSpec.ConfigValue<Double> agarEdgeBonus;
        public final ForgeConfigSpec.ConfigValue<Double> agarVertexBonus;
        public final ForgeConfigSpec.ConfigValue<Boolean> agarEmitsLight;

        public final List<String> defaultAllowedFluids = new ArrayList<>();
        public List<String> defaultAllowedExperienceItems = new ArrayList<>();

        public Common(ForgeConfigSpec.Builder builder){

            defaultAllowedFluids.add("mob_grinding_utils:fluid_xp");
            defaultAllowedFluids.add("cofh_core:experience");
            defaultAllowedFluids.add("industrialforegoing:essence");
            defaultAllowedFluids.add("sophisticatedcore:xp_still");
            defaultAllowedFluids.add("enderio:xp_juice");

            defaultAllowedExperienceItems.add("forbidden_arcanus:xpetrified_orb = 91.0f");
            defaultAllowedExperienceItems.add("mysticalagriculture:experience_droplet = 12.5f");
            defaultAllowedExperienceItems.add("actuallyadditions:solidified_experience = 8.0f");

            builder.push("Allowed Experience Fluids");
            this.allowedFluids = builder.comment("Add IDs of fluids you want the obelisk to support here in the form mod_id:fluid_name. Fluids have to be tagged forge:experience.")
                    .define("AllowedFluids", defaultAllowedFluids);
            builder.pop();

            builder.push("Experience Obelisk Capacity");
            this.capacity = builder.comment("The fluid capacity of the obelisk in mB. Default = 100000000, which is ~1072 levels' worth. Ensure that the new value is divisible by 20.")
                    .comment("Warning: setting this value above the default may lead to unintended loss or gain of XP. This is due to a rounding error in Minecraft's XP handling")
                    .defineInRange("Capacity", 100000000, 1000, 2147483640);
            builder.pop();

            builder.push("Allowed Experience Items");
            this.allowedExperienceItems = builder.comment("Add IDs of items you want the fountain to support here in the form mod_id:item_name = value in experience points.")
                    .comment("Whitelisted items will be able to be converted to Cognitium by right-clicking / shift-right-clicking a bound fountain.")
                    .comment("This list has to be identical on both client and server for valid items to be viewed in JEI")
                    .define("AllowedItems", defaultAllowedExperienceItems);
            builder.pop();

            builder.push("Enlightened Amulet");
            this.amuletRange = builder.comment("The range of the enlightened amulet in blocks. Accepts decimals. Default = 8.0.")
                    .defineInRange("Range", 8.0, 1, 16.0);
            this.amuletIgnoresFountainOrbs = builder.comment("Whether the enlightened amulet ignores orbs spawned by Experience Fountains. Default = true")
                    .define("Ignores", true);
            this.amuletIgnoresBookshelfOrbs = builder.comment("Whether the enlightened amulet ignores orbs spawned by Infected Bookshelves. Default = false")
                    .define("IgnoresBookshelfOrbs", false);
            builder.pop();

            builder.push("Staff of Attunement Range");
            this.bindingRange = builder.comment("The binding range of the Staff of Attunement & Memory Tablet in blocks. Accepts decimals. Default = 48.0.")
                    .comment("Lower this if you experience issues with bound blocks not working correctly.")
                    .defineInRange("Range", 48.0, 4, 100.0);
            builder.pop();

            builder.push("Metamorpher Name Formatting Recipes");
            this.formatting = builder.comment("Whether custom recipes that allow for the changing of item name color & formatting are enabled. Default = true")
                    .define("Formatting", true);
            builder.pop();

            builder.push("Fluorescent Jelly");
            this.jellyNutrition = builder.comment("How many hunger points Fluorescent Jelly gives the player. Set to 0 to disable eating completely. Default = 2")
                    .define("Nutrition", 2);
            this.jellySaturation = builder.comment("How much saturation Fluorescent Jelly gives the player. Default = 1.0")
                    .define("Saturation", 1.0);
            builder.pop();

            builder.push("Cognitive Bow");
            this.showAdditionalBowInfo = builder.comment("Whether to show the projectile velocity & accuracy boost in the tooltip. Default = true")
                    .comment("Disable this if tooltip components are being displayed in the wrong order.")
                    .define("Show", true);
            builder.pop();

            builder.push("Bookshelf Settings");
            this.dropDustChance = builder.comment("The chance that infected bookshelves of any kind drop Forgotten Dust upon decaying. Default = 0.5")
                    .comment("Set this value to 0.0 to prevent drops from decaying bookshelves completely")
                    .defineInRange("DropDustChance", 0.5, 0.0, 1.0);
            this.shelvesPermeableToDust = builder.comment("Whether Infected Bookshelves are permeable to Forgotten Dust item entities. Default = false")
                    .define("ShelvesPermeableToDust", false);

            builder.push("Infected Bookshelves");
            this.infectedSpawnDelayMin = builder.comment("The minimum spawn delay of Infected Bookshelves in ticks. Default = 300")
                    .defineInRange("SpawnDelayMin", 300, 1, 10000);
            this.infectedSpawnDelayMax = builder.comment("The maximum spawn delay of Infected Bookshelves in ticks. Default = 500")
                    .defineInRange("SpawnDelayMax", 500, 1, 10000);
            this.infectedOrbValue = builder.comment("The XP value of spawned orbs. Default = 12")
                    .defineInRange("OrbValue", 12, 1, 32767);
            this.infectedSpawns = builder.comment("The number of spawns until the bookshelf decays. Default = 50")
                    .defineInRange("Spawns", 50, 1, 10000);
            builder.pop();

            builder.push("Infected Enchanted Bookshelves");
            this.enchantedSpawnDelayMin = builder.comment("The minimum spawn delay of Enchanted Bookshelves in ticks. Default = 100")
                    .defineInRange("SpawnDelayMin", 300, 1, 10000);
            this.enchantedSpawnDelayMax = builder.comment("The maximum spawn delay of Enchanted Bookshelves in ticks. Default = 300")
                    .defineInRange("SpawnDelayMax", 500, 1, 10000);
            this.enchantedOrbValue = builder.comment("The XP value of spawned orbs. Default = 24")
                    .defineInRange("OrbValue", 24, 1, 32767);
            this.enchantedSpawns = builder.comment("The number of spawns until the bookshelf decays. Default = 100")
                    .defineInRange("Spawns", 100, 1, 10000);
            builder.pop();

            builder.push("Infected Archiver's Bookshelves");
            this.archiversSpawnDelayMin = builder.comment("The minimum spawn delay of Archiver's Bookshelves in ticks. Default = 180")
                    .defineInRange("SpawnDelayMin", 300, 1, 10000);
            this.archiversSpawnDelayMax = builder.comment("The maximum spawn delay of Archiver's Bookshelves in ticks. Default = 220")
                    .defineInRange("SpawnDelayMax", 500, 1, 10000);
            this.archiversOrbValue = builder.comment("The XP value of spawned orbs. Default = 12")
                    .defineInRange("OrbValue", 12, 1, 32767);
            this.archiversSpawns = builder.comment("The number of spawns until the bookshelf decays. Default = 50")
                    .defineInRange("Spawns", 100, 1, 10000);
            builder.pop();
            builder.pop();

            builder.push("Agar Settings");
            this.agarFaceBonus = builder.comment("The bonus that Insightful & Extravagant Agar apply to bookshelves sharing a face. Default = 1.35")
                    .defineInRange("AgarFaceBonus", 1.35, 0.0, 4);
            this.agarEdgeBonus = builder.comment("The bonus that Insightful & Extravagant Agar apply to bookshelves sharing an edge. Default = 1.15")
                    .defineInRange("AgarEdgeBonus", 1.15, 0.0, 4);
            this.agarVertexBonus = builder.comment("The bonus that Insightful & Extravagant Agar apply to bookshelves sharing a vertex. Default = 1.10")
                    .defineInRange("AgarVertexBonus", 1.10, 0.0, 4);
            this.agarEmitsLight = builder.comment("Whether or not Agar blocks emit light. Default = true.")
                    .comment("Set this to false if you are using intensive shader settings and are experiencing fps drops.")
                    .define("AgarEmitsLight", true);
            this.agarPermeableToDust = builder.comment("Whether Agar blocks are permeable to Forgotten Dust item entities. Default = false")
                    .define("ShelvesPermeableToDust", false);
            builder.pop();
        }

    }

    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static
    {
        Pair<Common, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON = commonSpecPair.getLeft();
        COMMON_SPEC = commonSpecPair.getRight();
    }
}
