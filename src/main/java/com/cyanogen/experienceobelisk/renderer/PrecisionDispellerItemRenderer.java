package com.cyanogen.experienceobelisk.renderer;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.cyanogen.experienceobelisk.item.PrecisionDispellerItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class PrecisionDispellerItemRenderer extends GeoItemRenderer<PrecisionDispellerItem>{

    public PrecisionDispellerItemRenderer() {
        super(new AnimatedGeoModel<>() {
            @Override
            public ResourceLocation getModelResource(PrecisionDispellerItem object) {
                return new ResourceLocation(ExperienceObelisk.MOD_ID, "geo/precision_dispeller.geo.json");
            }

            @Override
            public ResourceLocation getTextureResource(PrecisionDispellerItem object) {
                return new ResourceLocation(ExperienceObelisk.MOD_ID, "textures/custom_models/precision_dispeller.png");
            }

            @Override
            public ResourceLocation getAnimationResource(PrecisionDispellerItem animatable) {
                return new ResourceLocation(ExperienceObelisk.MOD_ID, "animations/precision_dispeller.json");
            }
        });
    }

    @Override
    public RenderType getRenderType(PrecisionDispellerItem animatable, float partialTicks, PoseStack stack, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }
}
