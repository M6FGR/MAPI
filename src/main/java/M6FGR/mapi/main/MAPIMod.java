package M6FGR.mapi.main;

import M6FGR.mapi.cls.ILoadableClass;
import M6FGR.mapi.events.dispatch.DispatchableEvent;
import M6FGR.mapi.events.mc.MinecraftDispatchableEvents;
import M6FGR.mapi.network.MAPINetworkManager;
import M6FGR.mapi.utils.environment.EnvironmentHelper;
import M6FGR.mapi.utils.environment.Environments;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

@Mod(MAPI.MOD_ID)
public class MAPIMod implements MAPI {
    static MAPIMod instance = null;
    private final ModContainer mapiContainer;
    private final IEventBus mapiBus;
    private final boolean constructed;

    public MAPIMod(IEventBus modEventBus, ModContainer modContainer) {
        instance = this;
        this.constructed = true;
        this.mapiContainer = modContainer;
        this.mapiBus = modEventBus;
        ILoadableClass.loadCls(modEventBus, MAPINetworkManager.class);
        MinecraftDispatchableEvents.CLIENT_START.dispatchEvent(this::logEvent);
    }

    public ResourceLocation identifier(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public ModContainer getModContainer() {
        return this.mapiContainer;
    }

    public IEventBus getModBus() {
        return this.mapiBus;
    }

    @Override
    public boolean isModConstructed() {
        return this.constructed;
    }

    @Override
    public boolean isDeveloperEnvironment() {
        return EnvironmentHelper.getEnvironment().is(Environments.IDE) || !FMLLoader.isProduction();
    }

    private <T extends DispatchableEvent> void logEvent(T event) {
        LOGGER.info("Hello from {}", event.getEventName());
    }

}
