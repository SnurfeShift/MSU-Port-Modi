package com.shift.msuportmodi.inventory.modus;

import com.mraof.minestuck.inventory.captchalogue.ModusType;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.fml.LogicalSide;

public class CycloneModus extends ArrayModus {

    public float cyclePosition = -20;
    public static final float cycleSpeed = 0.01f;

    public CycloneModus(ModusType<? extends ArrayModus> type, LogicalSide side) {
        super(type, side);
    }

    public void cycle() {
        cyclePosition+=cycleSpeed;
        if(cyclePosition>=size || cyclePosition<0)
            cyclePosition = 0;
    }

    @Override
    public void readFromNBT(CompoundTag nbt, HolderLookup.Provider provider) {
        super.readFromNBT(nbt, provider);
        if(nbt.contains("CyclePos")) {
            cyclePosition = nbt.getFloat("CyclePos");
        }
    }
    @Override
    public CompoundTag writeToNBT(CompoundTag nbt, HolderLookup.Provider provider) {
        if(cyclePosition!=-20)
            nbt.putFloat("CyclePos", cyclePosition);
        return super.writeToNBT(nbt, provider);
    }
}
