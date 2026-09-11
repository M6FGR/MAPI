package m6fgr.mapi.mixins.mc;

import m6fgr.mapi.events.mc.player.PlayerDispatchableEvents;
import m6fgr.mapi.utils.code.CodeUtils;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Player.class, priority = 1005)
public abstract class PlayerMixin {

    @Unique
    private final Player mapi$Player = (Player) (Object) this;

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    private void injectTickDE(CallbackInfo ci) {
        CodeUtils.construct(this.mapi$Player, PlayerDispatchableEvents.Tick::new);
    }

    @Inject(
            method = "die",
            remap = false,
            at = @At("HEAD")
    )
    private void injectDeathDE(DamageSource cause, CallbackInfo ci) {
        CodeUtils.construct(this.mapi$Player, cause, PlayerDispatchableEvents.Death::new);
    }
}
