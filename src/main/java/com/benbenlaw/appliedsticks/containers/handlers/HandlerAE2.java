package com.benbenlaw.appliedsticks.containers.handlers;

import appeng.api.AECapabilities;
import mrbysco.constructionstick.api.IContainerHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class HandlerAE2 implements IContainerHandler {
    @Override
    public boolean matches(Player player, ItemStack itemStack, ItemStack inventoryStack) {
        return false;
    }

    @Override
    public int countItems(Player player, ItemStack itemStack, ItemStack itemStack1) {
        return 0;
    }

    @Override
    public int useItems(Player player, ItemStack itemStack, ItemStack itemStack1, int i) {
        return 0;
    }
}
