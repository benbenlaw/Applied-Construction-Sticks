package com.benbenlaw.appliedsticks.integration;

import appeng.api.features.GridLinkables;
import appeng.api.features.IGridLinkableHandler;
import mrbysco.constructionstick.items.stick.ItemStickBasic;
import mrbysco.constructionstick.registry.ModItems;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.item.ItemStack;

public class AE2Integration {
    public static final IGridLinkableHandler LINKABLE_HANDLER = new LinkableHandler();

    public static void registerItems() {
        GridLinkables.register(ModItems.STICK_COPPER.get(), LINKABLE_HANDLER);
        GridLinkables.register(ModItems.STICK_DIAMOND.get(), LINKABLE_HANDLER);
        GridLinkables.register(ModItems.STICK_IRON.get(), LINKABLE_HANDLER);
        GridLinkables.register(ModItems.STICK_NETHERITE.get(), LINKABLE_HANDLER);
        GridLinkables.register(ModItems.STICK_WOODEN.get(), LINKABLE_HANDLER);

    }

    public static class LinkableHandler implements IGridLinkableHandler {
        @Override
        public boolean canLink(ItemStack stack) {
            return stack.getItem() instanceof ItemStickBasic;
        }

        @Override
        public void link(ItemStack itemStack, GlobalPos pos) {
            itemStack.set(AppliedStickComponent.ME_POS.get(), pos);
        }

        @Override
        public void unlink(ItemStack itemStack) {
            itemStack.remove(AppliedStickComponent.ME_POS.get());
        }
    }

}
