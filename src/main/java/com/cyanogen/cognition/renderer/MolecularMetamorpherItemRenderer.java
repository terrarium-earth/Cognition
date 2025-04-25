package com.cyanogen.cognition.renderer;

import com.cyanogen.cognition.Cognition;
import com.cyanogen.cognition.item.MolecularMetamorpherItem;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class MolecularMetamorpherItemRenderer extends GeoItemRenderer<MolecularMetamorpherItem> {

    public MolecularMetamorpherItemRenderer() {
        super(new GeoModel<>() {
            @Override
            public ResourceLocation getModelResource(MolecularMetamorpherItem object) {
                return ResourceLocation.fromNamespaceAndPath(Cognition.MOD_ID, "geo/molecular_metamorpher.geo.json");
            }

            @Override
            public ResourceLocation getTextureResource(MolecularMetamorpherItem object) {
                return ResourceLocation.fromNamespaceAndPath(Cognition.MOD_ID, "textures/custom_models/molecular_metamorpher.png");
            }

            @Override
            public ResourceLocation getAnimationResource(MolecularMetamorpherItem animatable) {
                return ResourceLocation.fromNamespaceAndPath(Cognition.MOD_ID, "animations/molecular_metamorpher.json");
            }

            @Override
            public RenderType getRenderType(MolecularMetamorpherItem animatable, ResourceLocation texture) {
                return RenderType.entityTranslucent(getTextureResource(animatable));
            }
        });
    }
}
