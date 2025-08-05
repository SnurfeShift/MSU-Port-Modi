package com.shift.msuportmodi.client.gui.tooltip;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

public class WalletEntityTooltip implements TooltipComponent {
    private final CompoundTag tag;

    public WalletEntityTooltip(CompoundTag tag) {
        this.tag = tag;
    }

    public CompoundTag getTag() {
        return tag;
    }
}
