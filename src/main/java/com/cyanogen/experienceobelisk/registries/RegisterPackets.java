package com.cyanogen.experienceobelisk.registries;

import com.cyanogen.experienceobelisk.network.experience_obelisk.UpdateContents;
import com.cyanogen.experienceobelisk.network.experience_obelisk.UpdateRadius;
import com.cyanogen.experienceobelisk.network.precision_dispeller.UpdateSlot;
import com.cyanogen.experienceobelisk.network.shared.UpdateInventory;
import com.cyanogen.experienceobelisk.network.shared.UpdateRedstone;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.MainThreadPayloadHandler;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class RegisterPackets {

    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1.0").executesOn(HandlerThread.MAIN);

        registrar.playToServer(UpdateContents.TYPE, UpdateContents.STREAM_CODEC,
                new MainThreadPayloadHandler<>(UpdateContents::handle));

        registrar.playToServer(UpdateRadius.TYPE, UpdateRadius.STREAM_CODEC,
                new MainThreadPayloadHandler<>(UpdateRadius::handleServer));

        registrar.playToServer(UpdateSlot.TYPE, UpdateSlot.STREAM_CODEC,
                new MainThreadPayloadHandler<>(UpdateSlot::handleServer));

        registrar.playToServer(UpdateInventory.TYPE, UpdateInventory.STREAM_CODEC,
                new MainThreadPayloadHandler<>(UpdateInventory::handleServer));

        registrar.playToServer(UpdateRedstone.TYPE, UpdateRedstone.STREAM_CODEC,
                new MainThreadPayloadHandler<>(UpdateRedstone::handleServer));

    }


}
