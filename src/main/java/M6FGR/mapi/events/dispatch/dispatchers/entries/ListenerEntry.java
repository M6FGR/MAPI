package M6FGR.mapi.events.dispatch.dispatchers.entries;


import M6FGR.mapi.events.dispatch.DispatchableEvent;
import M6FGR.mapi.events.dispatch.extra.EventDispatch;
import M6FGR.mapi.events.dispatch.extra.EventPriority;

public record ListenerEntry<T extends DispatchableEvent>(EventDispatch<T> listener, EventPriority priority, int sequence) {}

