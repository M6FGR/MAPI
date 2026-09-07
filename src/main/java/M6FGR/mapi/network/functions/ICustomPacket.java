package M6FGR.mapi.network.functions;

import M6FGR.mapi.utils.code.CodeUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.StreamMemberEncoder;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.system.NonnullDefault;

public interface ICustomPacket<T extends CustomPacketPayload> extends CustomPacketPayload {


    static <B extends ByteBuf, T extends CustomPacketPayload> StreamCodec<B, T> codecOf(
            Class<T> packetCls,
            StreamMemberEncoder<B, T> encoder,
            StreamMemberDecoder<B, T> decoder
    ) {
        return StreamCodec.ofMember(
                encoder,
                buf -> {
                    T instance = CodeUtils.newInstance(packetCls);
                    return decoder.decode(instance, buf);
                }
        );
    }


    void handle(IPayloadContext ctx);

    void encode(FriendlyByteBuf buf);

    T decode(FriendlyByteBuf buf);

    @Override
    Type<T> type();
}
