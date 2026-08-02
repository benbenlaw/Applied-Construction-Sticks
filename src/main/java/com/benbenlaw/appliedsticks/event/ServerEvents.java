package com.benbenlaw.appliedsticks.event;

import appeng.api.ids.AEComponents;
import com.benbenlaw.appliedsticks.AppliedSticks;
import com.benbenlaw.appliedsticks.network.StickJobPacket;
import mrbysco.constructionstick.items.stick.ItemStick;
import mrbysco.constructionstick.stick.StickJob;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

@EventBusSubscriber(modid = AppliedSticks.MOD_ID)
public class ServerEvents {

    private static final HashMap<Player, Set<BlockPos>> lastSentPositions = new HashMap<>();

    @SubscribeEvent
    public static void updateStickJob(PlayerTickEvent.Pre event) {
        Player player = event.getEntity();
        Level level = player.level();
        if (level.isClientSide()) return;

        ItemStack heldItem = player.getMainHandItem();

        boolean isConstructionStick = heldItem.has(AEComponents.WIRELESS_LINK_TARGET)
                && heldItem.is(TagKey.create(Registries.ITEM, Identifier.parse("constructionstick:construction_sticks")));

        if (!isConstructionStick) {
            lastSentPositions.remove(player);
            return;
        }

        HitResult result;
        try {
            result = player.pick(player.blockInteractionRange(), 0.0F, false);
        } catch (IllegalStateException e) {
            return;
        }

        if (result instanceof BlockHitResult hitResult) {
            StickJob job = ItemStick.getStickJob(player, level, hitResult, heldItem);
            Set<BlockPos> currentPositions = job.getBlockPositions();

            if (!Objects.equals(lastSentPositions.get(player), currentPositions)) {
                lastSentPositions.put(player, currentPositions);
                PacketDistributor.sendToPlayer((ServerPlayer) player, new StickJobPacket(currentPositions));
            }
        } else {
            lastSentPositions.remove(player);
        }
    }
}