package m6fgr.mapi.events.dispatch.dispatchers.entries;


import m6fgr.mapi.events.dispatch.DispatchableEvent;
import m6fgr.mapi.events.dispatch.extra.EventDispatch;
import m6fgr.mapi.events.dispatch.extra.EventPriority;

public record ListenerEntry<T extends DispatchableEvent>(EventDispatch<T> listener, EventPriority priority, int sequence) {}

