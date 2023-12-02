package com.cyanogen.experienceobelisk.renderer;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.cyanogen.experienceobelisk.block_entities.ExperienceFountainEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class ExperienceFountainBlockRenderer extends GeoBlockRenderer<ExperienceFountainEntity> {

    public ExperienceFountainBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(new GeoModel<>() {

            @Override
            public ResourceLocation getModelResource(ExperienceFountainEntity animatable) {
                return new ResourceLocation(ExperienceObelisk.MOD_ID, "geo/experience_fountain.geo.json");
            }

            @Override
            public ResourceLocation getTextureResource(ExperienceFountainEntity entity) {
                return new ResourceLocation(ExperienceObelisk.MOD_ID, "textures/custom_models/experience_fountain.png");
            }

            @Override
            public ResourceLocation getAnimationResource(ExperienceFountainEntity animatable) {
                return new ResourceLocation(ExperienceObelisk.MOD_ID, "animations/experience_fountain.json");
            }

            @Override
            public RenderType getRenderType(ExperienceFountainEntity animatable, ResourceLocation texture) {
                return RenderType.entityTranslucent(getTextureResource(animatable));
            }
        });
    }

}

