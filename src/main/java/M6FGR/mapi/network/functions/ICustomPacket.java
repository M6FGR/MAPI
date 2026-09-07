package M6FGR.mapi.network.functions;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public interface ICustomPacket<T> {

    void encode(FriendlyByteBuf buf);

    void handle(Supplier<NetworkEvent.Context> ctx);
}