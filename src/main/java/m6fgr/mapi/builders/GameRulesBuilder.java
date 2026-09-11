package m6fgr.mapi.builders;

import m6fgr.mapi.builders.helpers.SynchronizedGameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRuleType;

public class GameRulesBuilder {

    private GameRulesBuilder() {}

    public static SynchronizedGameRule<Boolean> newBoolean(String name, GameRuleCategory category, boolean defaultValue, boolean syncToAllPlayers) {
        return new SynchronizedGameRule<>(
                name, category, defaultValue, syncToAllPlayers, listener -> GameRuleType.BOOL
        );
    }

    public static SynchronizedGameRule<Boolean> newBoolean(String name, GameRuleCategory category, boolean defaultValue) {
        return newBoolean(name, category, defaultValue, false);
    }

    public static SynchronizedGameRule<Integer> newInteger(String name, GameRuleCategory category, int defaultValue, boolean syncToAllPlayers) {
        return new SynchronizedGameRule<>(
                name, category, defaultValue, syncToAllPlayers, listener -> GameRuleType.INT
        );
    }

    public static SynchronizedGameRule<Integer> newInteger(String name, GameRuleCategory category, int defaultValue) {
        return newInteger(name, category, defaultValue, false);
    }

}