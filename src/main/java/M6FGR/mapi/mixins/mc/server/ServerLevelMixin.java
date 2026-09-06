package M6FGR.mapi.mixins.mc.server;

import M6FGR.mapi.events.mc.player.PlayerDispatchableEvents;
import M6FGR.mapi.events.mc.player.PlayerDispatchableEvents.JoinServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ServerLevel.class, remap = false, priority = 1005)
public class ServerLevelMixin {
    @Inject(
            at = @At("HEAD"),
            method = "addPlayer",
            remap = false
    )

    private void injectPlayerJoinServer(ServerPlayer player, CallbackInfo ci) {
        PlayerDispatchableEvents.JoinServer joinServer = new JoinServer(player, (ServerLevel) (Object) this);
        joinServer.postEvent();
    }
}
