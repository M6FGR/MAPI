package m6fgr.mapi.events.dispatch;

import m6fgr.mapi.cls.Comment;
import m6fgr.mapi.events.dispatch.extra.IDispatchableEvent;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;

public abstract class DispatchableEvent extends Event implements IModBusEvent {

    protected boolean canceled;

    @Comment({
            "Here, it automatically posts the event once constructed if it implements IDispatchableEvent,",
            "But make sure postEvent(); is not empty!"
    })
    protected DispatchableEvent() {
        if (this instanceof IDispatchableEvent iDispatchableEvent) {
            iDispatchableEvent.postEvent();
        }
    }

    public boolean isCanceled() {
        return this.canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public String getEventName() {
        Class<?> superCls = this.getClass().getSuperclass();
        String eventName = this.getClass().getSimpleName();
        String superEventName = superCls.getSimpleName();
        return superCls != DispatchableEvent.class ? superEventName + "#" + eventName : eventName;
    }
}
