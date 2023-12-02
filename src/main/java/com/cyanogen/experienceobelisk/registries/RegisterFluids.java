package com.cyanogen.experienceobelisk.registries;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.function.Consumer;

public class RegisterFluids {
    public static final ResourceLocation flowingTexture = new ResourceLocation("experienceobelisk:block/cognitium");
    public static final ResourceLocation stillTexture = new ResourceLocation("experienceobelisk:block/cognitium");

    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ForgeRegistries.FLUIDS, ExperienceObelisk.MOD_ID);

    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, ExperienceObelisk.MOD_ID);

    //registering fluid

    public static final RegistryObject<FlowingFluid> COGNITIUM
            = FLUIDS.register("cognitium", () -> new ForgeFlowingFluid.Source(RegisterFluids.COGNITIUM_PROPERTIES));
    public static final RegistryObject<FlowingFluid> COGNITIUM_FLOWING
            = FLUIDS.register("cognitium_flowing", () -> new ForgeFlowingFluid.Flowing(RegisterFluids.COGNITIUM_PROPERTIES));


    public static final RegistryObject<FluidType> COGNITIUM_FLUID_TYPE = FLUID_TYPES.register("cognitium",
            () -> new FluidType(FluidType.Properties.create()
                    .lightLevel(10)
                    .viscosity(200)
                    .canDrown(false)
                    .canSwim(false)
                    .canPushEntity(false)
                    .canConvertToSource(false)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)){

                @Override
                public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {

                    consumer.accept(new IClientFluidTypeExtensions() {

                        @Override
                        public ResourceLocation getStillTexture() {
                            return stillTexture;
                        }

                        @Override
                        public ResourceLocation getFlowingTexture() {
                            return flowingTexture;
                        }

                        @Override
                        public @NotNull Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                            return new Vector3f(0.2f,1,0.3f);
                        }
                    });

                    super.initializeClient(consumer);
                }
            });


    public static final ForgeFlowingFluid.Properties COGNITIUM_PROPERTIES = new ForgeFlowingFluid.Properties(
            COGNITIUM_FLUID_TYPE,
            COGNITIUM,
            COGNITIUM_FLOWING)
            .bucket(RegisterItems.COGNITIUM_BUCKET)
            .block(RegisterBlocks.COGNITIUM);


public static void register(IEventBus eventBus){
        FLUIDS.register(eventBus);
        FLUID_TYPES.register(eventBus);
    }
}
