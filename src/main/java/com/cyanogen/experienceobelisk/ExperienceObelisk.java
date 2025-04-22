package com.cyanogen.experienceobelisk;

import com.cyanogen.experienceobelisk.config.Config;
import com.cyanogen.experienceobelisk.event.EventHandler;
import com.cyanogen.experienceobelisk.event.IModBusEventHandler;
import com.cyanogen.experienceobelisk.registries.*;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(ExperienceObelisk.MOD_ID)
public class ExperienceObelisk
{
    public static final String MOD_ID = "experienceobelisk";

    public ExperienceObelisk(IEventBus eventBus, ModContainer modContainer) {
        eventBus.addListener(this::commonSetup);
        eventBus.addListener(this::clientSetup);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.COMMON_SPEC);

        RegisterItems.register(eventBus);
        RegisterTiers.register(eventBus);
        RegisterCreativeTab.register(eventBus);
        RegisterBlocks.register(eventBus);
        RegisterBlockEntities.register(eventBus);
        RegisterFluids.register(eventBus);
        RegisterMenus.register(eventBus);
        RegisterRecipes.register(eventBus);
        RegisterSounds.register(eventBus);

        eventBus.register(new IModBusEventHandler()); //for events implementing IModBusEvent
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        NeoForge.EVENT_BUS.register(new EventHandler()); //for other events

        if(ModList.get().isLoaded("curios")){
            NeoForge.EVENT_BUS.register(new EventHandler.CuriosEventHandler());
        }
    }

    private void clientSetup(final FMLClientSetupEvent event){
        ItemBlockRenderTypes.setRenderLayer(RegisterFluids.COGNITIUM_SOURCE.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(RegisterFluids.COGNITIUM_FLOWING.get(), RenderType.translucent());
    }


}
