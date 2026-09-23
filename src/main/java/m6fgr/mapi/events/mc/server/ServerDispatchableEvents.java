package m6fgr.mapi.events.mc.server;

import m6fgr.mapi.events.dispatch.DispatchableEvent;
import m6fgr.mapi.events.dispatch.dispatchers.marks.ClientEvent;
import m6fgr.mapi.events.dispatch.extra.IDispatchableEvent;
import m6fgr.mapi.events.mc.MinecraftDispatchableEvents;
import net.minecraft.network.protocol.common.ServerCommonPacketListener;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.players.PlayerList;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public abstract class ServerDispatchableEvents extends DispatchableEvent implements IDispatchableEvent {

    private final MinecraftServer server;

    public ServerDispatchableEvents(MinecraftServer server) {
        this.server = server;
    }

    public ServerDispatchableEvents() {
        this(ServerLifecycleHooks.getCurrentServer());
    }

    public MinecraftServer getServer() {
        return this.server;
    }

    public abstract void postEvent();

    public static class StartPre extends ServerDispatchableEvents {

        public StartPre(MinecraftServer server) {
            super(server);
        }

        @Override
        public void postEvent() {
            MinecraftDispatchableEvents.SERVER_START_PRE.postEvent(this);
        }
    }

    public static class StartPost extends ServerDispatchableEvents {

        public StartPost(MinecraftServer server) {
            super(server);
        }

        @Override
        public void postEvent() {
            MinecraftDispatchableEvents.SERVER_START_POST.postEvent(this);
        }
    }

    public static class Stop extends ServerDispatchableEvents {

        public Stop(MinecraftServer server) {
            super(server);
        }

        @Override
        public void postEvent() {
            MinecraftDispatchableEvents.SERVER_STOP.postEvent(this);
        }
    }

    public static class Tick extends ServerDispatchableEvents {

        public Tick(MinecraftServer server) {
            super(server);

        }

        @Override
        public void postEvent() {
            MinecraftDispatchableEvents.SERVER_TICK.postEvent(this);
        }
    }
}
