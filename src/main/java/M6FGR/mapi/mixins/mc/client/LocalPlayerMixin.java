package M6FGR.mapi.mixins.mc.client;

import M6FGR.mapi.events.mc.player.PlayerDispatchableEvents;
import M6FGR.mapi.events.mc.player.PlayerDispatchableEvents.ItemUse;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LocalPlayer.class, remap = false, priority = 1005)
public class LocalPlayerMixin extends AbstractClientPlayer {

    public LocalPlayerMixin(ClientLevel clientLevel, GameProfile gameProfile) {
        super(clientLevel, gameProfile);
    }

    @Inject(
            method = "startUsingItem",
            remap = false,
            at = @At(value = "TAIL")
    )
    private void injectUsingItemDE(InteractionHand hand, CallbackInfo ci) {
        PlayerDispatchableEvents.ItemUse itemUse = new ItemUse((LocalPlayer) (Object) this, this.getItemInHand(hand));
        itemUse.postEvent();
    }
}
