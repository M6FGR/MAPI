package m6fgr.mapi.main;

import m6fgr.mapi.cls.ILoadableClass;
import m6fgr.mapi.cls.marks.AvoidUsage;
import m6fgr.mapi.events.mc.MinecraftDispatchableEvents;
import m6fgr.mapi.network.MAPINetworkManager;
import m6fgr.mapi.utils.environment.EnvironmentHelper;
import m6fgr.mapi.utils.environment.Environments;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import org.checkerframework.framework.qual.Unused;
import org.jetbrains.annotations.ApiStatus.Internal;

@Mod(MAPI.MOD_ID)
@AvoidUsage(why = "Use MAPI interface instead, this is an internal class")
public final class MAPIMod implements MAPI {
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
        return EnvironmentHelper.getEnvironment().is(Environments.IDE) || !FMLLoader.isProduction();
    }

}
