package M6FGR.mapi.utils.code;

import M6FGR.mapi.main.MAPI;
import M6FGR.mapi.network.functions.StreamMemberDecoder;
import org.apache.logging.log4j.util.StackLocatorUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CodeUtils {

    public static <T> T newInstance(Class<T> instanceCls) {
        return newInstance(instanceCls, AccessType.PUBLIC);
    }

    public static <T> void forEach(T[] array, Consumer<T> each) {
        for (T thing : array) {
            each.accept(thing);
        }
    }

    public static <A, T> void forEach(
            A anotherThing,
            T[] things,
            BiConsumer<A, T> action
    ) {
        if (things == null) return;

        for (T thing : things) {
            action.accept(anotherThing, thing);
        }
    }

    public static <T> T newInstance(Class<T> instanceCls, M6FGR.mapi.utils.code.AccessType accessType) {
        try {
            return switch (accessType) {
                case PUBLIC -> instanceCls.getConstructor().newInstance();
                case DECLARED -> {
                    Constructor<T> constructor = instanceCls.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    yield constructor.newInstance();
                }
            };
        } catch (NoSuchMethodException noCons) {
            throw new IllegalArgumentException("No default constructor found for " + instanceCls.getSimpleName(), noCons);
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate " + instanceCls.getSimpleName(), e);
        }
    }

    public static boolean hasAnnotationSuper(Class<?> cls, Class<? extends Annotation> annotationCls) {
        return cls.isAnnotationPresent(annotationCls) || cls.getSuperclass().isAnnotationPresent(annotationCls);
    }

    public static Class<?> getSuperClass(Class<?> cls) {
        return cls.getSuperclass();
    }

    public static Class<?> getCallerClass() {
        return StackLocatorUtil.getCallerClass(2);
    }

    public static boolean isNotNull(Object something) {
        return something != null;
    }

    public static void runTask(Runnable task) {
        task.run();
    }

    public static Method[] getMethods(Class<?> cls, AccessType access) {
        if (cls == null) return new Method[0];
        try {
            return switch (access) {
                case PUBLIC -> cls.getMethods();
                case DECLARED -> cls.getDeclaredMethods();
            };
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve methods for " + cls.getSimpleName(), e);
        }
    }

    public static Method[] getMethods(Class<?> cls) {
       return getMethods(cls, AccessType.PUBLIC);
    }

    public static Field[] getFields(Class<?> cls, AccessType access) {
        if (cls == null) return new Field[0];
        try {
            return switch (access) {
                case PUBLIC -> cls.getFields();
                case DECLARED -> cls.getDeclaredFields();
            };
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve fields for " + cls.getSimpleName(), e);
        }
    }

    public static Field[] getFields(Class<?> cls) {
        return getFields(cls, AccessType.PUBLIC);
    }

    public static boolean isOverridingMethods(Class<?> cls, Class<?> superCls) {
        if (cls == null || superCls == null || cls.equals(superCls) || !superCls.isAssignableFrom(cls)) {
            return false;
        }

        Method[] methods = getMethods(cls, AccessType.DECLARED);
        Method[] superMethods = getMethods(superCls, AccessType.DECLARED);

        if (methods == null || superMethods == null) {
            return false;
        }

        Set<MethodSignature> superSignatures = Arrays.stream(superMethods)
                .filter(CodeUtils::isOverridableInSuper)
                .map(MethodSignature::new)
                .collect(Collectors.toSet());

        if (superSignatures.isEmpty()) {
            return false;
        }

        for (Method method : methods) {
            if (isChildOverrideCandidate(method)) {
                if (superSignatures.contains(new MethodSignature(method))) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isOverridableInSuper(Method m) {
        int modifiers = m.getModifiers();
        return !Modifier.isPrivate(modifiers)
                && !Modifier.isStatic(modifiers)
                && !Modifier.isFinal(modifiers);
    }

    private static boolean isChildOverrideCandidate(Method m) {
        int mods = m.getModifiers();
        return !Modifier.isStatic(mods) && !Modifier.isPrivate(mods);
    }


    private record MethodSignature(String name, Class<?>[] parameterTypes) {
        public MethodSignature(Method method) {
            this(method.getName(), method.getParameterTypes());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MethodSignature that)) return false;
            return Objects.equals(name, that.name) && Arrays.equals(parameterTypes, that.parameterTypes);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, Arrays.hashCode(parameterTypes));
        }
    }
}