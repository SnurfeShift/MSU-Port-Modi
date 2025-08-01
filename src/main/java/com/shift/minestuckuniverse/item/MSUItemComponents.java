package com.shift.minestuckuniverse.item;

import com.shift.minestuckuniverse.MinestuckUniverseModus;
import com.shift.minestuckuniverse.item.Components.OperandiStoredItemComponent;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MSUItemComponents {
    public static final DeferredRegister.DataComponents REGISTRY = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, MinestuckUniverseModus.MOD_ID);

    public static final Supplier<DataComponentType<OperandiStoredItemComponent>> STORED_ITEM = REGISTRY.registerComponentType("stored_item",
            builder -> builder.persistent(OperandiStoredItemComponent.CODEC).networkSynchronized(OperandiStoredItemComponent.STREAM_CODEC));

    public static final Supplier<DataComponentType<CompoundTag>> STORED_ENTITY = REGISTRY.registerComponentType("entity_holder",
                    builder -> builder.persistent(CompoundTag.CODEC).networkSynchronized(ByteBufCodecs.COMPOUND_TAG));
}
