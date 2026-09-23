package m6fgr.mapi.events.dispatch.extra;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLLoader;

import java.util.function.Predicate;

public enum EventSide {
    CLIENT(Dist::isClient),
    SERVER(Dist::isDedicatedServer),
    BOTH(dist -> dist.isClient() || dist.isDedicatedServer());

    private final Predicate<Dist> distPredicate;

    EventSide(Predicate<Dist> distPredict) {
        this.distPredicate = distPredict;
    }


    public boolean canDispatch() {
        return this.distPredicate.test(FMLLoader.getDist());
    }
}
