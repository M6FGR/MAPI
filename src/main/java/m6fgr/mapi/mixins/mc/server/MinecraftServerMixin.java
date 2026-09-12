package m6fgr.mapi.mixins.mc.server;

import m6fgr.mapi.events.mc.server.ServerDispatchableEvents;
import m6fgr.mapi.events.mc.server.ServerDispatchableEvents.Tick;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(value = MinecraftServer.class, remap = false, priority = 1004)
public abstract class MinecraftServerMixin {


    @Inject(
            at = @At("HEAD"),
            method = "tickServer",
            remap = false
    )

    private void onServerTick(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        ServerDispatchableEvents.Tick tick = new Tick((MinecraftServer) (Object) this);
        tick.postEvent();
    }

}
