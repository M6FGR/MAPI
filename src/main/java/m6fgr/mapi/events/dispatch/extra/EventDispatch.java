package m6fgr.mapi.events.dispatch.extra;

import m6fgr.mapi.events.dispatch.DispatchableEvent;

@FunctionalInterface
public interface EventDispatch<T extends DispatchableEvent> {
    void dispatch(T event);
}
