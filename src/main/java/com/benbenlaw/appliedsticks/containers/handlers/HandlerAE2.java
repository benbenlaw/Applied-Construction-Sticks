package com.benbenlaw.appliedsticks.containers.handlers;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.MEStorage;
import com.benbenlaw.appliedsticks.integration.AE2Util;
import mrbysco.constructionstick.api.IContainerHandler;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class HandlerAE2 implements IContainerHandler {

    @Override
    public boolean matches(Player player, ItemStack itemStack, ItemStack inventoryStack) {

        if (!inventoryStack.is(TagKey.create(
                Registries.ITEM,
                Identifier.parse("constructionstick:construction_sticks")
        ))) {
            return false;
        }

        boolean isHeld =
                ItemStack.isSameItemSameComponents(inventoryStack, player.getMainHandItem()) ||
                        ItemStack.isSameItemSameComponents(inventoryStack, player.getOffhandItem());

        if (!isHeld) {
            return false;
        }

        if (!AE2Util.isLinkedToGrid(inventoryStack, player)) {
            return false;
        }

        return AE2Util.getStorage(inventoryStack, player) != null;
    }

    @Override
    public int countItems(Player player, ItemStack itemStack, ItemStack inventoryStack) {
        MEStorage storage = AE2Util.getStorage(inventoryStack, player);
        if (storage == null) return 0;

        var key = AEItemKey.of(itemStack);
        if (key == null) return 0;

        KeyCounter counter = new KeyCounter();
        storage.getAvailableStacks(counter);

        long amount = counter.get(key);
        return (int) Math.min(Integer.MAX_VALUE, amount);
    }

    @Override
    public int useItems(Player player, ItemStack itemStack, ItemStack inventoryStack, int count) {
        MEStorage storage = AE2Util.getStorage(inventoryStack, player);
        if (storage == null) return count;

        var key = AEItemKey.of(itemStack);
        if (key == null) return count;

        long canExtract = storage.extract(key, count, Actionable.SIMULATE, IActionSource.ofPlayer(player));
        if (canExtract <= 0) return count;

        long extracted = storage.extract(key, canExtract, Actionable.MODULATE, IActionSource.ofPlayer(player));

        return count - (int) extracted;
    }


}
