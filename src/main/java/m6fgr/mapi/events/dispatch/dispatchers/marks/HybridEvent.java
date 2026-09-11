package m6fgr.mapi.events.dispatch.dispatchers.marks;

import m6fgr.mapi.events.dispatch.extra.EventBus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Used to mark events that can either be dispatched by the EventDispatcher<>, or by the annotation @EventBusSubscriber
// Events with these marks can be executed with the @SubscribeEvent annotation, or EventDispatcher#dispatchEvent(); method
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HybridEvent {
    EventBus value() default EventBus.BOTH;
}
