package M6FGR.mapi.network.packets.server;

import M6FGR.mapi.main.MAPI;
import M6FGR.mapi.network.functions.ICustomPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameRules.BooleanValue;
import net.minecraft.world.level.GameRules.GameRuleTypeVisitor;
import net.minecraft.world.level.GameRules.IntegerValue;
import net.minecraft.world.level.GameRules.Value;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public record SPGameRuleSync(String ruleName, int value) implements ICustomPacket<SPGameRuleSync> {
    public static final ResourceLocation ID = MAPI.getInstance().identifier("gamerule_sync");

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.value);
        buf.writeUtf(this.ruleName);
    }

    public static SPGameRuleSync decode(FriendlyByteBuf buf) {
        int val = buf.readInt();
        String name = buf.readUtf();
        return new SPGameRuleSync(name, val);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> SPGameRuleSync.handleGameRulesSync(this));
        });
        ctx.setPacketHandled(true);
    }

    private static void handleGameRulesSync(SPGameRuleSync packet) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            GameRules.visitGameRuleTypes(new GameRuleTypeVisitor() {
                @Override
                public <T extends Value<T>> void visit(@NotNull GameRules.Key<T> key, @NotNull GameRules.Type<T> type) {
                    if (key.getId().equals(packet.ruleName())) {
                        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();

                        GameRules targetRules = server.getGameRules();
                        T rule = targetRules.getRule(key);

                        if (rule instanceof BooleanValue boolRule) {
                            boolRule.set(packet.value() != 0, server);
                        } else if (rule instanceof IntegerValue intRule) {
                            intRule.set(packet.value(), server);
                        }
                    }
                }
            });
        }
    }
}