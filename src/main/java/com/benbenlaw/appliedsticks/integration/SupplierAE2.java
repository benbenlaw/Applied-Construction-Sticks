package com.benbenlaw.appliedsticks.integration;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.storage.MEStorage;
import mrbysco.constructionstick.api.IStickSupplier;
import mrbysco.constructionstick.basics.ReplacementRegistry;
import mrbysco.constructionstick.basics.StickUtil;
import mrbysco.constructionstick.basics.option.StickOptions;
import mrbysco.constructionstick.basics.pool.IPool;
import mrbysco.constructionstick.basics.pool.OrderedPool;
import mrbysco.constructionstick.stick.undo.PlaceSnapshot;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class SupplierAE2 implements IStickSupplier {

    private final Player player;
    private final StickOptions options;
    private final MEStorage storage;

    public final Map<BlockItem, Long> itemCounts = new LinkedHashMap<>();
    public IPool<BlockItem> itemPool;

    public SupplierAE2(Player player, StickOptions options, MEStorage storage) {
        this.player = player;
        this.options = options;
        this.storage = storage;
    }

    @Override
    public void getSupply(@Nullable BlockItem target) {
        this.itemPool = new OrderedPool();
        this.itemCounts.clear();

        if (target != null) {
            addBlockItem(target);

            if (options.match.get() != StickOptions.MATCH.EXACT) {
                for (Item it : ReplacementRegistry.getMatchingSet(target)) {
                    if (it instanceof BlockItem bi) {
                        addBlockItem(bi);
                    }
                }
            }
        }
    }

    protected void addBlockItem(BlockItem item) {
        AEItemKey key = AEItemKey.of(item);
        if (key == null) return;

        long count = storage.getAvailableStacks()
                .get(key);

        if (count > 0) {
            itemCounts.put(item, count);
            itemPool.add(item);
        }
    }

    @Override
    public @Nullable PlaceSnapshot getPlaceSnapshot(Level level, BlockPos pos, BlockHitResult blockHitResult, @Nullable BlockState supportingBlock) {
        if (!StickUtil.isPositionPlaceable(level, player, pos, options.replace.get())) return null;

        itemPool.reset();

        while (true) {
            BlockItem item = itemPool.draw();
            if (item == null) return null;

            long count = itemCounts.getOrDefault(item, 0L);
            if (count > 0) {
                PlaceSnapshot snap = PlaceSnapshot.get(level, player, blockHitResult, pos, item, supportingBlock, options);
                if (snap != null) {
                    long n = count - 1;
                    itemCounts.put(item, n);
                    if (n == 0) itemPool.remove(item);
                    return snap;
                }
            }
        }
    }


    @Override
    public int takeItemStack(ItemStack stack) {
        if (player.isCreative()) return 0;

        AEItemKey key = AEItemKey.of(stack);
        if (key == null) return stack.getCount();

        long toExtract = stack.getCount();

        long extracted = storage.extract(
                key,
                toExtract,
                Actionable.MODULATE,
                IActionSource.ofPlayer(player)
        );

        return (int)(toExtract - extracted);
    }

    public static BlockItem getTargetBlockItem(Level level, BlockHitResult hit) {
        var item = level.getBlockState(hit.getBlockPos()).getBlock().asItem();
        return item instanceof BlockItem bi ? bi : null;
    }
}