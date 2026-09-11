package m6fgr.mapi.main;

import m6fgr.mapi.cls.ILoadableClass;
import m6fgr.mapi.network.MAPINetworkManager;
import m6fgr.mapi.utils.environment.EnvironmentHelper;
import m6fgr.mapi.utils.environment.Environments;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;

@Mod(MAPI.MOD_ID)
public class MAPIMod implements MAPI {
    static MAPIMod instance;
    private final ModContainer mapiContainer;
    private final IEventBus mapiBus;
    private final boolean constructed;

    public MAPIMod(IEventBus modEventBus, ModContainer modContainer) {
        instance = this;
        this.constructed = true;
        this.mapiContainer = modContainer;
        this.mapiBus = modEventBus;
        ILoadableClass.loadCls(modEventBus, MAPINetworkManager.class);
    }

    @Override
    public Identifier identifier(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    @Override
    public ModContainer getModContainer() {
        return this.mapiContainer;
    }

    @Override
    public IEventBus getModBus() {
        return this.mapiBus;
    }

    @Override
    public boolean isModConstructed() {
        return this.constructed;
    }

    @Override
    public boolean isDeveloperEnvironment() {
        return EnvironmentHelper.getEnvironment().is(Environments.IDE) || !FMLLoader.getCurrent().isProduction();
    }

}
