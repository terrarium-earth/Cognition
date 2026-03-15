package com.cyanogen.cognition.registries;

import com.cyanogen.cognition.Cognition;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class RegisterTags {

    public static class Items{
        public static final TagKey<Item> COGNITIVE_SET =
                TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Cognition.MOD_ID, "cognitive_set"));
        public static final TagKey<Item> NEUROGEL_BLACKLISTED =
                TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Cognition.MOD_ID, "neurogel_blacklisted"));
    }

    public static class Fluids{
        public static final TagKey<Fluid> EXPERIENCE =
                TagKey.create(Registries.FLUID, ResourceLocation.fromNamespaceAndPath("c", "experience"));
    }

    public static class Blocks{
        public static final TagKey<Block> INFECTIVE_BLOCKS =
                TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Cognition.MOD_ID, "infective_blocks"));
    }

}
