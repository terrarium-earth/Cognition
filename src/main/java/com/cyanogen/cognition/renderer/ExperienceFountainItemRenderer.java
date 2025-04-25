package com.cyanogen.cognition.renderer;

import com.cyanogen.cognition.Cognition;
import com.cyanogen.cognition.item.ExperienceFountainItem;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class ExperienceFountainItemRenderer extends GeoItemRenderer<ExperienceFountainItem> {

    public ExperienceFountainItemRenderer() {
        super(new GeoModel<>() {
            @Override
            public ResourceLocation getModelResource(ExperienceFountainItem object) {
                return ResourceLocation.fromNamespaceAndPath(Cognition.MOD_ID, "geo/experience_fountain.geo.json");
            }

            @Override
            public ResourceLocation getTextureResource(ExperienceFountainItem object) {
                return ResourceLocation.fromNamespaceAndPath(Cognition.MOD_ID, "textures/custom_models/experience_fountain.png");
            }

            @Override
            public ResourceLocation getAnimationResource(ExperienceFountainItem animatable) {
                return ResourceLocation.fromNamespaceAndPath(Cognition.MOD_ID, "animations/experience_fountain.json");
            }

            @Override
            public RenderType getRenderType(ExperienceFountainItem animatable, ResourceLocation texture) {
                return RenderType.entityTranslucent(getTextureResource(animatable));
            }
        });
    }
}
