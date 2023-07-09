package com.cyanogen.experienceobelisk.registries;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.cyanogen.experienceobelisk.renderer.ExperienceObeliskTileRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = ExperienceObelisk.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RegisterRenderer {

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    //registering geckolib geoblock custom renderer for xp obelisk block entity
    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(RegisterBlockEntities.EXPERIENCEOBELISK_BE.get(), ExperienceObeliskTileRenderer::new);
        LOGGER.info("Entity Renderer registered");
    }
}
