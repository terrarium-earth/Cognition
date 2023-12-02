package com.cyanogen.experienceobelisk.renderer;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.cyanogen.experienceobelisk.block_entities.PrecisionDispellerEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class PrecisionDispellerBlockRenderer extends GeoBlockRenderer<PrecisionDispellerEntity> {

    public PrecisionDispellerBlockRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
        super(new GeoModel<>() {

            @Override
            public ResourceLocation getModelResource(PrecisionDispellerEntity animatable) {
                return new ResourceLocation(ExperienceObelisk.MOD_ID, "geo/precision_dispeller.geo.json");
            }

            @Override
            public ResourceLocation getTextureResource(PrecisionDispellerEntity entity) {
                return new ResourceLocation(ExperienceObelisk.MOD_ID, "textures/custom_models/precision_dispeller.png");
            }

            @Override
            public ResourceLocation getAnimationResource(PrecisionDispellerEntity animatable) {
                return new ResourceLocation(ExperienceObelisk.MOD_ID, "animations/precision_dispeller.json");
            }

            @Override
            public RenderType getRenderType(PrecisionDispellerEntity animatable, ResourceLocation texture) {
                return RenderType.entityTranslucent(getTextureResource(animatable));
            }
        });
    }

}

