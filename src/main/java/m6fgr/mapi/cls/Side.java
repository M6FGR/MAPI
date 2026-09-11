package m6fgr.mapi.cls;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLLoader;

import java.util.function.Predicate;

public enum Side {
    CLIENT(Dist::isClient),
    SERVER(Dist::isDedicatedServer),
    BOTH(dist -> true);

    private final Predicate<Dist> sidePredict;

    Side(Predicate<Dist> side) {
        this.sidePredict = side;
    }

    public boolean shouldExecute() {
        return this.sidePredict.test(FMLLoader.getCurrent().getDist());
    }

}