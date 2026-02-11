package com.benbenlaw.appliedsticks;

import com.benbenlaw.appliedsticks.containers.handlers.AppliedSticksContainerRegistrar;
import com.benbenlaw.appliedsticks.integration.AE2Integration;
import com.benbenlaw.appliedsticks.network.AppliedSticksMessages;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(AppliedSticks.MOD_ID)
public class AppliedSticks{
    public static final String MOD_ID = "appliedsticks";
    public static final Logger LOGGER = LogManager.getLogger();

    public AppliedSticks(IEventBus eventBus, ModContainer modContainer) {
        eventBus.addListener(this::networkingSetup);
        eventBus.addListener(AppliedSticks::commonSetup);
    }

    public void networkingSetup(RegisterPayloadHandlersEvent event) {
        AppliedSticksMessages.registerNetworking(event);
    }

    public static void commonSetup(final FMLCommonSetupEvent event) {
        if (ModList.get().isLoaded("ae2")) {
            AE2Integration.registerItems();
            AppliedSticksContainerRegistrar.registerHandlers();
        }
    }
}
