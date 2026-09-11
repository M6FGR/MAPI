package m6fgr.mapi.mixins.mc.server;

import m6fgr.mapi.events.mc.player.PlayerDispatchableEvents;
import m6fgr.mapi.utils.code.CodeUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ServerLevel.class, remap = false, priority = 1005)
public class ServerLevelMixin {

    @Unique
    private final ServerLevel mapi$ServerLevel = (ServerLevel) (Object) this;

    @Inject(
            at = @At("HEAD"),
            method = "addPlayer",
            remap = false
    )

    private void injectPlayerJoinServer(ServerPlayer player, CallbackInfo ci) {
        CodeUtils.construct(player, this.mapi$ServerLevel, PlayerDispatchableEvents.JoinServer::new);
    }
}
