package com.cyanogen.cognition.renderer;

import com.cyanogen.cognition.Cognition;
import com.cyanogen.cognition.block_entities.ExperienceFountainEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class ExperienceFountainBlockRenderer extends GeoBlockRenderer<ExperienceFountainEntity> {

    public ExperienceFountainBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(new GeoModel<>() {

            @Override
            public ResourceLocation getModelResource(ExperienceFountainEntity animatable) {
                return ResourceLocation.fromNamespaceAndPath(Cognition.MOD_ID, "geo/experience_fountain.geo.json");
            }

            @Override
            public ResourceLocation getTextureResource(ExperienceFountainEntity entity) {
                return ResourceLocation.fromNamespaceAndPath(Cognition.MOD_ID, "textures/custom_models/experience_fountain.png");
            }

            @Override
            public ResourceLocation getAnimationResource(ExperienceFountainEntity animatable) {
                return ResourceLocation.fromNamespaceAndPath(Cognition.MOD_ID, "animations/experience_fountain.json");
            }

            @Override
            public RenderType getRenderType(ExperienceFountainEntity animatable, ResourceLocation texture) {
                return RenderType.entityTranslucent(getTextureResource(animatable));
            }
        });
    }

}

