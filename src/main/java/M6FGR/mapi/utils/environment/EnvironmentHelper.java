package M6FGR.mapi.utils.environment;

import net.minecraft.client.Minecraft;
import net.minecraft.client.User;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.gametest.framework.GameTestServer;
import net.minecraft.server.MinecraftServer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.Nullable;

public class EnvironmentHelper {

    public static final Dist DIST = FMLLoader.getDist();
    private static final boolean IS_DEVELOPER = !FMLLoader.isProduction();

    private static final MinecraftServer currentServer = ServerLifecycleHooks.getCurrentServer();

    private EnvironmentHelper() {}

    public static @Nullable MinecraftServer getCurrentServer() {
        return currentServer;
    }

    public static Minecraft getClient() {
        if (!DIST.isClient()) {
            throw new IllegalCallerException("Called getClient() on a server environment!");
        }
        return Minecraft.getInstance();
    }

    static boolean isOfficialMC() {
        if (!EnvironmentHelper.DIST.isClient()) return false;
        User user = EnvironmentHelper.getClient().getUser();
        return !user.getAccessToken().equals("0") && !user.getAccessToken().equals("NotValid");
    }


    static boolean isDevAndOfficialMC() {
        return IS_DEVELOPER && isOfficialMC();
    }

    static boolean isDev() {
        return IS_DEVELOPER;
    }

    static boolean isServerAuthenticated() {
        if (EnvironmentHelper.DIST.isClient()) return false;
        MinecraftServer currentServer = getCurrentServer();
        return currentServer != null && currentServer.usesAuthentication();
    }

    public static boolean isLogicalServer() {
        if (DIST.isClient()) return false;
        return currentServer != null && currentServer.isSameThread()
                || EnvironmentHelper.getEnvironment().isServer();
    }

    public static boolean isLogicalClient() {
        if (!DIST.isClient()) return false;
        return currentServer == null
                || !currentServer.isSameThread()
                || EnvironmentHelper.getEnvironment().isClient();
    }

    public static Environments getEnvironment() {
        MinecraftServer server = EnvironmentHelper.getCurrentServer();
        if (EnvironmentHelper.DIST.isDedicatedServer()) {
            if (server != null) {
                if (server instanceof GameTestServer) {
                    return Environments.GAME_TEST_SERVER;
                } else if (server.isDedicatedServer()) {
                    return Environments.DEDICATED_SERVER;
                }
            }
            return Environments.DEDICATED_SERVER;
        }

        if (EnvironmentHelper.DIST.isClient()) {
            if (server != null) {
                if (server instanceof IntegratedServer) {
                    return Environments.LAN_SERVER;
                }
            }
            return IS_DEVELOPER ? Environments.IDE : Environments.CLIENT;
        }

        return Environments.COMMON;
    }
}