package com.cyanogen.experienceobelisk.renderer;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.cyanogen.experienceobelisk.block_entities.ExperienceFountainEntity;
import com.cyanogen.experienceobelisk.block_entities.ExperienceObeliskEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class ExperienceFountainBlockRenderer extends GeoBlockRenderer<ExperienceFountainEntity> {

    public ExperienceFountainBlockRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
        super(rendererDispatcherIn, new AnimatedGeoModel<ExperienceFountainEntity>() {

            @Override
            public ResourceLocation getModelLocation(ExperienceFountainEntity animatable) {
                return new ResourceLocation(ExperienceObelisk.MOD_ID, "geo/experience_fountain.geo.json");
            }

            @Override
            public ResourceLocation getTextureLocation(ExperienceFountainEntity entity) {
                return new ResourceLocation(ExperienceObelisk.MOD_ID, "textures/custom_models/experience_fountain.png");
            }

            @Override
            public ResourceLocation getAnimationFileLocation(ExperienceFountainEntity animatable) {
                return new ResourceLocation(ExperienceObelisk.MOD_ID, "animations/experience_fountain.json");
            }
        });
    }

    @Override
    public RenderType getRenderType(ExperienceFountainEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }

}

