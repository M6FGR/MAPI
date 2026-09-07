package M6FGR.mapi.network.functions;

import M6FGR.mapi.network.functions.ICustomPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class PacketBufHandler {

    private static final String PROTOCOL_VERSION = "1";

    public static SimpleChannel createChannel(ResourceLocation channelName) {
        return NetworkRegistry.newSimpleChannel(
                channelName,
                () -> PROTOCOL_VERSION,
                PROTOCOL_VERSION::equals,
                PROTOCOL_VERSION::equals
        );
    }


    public static <T extends ICustomPacket<T>> void registerPacket(
            SimpleChannel channel,
            int index,
            Class<T> packetClass,
            BiConsumer<T, FriendlyByteBuf> encoder,
            Function<FriendlyByteBuf, T> decoder,
            BiConsumer<T, Supplier<NetworkEvent.Context>> handler,
            Optional<NetworkDirection> direction
    ) {
        channel.registerMessage(
                index,
                packetClass,
                encoder,
                decoder,
                handler,
                direction
        );
    }
}