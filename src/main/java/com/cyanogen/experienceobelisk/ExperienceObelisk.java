package com.cyanogen.experienceobelisk;

import com.cyanogen.experienceobelisk.block.ModBlocksInit;
import com.cyanogen.experienceobelisk.block_entities.ModTileEntitiesInit;
import com.cyanogen.experienceobelisk.config.Config;
import com.cyanogen.experienceobelisk.fluid.ModFluidsInit;
import com.cyanogen.experienceobelisk.item.ModItemsInit;
import com.cyanogen.experienceobelisk.network.PacketHandler;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ExperienceObelisk.MOD_ID)

public class ExperienceObelisk
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "experienceobelisk";

    public ExperienceObelisk() {
        // Register the setup method for modloading
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::setup);
        eventBus.addListener(this::clientSetup);

        ModItemsInit.register(eventBus);
        ModBlocksInit.register(eventBus);
        ModTileEntitiesInit.register(eventBus);
        ModFluidsInit.register(eventBus);
        GeckoLib.initialize();
        PacketHandler.init();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_SPEC);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {

    }

    private void clientSetup(final FMLClientSetupEvent event){
        ItemBlockRenderTypes.setRenderLayer(ModFluidsInit.RAW_EXPERIENCE.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModFluidsInit.RAW_EXPERIENCE_FLOWING.get(), RenderType.translucent());

        ItemBlockRenderTypes.setRenderLayer(ModFluidsInit.COGNITIUM.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModFluidsInit.COGNITIUM_FLOWING.get(), RenderType.translucent());
    }
}
