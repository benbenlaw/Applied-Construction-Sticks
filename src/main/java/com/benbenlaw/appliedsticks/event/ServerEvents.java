package com.benbenlaw.appliedsticks.event;

import com.benbenlaw.appliedsticks.AppliedSticks;
import com.benbenlaw.appliedsticks.integration.AppliedStickComponent;
import com.benbenlaw.appliedsticks.network.StickJobPacket;
import mrbysco.constructionstick.items.stick.ItemStick;
import mrbysco.constructionstick.stick.StickJob;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = AppliedSticks.MOD_ID)
public class ServerEvents {

    @SubscribeEvent
    public static void updateStickJob(PlayerTickEvent.Pre event) {

        Level level = event.getEntity().level();

        if (level.isClientSide()) return;
        if (level.getGameTime() % 5 != 0) return;

        Player player = event.getEntity();
        ItemStack heldItem = player.getMainHandItem();

        HitResult result = player.pick(player.blockInteractionRange(), 0.0F,false);

        if (!heldItem.has(AppliedStickComponent.ME_POS)) return;
        if (!(result instanceof BlockHitResult hitResult)) return;
        StickJob job = ItemStick.getStickJob(player, level, hitResult, heldItem);

        PacketDistributor.sendToPlayer((ServerPlayer) player, new StickJobPacket(job.getBlockPositions()));
        //System.out.println(job.getBlockPositions());

    }
}