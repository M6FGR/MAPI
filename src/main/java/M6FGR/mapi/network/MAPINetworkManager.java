package M6FGR.mapi.network;

import M6FGR.mapi.cls.ILoadableClass;
import M6FGR.mapi.cls.Side;
import M6FGR.mapi.cls.SidedLoadableClass;
import M6FGR.mapi.network.packets.server.SPGameRuleSync;
import M6FGR.mapi.utils.code.CodeUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Cod;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;


public final class MAPINetworkManager implements ILoadableClass {

    private static PayloadRegistrar registrar;

    private static void registerChannel(RegisterPayloadHandlersEvent event) {
        registrar = event.registrar("1");
        registerPacket(SPGameRuleSync.TYPE, SPGameRuleSync.CODEC, SPGameRuleSync::handle, PayLoadType.SERVER);
    }


    public static void sendToPlayer(ServerPlayer player, CustomPacketPayload... payloads) {
        CodeUtils.forEach(player, payloads, PacketDistributor::sendToPlayer);
    }

    public static void sendToAllPlayers(CustomPacketPayload... payloads) {
        CodeUtils.forEach(payloads, PacketDistributor::sendToAllPlayers);
    }

    public static void sendToServer(CustomPacketPayload... payloads) {
        CodeUtils.forEach(payloads, PacketDistributor::sendToServer);
    }

    public static void sendToAllTrackingThisEntity(Entity entity, CustomPacketPayload... payloads) {
        if (payloads == null || payloads.length == 0) return;
        CodeUtils.forEach(entity, payloads, PacketDistributor::sendToPlayersTrackingEntity);
    }

    public static void sendToAllTrackingThisEntityAndSelf(Entity entity, CustomPacketPayload... payloads) {
        if (payloads == null || payloads.length == 0) return;
        CodeUtils.forEach(entity, payloads, PacketDistributor::sendToPlayersTrackingEntityAndSelf);
    }


    private static <T extends CustomPacketPayload> void registerPacket(Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, IPayloadHandler<T> handler, PayLoadType payLoadType) {
        switch (payLoadType) {
            case CLIENT -> registrar.playToServer(type, codec, handler);
            case SERVER -> registrar.playToClient(type, codec, handler);
            case BIDIRECTIONAL -> registrar.playBidirectional(type, codec, handler);
        }
    }

    @Override
    public void onModConstructor(IEventBus modBus) {
        modBus.addListener(MAPINetworkManager::registerChannel);
    }

    // ignored
    @Override public void onGameConstructor(IEventBus gameBus) {}

    public enum PayLoadType {
        // Sends to the client
        SERVER,
        // Sends to the server
        CLIENT,
        // Sends to both
        BIDIRECTIONAL
    }


}
