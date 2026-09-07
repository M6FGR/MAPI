package M6FGR.mapi.main;

import M6FGR.mapi.cls.logging.Logger;
import M6FGR.mapi.cls.logging.LoggingManager;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import org.apache.logging.log4j.LogManager;

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

    ResourceLocation identifier(String path);

    boolean isModConstructed();

    boolean isDeveloperEnvironment();

}
