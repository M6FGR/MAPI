package m6fgr.mapi.events.dispatch.dispatchers.marks;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
// Used to mark events that are only fired in the client side
// Events with this mark cannot execute on the server side,
// For example, check PlayerDispatchableEvents#ItemUse; it calls LocalPlayer, which's client side only
// therefore, it will throw an IllegalSideException if it was executed on the server side
public @interface ClientEvent {}
