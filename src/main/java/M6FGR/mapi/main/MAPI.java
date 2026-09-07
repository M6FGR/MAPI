package M6FGR.mapi.main;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface MAPI  {
    String MOD_ID = "mapi";
    String MOD_NAME = "M-API";
    Logger LOGGER = LogManager.getLogger(MOD_NAME);

    static MAPI getInstance() {
        if (MAPIMod.instance == null) {
            throw new NullPointerException("Called the instance too early!");
        }
        return MAPIMod.instance;
    }

    ModContainer getModContainer();

    IEventBus getModBus();

    ResourceLocation identifier(String path);

    boolean isModConstructed();

    boolean isDeveloperEnvironment();

}
