package M6FGR.mapi.events.mc.server;

import M6FGR.mapi.events.dispatch.DispatchableEvent;
import M6FGR.mapi.events.dispatch.dispatchers.marks.ClientEvent;
import M6FGR.mapi.events.dispatch.extra.IDispatchableEvent;
import M6FGR.mapi.events.mc.MinecraftDispatchableEvents;
import net.minecraft.server.MinecraftServer;

@ClientEvent
public abstract class ServerDispatchableEvents extends DispatchableEvent implements IDispatchableEvent {

    private final MinecraftServer server;

    public ServerDispatchableEvents(MinecraftServer server) {
        this.server = server;
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
