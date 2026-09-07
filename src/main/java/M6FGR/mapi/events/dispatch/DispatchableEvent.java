package M6FGR.mapi.events.dispatch;


import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;

public abstract class DispatchableEvent extends Event implements IModBusEvent {

    protected boolean canceled;

    protected DispatchableEvent() {}

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
