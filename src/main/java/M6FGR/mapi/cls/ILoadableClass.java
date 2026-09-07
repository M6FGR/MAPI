package M6FGR.mapi.cls;

import M6FGR.mapi.cls.exceptions.ClassLoadingException;
import M6FGR.mapi.main.MAPI;
import M6FGR.mapi.utils.code.AccessType;
import M6FGR.mapi.utils.code.CodeUtils;
import net.minecraft.world.entity.animal.Cod;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.ApiStatus.Internal;

import java.util.ArrayList;
import java.util.List;

public interface ILoadableClass {

    @Internal
    List<Class<? extends ILoadableClass>> LOADED_CLASSES = new ArrayList<>();

    @SafeVarargs
    static void loadCls(IEventBus modBus, Class<? extends ILoadableClass>... classes) {
        if (classes == null || classes.length == 0) {
            MAPI.LOGGER.warn("Called an empty loadCls!");
            return;
        }

        for (Class<? extends ILoadableClass> cls : classes) {
            if (LOADED_CLASSES.contains(cls)) {
                throw new ClassLoadingException("Class [" + cls.getSimpleName() + "] is already loaded!");
            } else if (!CodeUtils.isOverridingMethods(cls, ILoadableClass.class)) {
                MAPI.LOGGER.warn("Class [{}] was loaded, but it doesn't override any methods!", cls.getSimpleName());
                continue;
            }

            try {
                SidedLoadableClass sidedLoadableClass = cls.getAnnotation(SidedLoadableClass.class);
                Side targetSide = (sidedLoadableClass != null) ? sidedLoadableClass.value() : Side.BOTH;

                if (!targetSide.shouldExecute()) {
                    MAPI.LOGGER.debug("Skipped loading class [{}] for side [{}]", cls::getSimpleName, targetSide::name);
                    continue;
                }

                ILoadableClass instance = CodeUtils.newInstance(cls, AccessType.DECLARED);
                if (targetSide.shouldExecute()) {
                    instance.onModConstructor(modBus);
                    instance.onGameConstructor(NeoForge.EVENT_BUS);

                    if (targetSide == Side.CLIENT) {
                        instance.onModClientConstructor(modBus);
                        instance.onGameClientConstructor(NeoForge.EVENT_BUS);
                    }
                }
                LOADED_CLASSES.add(cls);
                MAPI.LOGGER.info("Loaded class [{}]", cls.getSimpleName());
            } catch (Exception e) {
                MAPI.LOGGER.error("Failed to load class [{}], {}", cls.getSimpleName(), e);
            }
        }
    }

    void onModConstructor(IEventBus modBus);
    void onGameConstructor(IEventBus gameBus);

    @Comment({
            "Avoid calling if the loadable class doesn't have the annotation @SidedLoadableClass",
            "Only executes if the annotation was present and it was client sided"
    })
    default void onModClientConstructor(IEventBus modBusC) {}
    default void onGameClientConstructor(IEventBus gameBusC) {}

}
