package m6fgr.mapi.mixins.mc.client;

import m6fgr.mapi.events.mc.client.ClientDispatchableEvents;
import m6fgr.mapi.events.mc.client.ClientDispatchableEvents.Start;
import m6fgr.mapi.events.mc.client.ClientDispatchableEvents.Tick;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Minecraft.class, remap = false, priority = 1005)
public abstract class MinecraftMixin {

    @Inject(
            at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/client/ClientHooks;fireClientTickPre()V"),
            method = "tick",
            remap = false
    )
    private void injectTickDE(CallbackInfo ci) {
        ClientDispatchableEvents.Tick tick = new Tick((Minecraft) (Object) this);
        tick.postEvent();
    }

    @Inject(
            at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/client/ClientHooks;initClientHooks(Lnet/minecraft/client/Minecraft;Lnet/minecraft/server/packs/resources/ReloadableResourceManager;)V"),
            method = "<init>",
            remap = false
    )
    private void injectStartDE(CallbackInfo callbackInfo) {
        ClientDispatchableEvents.Start start = new Start((Minecraft) (Object) this);
        start.postEvent();
    }
}
