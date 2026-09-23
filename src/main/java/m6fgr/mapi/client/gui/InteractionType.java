package m6fgr.mapi.client.gui;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.inventory.MerchantMenu;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public enum InteractionType {
    TALK((player, target, ctx) -> player.sendSystemMessage(Component.literal("Interacted with " + target.getName().getString()))),

    ATTACK((player, target, ctx) -> target.hurt(player.damageSources().playerAttack(player), 2.0F)),

    HEAL((player, target, ctx) -> {
        if (target instanceof LivingEntity living) {
            living.heal(4.0F);
        }
    }),

    OPEN_TRADE((player, target, ctx) -> {
        if (target instanceof Villager villager) {
            villager.setTradingPlayer(player);

            player.openMenu(new SimpleMenuProvider(
                    (containerId, playerInventory, p) ->
                            new MerchantMenu(containerId, playerInventory, villager),
                    villager.getDisplayName()
            ));
        }
    });

    public static InteractionType createFrom(InteractionHandler handler) {
        TALK.handler = handler;
        return TALK;
    }

    @FunctionalInterface
    public interface InteractionHandler {
        void execute(ServerPlayer player, Entity target, IPayloadContext context);
    }

    private InteractionHandler handler;

    InteractionType(InteractionHandler handler) {
        this.handler = handler;
    }

    public void execute(ServerPlayer player, Entity target, IPayloadContext context) {
        if (this.handler != null) {
            this.handler.execute(player, target, context);
        }
    }
}