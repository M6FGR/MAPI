package m6fgr.mapi.events.dispatch.extra;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLLoader;

import java.util.function.Predicate;

public enum EventSide {
    CLIENT(Dist::isClient),
    SERVER(Dist::isDedicatedServer),
    COMMON(dist -> true);

    private final Predicate<Dist> disPredict;

    EventSide(Predicate<Dist> distPredict) {
        this.disPredict = distPredict;
    }


    public boolean canDispatch() {
        return this.disPredict.test(FMLLoader.getDist());
    }
}
