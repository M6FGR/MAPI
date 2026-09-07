package M6FGR.mapi.network;

import M6FGR.mapi.cls.ILoadableClass;
import M6FGR.mapi.main.MAPI;
import M6FGR.mapi.network.functions.PacketBufHandler;
import M6FGR.mapi.network.packets.server.SPGameRuleSync;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class MAPINetworkManager implements ILoadableClass {

    private static SimpleChannel CHANNEL;
    private static int packetId = 0;

    @Override
    public void onModConstructor(IEventBus modBus) {
        modBus.addListener(this::commonSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            CHANNEL = PacketBufHandler.createChannel(MAPI.getInstance().identifier("main"));

            registerPacket(
                    SPGameRuleSync.class,
                    SPGameRuleSync::encode,
                    SPGameRuleSync::decode,
                    SPGameRuleSync::handle,
                    PayLoadType.SERVER
            );
        });
    }

    public static <MSG> void sendToPlayer(ServerPlayer player, MSG message) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToAllPlayers(MSG message) {
        CHANNEL.send(PacketDistributor.ALL.noArg(), message);
    }

    public static <MSG> void sendToServer(MSG message) {
        CHANNEL.sendToServer(message);
    }

    public static <MSG> void sendToAllTrackingThisEntity(Entity entity, MSG message) {
        if (entity == null) return;
        CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
    }

    public static <MSG> void sendToAllTrackingThisEntityAndSelf(Entity entity, MSG message) {
        if (entity == null) return;
        CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), message);
    }

    private static <T> void registerPacket(
            Class<T> packetClass,
            BiConsumer<T, FriendlyByteBuf> encoder,
            Function<FriendlyByteBuf, T> decoder,
            BiConsumer<T, Supplier<net.minecraftforge.network.NetworkEvent.Context>> handler,
            PayLoadType payLoadType
    ) {
        Optional<NetworkDirection> direction = switch (payLoadType) {
            case CLIENT -> Optional.of(NetworkDirection.PLAY_TO_SERVER);
            case SERVER -> Optional.of(NetworkDirection.PLAY_TO_CLIENT);
            case BIDIRECTIONAL -> Optional.empty();
        };

        CHANNEL.registerMessage(
                packetId++,
                packetClass,
                encoder,
                decoder,
                handler,
                direction
        );
    }

    public enum PayLoadType {
        // Sends to the client
        SERVER,
        // Sends to the server
        CLIENT,
        // Sends to both
        BIDIRECTIONAL
    }
}