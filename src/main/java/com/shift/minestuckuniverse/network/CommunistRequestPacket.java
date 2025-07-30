package com.shift.minestuckuniverse.network;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.network.MSPacket;
import com.shift.minestuckuniverse.MinestuckUniverseModus;
import com.shift.minestuckuniverse.inventory.modus.CommunistModus;
import com.shift.minestuckuniverse.inventory.modus.CommunistModusData;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CommunistRequestPacket() implements MSPacket.PlayToServer
{
    public static final Type<CommunistRequestPacket> ID = new Type<>(MinestuckUniverseModus.id("communist_modus_request"));
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