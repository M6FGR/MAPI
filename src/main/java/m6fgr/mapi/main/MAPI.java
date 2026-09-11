package m6fgr.mapi.main;

import m6fgr.mapi.cls.logging.Logger;
import m6fgr.mapi.cls.logging.LoggingManager;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;

public interface MAPI {
    String MOD_ID = "mapi";
    String MOD_NAME = "M-API";
    Logger LOGGER = LoggingManager.getLogger(MOD_NAME);

    static MAPI getInstance() {
        if (MAPIMod.instance == null) {
            throw new NullPointerException("Called the instance of MAPI too early!");
        }
        return MAPIMod.instance;
    }

    ModContainer getModContainer();

    IEventBus getModBus();

    Identifier identifier(String path);

    boolean isModConstructed();

    boolean isDeveloperEnvironment();

}
