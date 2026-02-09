package com.benbenlaw.appliedsticks.event;

import appeng.api.ids.AEComponents;
import com.benbenlaw.appliedsticks.AppliedSticks;
import com.benbenlaw.appliedsticks.client.ClientStickJobHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mrbysco.constructionstick.basics.StickUtil;
import mrbysco.constructionstick.client.KeybindHandler;
import mrbysco.constructionstick.items.stick.ItemStick;
import mrbysco.constructionstick.stick.StickJob;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;

import java.util.Set;

@EventBusSubscriber(modid = AppliedSticks.MOD_ID, value = Dist.CLIENT)
class ClientEvents {

    private static StickJob stickJob;
    public static Set<BlockPos> undoBlocks;

    @SubscribeEvent
    public static void renderBlockHighlight(RenderHighlightEvent.Block event) {
        if (event.getTarget().getType() == HitResult.Type.BLOCK) {
            BlockHitResult target = event.getTarget();
            Entity entity = event.getCamera().getEntity();
            if (entity instanceof Player player) {
                float colorR = 0.0F;
                float colorG = 0.0F;
                float colorB = 0.0F;
                ItemStack stick = StickUtil.holdingStick(player);
                if (stick != null) {
                    Set<BlockPos> blocks;
                    if (!KeybindHandler.KEY_SHOW_PREVIOUS.isDown()) {
                        if (stickJob == null || !compareRTR(stickJob.blockHitResult, target)
                                || !stickJob.stick.equals(stick) || stickJob.blockCount() < 2) {
                            stickJob = ItemStick.getStickJob(player, player.level(), target, stick);
                        }
                        blocks = stickJob.getBlockPositions();
                    } else {
                        blocks = undoBlocks;
                        colorG = 1.0F;
                    }

                    if (stick.has(AEComponents.WIRELESS_LINK_TARGET)) {
                        colorB = 1.0F;
                    }

                    if (blocks != null && !blocks.isEmpty()) {
                        PoseStack ms = event.getPoseStack();
                        MultiBufferSource buffer = event.getMultiBufferSource();
                        VertexConsumer lineBuilder = buffer.getBuffer(RenderType.LINES);
                        Camera info = event.getCamera();
                        double d0 = info.getPosition().x();
                        double d1 = info.getPosition().y();
                        double d2 = info.getPosition().z();

                        for (BlockPos block : blocks) {
                            AABB aabb = (new AABB(block)).move(-d0, -d1, -d2);
                            LevelRenderer.renderLineBox(ms, lineBuilder, aabb, colorR, colorG, colorB, 0.4F);
                        }

                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    private static boolean compareRTR(BlockHitResult rtr1, BlockHitResult rtr2) {
        return rtr1.getBlockPos().equals(rtr2.getBlockPos()) && rtr1.getDirection().equals(rtr2.getDirection());
    }
}


    /* THIS WORKS
    @SubscribeEvent
    public static void renderBlockHighlight(RenderHighlightEvent.Block event) {
        if (event.getTarget().getType() == HitResult.Type.BLOCK) {
            BlockHitResult target = event.getTarget();
            Entity entity = event.getCamera().getEntity();
            if (entity instanceof Player) {
                Player player = (Player) entity;
                float colorR = 0.0F;
                float colorG = 0.0F;
                float colorB = 0.0F;
                ItemStack stick = StickUtil.holdingStick(player);
                if (stick != null) {

                    Set<BlockPos> blocks;

                    if (stick.has(AppliedStickComponent.ME_POS)) {
                        blocks = ClientStickJobHandler.getPositions();
                        colorB = 1.0F;
                    } else {

                        blocks = ItemStick.getStickJob(player, player.level(), target, stick).getBlockPositions();
                        colorG = 1.0F;
                    }

                    if (blocks != null && !blocks.isEmpty()) {
                        PoseStack ms = event.getPoseStack();
                        MultiBufferSource buffer = event.getMultiBufferSource();
                        VertexConsumer lineBuilder = buffer.getBuffer(RenderType.LINES);
                        Camera info = event.getCamera();
                        double d0 = info.getPosition().x();
                        double d1 = info.getPosition().y();
                        double d2 = info.getPosition().z();

                        for (BlockPos block : blocks) {
                            AABB aabb = (new AABB(block)).move(-d0, -d1, -d2);
                            LevelRenderer.renderLineBox(ms, lineBuilder, aabb, colorR, colorG, colorB, 0.4F);
                        }

                        event.setCanceled(true);
                    }
                }
            }
        }
    }




}

     */
