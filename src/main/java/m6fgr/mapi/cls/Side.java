package m6fgr.mapi.cls;

import m6fgr.mapi.utils.environment.EnvironmentHelper;
import m6fgr.mapi.utils.environment.Environments;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLLoader;

import java.util.function.Predicate;

public enum Side {
    CLIENT(Environments::isClient),
    SERVER(Environments::isServer),
    BOTH(env -> env.isClient() || env.isServer());

    private final Predicate<Environments> sidePredict;

    Side(Predicate<Environments> side) {
        this.sidePredict = side;
    }

    public boolean shouldExecute() {
        return this.sidePredict.test(EnvironmentHelper.getEnvironment());
    }

    public boolean is(Side side) {
        return this == side;
    }

}