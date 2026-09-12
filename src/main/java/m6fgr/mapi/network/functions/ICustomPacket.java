package m6fgr.mapi.network.functions;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public interface ICustomPacket<T extends CustomPacketPayload> extends CustomPacketPayload {

    void handle(IPayloadContext ctx);

    void encode(FriendlyByteBuf buf);

    T decode(FriendlyByteBuf buf);

    @Override
    Type<T> type();
}
