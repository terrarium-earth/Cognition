package com.cyanogen.experienceobelisk.block_entities;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class ExperienceObeliskTileRenderer extends GeoBlockRenderer<ExperienceObeliskEntity> {
    public ExperienceObeliskTileRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
        super(rendererDispatcherIn, new AnimatedGeoModel<ExperienceObeliskEntity>() {
            //render setup for geckolib animated model
            @Override
            public ResourceLocation getModelLocation(ExperienceObeliskEntity animatable) {
                return new ResourceLocation(ExperienceObelisk.MOD_ID, "geo/experience_obelisk.geo.json");
            }

            @Override
            public ResourceLocation getTextureLocation(ExperienceObeliskEntity entity) {
                return new ResourceLocation(ExperienceObelisk.MOD_ID, "textures/custom_models/experience_obelisk.png");
            }

            @Override
            public ResourceLocation getAnimationFileLocation(ExperienceObeliskEntity animatable) {
                return new ResourceLocation(ExperienceObelisk.MOD_ID, "animations/experience_obelisk.json");
            }
        });
    }

    @Override
    public RenderType getRenderType(ExperienceObeliskEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }

}

