package com.cyanogen.cognition;

import com.cyanogen.cognition.config.Config;
import com.cyanogen.cognition.event.EventHandler;
import com.cyanogen.cognition.event.IModBusEventHandler;
import com.cyanogen.cognition.item.CognitiveToolset;
import com.cyanogen.cognition.registries.*;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(Cognition.MOD_ID)
public class Cognition
{
    public static final String MOD_ID = "cognition";

    public Cognition(IEventBus eventBus, ModContainer modContainer) {
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
        RegisterLootModifiers.register(eventBus);

        eventBus.register(new IModBusEventHandler()); //for events implementing IModBusEvent
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        NeoForge.EVENT_BUS.register(new EventHandler()); //for other events
    }

    private void clientSetup(final FMLClientSetupEvent event){
        ItemBlockRenderTypes.setRenderLayer(RegisterFluids.COGNITIUM_SOURCE.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(RegisterFluids.COGNITIUM_FLOWING.get(), RenderType.translucent());
        event.enqueueWork(CognitiveToolset.CognitiveBowItem::registerProperties);
    }

}
