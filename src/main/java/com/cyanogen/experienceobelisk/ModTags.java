package com.cyanogen.experienceobelisk;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

public class ModTags {

    public static class Fluids{
        public static final TagKey<Fluid> EXPERIENCE = TagKey.create(Registry.FLUID_REGISTRY, new ResourceLocation("forge", "experience"));

    }
}
