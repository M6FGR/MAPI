package m6fgr.mapi.network.packets.client;

import m6fgr.mapi.client.gui.InteractionType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CPEntityInteraction(int targetEntityId, InteractionType interactionType) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<CPEntityInteraction> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("mapi", "do_entity_interaction"));

    public static final StreamCodec<RegistryFriendlyByteBuf, CPEntityInteraction> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, CPEntityInteraction::targetEntityId,
            ByteBufCodecs.idMapper(id -> InteractionType.values()[id], InteractionType::ordinal), CPEntityInteraction::interactionType,
            CPEntityInteraction::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                Entity target = player.level().getEntity(this.targetEntityId());

                if (target != null && target.isAlive() && player.distanceToSqr(target) <= 64.0) {
                    this.interactionType().execute(player, target, context);
                }
            }
        });
    }
}