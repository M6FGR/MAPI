package m6fgr.mapi.events.dispatch.extra;

import org.jetbrains.annotations.ApiStatus.OverrideOnly;

@OverrideOnly
public interface IDispatchableEvent {
    void postEvent();

    default boolean shouldPostOnInitialize() {
        return false;
    }
}
