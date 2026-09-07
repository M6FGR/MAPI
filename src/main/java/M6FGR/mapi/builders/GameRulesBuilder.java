package M6FGR.mapi.builders;

import M6FGR.mapi.builders.helpers.SynchronizedGameRule;
import M6FGR.mapi.network.MAPINetworkManager;
import M6FGR.mapi.network.packets.server.SPGameRuleSync;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameRules.IntegerValue;

public class GameRulesBuilder {

    private GameRulesBuilder() {}

    public static SynchronizedGameRule<GameRules.BooleanValue> newBoolean(String name, GameRules.Category category, boolean defaultValue, boolean syncToAllPlayers) {
        return new SynchronizedGameRule<>(
                name, category, syncToAllPlayers,
                listener -> GameRules.BooleanValue.create(defaultValue, listener)
        );
    }

    public static SynchronizedGameRule<GameRules.BooleanValue> newBoolean(String name, GameRules.Category category, boolean defaultValue) {
        return newBoolean(name, category, defaultValue, false);
    }

    public static SynchronizedGameRule<GameRules.IntegerValue> newInteger(String name, GameRules.Category category, int defaultValue, boolean syncToAllPlayers) {
        return new SynchronizedGameRule<>(
                name, category, syncToAllPlayers,
                listener -> GameRules.IntegerValue.create(defaultValue, listener)
        );
    }

    public static SynchronizedGameRule<IntegerValue> newInteger(String name, GameRules.Category category, int defaultValue) {
        return newInteger(name, category, defaultValue, false);
    }

}