package m6fgr.mapi.cls.marks;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target({
        ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR,
        ElementType.FIELD,
        ElementType.TYPE,
        ElementType.TYPE_PARAMETER,
        ElementType.TYPE_USE,
        ElementType.RECORD_COMPONENT,
        ElementType.LOCAL_VARIABLE,
        ElementType.PACKAGE,
        ElementType.METHOD,
        ElementType.MODULE,
        ElementType.PARAMETER,
})
public @interface Conditional {
    String value() default "";
}
