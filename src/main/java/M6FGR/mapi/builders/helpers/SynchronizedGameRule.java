package M6FGR.mapi.builders.helpers;

import M6FGR.mapi.network.MAPINetworkManager;
import M6FGR.mapi.network.packets.server.SPGameRuleSync;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameRules.Category;
import net.minecraft.world.level.GameRules.Key;
import net.minecraft.world.level.GameRules.Type;
import net.minecraft.world.level.GameRules.Value;
import net.minecraft.world.level.Level;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class SynchronizedGameRule<V extends Value<V>> {

    private final String name;
    private final GameRules.Category category;
    private final boolean sync;
    private final Function<BiConsumer<MinecraftServer, V>, Type<V>> typeFactory;
    private GameRules.Key<V> key;
    private GameRules.Type<V> type;

    public SynchronizedGameRule(String name, GameRules.Category category, boolean sync, Function<BiConsumer<MinecraftServer, V>, GameRules.Type<V>> typeFactory) {
        this.name = name;
        this.category = category;
        this.sync = sync;
        this.typeFactory = typeFactory;
    }

    public SynchronizedGameRule<V> register() {
        BiConsumer<MinecraftServer, V> changeListener = (server, value) -> {
            if (this.sync && server != null) {
                this.broadcastToServer(server, this.name, value.getCommandResult());
            }
        };
        this.type = this.typeFactory.apply(changeListener);
        this.key = new Key<>(this.name, this.category);
        return this;
    }

    public Type<V> getType() {
        return this.type;
    }

    public GameRules.Key<V> getKey() {
        return this.key;
    }

    public V getValue(Level level) {
        return level.getGameRules().getRule(this.key);
    }

    public V getValue(MinecraftServer server) {
        return server.getGameRules().getRule(this.key);
    }

    public Category getCategory() {
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
