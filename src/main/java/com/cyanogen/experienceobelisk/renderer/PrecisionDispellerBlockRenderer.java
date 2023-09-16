package com.cyanogen.experienceobelisk.renderer;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.cyanogen.experienceobelisk.block_entities.ExperienceObeliskEntity;
import com.cyanogen.experienceobelisk.block_entities.PrecisionDispellerEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class PrecisionDispellerBlockRenderer extends GeoBlockRenderer<PrecisionDispellerEntity> {

    public PrecisionDispellerBlockRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
        super(rendererDispatcherIn, new AnimatedGeoModel<>() {

            @Override
            public ResourceLocation getModelLocation(PrecisionDispellerEntity animatable) {
                return new ResourceLocation(ExperienceObelisk.MOD_ID, "geo/precision_dispeller.geo.json");
            }

            @Override
            public ResourceLocation getTextureLocation(PrecisionDispellerEntity entity) {
                return new ResourceLocation(ExperienceObelisk.MOD_ID, "textures/custom_models/precision_dispeller.png");
            }

            @Override
            public ResourceLocation getAnimationFileLocation(PrecisionDispellerEntity animatable) {
                return new ResourceLocation(ExperienceObelisk.MOD_ID, "animations/precision_dispeller.json");
            }
        });
    }

    @Override
    public RenderType getRenderType(PrecisionDispellerEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }

}

