package m6fgr.mapi.builders;

import m6fgr.mapi.builders.helpers.SynchronizedGameRule;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameRules.BooleanValue;
import net.minecraft.world.level.GameRules.IntegerValue;

public class GameRulesBuilder {

    private GameRulesBuilder() {}

    public static SynchronizedGameRule<Boolean,GameRules.BooleanValue> newBoolean(String name, GameRules.Category category, boolean defaultValue, boolean syncToAllPlayers) {
        return new SynchronizedGameRule<>(
                name, category, syncToAllPlayers, defaultValue, BooleanValue::create
        );
    }

    public static SynchronizedGameRule<Boolean,GameRules.BooleanValue> newBoolean(String name, GameRules.Category category, boolean defaultValue) {
        return newBoolean(name, category, defaultValue, false);
    }

    public static SynchronizedGameRule<Integer,GameRules.IntegerValue> newInteger(String name, GameRules.Category category, int defaultValue, boolean syncToAllPlayers) {
        return new SynchronizedGameRule<>(
                name, category, syncToAllPlayers, defaultValue, GameRules.IntegerValue::create
        );
    }

    public static SynchronizedGameRule<Integer,IntegerValue> newInteger(String name, GameRules.Category category, int defaultValue) {
        return newInteger(name, category, defaultValue, false);
    }

}