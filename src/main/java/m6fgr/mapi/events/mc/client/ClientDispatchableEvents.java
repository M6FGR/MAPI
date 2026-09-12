package m6fgr.mapi.events.mc.client;

import m6fgr.mapi.events.dispatch.DispatchableEvent;
import m6fgr.mapi.events.dispatch.dispatchers.marks.ClientEvent;
import m6fgr.mapi.events.dispatch.extra.IDispatchableEvent;
import m6fgr.mapi.events.mc.MinecraftDispatchableEvents;
import net.minecraft.client.Minecraft;

@ClientEvent
public abstract class ClientDispatchableEvents extends DispatchableEvent implements IDispatchableEvent {

    protected final Minecraft mc;

    protected ClientDispatchableEvents(Minecraft mc) {
        this.mc = mc;
    }

    public Minecraft getMinecraft() {
        return this.mc;
    }

    public abstract void postEvent();

    public static class Start extends ClientDispatchableEvents {

        public Start(Minecraft mc) {
            super(mc);
        }

        @Override
        public void postEvent() {
            MinecraftDispatchableEvents.CLIENT_START.postEvent(this);
        }
    }

    public static class Stop extends ClientDispatchableEvents {

        public Stop(Minecraft mc) {
            super(mc);
        }

        @Override
        public void postEvent() {
            MinecraftDispatchableEvents.CLIENT_STOP.postEvent(this);
        }
    }

    public static class Tick extends ClientDispatchableEvents {

        public Tick(Minecraft mc) {
            super(mc);
        }

        @Override
        public void postEvent() {
            MinecraftDispatchableEvents.CLIENT_TICK.postEvent(this);
        }
    }
}
