package com.cyanogen.experienceobelisk.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class Config {

    public static class Common{

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> allowedFluids;
        public final ForgeConfigSpec.ConfigValue<Integer> capacity;
        public final ForgeConfigSpec.ConfigValue<Integer> lightLevel;

        public List<String> defaultValues = new ArrayList<>();
        public int defaultCapacity = 100000000;
        public int defaultLightLevel = 10;

        public Common(ForgeConfigSpec.Builder builder){

            defaultValues.add("mob_grinding_utils:fluid_xp");
            defaultValues.add("cofh_core:experience");
            defaultValues.add("industrialforegoing:essence");

            builder.push("Allowed Experience Fluids");
            this.allowedFluids = builder.comment("Add IDs of fluids you want the obelisk to support here in the form mod_id:fluid_name")
                    .define("AllowedFluids", defaultValues);
            builder.pop();

            builder.push("Experience Obelisk Capacity");
            this.capacity = builder.comment("The fluid capacity of the obelisk. Default = 100000000")
                    .define("Capacity", defaultCapacity);
            builder.pop();

            builder.push("Experience Obelisk Light Level");
            this.lightLevel = builder.comment("The light level of the obelisk block. Default = 10")
                    .define("LightLevel", defaultLightLevel);
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
