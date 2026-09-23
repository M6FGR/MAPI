package m6fgr.mapi.mixins.mc.server;

import m6fgr.mapi.events.mc.server.PingUpdateDispatchableEvent;
import net.minecraft.Util;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundKeepAlivePacket;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerCommonPacketListenerImpl.class)
public abstract class ServerCommonPacketListenerImplMixin {

    @Shadow private long keepAliveTime;
    @Shadow private boolean keepAlivePending;
    @Shadow private long keepAliveChallenge;


    @Shadow
    @Final
    protected Connection connection;

    @Shadow
    public abstract void send(Packet<?> packet);

    @Shadow
    public abstract void disconnect(Component reason);

    @Shadow
    protected abstract boolean isSingleplayerOwner();

    @Shadow
    protected abstract boolean checkIfClosed(long time);

    @Inject(method = "keepConnectionAlive", at = @At("HEAD"), cancellable = true)
    private void onKeepConnectionAlive(CallbackInfo ci) {
        ServerCommonPacketListenerImpl self = (ServerCommonPacketListenerImpl) (Object) this;

        if (!this.connection.isConnected()) {
            ci.cancel();
            return;
        }

        PingUpdateDispatchableEvent pingUpdate = new PingUpdateDispatchableEvent(self);
        pingUpdate.postEvent();

        long now = Util.getMillis();

        if (this.keepAliveTime == 0L) {
            this.keepAliveTime = now;
            ci.cancel();
            return;
        }

        long intervalMs = pingUpdate.getKeepAliveTicks() * 50L;

        if (now - this.keepAliveTime >= intervalMs &&! this.isSingleplayerOwner()) {
            if (this.keepAlivePending) {
                if (now - this.keepAliveTime >= 15000L) {
                    this.disconnect(Component.literal("Timed out (no keep-alive response received for: " + intervalMs + "ms long"));
                }
            } else if (this.checkIfClosed(now)) {
                this.keepAlivePending = true;
                this.keepAliveTime = now;
                this.keepAliveChallenge = now;

                this.send(new ClientboundKeepAlivePacket(this.keepAliveChallenge));
            }
        }

        ci.cancel();
    }
}