package com.shift.msuportmodi.network;

import com.mraof.minestuck.network.MSPacket;
import com.shift.msuportmodi.MSUPortModi;
import com.shift.msuportmodi.inventory.modus.CommunistModus;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CommunistUpdatePacket(CompoundTag items) implements MSPacket.PlayToClient
{
    public static final Type<CommunistUpdatePacket> ID = new Type<>(MSUPortModi.id("communist_modus_update"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CommunistUpdatePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG,
            CommunistUpdatePacket::items,
            CommunistUpdatePacket::new
    );

    @Override
    public void execute(IPayloadContext context)
    {
        CommunistModus.updateClientList(this);
    }

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return ID;
    }
}

// Custom packet may not be necessary as it's only here to updateContent()