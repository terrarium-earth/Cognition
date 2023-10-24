package com.cyanogen.experienceobelisk.renderer;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.cyanogen.experienceobelisk.item.ExperienceFountainItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class ExperienceFountainItemRenderer extends GeoItemRenderer<ExperienceFountainItem> {

    public ExperienceFountainItemRenderer() {
        super(new AnimatedGeoModel<>() {
            @Override
            public ResourceLocation getModelLocation(ExperienceFountainItem object) {
                return new ResourceLocation(ExperienceObelisk.MOD_ID, "geo/experience_fountain_experimental.geo.json");
            }

            @Override
            public ResourceLocation getTextureLocation(ExperienceFountainItem object) {
                return new ResourceLocation(ExperienceObelisk.MOD_ID, "textures/custom_models/experience_fountain.png");
            }

            @Override
            public ResourceLocation getAnimationFileLocation(ExperienceFountainItem animatable) {
                return new ResourceLocation(ExperienceObelisk.MOD_ID, "animations/experience_fountain.json");
            }
        });
    }

    @Override
    public RenderType getRenderType(ExperienceFountainItem animatable, float partialTicks, PoseStack stack, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }
}
