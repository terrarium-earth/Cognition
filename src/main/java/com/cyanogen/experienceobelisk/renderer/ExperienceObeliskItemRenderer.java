package com.cyanogen.experienceobelisk.renderer;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.cyanogen.experienceobelisk.item.ExperienceObeliskItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class ExperienceObeliskItemRenderer extends GeoItemRenderer<ExperienceObeliskItem> {

    public ExperienceObeliskItemRenderer() {
        super(new AnimatedGeoModel<>() {
            @Override
            public ResourceLocation getModelLocation(ExperienceObeliskItem object) {
                return new ResourceLocation(ExperienceObelisk.MOD_ID, "geo/experience_obelisk.geo.json");
            }

            @Override
            public ResourceLocation getTextureLocation(ExperienceObeliskItem object) {
                return new ResourceLocation(ExperienceObelisk.MOD_ID, "textures/custom_models/experience_obelisk.png");
            }

            @Override
            public ResourceLocation getAnimationFileLocation(ExperienceObeliskItem animatable) {
                return new ResourceLocation(ExperienceObelisk.MOD_ID, "animations/experience_obelisk.json");
            }
        });
    }

    @Override
    public RenderType getRenderType(ExperienceObeliskItem animatable, float partialTicks, PoseStack stack, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }
}
