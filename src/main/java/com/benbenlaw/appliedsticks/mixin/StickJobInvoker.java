package com.benbenlaw.appliedsticks.mixin;

import mrbysco.constructionstick.stick.StickJob;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(StickJob.class)
public interface StickJobInvoker {

    @Invoker("getTargetItem")
    static BlockItem callGetTargetItem(Level level, BlockHitResult rayTrace) {
        throw new UnsupportedOperationException();
    }
}