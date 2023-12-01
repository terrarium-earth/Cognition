package com.cyanogen.experienceobelisk.renderer;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.cyanogen.experienceobelisk.item.PrecisionDispellerItem;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class PrecisionDispellerItemRenderer extends GeoItemRenderer<PrecisionDispellerItem> {

    public PrecisionDispellerItemRenderer() {
        super(new GeoModel<>() {
            @Override
            public ResourceLocation getModelResource(PrecisionDispellerItem object) {
                return new ResourceLocation(ExperienceObelisk.MOD_ID, "geo/precision_dispeller.geo.json");
            }

            @Override
            public ResourceLocation getTextureResource(PrecisionDispellerItem object) {
                return new ResourceLocation(ExperienceObelisk.MOD_ID, "textures/custom_models/precision_dispeller_item.png");
            }

            @Override
            public ResourceLocation getAnimationResource(PrecisionDispellerItem animatable) {
                return new ResourceLocation(ExperienceObelisk.MOD_ID, "animations/precision_dispeller.json");
            }

            @Override
            public RenderType getRenderType(PrecisionDispellerItem animatable, ResourceLocation texture) {
                return RenderType.entityTranslucent(getTextureResource(animatable));
            }
        });

    }
}
