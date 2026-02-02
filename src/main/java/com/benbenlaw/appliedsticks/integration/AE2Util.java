package com.benbenlaw.appliedsticks.integration;

import appeng.api.AECapabilities;
import appeng.api.config.Actionable;
import appeng.api.implementations.blockentities.IWirelessAccessPoint;
import appeng.api.networking.IGrid;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEItemKey;
import appeng.api.storage.MEStorage;
import mrbysco.constructionstick.items.stick.ItemStickBasic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Iterator;

public class AE2Util {

    public static boolean isLinkedToGrid(ItemStack stick, Player player) {
        return stick.getItem() instanceof ItemStickBasic && getLinkedPos(stick) != null;
    }

    public static GlobalPos getLinkedPos(ItemStack stick) {
        return stick.has(AppliedStickComponent.ME_POS.get()) ? stick.get(AppliedStickComponent.ME_POS.get()) : null;
    }

    public static MEStorage getStorage(ItemStack stick, Player player) {
        GlobalPos pos = getLinkedPos(stick);
        if (pos == null) return null;

        Level level = player.level();
        BlockEntity blockEntity = level.getBlockEntity(pos.pos());
        if (!(blockEntity instanceof IWirelessAccessPoint accessPoint)) return null;

        IGrid grid = accessPoint.getGrid();
        if (grid == null) return null;

        System.out.println(grid.getStorageService().getInventory().getAvailableStacks().getFirstEntry());
        return grid.getStorageService().getInventory();
    }
}