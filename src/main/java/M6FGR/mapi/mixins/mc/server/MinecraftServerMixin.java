package M6FGR.mapi.mixins.mc.server;

import M6FGR.mapi.events.mc.server.ServerDispatchableEvents;
import M6FGR.mapi.utils.code.CodeUtils;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(value = MinecraftServer.class, remap = false, priority = 1004)
public abstract class MinecraftServerMixin {

    @Unique
    private final MinecraftServer mapi$MinecraftServer = (MinecraftServer) (Object) this;

    @Inject(
            at = @At("HEAD"),
            method = "tickServer",
            remap = false
    )

    private void onServerTick(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        CodeUtils.construct(this.mapi$MinecraftServer, ServerDispatchableEvents.Tick::new);
    }

}
