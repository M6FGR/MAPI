package M6FGR.mapi.mixins.mc;

import M6FGR.mapi.events.mapi.registries.GameRulesRegistryDispatchableEvent;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameRules.Key;
import net.minecraft.world.level.GameRules.Type;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(value = GameRules.class, priority = 1005)
public class GameRulesMixin {

    @Shadow @Final
    private static Map<Key<?>, Type<?>> GAME_RULE_TYPES;

    @Inject(
            method = "<init>()V",
            at = @At("TAIL"),
            remap = false
    )

    private void onGameRulesRegistry(CallbackInfo ci) {
        GameRulesRegistryDispatchableEvent gameRulesRegistryDE = new GameRulesRegistryDispatchableEvent(GAME_RULE_TYPES);
        gameRulesRegistryDE.postEvent();
    }

}
