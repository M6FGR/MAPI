package m6fgr.mapi.events.mc;

import m6fgr.mapi.events.dispatch.dispatchers.EventDispatcher;
import m6fgr.mapi.events.dispatch.extra.EventSide;
import m6fgr.mapi.events.mc.client.ClientDispatchableEvents;
import m6fgr.mapi.events.mc.player.PlayerDispatchableEvents;
import m6fgr.mapi.events.mc.server.ServerDispatchableEvents;

public final class MinecraftDispatchableEvents {

    private MinecraftDispatchableEvents() {}


    // Server Events
    public static final EventDispatcher<ServerDispatchableEvents.StartPre> SERVER_START_PRE = EventDispatcher.createDispatch(ServerDispatchableEvents.StartPre.class, EventSide.SERVER);
    public static final EventDispatcher<ServerDispatchableEvents.StartPost> SERVER_START_POST = EventDispatcher.createDispatch(ServerDispatchableEvents.StartPost.class, EventSide.SERVER);
    public static final EventDispatcher<ServerDispatchableEvents.Tick> SERVER_TICK = EventDispatcher.createDispatch(ServerDispatchableEvents.Tick.class, EventSide.SERVER);
    public static final EventDispatcher<ServerDispatchableEvents.Stop> SERVER_STOP = EventDispatcher.createDispatch(ServerDispatchableEvents.Stop.class, EventSide.SERVER);

    // Client Events
    public static final EventDispatcher<ClientDispatchableEvents.Start> CLIENT_START = EventDispatcher.createDispatch(ClientDispatchableEvents.Start.class, EventSide.CLIENT);
    public static final EventDispatcher<ClientDispatchableEvents.Stop> CLIENT_STOP = EventDispatcher.createDispatch(ClientDispatchableEvents.Stop.class, EventSide.CLIENT);
    public static final EventDispatcher<ClientDispatchableEvents.Tick> CLIENT_TICK = EventDispatcher.createDispatch(ClientDispatchableEvents.Tick.class, EventSide.CLIENT);

    // Player Events
    public static final EventDispatcher<PlayerDispatchableEvents.Tick> PLAYER_TICK = EventDispatcher.createDispatch(PlayerDispatchableEvents.Tick.class);
    public static final EventDispatcher<PlayerDispatchableEvents.Death> PLAYER_DEATH = EventDispatcher.createDispatch(PlayerDispatchableEvents.Death.class);
    public static final EventDispatcher<PlayerDispatchableEvents.JoinServer> PLAYER_JOIN_SERVER = EventDispatcher.createDispatch(PlayerDispatchableEvents.JoinServer.class);

    public static final EventDispatcher<PlayerDispatchableEvents.JoinClient> PLAYER_JOIN_CLIENT = EventDispatcher.createDispatch(PlayerDispatchableEvents.JoinClient.class, EventSide.CLIENT);
    public static final EventDispatcher<PlayerDispatchableEvents.ItemUse> PLAYER_ITEM_USE = EventDispatcher.createDispatch(PlayerDispatchableEvents.ItemUse.class, EventSide.CLIENT);



}
