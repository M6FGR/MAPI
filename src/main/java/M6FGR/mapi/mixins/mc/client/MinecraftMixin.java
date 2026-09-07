package M6FGR.mapi.mixins.mc.client;

import M6FGR.mapi.events.mc.client.ClientDispatchableEvents;
import M6FGR.mapi.utils.code.CodeUtils;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Minecraft.class, remap = false, priority = 1005)
public abstract class MinecraftMixin {
    @Unique
    private final Minecraft mapi$Minecraft = (Minecraft) (Object) this;

    @Inject(
            at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/client/ClientHooks;fireClientTickPre()V"),
            method = "tick",
            remap = false
    )
    private void injectTickDE(CallbackInfo ci) {
        CodeUtils.construct(this.mapi$Minecraft, ClientDispatchableEvents.Tick::new);
    }

    @Inject(
            at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/client/ClientHooks;initClientHooks(Lnet/minecraft/client/Minecraft;Lnet/minecraft/server/packs/resources/ReloadableResourceManager;)V"),
            method = "<init>",
            remap = false
    )
    private void injectStartDE(CallbackInfo callbackInfo) {
        CodeUtils.construct(this.mapi$Minecraft, ClientDispatchableEvents.Start::new);
    }

    @Inject(
            at = @At("HEAD"),
            method = "stop",
            remap = false
    )
    private void injectStopDE(CallbackInfo ci) {
        CodeUtils.construct(this.mapi$Minecraft, ClientDispatchableEvents.Stop::new);
    }
}
