package M6FGR.mapi.events.mapi.registries;

import M6FGR.mapi.builders.helpers.SynchronizedGameRule;
import M6FGR.mapi.events.dispatch.DispatchableEvent;
import M6FGR.mapi.events.dispatch.dispatchers.marks.HybridEvent;
import M6FGR.mapi.events.dispatch.extra.IDispatchableEvent;
import M6FGR.mapi.events.mapi.MAPIDispatchableEvents;
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

    public <T extends Value<T>> Key<T> register(String name, Category category, Type<T> type) {
        Key<T> key = new Key<>(name, category);
        if (this.rulesMap != null) {
            this.rulesMap.put(key, type);
        } else {
            GameRules.register(name, category, type);
        }
        return key;
    }

    public <T extends Value<T>> Key<T> register(Key<T> key, Type<T> type) {
        if (this.rulesMap != null) {
            this.rulesMap.put(key, type);
        } else {
            GameRules.register(key.getId(), key.getCategory(), type);
        }
        return key;
    }

    public <T extends Value<T>> void register(SynchronizedGameRule<T> syncedRule) {
        SynchronizedGameRule<T> registeredRule = syncedRule.register();
        Key<T> key = registeredRule.getKey();
        Type<T> type = registeredRule.getType();
        if (this.rulesMap != null) {
            this.rulesMap.put(key, type);
        } else {
            GameRules.register(key.getId(), registeredRule.getCategory(), type);
        }
    }

    @SafeVarargs
    public final <T extends Value<T>> void register(SynchronizedGameRule<T>... syncedRules) {
        for (SynchronizedGameRule<T> syncedRule : syncedRules) {
            this.register(syncedRule);
        }
    }

    @Override
    public void postEvent() {
        MAPIDispatchableEvents.GAMERULES_REGISTRY.postEvent(this);
    }
}
