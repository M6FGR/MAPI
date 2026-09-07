package M6FGR.mapi.cls;


import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLLoader;

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
        return this.sidePredict.test(FMLLoader.getDist());
    }

}