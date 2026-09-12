package m6fgr.mapi.network.functions;

import m6fgr.mapi.utils.code.CodeUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.StreamMemberEncoder;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface PacketBufHandler extends CustomPacketPayload {

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
}