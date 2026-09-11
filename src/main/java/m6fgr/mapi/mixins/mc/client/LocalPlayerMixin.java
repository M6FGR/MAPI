package m6fgr.mapi.mixins.mc.client;

import m6fgr.mapi.events.mc.player.PlayerDispatchableEvents;
import m6fgr.mapi.utils.code.CodeUtils;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LocalPlayer.class, remap = false, priority = 1005)
public class LocalPlayerMixin extends AbstractClientPlayer {
    @Unique
    private final LocalPlayer mapi$LocalPlayer = (LocalPlayer) (Object) this;

    public LocalPlayerMixin(ClientLevel clientLevel, GameProfile gameProfile) {
        super(clientLevel, gameProfile);
    }

    @Inject(
            method = "startUsingItem",
            remap = false,
            at = @At(value = "TAIL")
    )
    private void injectUsingItemDE(InteractionHand hand, CallbackInfo ci) {
        CodeUtils.construct(this.mapi$LocalPlayer, this.getItemInHand(hand), PlayerDispatchableEvents.ItemUse::new);
    }
}
