package com.cyanogen.experienceobelisk.block_entities;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.cyanogen.experienceobelisk.item.ExperienceObeliskItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class ExperienceObeliskItemRenderer extends GeoItemRenderer<ExperienceObeliskItem> {

    public ExperienceObeliskItemRenderer() {
        super(new AnimatedGeoModel<ExperienceObeliskItem>() {
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

}
