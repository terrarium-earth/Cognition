package com.cyanogen.experienceobelisk.registries;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.cyanogen.experienceobelisk.renderer.ExperienceFountainBlockRenderer;
import com.cyanogen.experienceobelisk.renderer.ExperienceObeliskBlockRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExperienceObelisk.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RegisterRenderer {

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(RegisterBlockEntities.EXPERIENCEOBELISK_BE.get(), ExperienceObeliskBlockRenderer::new);
        event.registerBlockEntityRenderer(RegisterBlockEntities.EXPERIENCEFOUNTAIN_BE.get(), ExperienceFountainBlockRenderer::new);

    }
}
