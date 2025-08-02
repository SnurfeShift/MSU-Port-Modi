package com.shift.msuportmodi.network;

import com.mraof.minestuck.network.MSPacket;
import com.shift.msuportmodi.MSUPortModi;
import com.shift.msuportmodi.inventory.modus.CommunistModusData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CommunistRequestPacket() implements MSPacket.PlayToServer
{
    public static final Type<CommunistRequestPacket> ID = new Type<>(MSUPortModi.id("communist_modus_request"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CommunistRequestPacket> STREAM_CODEC = StreamCodec.unit(new CommunistRequestPacket());

    @Override
    public void execute(IPayloadContext context, ServerPlayer player)
    {
        CommunistModusData.initializePlayer(player);
    }

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return ID;
    }
}

// Custom packet may not be necessary as it's only here to updateContent()