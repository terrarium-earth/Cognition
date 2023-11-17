package com.cyanogen.experienceobelisk.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class Config {

    public static class Common{

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> allowedFluids;
        public final ForgeConfigSpec.ConfigValue<Integer> capacity;
        public final ForgeConfigSpec.ConfigValue<Double> range;

        public List<String> defaultValues = new ArrayList<>();
        public int defaultCapacity = 100000000;
        public double defaultRange = 8.0;

        public Common(ForgeConfigSpec.Builder builder){

            defaultValues.add("mob_grinding_utils:fluid_xp");
            defaultValues.add("cofh_core:experience");
            defaultValues.add("industrialforegoing:essence");
            defaultValues.add("sophisticatedcore:xp_still");
            defaultValues.add("enderio:xp_juice");

            builder.push("Allowed Experience Fluids");
            this.allowedFluids = builder.comment("Add IDs of fluids you want the obelisk to support here in the form mod_id:fluid_name. Fluids have to be tagged forge:experience")
                    .define("AllowedFluids", defaultValues);
            builder.pop();

            builder.push("Experience Obelisk Capacity");
            this.capacity = builder.comment("The fluid capacity of the obelisk in mB. Default = 100000000. This is ~1072 levels' worth.")
                    .defineInRange("Capacity", defaultCapacity, 20, 2147483640);
            builder.pop();

            builder.push("Enlightened Amulet Range");
            this.range = builder.comment("The range of the enlightened amulet. Accepts decimals. Default = 8.0. Max = 32.0")
                    .defineInRange("Range", defaultRange, 1, 32.0);
            builder.pop();

        }

    }

    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static //constructor
    {
        Pair<Common, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON = commonSpecPair.getLeft();
        COMMON_SPEC = commonSpecPair.getRight();
    }
}
