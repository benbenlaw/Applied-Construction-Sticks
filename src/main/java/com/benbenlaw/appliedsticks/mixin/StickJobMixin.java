package com.benbenlaw.appliedsticks.mixin;

import com.benbenlaw.appliedsticks.integration.AE2Util;
import com.benbenlaw.appliedsticks.integration.AppliedStickComponent;
import com.benbenlaw.appliedsticks.integration.SupplierAE2;
import mrbysco.constructionstick.api.IStickSupplier;
import mrbysco.constructionstick.basics.option.StickOptions;
import mrbysco.constructionstick.stick.StickJob;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(StickJob.class)
public class StickJobMixin {

    @Inject(method = "<init>", at = @At("RETURN"))
    private void injectAE2Supplier(Player player, Level level, net.minecraft.world.phys.BlockHitResult hitResult,
                                   ItemStack stick, CallbackInfo ci) {
        StickJob self = (StickJob) (Object) this;
        StickOptions options = self.options;

        if (AE2Util.isLinkedToGrid(stick, player)) {
            var storage = AE2Util.getStorage(stick, player);
            if (storage != null) {
                IStickSupplier supplier = new SupplierAE2(player, options, storage);
                supplier.getSupply(StickJobInvoker.callGetTargetItem(level, hitResult));

                try {
                    java.lang.reflect.Field f = StickJob.class.getDeclaredField("stickSupplier");
                    f.setAccessible(true);
                    f.set(self, supplier);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}