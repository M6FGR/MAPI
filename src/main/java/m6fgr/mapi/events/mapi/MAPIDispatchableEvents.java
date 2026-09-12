package m6fgr.mapi.events.mapi;

import m6fgr.mapi.events.dispatch.dispatchers.EventDispatcher;
import m6fgr.mapi.events.mapi.registries.GameRulesRegistryDispatchableEvent;

public final class MAPIDispatchableEvents {
    private MAPIDispatchableEvents() {}

    // Registry Events
    public static final EventDispatcher<GameRulesRegistryDispatchableEvent> GAMERULES_REGISTRY = EventDispatcher.createDispatch(GameRulesRegistryDispatchableEvent.class);
}
