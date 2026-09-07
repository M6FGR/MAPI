package M6FGR.mapi.events.dispatch.extra;

import M6FGR.mapi.events.dispatch.DispatchableEvent;

@FunctionalInterface
public interface EventDispatch<T extends DispatchableEvent> {
    void dispatch(T event);
}
