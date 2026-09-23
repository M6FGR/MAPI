package m6fgr.mapi.client.gui;

import m6fgr.mapi.client.gui.data.DialogueData;
import m6fgr.mapi.client.gui.screens.InteractionMiniScreen;
import m6fgr.mapi.client.gui.screens.InteractionMiniScreen.OptionStyle;
import m6fgr.mapi.events.mapi.common.EntityInteractDispatchableEvent;
import m6fgr.mapi.utils.MathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public final class InteractionAPI {

    private static final InteractionAPI INSTANCE = new InteractionAPI();

    private boolean interacting;
    private Entity currentTarget;
    private double maxInteractionDistanceSqr = 36.0;

    private static final float ROTATION_SPEED = 12.0F;

    private float lookAtPlayerDuration = 3.0F; // Entity turns head for 3 seconds
    private float lookTimer = 0.0F;

    private InteractionMiniScreen interactionMiniScreen;

    private final List<Predicate<Entity>> interactionFilters = new ArrayList<>();

    public static InteractionAPI getInstance() {
        return INSTANCE;
    }

    private InteractionAPI() {
        this.registerFilter(entity -> entity instanceof LivingEntity
                && !(entity instanceof Projectile)
                && entity.isAlive());
    }

    public void interactWithEntity(Player interacter, Entity interactedEntity) {
        DialogueData defaultDialogue = DialogueData.builder(
                interactedEntity.getDisplayName(),
                "Hello!"
        ).addOption(Component.literal("Hi"), InteractionType.TALK)
         .addOption(Component.literal("Go fuck yourself"), InteractionType.TALK)
         .style(OptionStyle.QUESTION)
         .build();

        this.interactWithEntity(interacter, interactedEntity, defaultDialogue);
    }

    public void interactWithEntity(Player interacter, Entity interactedEntity, DialogueData dialogueData) {
        if (!(interacter instanceof LocalPlayer localPlayer)) {
            return;
        }

        if (localPlayer.distanceToSqr(interactedEntity) > this.maxInteractionDistanceSqr) {
            localPlayer.displayClientMessage(Component.literal("Target is too far away!"), true);
            return;
        }

        EntityInteractDispatchableEvent entityInteractDE = new EntityInteractDispatchableEvent(interacter, interactedEntity, INSTANCE);
        entityInteractDE.postEvent();

        if (entityInteractDE.isCanceled()) {
            return;
        }

        Entity target = entityInteractDE.getInteractedTarget();

        if (this.canInteractWith(target)) {
            Minecraft mc = Minecraft.getInstance();
            InteractionMiniScreen screen = new InteractionMiniScreen(target, dialogueData, dialogueData.getOptionStyle());

            this.interactionMiniScreen = screen;
            mc.setScreen(screen);
            this.startInteraction(target);
        } else {
            Minecraft.getInstance().gui.setOverlayMessage(
                    Component.literal("Entity " + target.getDisplayName().getString() + " cannot speak!"),
                    false
            );
        }
    }

    public void setEntityNameColor(int red, int green, int blue, int transparency) {
        this.interactionMiniScreen.setEntityNameColor(MathUtil.toHex(red, green, blue, transparency));
    }

    public void setInteractionScreenColor(int red, int green, int blue, int transparency) {
        this.interactionMiniScreen.setWidgetColor(MathUtil.toHex(red, green, blue, transparency));
    }

    public void setInteractionScreenOutlineColor(int red, int green, int blue, int transparency) {
        this.interactionMiniScreen.setWidgetOutlineColor(MathUtil.toHex(red, green, blue, transparency));
    }

    public void setTextColor(int red, int green, int blue, int transparency) {
        this.interactionMiniScreen.setTextColor(MathUtil.toHex(red, green, blue, transparency));
    }

    public void startInteraction(Entity target) {
        this.interacting = true;
        this.currentTarget = target;
        this.lookTimer = 0.0F;
    }


    public void updateInteractionLock(float deltaSeconds) {
        if (!this.interacting) {
            return;
        }

        float clampedDelta = Math.min(deltaSeconds, 0.1F);
        this.renderInteractionLockDelta(clampedDelta);
    }

    private void renderInteractionLockDelta(float deltaSeconds) {
        if (!this.interacting || this.currentTarget == null || !this.currentTarget.isAlive()) {
            this.closeInteraction();
            return;
        }

        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;

        if (player.distanceToSqr(this.currentTarget) > this.maxInteractionDistanceSqr) {
            this.closeInteraction();
            return;
        }

        Vec3 currentVelocity = player.getDeltaMovement();
        player.setDeltaMovement(0.0, currentVelocity.y, 0.0);

        Vec3 playerEyePos = player.getEyePosition();
        Vec3 targetEyePos = this.currentTarget.getEyePosition();

        double dx = targetEyePos.x - playerEyePos.x;
        double dy = targetEyePos.y - playerEyePos.y;
        double dz = targetEyePos.z - playerEyePos.z;

        double horizontalDist = Math.sqrt(dx * dx + dz * dz);

        float targetYaw = (float) (Mth.atan2(dz, dx) * (180.0 / Math.PI)) - 90.0F;
        float targetPitch = (float) (-(Mth.atan2(dy, horizontalDist) * (180.0 / Math.PI)));

        float lerpFactor = 1.0F - (float) Math.exp(-ROTATION_SPEED * deltaSeconds);

        float currentYaw = player.getYRot();
        float currentPitch = player.getXRot();

        float yawDelta = Mth.wrapDegrees(targetYaw - currentYaw);
        float pitchDelta = targetPitch - currentPitch;

        float newYaw = currentYaw + yawDelta * lerpFactor;
        float newPitch = currentPitch + pitchDelta * lerpFactor;

        player.yRotO = currentYaw;
        player.xRotO = currentPitch;
        player.setYRot(newYaw);
        player.setXRot(newPitch);

        if (this.lookTimer < this.lookAtPlayerDuration) {
            this.lookTimer += deltaSeconds;

            if (this.currentTarget instanceof LivingEntity livingTarget) {
                double revDx = playerEyePos.x - targetEyePos.x;
                double revDy = playerEyePos.y - targetEyePos.y;
                double revDz = playerEyePos.z - targetEyePos.z;
                double revHoriz = Math.sqrt(revDx * revDx + revDz * revDz);

                float lookYaw = (float) (Mth.atan2(revDz, revDx) * (180.0 / Math.PI)) - 90.0F;
                float lookPitch = (float) (-(Mth.atan2(revDy, revHoriz) * (180.0 / Math.PI)));

                float bodyYaw = livingTarget.yBodyRot;
                float angleToPlayer = Math.abs(Mth.wrapDegrees(lookYaw - bodyYaw));

                boolean inFieldOfView = angleToPlayer <= 60.0F;
                boolean hasLineOfSight = livingTarget.hasLineOfSight(player);

                if (inFieldOfView && hasLineOfSight) {
                    if (this.currentTarget instanceof Mob mobTarget) {
                        mobTarget.getNavigation().stop();
                    }

                    livingTarget.xRotO = livingTarget.getXRot();
                    livingTarget.yHeadRotO = livingTarget.getYHeadRot();

                    float targetHeadYawDelta = Mth.wrapDegrees(lookYaw - livingTarget.getYHeadRot());
                    float targetPitchDelta = lookPitch - livingTarget.getXRot();

                    livingTarget.setXRot(livingTarget.getXRot() + targetPitchDelta * lerpFactor);
                    livingTarget.setYHeadRot(livingTarget.getYHeadRot() + targetHeadYawDelta * lerpFactor);
                }
            }
        }
    }

    public void closeInteraction() {
        if (this.interacting) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.screen instanceof InteractionMiniScreen) {
                mc.setScreen(null);
            }
            this.resetState();
        }
    }

    public void resetState() {
        this.interacting = false;
        this.currentTarget = null;
        this.lookTimer = 0.0F;
    }

    public void registerFilter(Predicate<Entity> filter) {
        this.interactionFilters.add(filter);
    }

    public boolean canInteractWith(Entity entity) {
        if (entity == null) return false;

        for (Predicate<Entity> filter : this.interactionFilters) {
            if (!filter.test(entity)) {
                return false;
            }
        }
        return true;
    }

    public boolean isInteracting() {
        return this.interacting;
    }

    public boolean isInteractingWith(Entity entity) {
        return this.isInteracting() && this.currentTarget == entity;
    }

    public Entity getCurrentTarget() {
        return this.currentTarget;
    }

    public double getMaxInteractionDistanceSqr() {
        return this.maxInteractionDistanceSqr;
    }

    public void setMaxInteractionDistance(float blocks) {
        this.maxInteractionDistanceSqr = blocks * blocks;
    }

    public float getLookAtPlayerDuration() {
        return this.lookAtPlayerDuration;
    }

    public void setLookAtPlayerDuration(float seconds) {
        this.lookAtPlayerDuration = seconds;
    }
}