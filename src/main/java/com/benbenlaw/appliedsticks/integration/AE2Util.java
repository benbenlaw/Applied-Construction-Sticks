package com.benbenlaw.appliedsticks.integration;

import appeng.api.ids.AEComponents;
import appeng.api.implementations.blockentities.IWirelessAccessPoint;
import appeng.api.networking.IGrid;
import appeng.api.storage.MEStorage;
import mrbysco.constructionstick.items.stick.ItemStickBasic;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class AE2Util {

    public static boolean isLinkedToGrid(ItemStack stick, Player player) {
        return stick.getItem() instanceof ItemStickBasic && getLinkedPos(stick) != null;
    }

    public static GlobalPos getLinkedPos(ItemStack stick) {
        return stick.has(AEComponents.WIRELESS_LINK_TARGET) ? stick.get(AEComponents.WIRELESS_LINK_TARGET) : null;
    }

    public static MEStorage getStorage(ItemStack stick, Player player) {
        GlobalPos pos = getLinkedPos(stick);
        if (pos == null) return null;

        if (!(player instanceof ServerPlayer serverPlayer)) return null;

        MinecraftServer server = serverPlayer.getServer();

        ServerLevel targetLevel = server.getLevel(pos.dimension());
        if (targetLevel == null) return null;

        if (!targetLevel.isLoaded(pos.pos())) return null;

        BlockEntity blockEntity = targetLevel.getBlockEntity(pos.pos());
        if (!(blockEntity instanceof IWirelessAccessPoint accessPoint)) return null;

        IGrid grid = accessPoint.getGrid();
        if (grid == null) return null;

        return grid.getStorageService().getInventory();
    }
}