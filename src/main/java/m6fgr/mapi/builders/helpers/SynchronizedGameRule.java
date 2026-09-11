package m6fgr.mapi.builders.helpers;

import m6fgr.mapi.network.MAPINetworkManager;
import m6fgr.mapi.network.packets.server.SPGameRuleSync;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRuleType;
import org.jetbrains.annotations.ApiStatus.Internal;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class SynchronizedGameRule<V> {

    private final String name;
    private final GameRuleCategory category;
    private final boolean sync;
    private final Function<BiConsumer<MinecraftServer, V>, GameRuleType> typeFactory;
    private GameRule<V> rule;
    private GameRuleType gameRuleType;
    private V defaultValue;

    public SynchronizedGameRule(String name, GameRuleCategory category, V defaultValue, boolean sync, Function<BiConsumer<MinecraftServer, V>, GameRuleType> typeFactory) {
        this.name = name;
        this.category = category;
        this.sync = sync;
        this.typeFactory = typeFactory;
        this.defaultValue = defaultValue;
    }

    @Internal
    public SynchronizedGameRule<V> register() {
        BiConsumer<MinecraftServer, V> changeListener = (server, value) -> {
            if (this.sync && server != null && this.rule != null) {
                this.broadcastToServer(server, this.name, this.rule.getCommandResult(value));
            }
        };
        this.gameRuleType = this.typeFactory.apply(changeListener);
        return this;
    }

    public GameRuleType getGameRuleType() {
        return this.gameRuleType;
    }

    public GameRule<V> getRule() {
        return this.rule;
    }

    public void setRule(GameRule<V> rule) {
        this.rule = rule;
    }

    public V getValue(ServerLevel level) {
        return level.getGameRules().get(this.rule);
    }

    public V getValue(MinecraftServer server) {
        return server.getGameRules().get(this.rule);
    }

    public V getDefaultValue() {
        return this.defaultValue;
    }

    public GameRuleCategory getCategory() {
        return this.category;
    }

    public String getName() {
        return this.name;
    }

    public boolean isSynced() {
        return this.sync;
    }

    private void broadcastToServer(MinecraftServer server, String name, int value) {
        SPGameRuleSync packet = new SPGameRuleSync(name, value);
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            MAPINetworkManager.sendToPlayer(player, packet);
        }
    }
}