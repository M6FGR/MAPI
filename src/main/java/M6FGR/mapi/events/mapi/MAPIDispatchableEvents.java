package M6FGR.mapi.events.mapi;

import M6FGR.mapi.events.dispatch.dispatchers.EventDispatcher;
import M6FGR.mapi.events.mapi.registries.GameRulesRegistryDispatchableEvent;

public final class MAPIDispatchableEvents {
    private MAPIDispatchableEvents() {}

    // Registry Events
    public static final EventDispatcher<GameRulesRegistryDispatchableEvent> GAMERULES_REGISTRY = EventDispatcher.createDispatch(GameRulesRegistryDispatchableEvent.class);
}
