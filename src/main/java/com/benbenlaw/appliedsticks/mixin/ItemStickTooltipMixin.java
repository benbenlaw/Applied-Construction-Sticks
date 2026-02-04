package com.benbenlaw.appliedsticks.mixin;

import appeng.api.ids.AEComponents;
import com.benbenlaw.appliedsticks.integration.AppliedStickComponent;
import mrbysco.constructionstick.items.stick.ItemStick;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ItemStick.class)
public abstract class ItemStickTooltipMixin {

    @Inject(
            method = "appendHoverText",
            at = @At(
                    value = "INVOKE",
                    target = "Lmrbysco/constructionstick/basics/option/StickUpgradesSelectable;getUpgrades()Ljava/util/List;",
                    ordinal = 0,
                    shift = At.Shift.BEFORE
            )
    )
    private void addLinkedPosOnce(ItemStack stack, Item.TooltipContext context, List<Component> lines, TooltipFlag flag, CallbackInfo ci) {
        if (!Screen.hasShiftDown()) {
            return;
        }

        if (!stack.has(AEComponents.WIRELESS_LINK_TARGET)) {
            return;
        }

        GlobalPos globalPos = stack.get(AEComponents.WIRELESS_LINK_TARGET);
        if (globalPos == null) {
            return;
        }

        BlockPos pos = globalPos.pos();
        String dim = globalPos.dimension().location().toString();

        lines.add(Component.literal("Linked to ME Network at:").withStyle(ChatFormatting.AQUA));

        lines.add(Component.literal(dim).withStyle(ChatFormatting.GREEN));

        lines.add(Component.literal(" [" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "]").withStyle(ChatFormatting.GREEN));
    }
}
