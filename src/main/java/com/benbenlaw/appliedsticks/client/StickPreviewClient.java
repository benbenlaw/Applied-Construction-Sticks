package com.benbenlaw.appliedsticks.client;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class StickPreviewClient {

    private static Map<BlockPos, ResourceLocation> previewMap = new HashMap<>();

    public static void setPreview(Set<BlockPos> positions, ResourceLocation blockId) {
        previewMap.clear();
        for (BlockPos pos : positions) {
            previewMap.put(pos, blockId);
        }
    }

    public static ResourceLocation getBlock(BlockPos pos) {
        return previewMap.get(pos);
    }

    public static void clear() {
        previewMap.clear();
    }

    public static Map<BlockPos, ResourceLocation> getPreviewMap() {
        return Collections.unmodifiableMap(previewMap);
    }
}