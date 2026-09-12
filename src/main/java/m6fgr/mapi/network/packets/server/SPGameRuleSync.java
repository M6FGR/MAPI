package m6fgr.mapi.network.packets.server;

import m6fgr.mapi.main.MAPI;
import m6fgr.mapi.network.functions.ICustomPacket;
import m6fgr.mapi.network.functions.PacketBufHandler;
import m6fgr.mapi.utils.environment.EnvironmentHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.MinecraftServer;
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

    public static final StreamCodec<FriendlyByteBuf, SPGameRuleSync> CODEC =
            PacketBufHandler.codecOf(
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
        if (mc.level != null) {
            GameRules.visitGameRuleTypes(new GameRuleTypeVisitor() {
                @Override
                public <T extends Value<T>> void visit(@NotNull GameRules.Key<T> key, @NotNull GameRules.Type<T> type) {
                    if (key.getId().equals(packet.ruleName())) {
                        T rule = mc.level.getGameRules().getRule(key);
                        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
                        switch (rule) {
                            case BooleanValue boolRule -> boolRule.set(packet.value() != 0, server);
                            case IntegerValue intRule -> intRule.set(packet.value(), server);
                            default -> throw new IllegalStateException("Unexpected value: " + rule);
                        }
                    }
                }
            });
        }
    }
}