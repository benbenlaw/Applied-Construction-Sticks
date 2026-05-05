package com.benbenlaw.appliedsticks.network;

import com.benbenlaw.appliedsticks.AppliedSticks;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class AppliedSticksMessages {


    public static void registerNetworking(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(AppliedSticks.MOD_ID);

        registrar.playToClient(StickJobPacket.TYPE, StickJobPacket.STREAM_CODEC, StickJobPacket.HANDLER);
    }
}
