package com.cyanogen.cognition.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class Config {

    public static class Common{

        public final ModConfigSpec.ConfigValue<List<String>> allowedFluids;
        public final ModConfigSpec.ConfigValue<Integer> capacity;
        public final ModConfigSpec.ConfigValue<Double> amuletRange;
        public final ModConfigSpec.ConfigValue<Boolean> amuletIgnoresFountainOrbs;
        public final ModConfigSpec.ConfigValue<Boolean> amuletIgnoresBookshelfOrbs;
        public final ModConfigSpec.ConfigValue<Double> bindingRange;
        public final ModConfigSpec.ConfigValue<Boolean> formatting;
        public final ModConfigSpec.ConfigValue<Integer> jellyNutrition;
        public final ModConfigSpec.ConfigValue<Double> jellySaturation;
        public final ModConfigSpec.ConfigValue<Boolean> showAdditionalBowInfo;

        public final ModConfigSpec.ConfigValue<Double> dropDustChance;
        public final ModConfigSpec.ConfigValue<Boolean> shelvesPermeableToDust;

        public final ModConfigSpec.ConfigValue<Integer> infectedSpawnDelayMin;
        public final ModConfigSpec.ConfigValue<Integer> infectedSpawnDelayMax;
        public final ModConfigSpec.ConfigValue<Integer> infectedOrbValue;
        public final ModConfigSpec.ConfigValue<Integer> infectedSpawns;
        public final ModConfigSpec.ConfigValue<Float> infectedInfectivity;

        public final ModConfigSpec.ConfigValue<Integer> enchantedSpawnDelayMin;
        public final ModConfigSpec.ConfigValue<Integer> enchantedSpawnDelayMax;
        public final ModConfigSpec.ConfigValue<Integer> enchantedOrbValue;
        public final ModConfigSpec.ConfigValue<Integer> enchantedSpawns;
        public final ModConfigSpec.ConfigValue<Float> enchantedInfectivity;

        public final ModConfigSpec.ConfigValue<Integer> archiversSpawnDelayMin;
        public final ModConfigSpec.ConfigValue<Integer> archiversSpawnDelayMax;
        public final ModConfigSpec.ConfigValue<Integer> archiversOrbValue;
        public final ModConfigSpec.ConfigValue<Integer> archiversSpawns;
        public final ModConfigSpec.ConfigValue<Float> archiversInfectivity;

        public final ModConfigSpec.ConfigValue<Double> agarFaceBonus;
        public final ModConfigSpec.ConfigValue<Double> agarEdgeBonus;
        public final ModConfigSpec.ConfigValue<Double> agarVertexBonus;
        public final ModConfigSpec.ConfigValue<Boolean> agarEmitsLight;
        public final ModConfigSpec.ConfigValue<Boolean> agarPermeableToDust;

        public List<String> defaultAllowedFluids = new ArrayList<>();

        public Common(ModConfigSpec.Builder builder){

            defaultAllowedFluids.add("mob_grinding_utils:fluid_xp");
            defaultAllowedFluids.add("cofh_core:experience");
            defaultAllowedFluids.add("industrialforegoing:essence");
            defaultAllowedFluids.add("sophisticatedcore:xp_still");
            defaultAllowedFluids.add("enderio:fluid_xp_juice_still");
            defaultAllowedFluids.add("reliquary:xp_still");

            builder.push("Allowed Experience Fluids");
            this.allowedFluids = builder.comment("Add IDs of fluids you want the obelisk to support here in the form mod_id:fluid_name. Fluids have to be tagged forge:experience.")
                    .define("AllowedFluids", defaultAllowedFluids);
            builder.pop();

            builder.push("Experience Obelisk Capacity");
            this.capacity = builder.comment("The fluid capacity of the obelisk in mB. Default = 100000000, which is ~1072 levels' worth. Ensure that the new value is divisible by 20.")
                    .comment("Warning: setting this value above the default may lead to unintended loss or gain of XP. This is due to a rounding error in Minecraft's XP handling")
                    .defineInRange("Capacity", 100000000, 1000, 2147483640);
            builder.pop();

            builder.push("Enlightened Amulet");
            this.amuletRange = builder.comment("The range of the enlightened amulet in blocks. Accepts decimals.")
                    .defineInRange("Range", 8.0, 1, 16.0);
            this.amuletIgnoresFountainOrbs = builder.comment("Whether the enlightened amulet ignores orbs spawned by Experience Fountains.")
                    .define("IgnoresFountainOrbs", true);
            this.amuletIgnoresBookshelfOrbs = builder.comment("Whether the enlightened amulet ignores orbs spawned by Infected Bookshelves.")
                    .define("IgnoresBookshelfOrbs", false);
            builder.pop();

            builder.push("Binding Range");
            this.bindingRange = builder.comment("The binding range of the Staff of Attunement & Memory Tablet in blocks. Accepts decimals.")
                    .comment("Lower this if you experience issues with bound blocks not working correctly.")
                    .defineInRange("Range", 48.0, 4, 100.0);
            builder.pop();

            builder.push("Enable Name Formatting Recipes");
            this.formatting = builder.comment("Whether custom recipes that allow for the changing of item name color & formatting are enabled.")
                    .define("Formatting", true);
            builder.pop();

            builder.push("Fluorescent Jelly");
            this.jellyNutrition = builder.comment("How many hunger points Fluorescent Jelly gives the player. Set to 0 to disable eating completely.")
                    .define("Nutrition", 2);
            this.jellySaturation = builder.comment("How much saturation Fluorescent Jelly gives the player.")
                    .define("Saturation", 1.0);
            builder.pop();

            builder.push("Cognitive Bow");
            this.showAdditionalBowInfo = builder.comment("Whether to show the projectile velocity & accuracy boost in the tooltip.")
                    .comment("Disable this if tooltip components are being displayed in the wrong order.")
                    .define("Show", true);
            builder.pop();

            builder.push("Bookshelf Settings");
            this.dropDustChance = builder.comment("The chance that infected bookshelves of any kind drop Forgotten Dust upon decaying.")
                    .comment("Set this value to 0.0 to prevent drops from decaying bookshelves completely")
                    .defineInRange("DropDustChance", 0.5, 0.0, 1.0);
            this.shelvesPermeableToDust = builder.comment("Whether Infected Bookshelves are permeable to Forgotten Dust item entities.")
                    .define("ShelvesPermeableToDust", false);

            builder.push("Infected Bookshelves");
            this.infectedSpawnDelayMin = builder.comment("The minimum spawn delay of Infected Bookshelves in ticks.")
                    .defineInRange("SpawnDelayMin", 300, 1, 10000);
            this.infectedSpawnDelayMax = builder.comment("The maximum spawn delay of Infected Bookshelves in ticks.")
                    .defineInRange("SpawnDelayMax", 500, 1, 10000);
            this.infectedOrbValue = builder.comment("The XP value of spawned orbs.")
                    .defineInRange("OrbValue", 12, 1, 32767);
            this.infectedSpawns = builder.comment("The number of spawns until the bookshelf decays.")
                    .defineInRange("Spawns", 50, 1, 10000);
            this.infectedInfectivity = builder.comment("The chance for an Infected Bookshelf to infect a valid adjacent block every second.")
                    .define("Infectivity", 0.02f);
            builder.pop();

            builder.push("Infected Enchanted Bookshelves");
            this.enchantedSpawnDelayMin = builder.comment("The minimum spawn delay of Enchanted Bookshelves in ticks.")
                    .defineInRange("SpawnDelayMin", 300, 1, 10000);
            this.enchantedSpawnDelayMax = builder.comment("The maximum spawn delay of Enchanted Bookshelves in ticks.")
                    .defineInRange("SpawnDelayMax", 500, 1, 10000);
            this.enchantedOrbValue = builder.comment("The XP value of spawned orbs.")
                    .defineInRange("OrbValue", 24, 1, 32767);
            this.enchantedSpawns = builder.comment("The number of spawns until the bookshelf decays.")
                    .defineInRange("Spawns", 100, 1, 10000);
            this.enchantedInfectivity = builder.comment("The chance for an Enchanted Bookshelf to infect a valid adjacent block every second.")
                    .define("Infectivity", 0.02f);
            builder.pop();

            builder.push("Infected Archiver's Bookshelves");
            this.archiversSpawnDelayMin = builder.comment("The minimum spawn delay of Archiver's Bookshelves in ticks.")
                    .defineInRange("SpawnDelayMin", 300, 1, 10000);
            this.archiversSpawnDelayMax = builder.comment("The maximum spawn delay of Archiver's Bookshelves in ticks.")
                    .defineInRange("SpawnDelayMax", 500, 1, 10000);
            this.archiversOrbValue = builder.comment("The XP value of spawned orbs.")
                    .defineInRange("OrbValue", 12, 1, 32767);
            this.archiversSpawns = builder.comment("The number of spawns until the bookshelf decays.")
                    .defineInRange("Spawns", 100, 1, 10000);
            this.archiversInfectivity = builder.comment("The chance for an Archiver's Bookshelf to infect a valid adjacent block every second.")
                    .define("Infectivity", 0.02f);
            builder.pop();
            builder.pop();

            builder.push("Agar Settings");
            this.agarFaceBonus = builder.comment("The bonus that Insightful & Extravagant Agar apply to bookshelves sharing a face.")
                    .defineInRange("AgarFaceBonus", 1.35, 0.0, 4);
            this.agarEdgeBonus = builder.comment("The bonus that Insightful & Extravagant Agar apply to bookshelves sharing an edge.")
                    .defineInRange("AgarEdgeBonus", 1.15, 0.0, 4);
            this.agarVertexBonus = builder.comment("The bonus that Insightful & Extravagant Agar apply to bookshelves sharing a vertex.")
                    .defineInRange("AgarVertexBonus", 1.10, 0.0, 4);
            this.agarEmitsLight = builder.comment("Whether Agar blocks emit light. Set this to false if you are using intensive shader settings and are experiencing fps drops.")
                    .define("AgarEmitsLight", true);
            this.agarPermeableToDust = builder.comment("Whether Agar blocks are permeable to Forgotten Dust item entities.")
                    .define("AgarPermeableToDust", false);
            builder.pop();
        }

    }

    public static final Common COMMON;
    public static final ModConfigSpec COMMON_SPEC;

    static
    {
        Pair<Common, ModConfigSpec> commonSpecPair = new ModConfigSpec.Builder().configure(Common::new);
        COMMON = commonSpecPair.getLeft();
        COMMON_SPEC = commonSpecPair.getRight();
    }
}
