package com.cyanogen.cognition.registries;

import com.cyanogen.cognition.Cognition;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class RegisterFluids {

    public static final ResourceLocation flowingTexture = ResourceLocation.fromNamespaceAndPath("cognition","block/cognitium_flow");
    public static final ResourceLocation stillTexture = ResourceLocation.fromNamespaceAndPath("cognition","block/cognitium_still");
    public static final ResourceLocation overlayTexture = ResourceLocation.fromNamespaceAndPath("cognition","block/cognitium_overlay");

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(BuiltInRegistries.FLUID, Cognition.MOD_ID);
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, Cognition.MOD_ID);

    public static final DeferredHolder<FluidType, CognitiumFluidType> COGNITIUM_FLUID_TYPE = FLUID_TYPES.register("cognitium", CognitiumFluidType::new);
    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> COGNITIUM_SOURCE = FLUIDS.register("cognitium_source",
            () -> new BaseFlowingFluid.Source(RegisterFluids.COGNITIUM_PROPERTIES));
    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> COGNITIUM_FLOWING = FLUIDS.register("cognitium_flowing",
            () -> new BaseFlowingFluid.Flowing(RegisterFluids.COGNITIUM_PROPERTIES));

    public static class CognitiumFluidType extends FluidType implements IClientFluidTypeExtensions {

        public CognitiumFluidType() {
            super(FluidType.Properties.create().lightLevel(10)
                    .viscosity(200)
                    .density(200)
                    .temperature(450)
                    .canDrown(false)
                    .canSwim(false)
                    .canPushEntity(false)
                    .canConvertToSource(false)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY));
        }

        @Override
        public ResourceLocation getFlowingTexture() {
            return flowingTexture;
        }

        @Override
        public ResourceLocation getStillTexture() {
            return stillTexture;
        }

    }

    public static final BaseFlowingFluid.Properties COGNITIUM_PROPERTIES = new BaseFlowingFluid.Properties(
            COGNITIUM_FLUID_TYPE,
            COGNITIUM_SOURCE,
            COGNITIUM_FLOWING)
            .bucket(RegisterItems.COGNITIUM_BUCKET)
            .block(RegisterBlocks.COGNITIUM);

    public static void register(IEventBus eventBus){
        FLUIDS.register(eventBus);
        FLUID_TYPES.register(eventBus);
    }

}
