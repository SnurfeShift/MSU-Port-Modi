package com.shift.minestuckuniverse.network;

import com.mraof.minestuck.network.MSPacket;
import com.shift.minestuckuniverse.MinestuckUniverseModus;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

//Credits to Minestuck Team, Lunar Sway for MSPackets and MSPayloads
@EventBusSubscriber(modid = MinestuckUniverseModus.MOD_ID)
public class MSUPayloads {
    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");

        registerPlayToClient(registrar, CommunistUpdatePacket.ID, CommunistUpdatePacket.STREAM_CODEC);
        registerPlayToServer(registrar, CommunistRequestPacket.ID, CommunistRequestPacket.STREAM_CODEC);
    }

    private static <T extends MSPacket.PlayToClient> void registerPlayToClient(PayloadRegistrar registrar, CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec)
    {
        registrar.playToClient(type, codec, MSPacket.PlayToClient::execute);
    }
    private static <T extends MSPacket.PlayToServer> void registerPlayToServer(PayloadRegistrar registrar, CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec)
    {
        registrar.playToServer(type, codec, (t, context) -> t.execute(context, (ServerPlayer) context.player()));
    }
}
