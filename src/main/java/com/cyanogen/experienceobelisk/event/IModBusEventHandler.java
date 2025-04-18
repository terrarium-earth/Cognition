package com.cyanogen.experienceobelisk.event;

import com.cyanogen.experienceobelisk.gui.ExperienceObeliskScreen;
import com.cyanogen.experienceobelisk.gui.MolecularMetamorpherScreen;
import com.cyanogen.experienceobelisk.gui.PrecisionDispellerScreen;
import com.cyanogen.experienceobelisk.registries.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterItemDecorationsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public class IModBusEventHandler {

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onRegisterItemDecorator(RegisterItemDecorationsEvent event){
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onRegisterMenuScreens(RegisterMenuScreensEvent event){
        event.register(RegisterMenus.EXPERIENCE_OBELISK_MENU.get(), ExperienceObeliskScreen::new);
        event.register(RegisterMenus.PRECISION_DISPELLER_MENU.get(), PrecisionDispellerScreen::new);
        event.register(RegisterMenus.MOLECULAR_METAMORPHER_MENU.get(), MolecularMetamorpherScreen::new);

    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event){
        RegisterPackets.register(event);
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onRegisterClientExtensions(RegisterClientExtensionsEvent event){
        event.registerFluidType(RegisterFluids.COGNITIUM_FLUID_TYPE.get(), RegisterFluids.COGNITIUM_FLUID_TYPE);
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event){
        RegisterRenderers.register(event);
    }

    @SubscribeEvent
    public void onRegisterCapabilities(RegisterCapabilitiesEvent event){
        RegisterCapabilities.register(event);
    }

}
