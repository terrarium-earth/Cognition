package com.cyanogen.cognition.renderer;

import com.cyanogen.cognition.Cognition;
import com.cyanogen.cognition.item.ExperienceObeliskItem;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class ExperienceObeliskItemRenderer extends GeoItemRenderer<ExperienceObeliskItem> {

    public ExperienceObeliskItemRenderer() {
        super(new GeoModel<>() {
            @Override
            public ResourceLocation getModelResource(ExperienceObeliskItem object) {
                return ResourceLocation.fromNamespaceAndPath(Cognition.MOD_ID, "geo/experience_obelisk.geo.json");
            }

            @Override
            public ResourceLocation getTextureResource(ExperienceObeliskItem object) {
                return ResourceLocation.fromNamespaceAndPath(Cognition.MOD_ID, "textures/custom_models/experience_obelisk.png");
            }

            @Override
            public ResourceLocation getAnimationResource(ExperienceObeliskItem animatable) {
                return ResourceLocation.fromNamespaceAndPath(Cognition.MOD_ID, "animations/experience_obelisk.animation.json");
            }

            @Override
            public RenderType getRenderType(ExperienceObeliskItem animatable, ResourceLocation texture) {
                return RenderType.entityTranslucent(getTextureResource(animatable));
            }
        });
    }
}
