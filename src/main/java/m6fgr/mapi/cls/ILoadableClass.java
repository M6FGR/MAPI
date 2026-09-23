package m6fgr.mapi.cls;

import m6fgr.mapi.cls.exceptions.ClassLoadingException;
import m6fgr.mapi.cls.marks.Conditional;
import m6fgr.mapi.main.MAPI;
import m6fgr.mapi.utils.code.CodeUtils;
import m6fgr.mapi.utils.environment.EnvironmentHelper;
import m6fgr.mapi.utils.environment.Environments;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.ApiStatus.Internal;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;

public interface ILoadableClass {

    @Internal
    List<Class<? extends ILoadableClass>> LOADED_CLASSES = new ArrayList<>();

    @SafeVarargs
    static void loadCls(IEventBus modBus, Class<? extends ILoadableClass>... classes) {
        if (classes == null || classes.length == 0) {
            MAPI.LOGGER.warn("Called an empty loadCls!");
            return;
        }

        Map<Class<?>, Object> allowedConstructorArgs = Map.of(
                IEventBus.class, modBus,
                Environments.class, EnvironmentHelper.getEnvironment()
        );

        for (Class<? extends ILoadableClass> cls : classes) {
            if (LOADED_CLASSES.contains(cls)) {
                throw new ClassLoadingException("Class [" + cls.getSimpleName() + "] is already loaded!");
            }

            try {
                Constructor<?>[] constructors = cls.getDeclaredConstructors();
                if (constructors.length != 1) {
                    throw new ClassLoadingException("Class [" + cls.getSimpleName() + "] must have exactly 1 constructor, found " + constructors.length);
                }

                Constructor<?> constructor = constructors[0];
                constructor.setAccessible(true);

                Class<?>[] parameterTypes = constructor.getParameterTypes();

                boolean hasCustomConstructor = parameterTypes.length > 0;
                boolean overridesMethods = CodeUtils.isOverridingMethods(cls, ILoadableClass.class);

                if (!hasCustomConstructor && !overridesMethods) {
                    MAPI.LOGGER.warn("Class [{}] was loaded, but it has a no-arg constructor and doesn't override any ILoadableClass methods!", cls.getSimpleName());
                    continue;
                }

                SidedLoadableClass sidedLoadableClass = cls.getAnnotation(SidedLoadableClass.class);
                Side targetSide = (sidedLoadableClass != null) ? sidedLoadableClass.value() : Side.BOTH;

                if (!targetSide.shouldExecute()) {
                    MAPI.LOGGER.debug("Skipped loading class [{}] for side [{}]", cls.getSimpleName(), targetSide);
                    continue;
                }

                Object[] constructorArgs = new Object[parameterTypes.length];
                Set<Class<?>> foundArgs = new HashSet<>();

                for (int i = 0; i < parameterTypes.length; i++) {
                    Class<?> paramType = parameterTypes[i];
                    Object argInstance = allowedConstructorArgs.get(paramType);

                    if (argInstance == null) {
                        String allowed = allowedConstructorArgs.keySet().stream()
                                .map(Class::getName)
                                .collect(Collectors.joining(", "));
                        throw new ClassLoadingException("Class [" + cls.getSimpleName() + "] constructor has unsupported argument: " + paramType.getName() + ". Allowed optional arguments: " + allowed);
                    }

                    if (foundArgs.contains(paramType)) {
                        throw new ClassLoadingException("Duplicate constructor argument type [" + paramType.getSimpleName() + "] in class [" + cls.getSimpleName() + "]");
                    }

                    foundArgs.add(paramType);
                    constructorArgs[i] = argInstance;
                }

                ILoadableClass instance = (ILoadableClass) constructor.newInstance(constructorArgs);

                if (targetSide.shouldExecute()) {
                    instance.onModConstructor(modBus);
                    instance.onGameConstructor(NeoForge.EVENT_BUS);

                    if (targetSide.is(Side.CLIENT)) {
                        instance.onModClientConstructor(modBus);
                        instance.onGameClientConstructor(NeoForge.EVENT_BUS);
                    }
                }
                LOADED_CLASSES.add(cls);
                MAPI.LOGGER.info("Loaded class [{}]", cls.getSimpleName());
            } catch (Exception e) {
                MAPI.LOGGER.error("Failed to load class [{}]:", cls.getSimpleName(), e);
            }
        }
    }

    default void onModConstructor(IEventBus modBus) {}

    default void onGameConstructor(IEventBus gameBus) {}

    @Conditional("don't override if your class is not sided, only executes if the loading side was CLIENT as seen above")
    default void onModClientConstructor(IEventBus modBusC) {}
    default void onGameClientConstructor(IEventBus gameBusC) {}

}