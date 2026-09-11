package m6fgr.mapi.network.packets.server;

import m6fgr.mapi.main.MAPI;
import m6fgr.mapi.network.functions.ICustomPacket;
import m6fgr.mapi.utils.environment.EnvironmentHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRules;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SPGameRuleSync(String ruleName, int value) implements ICustomPacket<SPGameRuleSync> {
    public static final Type<SPGameRuleSync> TYPE = new Type<>(MAPI.getInstance().identifier("gamerule_sync"));

    public static final StreamCodec<FriendlyByteBuf, SPGameRuleSync> CODEC = ICustomPacket.codecOf(
            SPGameRuleSync.class,
            SPGameRuleSync::encode,
            SPGameRuleSync::decode
    );

    @Override
    public Type<SPGameRuleSync> type() {
        return TYPE;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.value);
        buf.writeUtf(this.ruleName);
    }

    @Override
    public SPGameRuleSync decode(FriendlyByteBuf buf) {
       return new SPGameRuleSync(buf.readUtf(), buf.readInt());
    }

    @Override
    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (EnvironmentHelper.getEnvironment().isClient())
                SPGameRuleSync.handleGameRulesSync(this);
        });
    }


    @OnlyIn(Dist.CLIENT)
    public static void handleGameRulesSync(SPGameRuleSync packet) {
        Minecraft mc = Minecraft.getInstance();
        mc.execute(() -> {
            if (mc.level == null) return;

            GameRules gameRules = mc.level.getServer().getGameRules();
            Identifier ruleId = Identifier.tryParse(packet.ruleName());

            if (ruleId == null) return;

            BuiltInRegistries.GAME_RULE.getOptional(ruleId).ifPresent(rule -> setRuleValue(gameRules, rule, packet.value()));
        });
    }

    @SuppressWarnings("unchecked")
    private static <V> void setRuleValue(GameRules gameRules, GameRule<V> rule, int rawValue) {
        if (rule.defaultValue() instanceof Boolean) {
            gameRules.set((GameRule<Boolean>) rule, rawValue != 0, null);
        } else if (rule.defaultValue() instanceof Integer) {
            gameRules.set((GameRule<Integer>) rule, rawValue, null);
        }
    }
}