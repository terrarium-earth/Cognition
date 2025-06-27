package com.cyanogen.cognition.renderer;

import com.cyanogen.cognition.Cognition;
import com.cyanogen.cognition.block_entities.MolecularMetamorpherEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class MolecularMetamorpherBlockRenderer extends GeoBlockRenderer<MolecularMetamorpherEntity> {

    public MolecularMetamorpherBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(new GeoModel<>() {

            @Override
            public ResourceLocation getModelResource(MolecularMetamorpherEntity animatable) {
                return ResourceLocation.fromNamespaceAndPath(Cognition.MOD_ID, "geo/molecular_metamorpher.geo.json");
            }

            @Override
            public ResourceLocation getTextureResource(MolecularMetamorpherEntity entity) {
                return ResourceLocation.fromNamespaceAndPath(Cognition.MOD_ID, "textures/custom_models/molecular_metamorpher.png");
            }

            @Override
            public ResourceLocation getAnimationResource(MolecularMetamorpherEntity animatable) {
                return ResourceLocation.fromNamespaceAndPath(Cognition.MOD_ID, "animations/molecular_metamorpher.animation.json");
            }

            @Override
            public RenderType getRenderType(MolecularMetamorpherEntity animatable, ResourceLocation texture) {
                return RenderType.entityTranslucent(getTextureResource(animatable));
            }
        });
    }



}

