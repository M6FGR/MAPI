package M6FGR.mapi.events.dispatch;

import M6FGR.mapi.cls.Comment;
import M6FGR.mapi.events.dispatch.extra.IDispatchableEvent;
import M6FGR.mapi.main.MAPI;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;
import org.checkerframework.checker.units.qual.C;

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
