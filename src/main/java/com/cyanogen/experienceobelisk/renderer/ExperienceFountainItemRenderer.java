package com.cyanogen.experienceobelisk.renderer;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.cyanogen.experienceobelisk.item.ExperienceFountainItem;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class ExperienceFountainItemRenderer extends GeoItemRenderer<ExperienceFountainItem> {

    public ExperienceFountainItemRenderer() {
        super(new GeoModel<>() {
            @Override
            public ResourceLocation getModelResource(ExperienceFountainItem object) {
                return new ResourceLocation(ExperienceObelisk.MOD_ID, "geo/experience_fountain.geo.json");
            }

            @Override
            public ResourceLocation getTextureResource(ExperienceFountainItem object) {
                return new ResourceLocation(ExperienceObelisk.MOD_ID, "textures/custom_models/experience_fountain_item.png");
            }

            @Override
            public ResourceLocation getAnimationResource(ExperienceFountainItem animatable) {
                return new ResourceLocation(ExperienceObelisk.MOD_ID, "animations/experience_fountain.json");
            }

            @Override
            public RenderType getRenderType(ExperienceFountainItem animatable, ResourceLocation texture) {
                return RenderType.entityTranslucent(getTextureResource(animatable));
            }
        });
    }
}
