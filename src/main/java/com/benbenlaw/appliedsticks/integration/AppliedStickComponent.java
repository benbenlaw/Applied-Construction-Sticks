package com.benbenlaw.appliedsticks.integration;

import com.benbenlaw.appliedsticks.AppliedSticks;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AppliedStickComponent {

    public static final DeferredRegister<DataComponentType<?>> COMPONENTS = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, AppliedSticks.MOD_ID);

    //public static final DeferredHolder<DataComponentType<?>, DataComponentType<GlobalPos>> ME_POS =
    //        COMPONENTS.register("me_pos", () ->
    //                DataComponentType.<GlobalPos>builder().persistent(GlobalPos.CODEC).networkSynchronized(GlobalPos.STREAM_CODEC).build());
//
}
