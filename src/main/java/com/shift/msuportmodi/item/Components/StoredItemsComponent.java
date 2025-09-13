/*package com.shift.msuportmodi.item.Components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.shift.msuportmodi.item.MSUItemComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public record StoredItemsComponent(CompoundTag nbt) {
    public static final Codec<StoredItemsComponent> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
                CompoundTag.CODEC.fieldOf("tag").forGetter(StoredItemsComponent::nbt)
        ).apply(instance, StoredItemsComponent::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, StoredItemsComponent> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG,
            StoredItemsComponent::nbt,
            StoredItemsComponent::new
    );

    public static final StoredItemsComponent EMPTY = new StoredItemsComponent(new CompoundTag());

    public static CompoundTag getStoredTag(ItemStack item) {
        return Optional.ofNullable(item.get(MSUItemComponents.STORED_ITEMS))
                .map(StoredItemsComponent::nbt).orElse(new CompoundTag());
    } 

}
*/