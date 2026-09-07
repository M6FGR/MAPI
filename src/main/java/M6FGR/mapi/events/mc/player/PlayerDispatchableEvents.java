package M6FGR.mapi.events.mc.player;

import M6FGR.mapi.events.dispatch.DispatchableEvent;
import M6FGR.mapi.events.dispatch.dispatchers.marks.ClientEvent;
import M6FGR.mapi.events.dispatch.extra.IDispatchableEvent;
import M6FGR.mapi.events.mc.MinecraftDispatchableEvents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.player.PlayerEnchantItemEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public abstract class PlayerDispatchableEvents extends DispatchableEvent implements IDispatchableEvent {

    protected final Player player;

    public PlayerDispatchableEvents(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    public abstract void postEvent();

    public static class Tick extends PlayerDispatchableEvents {

        public Tick(Player player) {
            super(player);
        }

        @Override
        public void postEvent() {
            MinecraftDispatchableEvents.PLAYER_TICK.postEvent(this);
        }
    }

    public static class JoinServer extends PlayerDispatchableEvents {

        private final ServerLevel serverLevel;

        public JoinServer(ServerPlayer player, ServerLevel serverLevel) {
            super(player);
            this.serverLevel = serverLevel;
        }

        public ServerLevel getServerLevel() {
            return this.serverLevel;
        }

        public ServerPlayer getServerPlayer() {
            return (ServerPlayer) this.player;
        }

        @Override
        public void postEvent() {
            MinecraftDispatchableEvents.PLAYER_JOIN_SERVER.postEvent(this);
        }
    }

    public static class Death extends PlayerDispatchableEvents {

        private final DamageSource cause;

        public Death(Player player, DamageSource cause) {
            super(player);
            this.cause = cause;
        }

        public DamageSource getCause() {
            return this.cause;
        }

        @Override
        public void postEvent() {
            MinecraftDispatchableEvents.PLAYER_DEATH.postEvent(this);
        }
    }

    @ClientEvent
    public static class ItemUse extends PlayerDispatchableEvents {

        private final ItemStack item;

        public ItemUse(LocalPlayer player, ItemStack item) {
            super(player);
            this.item = item;
        }


        public ItemStack getUsingItem() {
            return this.item;
        }

        @Override
        public void postEvent() {
            MinecraftDispatchableEvents.PLAYER_ITEM_USE.postEvent(this);
        }
    }

    @ClientEvent
    public static class JoinClient extends PlayerDispatchableEvents {

        private final ClientLevel clientLevel;

        public JoinClient(Player player, ClientLevel clientLevel) {
            super(player);
            this.clientLevel = clientLevel;
        }

        public ClientLevel getClientLevel() {
            return this.clientLevel;
        }

        @Override
        public void postEvent() {
            MinecraftDispatchableEvents.PLAYER_JOIN_CLIENT.postEvent(this);
        }
    }
}
