package com.shift.minestuckuniverse.item.Components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.shift.minestuckuniverse.item.MSUItemComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public record OperandiStoredItemComponent(ItemStack storedStack) {
    public static final Codec<OperandiStoredItemComponent> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    ItemStack.CODEC.fieldOf("item").forGetter(OperandiStoredItemComponent::storedStack)
            ).apply(instance, OperandiStoredItemComponent::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, OperandiStoredItemComponent> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC,
            OperandiStoredItemComponent::storedStack,
            OperandiStoredItemComponent::new
    );

    public static final OperandiStoredItemComponent EMPTY = new OperandiStoredItemComponent(ItemStack.EMPTY);

    public static ItemStack getStoredItem(ItemStack item) {
        return Optional.ofNullable(item.get(MSUItemComponents.STORED_ITEM))
                .map(OperandiStoredItemComponent::storedStack).orElse(ItemStack.EMPTY);
    }
}
