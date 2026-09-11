package m6fgr.mapi.utils.environment;

import org.jetbrains.annotations.Nullable;

import java.util.function.BooleanSupplier;

public enum Environments {
    CLIENT,
    IDE(CLIENT),
    LAN_SERVER(CLIENT),
    COMMON,
    DEDICATED_SERVER,
    GAME_TEST_SERVER;

    private final boolean isDeveloper;
    private final @Nullable Environments parent;

    Environments(BooleanSupplier developer, @Nullable Environments parent) {
        this.isDeveloper = developer.getAsBoolean();
        this.parent = parent;
    }

    Environments(@Nullable Environments parent) {
        this(EnvironmentHelper::isDev, parent);
    }

    Environments() {
        this(EnvironmentHelper::isDev, null);
    }

    public boolean isDevEnv() {
        return this.isDeveloper;
    }

    @Nullable
    public Environments getParent() {
        return this.parent;
    }

    public String getAdvancedName() {
        if (this.is(Environments.IDE)) return this.name();
        String[] words = name().split("_");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }

        return result.toString().trim();
    }

    public boolean isAuthenticated() {
        if (this == CLIENT || this == LAN_SERVER) return EnvironmentHelper.isOfficialMC();
        if (this == IDE) return EnvironmentHelper.isDevAndOfficialMC();
        if (this == DEDICATED_SERVER) return EnvironmentHelper.isServerAuthenticated();
        return false;
    }

    public boolean is(Environments matching) {
        if (this == matching) {
            return true;
        }
        if (this.parent != null) {
            return this.parent.is(matching);
        }
        return false;
    }

    public boolean isClient() {
        return this.is(Environments.CLIENT);
    }

    public boolean isServer() {
        return !this.isClient() && this.is(DEDICATED_SERVER, GAME_TEST_SERVER);
    }

    public boolean is(Environments... matchings) {
        for (Environments env : matchings) {
            return this.is(env);
        }
        return false;
    }

    public boolean isSameAndAuthenticated(Environments matching) {
        if (this == matching) {
            return this.isAuthenticated();
        }
        if (this.parent != null) {
            return this.parent.isSameAndAuthenticated(matching);
        }
        return false;
    }

}