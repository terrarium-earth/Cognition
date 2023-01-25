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

public class XPObeliskTileRenderer extends GeoBlockRenderer<XPObeliskEntity> {
    public XPObeliskTileRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
        super(rendererDispatcherIn, new AnimatedGeoModel<XPObeliskEntity>() {
            //render setup for geckolib animated model
            @Override
            public ResourceLocation getModelLocation(XPObeliskEntity animatable) {
                return new ResourceLocation(ExperienceObelisk.MOD_ID, "geo/xpobelisk.geo.json");
            }

            @Override
            public ResourceLocation getTextureLocation(XPObeliskEntity entity) {
                return new ResourceLocation(ExperienceObelisk.MOD_ID, "textures/custom_models/wholetexture.png");
            }

            @Override
            public ResourceLocation getAnimationFileLocation(XPObeliskEntity animatable) {
                return new ResourceLocation(ExperienceObelisk.MOD_ID, "animations/xpobelisk.anim.json");
            }
        });
    }

    @Override
    public RenderType getRenderType(XPObeliskEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }

}

