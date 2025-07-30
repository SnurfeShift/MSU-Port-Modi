package com.shift.minestuckuniverse.network;

import com.mraof.minestuck.network.MSPacket;
import com.shift.minestuckuniverse.MinestuckUniverseModus;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = MinestuckUniverseModus.MOD_ID)
public class MSUPayloads {
    public static void register(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");

        registerPlayToClient(registrar, CommunistUpdatePacket.TYPE, CommunistUpdatePacket.STREAM_CODEC);
    }
    private static <T extends MSPacket.PlayToClient> void registerPlayToClient(PayloadRegistrar registrar, CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec)
    {
        registrar.playToClient(type, codec, MSPacket.PlayToClient::execute);
    }
}
