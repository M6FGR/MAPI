package m6fgr.mapi.events.mapi.common;

import m6fgr.mapi.client.gui.InteractionAPI;
import m6fgr.mapi.events.dispatch.DispatchableEvent;
import m6fgr.mapi.events.dispatch.dispatchers.marks.HybridEvent;
import m6fgr.mapi.events.dispatch.extra.EventBus;
import m6fgr.mapi.events.dispatch.extra.IDispatchableEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

@HybridEvent(EventBus.MOD)
public class EntityInteractDispatchableEvent extends DispatchableEvent implements IDispatchableEvent {

    private final Player interactor;
    private final Entity interactedTarget;
    private final InteractionAPI interactionAPI;

    public EntityInteractDispatchableEvent(Player interactor, Entity interactedTarget, InteractionAPI interactionAPI) {
        this.interactor = interactor;
        this.interactedTarget = interactedTarget;
        this.interactionAPI = interactionAPI;
    }

    public Player getInteractor() {
        return this.interactor;
    }

    public Entity getInteractedTarget() {
        return this.interactedTarget;
    }

    public InteractionAPI getInteractionAPI() {
        return this.interactionAPI;
    }

    @Override
    public void postEvent() {

    }
}
