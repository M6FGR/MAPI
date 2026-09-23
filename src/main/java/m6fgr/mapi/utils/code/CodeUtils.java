package m6fgr.mapi.utils.code;

import m6fgr.mapi.main.MAPI;
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
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CodeUtils {

    public static <T> T newInstance(Class<T> instanceCls) {
        return newInstance(instanceCls, AccessType.PUBLIC);
    }

    public static <T> void forEach(T[] things, Consumer<T> lambdaForThing) {
        for (T thing : things) {
            lambdaForThing.accept(thing);
        }
    }

    public static <T> void forEach(T[] things, Runnable lambdaForThing) {
        for (T ignored : things) {
            CodeUtils.runTask(lambdaForThing);
        }
    }

    public static <A, T> void forEach(
            T[] things,
            A thing,
            BiConsumer<A, T> lambdaForThing
    ) {
        if (things == null) return;

        for (T eachThing : things) {
            lambdaForThing.accept(thing, eachThing);
        }
    }

    public static <T> T newInstance(Class<T> instanceCls, AccessType accessType) {
        return newInstance(instanceCls, accessType, (Object[]) null);
    }

    public static <T> T newInstance(Class<T> instanceCls, AccessType accessType, Object... constructorArgs) {
        boolean hasArgs = constructorArgs != null && constructorArgs.length > 0;
        Class<?>[] paramTypes = hasArgs
                ? Arrays.stream(constructorArgs).map(Object::getClass).toArray(Class<?>[]::new)
                : new Class<?>[0];

        try {
            Constructor<T> constructor = findMatchingConstructor(instanceCls, accessType, paramTypes);
            constructor.setAccessible(true);
            return hasArgs ? constructor.newInstance(constructorArgs) : constructor.newInstance();

        } catch (NoSuchMethodException noCons) {
            // Fallback: If parametric constructor failed, attempt default zero-arg constructor
            if (hasArgs) {
                try {
                    Constructor<T> defaultCons = (accessType == AccessType.PUBLIC)
                            ? instanceCls.getConstructor()
                            : instanceCls.getDeclaredConstructor();

                    defaultCons.setAccessible(true);
                    return defaultCons.newInstance();
                } catch (Exception ignored) {
                    // Ignore fallback failure to throw descriptive exception below
                }
            }
            throw new IllegalArgumentException("No matching constructor found for " + instanceCls.getSimpleName(), noCons);
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate " + instanceCls.getSimpleName(), e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> Constructor<T> findMatchingConstructor(Class<T> instanceCls, AccessType accessType, Class<?>[] paramTypes) throws NoSuchMethodException {
        // 1. First Pass: Search using the requested AccessType
        Constructor<?>[] constructors = (accessType == AccessType.PUBLIC)
                ? instanceCls.getConstructors()
                : instanceCls.getDeclaredConstructors();

        for (Constructor<?> c : constructors) {
            Class<?>[] cParams = c.getParameterTypes();
            if (cParams.length == paramTypes.length && isParamMatch(cParams, paramTypes)) {
                return (Constructor<T>) c;
            }
        }

        // 2. Second Pass: If PUBLIC was requested but failed, check DECLARED constructors as fallback
        if (accessType == AccessType.PUBLIC) {
            for (Constructor<?> c : instanceCls.getDeclaredConstructors()) {
                Class<?>[] cParams = c.getParameterTypes();
                if (cParams.length == paramTypes.length && isParamMatch(cParams, paramTypes)) {
                    return (Constructor<T>) c;
                }
            }
        }

        // 3. Third Pass: Fallback direct lookup for exact matches
        return (accessType == AccessType.PUBLIC)
                ? instanceCls.getConstructor(paramTypes)
                : instanceCls.getDeclaredConstructor(paramTypes);
    }

    private static boolean isParamMatch(Class<?>[] constructorParams, Class<?>[] passedArgs) {
        for (int i = 0; i < constructorParams.length; i++) {
            if (!constructorParams[i].isAssignableFrom(passedArgs[i])) {
                return false;
            }
        }
        return true;
    }

    public static <T> T newInstance(Class<T> instanceCls, Object... constructorArgs) {
        return newInstance(instanceCls, AccessType.PUBLIC, constructorArgs);
    }

    public static boolean hasAnnotationSuper(Class<?> cls, Class<? extends Annotation> annotationCls) {
        return cls.isAnnotationPresent(annotationCls) || cls.getSuperclass().isAnnotationPresent(annotationCls);
    }

    public static boolean hasAnnotationSuper(Class<? extends Annotation> annotationCls) {
        Class<?> cls = CodeUtils.getCallerClass();
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
        return CodeUtils.getMethods(cls, AccessType.PUBLIC);
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
        return CodeUtils.getFields(cls, AccessType.PUBLIC);
    }

    public static boolean isOverridingMethods(Class<?> cls, Class<?> superCls) {
        if (cls == null || superCls == null || cls.equals(superCls) || !superCls.isAssignableFrom(cls)) {
            return false;
        }

        Method[] methods = getMethods(cls, AccessType.DECLARED);
        Method[] superMethods = superCls.isInterface() ? superCls.getMethods() : getMethods(superCls, AccessType.DECLARED);

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

    public static boolean isOverridableInSuper(Method m) {
        int modifiers = m.getModifiers();
        return !Modifier.isPrivate(modifiers)
                && !Modifier.isStatic(modifiers)
                && !Modifier.isFinal(modifiers);
    }

    public static boolean isChildOverrideCandidate(Method m) {
        int mods = m.getModifiers();
        return !Modifier.isStatic(mods) && !Modifier.isPrivate(mods);
    }

    public static boolean isNotAccessible(Method m) {
        int modifiers = m.getModifiers();
        return Modifier.isPrivate(modifiers) || Modifier.isProtected(modifiers);
    }

    public static <R> void construct(Supplier<R> factory) {
        factory.get();
    }

    public static <A, R> void construct(A arg1, Function<A, R> factory) {
        factory.apply(arg1);
    }

    public static <A, B, R> void construct(A arg1, B arg2, BiFunction<A, B, R> factory) {
        factory.apply(arg1, arg2);
    }

    public static <A, B, C, R> void construct(A arg1, B arg2, C arg3, TriFunction<A, B, C, R> factory) {
        factory.apply(arg1, arg2, arg3);
    }

    private record MethodSignature(String name, Class<?>[] parameterTypes) {
        public MethodSignature(Method method) {
            this(method.getName(), method.getParameterTypes());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MethodSignature(String name1, Class<?>[] types))) return false;
            return Objects.equals(name, name1) && Arrays.equals(parameterTypes, types);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, Arrays.hashCode(parameterTypes));
        }
    }

    @FunctionalInterface
    public interface TriFunction<A, B, C, R> {
        R apply(A a, B b, C c);
    }

    @FunctionalInterface
    public interface QuadConsumer<A, B, C, D> {
        void apply(A a, B b, C c, D d);
    }
}