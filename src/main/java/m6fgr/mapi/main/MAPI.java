package m6fgr.mapi.main;

import m6fgr.mapi.cls.logging.Logger;
import m6fgr.mapi.cls.logging.LoggingManager;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;

import java.util.function.Consumer;

public interface MAPI  {
    String MOD_ID = "mapi";
    String MOD_NAME = "M-API";
    Logger LOGGER = LoggingManager.getLogger(MOD_NAME);

    static MAPI getInstance() {
        if (MAPIMod.instance == null) {
            throw new NullPointerException("Called the instance too early!");
        }
        return MAPIMod.instance;
    }

    ModContainer getModContainer();

    IEventBus getModBus();

    boolean isModConstructed();

    boolean isDeveloperEnvironment();

    default <T extends Event> void addListener(Consumer<T> listener) {
       this.getModBus().addListener(listener);
    }

    default ResourceLocation identifier(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

}
