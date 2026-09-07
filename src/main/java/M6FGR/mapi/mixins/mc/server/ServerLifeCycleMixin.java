package M6FGR.mapi.mixins.mc.server;

import M6FGR.mapi.events.mc.server.ServerDispatchableEvents;
import M6FGR.mapi.utils.code.CodeUtils;
import net.minecraft.server.MinecraftServer;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ServerLifecycleHooks.class, remap = false, priority = 1005)
public class ServerLifeCycleMixin {

    @Inject(
            at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/server/permission/PermissionAPI;initializePermissionAPI()V"),
            method = "handleServerStarting",
            remap = false
    )
    private static void onServerStartPre(MinecraftServer server, CallbackInfo ci) {
        CodeUtils.construct(server, ServerDispatchableEvents.StartPre::new);
    }

    @Inject(
            at = @At("HEAD"),
            method = "handleServerStarted",
            remap = false
    )

    private static void onServerStartPost(MinecraftServer server, CallbackInfo ci) {
        CodeUtils.construct(server, ServerDispatchableEvents.StartPost::new);
    }

    @Inject(
            at = @At("HEAD"),
            method = "handleServerStopping",
            remap = false
    )
    private static void onServerStop(MinecraftServer server, CallbackInfo ci) {
        CodeUtils.construct(server, ServerDispatchableEvents.Stop::new);
    }
}
