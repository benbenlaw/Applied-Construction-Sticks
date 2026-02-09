package com.benbenlaw.appliedsticks.containers.handlers;

import mrbysco.constructionstick.ConstructionStick;

public class AppliedSticksContainerRegistrar {

    public static void registerHandlers() {
        ConstructionStick.containerManager.register(new HandlerAE2());
        System.out.println("Registered AE2 Container Handler");
    }
}
