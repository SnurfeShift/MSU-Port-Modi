package com.shift.msuportmodi.client.gui.tooltip;

import com.mraof.minestuck.api.alchemy.GristSet;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

public class GristSetTooltip implements TooltipComponent {
    private final GristSet cost;

    public GristSetTooltip(GristSet cost) {
        this.cost = cost;
    }

    public GristSet getCost() {
        return cost;
    }
}
