package M6FGR.mapi.mixins.mc.server;

import M6FGR.mapi.events.mc.server.ServerDispatchableEvents;
import M6FGR.mapi.events.mc.server.ServerDispatchableEvents.StartPost;
import M6FGR.mapi.events.mc.server.ServerDispatchableEvents.StartPre;
import M6FGR.mapi.events.mc.server.ServerDispatchableEvents.Stop;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ServerLifecycleHooks.class, remap = false, priority = 1005)
public class ServerLifeCycleMixin {

    @Inject(
            at = @At(value = "INVOKE", target = "Lnet/minecraftforge/server/permission/PermissionAPI;initializePermissionAPI()V"),
            method = "handleServerStarting",
            remap = false
    )
    private static void onServerStartPre(MinecraftServer server, CallbackInfoReturnable<Boolean> cir) {
        ServerDispatchableEvents.StartPre startPre = new StartPre(server);
        startPre.postEvent();
    }

    @Inject(
            at = @At("HEAD"),
            method = "handleServerStarted",
            remap = false
    )

    private static void onServerStartPost(MinecraftServer server, CallbackInfo ci) {
        ServerDispatchableEvents.StartPost startPost = new StartPost(server);
        startPost.postEvent();
    }

    @Inject(
            at = @At("HEAD"),
            method = "handleServerStopping",
            remap = false
    )
    private static void onServerStop(MinecraftServer server, CallbackInfo ci) {
        ServerDispatchableEvents.Stop stop = new Stop(server);
        stop.postEvent();
    }
}
