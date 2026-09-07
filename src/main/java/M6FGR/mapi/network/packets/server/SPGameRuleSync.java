package M6FGR.mapi.network.packets.server;

import M6FGR.mapi.main.MAPI;
import M6FGR.mapi.network.functions.ICustomPacket;
import M6FGR.mapi.utils.environment.EnvironmentHelper;
import com.nimbusds.openid.connect.sdk.federation.config.FederationEntityConfigurationErrorResponse;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameRules.BooleanValue;
import net.minecraft.world.level.GameRules.GameRuleTypeVisitor;
import net.minecraft.world.level.GameRules.IntegerValue;
import net.minecraft.world.level.GameRules.Value;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;

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


    private static void handleGameRulesSync(SPGameRuleSync packet) {
        Minecraft mc = Minecraft.getInstance();
        mc.execute(() -> {
            if (mc.level == null) return;

            GameRules gameRules = new GameRules(FeatureFlagSet.of());
            gameRules.visitGameRuleTypes(new GameRuleTypeVisitor() {
                @Override
                public <T extends GameRules.Value<T>> void visit(@NotNull GameRules.Key<T> key, @NotNull GameRules.Type<T> type) {
                    if (key.getId().equals(packet.ruleName())) {
                        T rule = gameRules.getRule(key);

                        // Client-side rule updates do not pass a MinecraftServer instance
                        if (rule instanceof GameRules.BooleanValue boolRule) {
                            boolRule.set(packet.value() != 0, null);
                        } else if (rule instanceof GameRules.IntegerValue intRule) {
                            intRule.set(packet.value(), null);
                        }
                    }
                }
            });
        });
    }
}