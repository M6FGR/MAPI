package m6fgr.mapi.events.mc.server;

import m6fgr.mapi.events.dispatch.DispatchableEvent;
import m6fgr.mapi.events.dispatch.extra.IDispatchableEvent;
import m6fgr.mapi.events.mc.MinecraftDispatchableEvents;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.players.PlayerList;

public class PingUpdateDispatchableEvent extends DispatchableEvent implements IDispatchableEvent {
    private PlayerList playerList;
    private ServerCommonPacketListenerImpl listener;
    private int pingUpdateTicks = 600;
    private int keepAliveTicks = 300;
    private boolean changedValue = false;

    public PingUpdateDispatchableEvent(PlayerList playerList) {
        this.playerList = playerList;
    }

    public PingUpdateDispatchableEvent(ServerCommonPacketListenerImpl listener) {
        this.listener = listener;
    }

    public PlayerList getPlayerList() {
        return this.playerList;
    }

    public ServerCommonPacketListenerImpl getListener() {
        return this.listener;
    }

    public int getPingUpdateTicks() {
        return pingUpdateTicks;
    }

    public int getKeepAliveTicks() {
        return this.keepAliveTicks;
    }

    public void setPingUpdateTicks(int ticks) {
        this.pingUpdateTicks = ticks;
    }

    public void setKeepAliveTick(int ticks) {
        this.keepAliveTicks = ticks;
    }

    public void setKeepAliveSeconds(int seconds) {
        this.setKeepAliveTick(seconds * 20);
    }

    public void setPingUpdateSeconds(int seconds) {
        this.setPingUpdateTicks(seconds * 20);
    }

    @Override
    public void postEvent() {
        MinecraftDispatchableEvents.PING_UPDATE.postEvent(this);
    }
}
