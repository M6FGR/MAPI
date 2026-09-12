package m6fgr.mapi.events.mapi.registries;

import m6fgr.mapi.builders.helpers.SynchronizedGameRule;
import m6fgr.mapi.events.dispatch.DispatchableEvent;
import m6fgr.mapi.events.dispatch.dispatchers.marks.HybridEvent;
import m6fgr.mapi.events.dispatch.extra.IDispatchableEvent;
import m6fgr.mapi.events.mapi.MAPIDispatchableEvents;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameRules.Category;
import net.minecraft.world.level.GameRules.Key;
import net.minecraft.world.level.GameRules.Type;
import net.minecraft.world.level.GameRules.Value;

import java.util.Map;

@HybridEvent
public class GameRulesRegistryDispatchableEvent extends DispatchableEvent implements IDispatchableEvent {

    private final Map<Key<?>, Type<?>> rulesMap;


    public GameRulesRegistryDispatchableEvent(Map<Key<?>, Type<?>> map) {
        this.rulesMap = map;
    }

    public <T extends Value<T>> Key<T> newGameRule(String name, Category category, Type<T> type) {
        Key<T> key = new Key<>(name, category);
        if (this.rulesMap != null) {
            this.rulesMap.put(key, type);
        } else {
            GameRules.register(name, category, type);
        }
        return key;
    }

    public <T extends Value<T>> Key<T> newGameRule(Key<T> key, Type<T> type) {
        if (this.rulesMap != null) {
            this.rulesMap.put(key, type);
        } else {
            GameRules.register(key.getId(), key.getCategory(), type);
        }
        return key;
    }

    public <T, V extends Value<V>> void registerSynchronized(SynchronizedGameRule<T, V> syncedRule) {
        SynchronizedGameRule<T, V> registeredRule = syncedRule.register();
        Key<V> key = registeredRule.getKey();
        Type<V> type = registeredRule.getType();
        if (this.rulesMap != null) {
            this.rulesMap.put(key, type);
        } else {
            GameRules.register(key.getId(), registeredRule.getCategory(), type);
        }
    }

    @SafeVarargs
    public final <T, V extends Value<V>> void registerSynchronized(SynchronizedGameRule<T, V>... syncedRules) {
        for (SynchronizedGameRule<T, V> syncedRule : syncedRules) {
            this.registerSynchronized(syncedRule);
        }
    }

    @Override
    public void postEvent() {
        MAPIDispatchableEvents.GAMERULES_REGISTRY.postEvent(this);
    }
}
