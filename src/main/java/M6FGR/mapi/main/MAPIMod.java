package M6FGR.mapi.main;

import M6FGR.mapi.cls.ILoadableClass;
import M6FGR.mapi.events.dispatch.DispatchableEvent;
import M6FGR.mapi.events.mc.MinecraftDispatchableEvents;
import M6FGR.mapi.network.MAPINetworkManager;
import M6FGR.mapi.utils.environment.EnvironmentHelper;
import M6FGR.mapi.utils.environment.Environments;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;

@Mod(MAPI.MOD_ID)
public class MAPIMod implements MAPI {
    static MAPIMod instance = null;
    private final ModContainer mapiContainer;
    private final IEventBus mapiBus;
    private final boolean constructed;

    public MAPIMod(FMLJavaModLoadingContext context) {
        instance = this;
        IEventBus modEventBus = context.getModEventBus();
        ModContainer modContainer = context.getContainer();
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
