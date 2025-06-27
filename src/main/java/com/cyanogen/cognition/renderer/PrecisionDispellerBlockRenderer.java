package com.cyanogen.cognition.renderer;

import com.cyanogen.cognition.Cognition;
import com.cyanogen.cognition.block_entities.PrecisionDispellerEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class PrecisionDispellerBlockRenderer extends GeoBlockRenderer<PrecisionDispellerEntity> {

    public PrecisionDispellerBlockRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
        super(new GeoModel<>() {

            @Override
            public ResourceLocation getModelResource(PrecisionDispellerEntity animatable) {
                return ResourceLocation.fromNamespaceAndPath(Cognition.MOD_ID, "geo/precision_dispeller.geo.json");
            }

            @Override
            public ResourceLocation getTextureResource(PrecisionDispellerEntity entity) {
                return ResourceLocation.fromNamespaceAndPath(Cognition.MOD_ID, "textures/custom_models/precision_dispeller.png");
            }

            @Override
            public ResourceLocation getAnimationResource(PrecisionDispellerEntity animatable) {
                return ResourceLocation.fromNamespaceAndPath(Cognition.MOD_ID, "animations/precision_dispeller.animation.json");
            }

            @Override
            public RenderType getRenderType(PrecisionDispellerEntity animatable, ResourceLocation texture) {
                return RenderType.entityTranslucent(getTextureResource(animatable));
            }
        });
    }

}

