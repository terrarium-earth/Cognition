package com.cyanogen.cognition.registries;

import com.cyanogen.cognition.renderer.ExperienceFountainBlockRenderer;
import com.cyanogen.cognition.renderer.ExperienceObeliskBlockRenderer;
import com.cyanogen.cognition.renderer.MolecularMetamorpherBlockRenderer;
import com.cyanogen.cognition.renderer.PrecisionDispellerBlockRenderer;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public class RegisterRenderers {

    public static void register(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(RegisterBlockEntities.EXPERIENCE_OBELISK.get(), ExperienceObeliskBlockRenderer::new);
        event.registerBlockEntityRenderer(RegisterBlockEntities.EXPERIENCE_FOUNTAIN.get(), ExperienceFountainBlockRenderer::new);
        event.registerBlockEntityRenderer(RegisterBlockEntities.PRECISION_DISPELLER.get(), PrecisionDispellerBlockRenderer::new);
        event.registerBlockEntityRenderer(RegisterBlockEntities.MOLECULAR_METAMORPHER.get(), MolecularMetamorpherBlockRenderer::new);
    }

}
