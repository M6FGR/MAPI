package m6fgr.mapi.mixins.mc;

import m6fgr.mapi.events.mc.player.PlayerDispatchableEvents;
import m6fgr.mapi.events.mc.player.PlayerDispatchableEvents.Death;
import m6fgr.mapi.events.mc.player.PlayerDispatchableEvents.Tick;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Player.class, remap = false, priority = 1005)
public abstract class PlayerMixin {


    @Inject(
            method = "tick",
            remap = false,
            at = @At("HEAD")
    )
    private void injectTickDE(CallbackInfo ci) {
        PlayerDispatchableEvents.Tick tick = new Tick((Player) (Object) this);
        tick.postEvent();
    }

    @Inject(
            method = "die",
            remap = false,
            at = @At("HEAD")
    )
    private void injectDeathDE(DamageSource cause, CallbackInfo ci) {
        PlayerDispatchableEvents.Death death = new Death((Player) (Object) this, cause);
        death.postEvent();
    }

}
