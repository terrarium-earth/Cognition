package com.cyanogen.experienceobelisk.registries;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class RegisterTags {

    public static class Fluids{
        public static final TagKey<Fluid> EXPERIENCE = TagKey.create(Registries.FLUID, new ResourceLocation("forge", "experience"));
    }

    public static class Blocks{
        public static final TagKey<Block> INFECTIVE_BLOCKS =
                TagKey.create(Registries.BLOCK, new ResourceLocation(ExperienceObelisk.MOD_ID, "infective_blocks"));
    }
}
