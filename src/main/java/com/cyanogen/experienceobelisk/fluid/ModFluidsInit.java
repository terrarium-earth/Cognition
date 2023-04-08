package com.cyanogen.experienceobelisk.fluid;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.cyanogen.experienceobelisk.block.ModBlocksInit;
import com.cyanogen.experienceobelisk.item.ModItemsInit;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFluidsInit {
    public static final ResourceLocation flowingTexture = new ResourceLocation("experienceobelisk:custom_models/green");
    public static final ResourceLocation stillTexture = new ResourceLocation("experienceobelisk:custom_models/green");
    public static final ResourceLocation overlay = new ResourceLocation("block/water_overlay");

    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ForgeRegistries.FLUIDS, ExperienceObelisk.MOD_ID);

    //registering fluid

    //legacy
    public static final RegistryObject<FlowingFluid> RAW_EXPERIENCE
            = FLUIDS.register("raw_experience", () -> new ForgeFlowingFluid.Source(ModFluidsInit.RAW_EXPERIENCE_PROPERTIES));
    public static final RegistryObject<FlowingFluid> RAW_EXPERIENCE_FLOWING
            = FLUIDS.register("raw_experience_flowing", () -> new ForgeFlowingFluid.Flowing(ModFluidsInit.RAW_EXPERIENCE_PROPERTIES));

    //new
    public static final RegistryObject<FlowingFluid> COGNITIVE_ESSENCE
            = FLUIDS.register("cognitive_essence", () -> new ForgeFlowingFluid.Source(ModFluidsInit.COGNITIVE_ESSENCE_PROPERTIES));
    public static final RegistryObject<FlowingFluid> COGNITIVE_ESSENCE_FLOWING
            = FLUIDS.register("cognitive_essence_flowing", () -> new ForgeFlowingFluid.Flowing(ModFluidsInit.COGNITIVE_ESSENCE_PROPERTIES));

    //setting properties for raw experience fluid
    public static final ForgeFlowingFluid.Properties RAW_EXPERIENCE_PROPERTIES = new ForgeFlowingFluid.Properties(
            RAW_EXPERIENCE,
            RAW_EXPERIENCE_FLOWING,
            FluidAttributes.builder(flowingTexture, stillTexture)
                    .density(1000)
                    .luminosity(10)
                    .viscosity(1000)
                    .temperature(300)
                    .sound(SoundEvents.BUCKET_FILL, SoundEvents.BUCKET_EMPTY)
                    .overlay(overlay))
            .bucket(ModItemsInit.RAW_EXPERIENCE_BUCKET)
            .block(ModBlocksInit.RAW_EXPERIENCE);

    public static final ForgeFlowingFluid.Properties COGNITIVE_ESSENCE_PROPERTIES = new ForgeFlowingFluid.Properties(
            COGNITIVE_ESSENCE,
            COGNITIVE_ESSENCE_FLOWING,
            FluidAttributes.builder(flowingTexture, stillTexture)
                    .density(1000)
                    .luminosity(10)
                    .viscosity(1000)
                    .temperature(300)
                    .sound(SoundEvents.BUCKET_FILL, SoundEvents.BUCKET_EMPTY)
                    .overlay(overlay))
            .bucket(ModItemsInit.COGNITIVE_ESSENCE_BUCKET)
            .block(ModBlocksInit.COGNITIVE_ESSENCE);


public static void register(IEventBus eventBus){
        FLUIDS.register(eventBus);
    }
}
