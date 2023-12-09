package com.cyanogen.experienceobelisk.renderer;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.cyanogen.experienceobelisk.block_entities.ExperienceObeliskEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class ExperienceObeliskBlockRenderer extends GeoBlockRenderer<ExperienceObeliskEntity> {

    public ExperienceObeliskBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(new GeoModel<>() {

            @Override
            public ResourceLocation getModelResource(ExperienceObeliskEntity animatable) {
                return new ResourceLocation(ExperienceObelisk.MOD_ID, "geo/experience_obelisk.geo.json");
            }

            @Override
            public ResourceLocation getTextureResource(ExperienceObeliskEntity entity) {
                return new ResourceLocation(ExperienceObelisk.MOD_ID, "textures/custom_models/experience_obelisk.png");
            }

            @Override
            public ResourceLocation getAnimationResource(ExperienceObeliskEntity animatable) {
                return new ResourceLocation(ExperienceObelisk.MOD_ID, "animations/experience_obelisk.json");
            }

            @Override
            public RenderType getRenderType(ExperienceObeliskEntity animatable, ResourceLocation texture) {
                return RenderType.entityTranslucent(getTextureResource(animatable));
            }
        });
    }



}

