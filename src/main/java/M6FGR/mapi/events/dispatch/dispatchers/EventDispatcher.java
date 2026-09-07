package M6FGR.mapi.events.dispatch.dispatchers;

import M6FGR.mapi.cls.exceptions.IllegalSideException;
import M6FGR.mapi.events.dispatch.DispatchableEvent;
import M6FGR.mapi.events.dispatch.dispatchers.entries.ListenerEntry;
import M6FGR.mapi.events.dispatch.dispatchers.marks.ClientEvent;
import M6FGR.mapi.events.dispatch.dispatchers.marks.HybridEvent;
import M6FGR.mapi.events.dispatch.extra.EventDispatch;
import M6FGR.mapi.events.dispatch.extra.EventPriority;
import M6FGR.mapi.events.dispatch.extra.EventSide;
import M6FGR.mapi.utils.environment.EnvironmentHelper;
import M6FGR.mapi.utils.code.CodeUtils;
import net.neoforged.fml.ModLoader;
import net.neoforged.neoforge.common.NeoForge;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class EventDispatcher<T extends DispatchableEvent> {

    protected final EventSide side;
    protected final Class<T> eventType;
    protected final List<ListenerEntry<T>> listeners = new CopyOnWriteArrayList<>();
    private final AtomicInteger sequenceGenerator = new AtomicInteger();

    protected EventDispatcher(Class<T> eventType, EventSide side) {
        this.eventType = eventType;
        this.side = side;
    }

    protected EventDispatcher(Class<T> eventType) {
        this(eventType, EventSide.COMMON);
    }

    public static <T extends DispatchableEvent> EventDispatcher<T> createDispatch(Class<T> eventType) {
        return new EventDispatcher<>(eventType);
    }

    public static <T extends DispatchableEvent> EventDispatcher<T> createDispatch(Class<T> eventType, EventSide side) {
        return new EventDispatcher<>(eventType, side);
    }

    public EventDispatcher<T> dispatchEvent(EventDispatch<T> listener) {
        return this.dispatchEvent(listener, EventPriority.MIDDLE);
    }

    public EventDispatcher<T> dispatchEvent(EventDispatch<T> listener, EventPriority priority) {
        if (this.eventType != null && CodeUtils.hasAnnotationSuper(this.eventType, ClientEvent.class)) {
            if (!EnvironmentHelper.DIST.isClient()) {
                throw new IllegalSideException("Cannot register listener for @ClientEvent ["
                        + this.getFormattedClassName(this.eventType) + "] in a non-client physical environment: "
                        + EnvironmentHelper.DIST);
            }

            if (this.side != EventSide.CLIENT) {
                throw new IllegalSideException("Cannot register listener for @ClientEvent ["
                        + this.getFormattedClassName(this.eventType) + "] on non-client side dispatcher: " + this.side);
            }

            if (!this.side.canDispatch()) {
                throw new IllegalStateException("Cannot register listener for @ClientEvent ["
                        + this.getFormattedClassName(this.eventType) + "] because side " + this.side + " couldn't dispatch.");
            }
        }

        if (priority == EventPriority.OVERRIDE) {
            this.listeners.removeIf(entry -> entry.priority() != priority);
        }
        int sequence = this.sequenceGenerator.incrementAndGet();
        this.addListener(new ListenerEntry<>(listener, priority, sequence));

        this.listeners.sort(
                Comparator.<ListenerEntry<T>>comparingInt(entry -> entry.priority().getNumber())
                        .reversed()
                        .thenComparingInt(ListenerEntry::sequence)
        );
        return this;
    }

    private String getFormattedClassName(Class<?> clazz) {
        return clazz.getSuperclass() != null
                ? clazz.getSuperclass().getSimpleName() + "#" + clazz.getSimpleName()
                : clazz.getSimpleName();
    }

    public void removeEvent(EventDispatch<T> listener) {
        this.listeners.removeIf(entry -> entry.listener().equals(listener));
    }

    public void forEachListener(Consumer<ListenerEntry<T>> eachListener) {
        this.listeners.forEach(eachListener);
    }

    public void clearListeners() {
        this.listeners.clear();
    }

    public void addListener(ListenerEntry<T> entry) {
        this.listeners.add(entry);
    }

    public T postEvent(T dispatchableEvent) {
        if (dispatchableEvent == null) return null;

        Class<?> eventClass = dispatchableEvent.getClass();

        boolean clientOnly = CodeUtils.hasAnnotationSuper(eventClass, ClientEvent.class);
        if (clientOnly && this.side != EventSide.CLIENT) {
            throw new IllegalSideException("Tried to post client-only event [" + this.getFormattedClassName(this.eventType) + "] for side: " + this.side);
        }

        boolean isHybrid = CodeUtils.hasAnnotationSuper(eventClass, HybridEvent.class);
        if (isHybrid) {
            HybridEvent hybridEvent = eventClass.getAnnotation(HybridEvent.class);
            if (hybridEvent != null) {
                switch (hybridEvent.value()) {
                    case MOD -> ModLoader.postEvent(dispatchableEvent);
                    case GAME -> NeoForge.EVENT_BUS.post(dispatchableEvent);
                    case BOTH -> EventDispatcher.postToAllBusses(dispatchableEvent);
                    default -> throw new IllegalArgumentException("Cannot accept any other than MOD, GAME and BOTH event busses");
                }
            }
        }

        if (!this.listeners.isEmpty() && this.side.canDispatch()) {
            for (ListenerEntry<T> entry : this.listeners) {
                entry.listener().dispatch(dispatchableEvent);
            }
        }

        return dispatchableEvent;
    }

    private static <T extends DispatchableEvent> void postToAllBusses(T dispatchableEvent) {
        ModLoader.postEvent(dispatchableEvent);
        NeoForge.EVENT_BUS.post(dispatchableEvent);
    }

    public boolean postAndCheckCanceled(T dispatchableEvent) {
        T result = this.postEvent(dispatchableEvent);
        return result != null && result.isCanceled();
    }

    public void postNoReturn(T dispatchableEvent) {
        this.postEvent(dispatchableEvent);
    }

    public EventSide getSide() {
        return this.side;
    }

    public boolean hasListeners() {
        return !this.listeners.isEmpty();
    }
}