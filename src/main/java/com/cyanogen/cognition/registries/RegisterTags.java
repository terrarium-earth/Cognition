package com.cyanogen.cognition.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

public class RegisterTags {

    public static class Fluids{
        public static final TagKey<Fluid> EXPERIENCE =
                TagKey.create(Registries.FLUID, ResourceLocation.fromNamespaceAndPath("c", "experience"));
    }


}
