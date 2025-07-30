package com.shift.minestuckuniverse.network;

import com.mraof.minestuck.network.MSPacket;
import com.shift.minestuckuniverse.MinestuckUniverseModus;
import com.shift.minestuckuniverse.inventory.modus.CommunistModus;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CommunistUpdatePacket(NonNullList<ItemStack> items) implements MSPacket.PlayToClient
{
    public static final Type<CommunistUpdatePacket> ID = new Type<>(MinestuckUniverseModus.id("communist_modus_update"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CommunistUpdatePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.list(4),
            CommunistUpdatePacket::items,
            CommunistUpdatePacket::new
    );

    @Override
    public void execute(IPayloadContext context)
    {
        CommunistModus.updateClientList(items);
    }

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return ID;
    }
}

// Custom packet may not be necessary as it's only here to updateContent()