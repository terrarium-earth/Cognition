package com.cyanogen.experienceobelisk;

import com.cyanogen.experienceobelisk.block.ModBlocksInit;
import com.cyanogen.experienceobelisk.block_entities.ModTileEntitiesInit;
import com.cyanogen.experienceobelisk.config.Config;
import com.cyanogen.experienceobelisk.enchantment.ModEnchantmentsInit;
import com.cyanogen.experienceobelisk.event.EventHandler;
import com.cyanogen.experienceobelisk.fluid.ModFluidsInit;
import com.cyanogen.experienceobelisk.gui.ModMenusInit;
import com.cyanogen.experienceobelisk.gui.PrecisionDispellerScreen;
import com.cyanogen.experienceobelisk.item.ModItemsInit;
import com.cyanogen.experienceobelisk.network.PacketHandler;
import net.minecraft.client.gui.screens.MenuScreens;
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

@Mod(ExperienceObelisk.MOD_ID)

public class ExperienceObelisk
{
    public static final String MOD_ID = "experienceobelisk";

    public ExperienceObelisk() {

        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::setup);
        eventBus.addListener(this::clientSetup);

        ModItemsInit.register(eventBus);
        ModBlocksInit.register(eventBus);
        ModTileEntitiesInit.register(eventBus);
        ModFluidsInit.register(eventBus);
        ModMenusInit.register(eventBus);
        ModEnchantmentsInit.register(eventBus);

        GeckoLib.initialize();
        PacketHandler.init();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_SPEC);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }

    private void clientSetup(final FMLClientSetupEvent event){

        MenuScreens.register(ModMenusInit.PRECISION_DISPELLER_MENU.get(), PrecisionDispellerScreen::new);

        ItemBlockRenderTypes.setRenderLayer(ModFluidsInit.COGNITIUM.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModFluidsInit.COGNITIUM_FLOWING.get(), RenderType.translucent());
    }
}
