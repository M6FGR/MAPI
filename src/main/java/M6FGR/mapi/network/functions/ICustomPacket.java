package M6FGR.mapi.network.functions;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.system.NonnullDefault;

public interface ICustomPacket<T extends CustomPacketPayload> extends CustomPacketPayload {

    void handle(IPayloadContext ctx);

    void encode(FriendlyByteBuf buf);

    T decode(FriendlyByteBuf buf);

    @Override
    Type<T> type();
}
