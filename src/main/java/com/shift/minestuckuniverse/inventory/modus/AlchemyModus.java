package com.shift.minestuckuniverse.inventory.modus;

import com.mraof.minestuck.inventory.captchalogue.ModusType;
import net.neoforged.fml.LogicalSide;

public class AlchemyModus extends ArrayModus{
    public AlchemyModus(ModusType<? extends ArrayModus> type, LogicalSide side) {
        super(type, side);
    }
}
