package m6fgr.mapi.mixins.mc.client;

import m6fgr.mapi.events.mc.player.PlayerDispatchableEvents;
import m6fgr.mapi.events.mc.player.PlayerDispatchableEvents.JoinClient;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ClientLevel.class, remap = false, priority = 1005)
public class ClientLevelMixin {

    @Inject(
            at = @At("HEAD"),
            method = "addEntity",
            remap = false
    )
    private void injectPlayerJoinDE(Entity entity, CallbackInfo ci) {
        if (entity instanceof Player player) {
            PlayerDispatchableEvents.JoinClient joinClient = new JoinClient(player, (ClientLevel) (Object) this);
            joinClient.postEvent();
        }
    }

}
