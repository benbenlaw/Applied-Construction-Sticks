package com.benbenlaw.appliedsticks.mixin;

import mrbysco.constructionstick.client.RenderBlockPreview;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderBlockPreview.class)
public class RenderBlockPreviewMixin {

    @Inject(method = "renderBlockHighlight", at = @At("HEAD"), cancellable = true)
    private void cancelRenderBlockHighlight(RenderHighlightEvent.Block event, CallbackInfo ci) {
        event.setCanceled(true);
        ci.cancel();
    }
}
