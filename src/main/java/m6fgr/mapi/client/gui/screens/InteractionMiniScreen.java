package m6fgr.mapi.client.gui.screens;

import m6fgr.mapi.client.gui.InteractionAPI;
import m6fgr.mapi.client.gui.data.DialogueData;
import m6fgr.mapi.network.MAPINetworkManager;
import m6fgr.mapi.network.packets.client.CPEntityInteraction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class InteractionMiniScreen extends Screen {

    public enum OptionStyle {
        CUSTOM,
        SINGLE,
        // Add 2 options for it to render both!
        QUESTION
    }

    private final Entity targetEntity;
    private final DialogueData dialogueData;
    private final OptionStyle optionStyle;
    private final List<Button> optionButtons = new ArrayList<>();

    private int visibleCharCount = 0;
    private boolean isTypingComplete = false;

    private static final int BOX_WIDTH = 220;
    private static final int BOX_HEIGHT = 60;

    private int widgetColor = 0x80101010;
    private int widgetOutlineColor = 0xAA555555;
    private int entityNameColor = 0xFFFF55;
    private int textColor = 0xFFFFFF;

    public InteractionMiniScreen(Entity targetEntity, DialogueData dialogueData, OptionStyle optionStyle) {
        super(dialogueData.getSpeakerName());
        this.targetEntity = targetEntity;
        this.dialogueData = dialogueData;
        this.optionStyle = optionStyle;
    }

    public InteractionMiniScreen(Entity targetEntity, DialogueData dialogueData) {
        this(targetEntity, dialogueData, OptionStyle.CUSTOM);
    }

    @Override
    protected void init() {
        this.optionButtons.clear();

        int centerX = this.width / 2;
        int boxY = this.height - 100;
        int boxLeft = centerX - (BOX_WIDTH / 2);
        int buttonStartY = boxY + 38;

        List<DialogueData.DialogueOption> options = this.dialogueData.getOptions();
        if (options.isEmpty()) return;

        switch (this.optionStyle) {
            case SINGLE -> {
                DialogueData.DialogueOption singleOption = options.getFirst();
                int btnWidth = 100;
                int btnX = centerX - (btnWidth / 2);

                Button btn = createOptionButton(singleOption, btnX, buttonStartY, btnWidth, 18);
                this.optionButtons.add(btn);
                this.addRenderableWidget(btn);
            }
            case QUESTION, CUSTOM -> {
                // Dynamically computes equal column widths for N options across the dialogue frame width
                int totalOptions = options.size();
                int padding = 5;
                int totalPadding = padding * (totalOptions + 1);
                int availableWidth = BOX_WIDTH - totalPadding;
                int btnWidth = Math.max(30, availableWidth / totalOptions);

                for (int i = 0; i < totalOptions; i++) {
                    DialogueData.DialogueOption option = options.get(i);
                    int btnX = boxLeft + padding + i * (btnWidth + padding);

                    Button btn = createOptionButton(option, btnX, buttonStartY, btnWidth, 18);
                    this.optionButtons.add(btn);
                    this.addRenderableWidget(btn);
                }
            }
        }
    }

    private Button createOptionButton(DialogueData.DialogueOption option, int x, int y, int width, int height) {
        Button btn = Button.builder(option.label(), button -> {
            option.clientCallback().run();

            if (option.interactionType() != null && this.targetEntity != null) {
                MAPINetworkManager.sendToServer(new CPEntityInteraction(this.targetEntity.getId(), option.interactionType()));
            }
            this.onClose();
        }).bounds(x, y, width, height).build();

        btn.visible = this.isTypingComplete;
        return btn;
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {}

    @Override
    public void tick() {
        super.tick();
        String fullText = this.dialogueData.getText();
        if (!this.isTypingComplete) {
            this.visibleCharCount = Math.min(fullText.length(), this.visibleCharCount + this.dialogueData.getCharsPerTick());

            if (this.visibleCharCount < fullText.length()) {
                Minecraft.getInstance().getSoundManager().play(
                        SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK.value(), 1.8F, 0.25F)
                );
            } else {
                this.finishSentence();
            }
        }
    }

    private void finishSentence() {
        this.isTypingComplete = true;
        this.visibleCharCount = dialogueData.getText().length();

        for (Button btn : this.optionButtons) {
            btn.visible = true;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!this.isTypingComplete && button == 0) {
            this.finishSentence();
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!this.isTypingComplete && (keyCode == GLFW.GLFW_KEY_SPACE || keyCode == GLFW.GLFW_KEY_ENTER)) {
            this.finishSentence();
            return true;
        }

        if (this.isTypingComplete && keyCode >= GLFW.GLFW_KEY_1 && keyCode <= GLFW.GLFW_KEY_9) {
            int optionIndex = keyCode - GLFW.GLFW_KEY_1;
            if (optionIndex < this.optionButtons.size()) {
                Button btn = this.optionButtons.get(optionIndex);
                if (btn.visible && btn.active) {
                    btn.onPress();
                    return true;
                }
            }
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        int centerX = this.width / 2;
        int boxY = this.height - 100;
        int boxLeft = centerX - (BOX_WIDTH / 2);
        int boxRight = centerX + (BOX_WIDTH / 2);

        guiGraphics.fill(boxLeft, boxY, boxRight, boxY + BOX_HEIGHT, this.widgetColor);
        guiGraphics.renderOutline(boxLeft, boxY, BOX_WIDTH, BOX_HEIGHT, this.widgetOutlineColor);

        guiGraphics.drawString(this.font, this.title, boxLeft + 6, boxY + 6, this.entityNameColor, true);

        String currentText = this.dialogueData.getText().substring(0, this.visibleCharCount);
        guiGraphics.drawWordWrap(this.font, Component.literal(currentText), boxLeft + 6, boxY + 18, BOX_WIDTH - 12, this.textColor);
    }

    public void setEntityNameColor(int entityNameColor) {
        this.entityNameColor = entityNameColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setWidgetColor(int widgetColor) {
        this.widgetColor = widgetColor;
    }

    public void setWidgetOutlineColor(int widgetOutlineColor) {
        this.widgetOutlineColor = widgetOutlineColor;
    }

    @Override
    public void onClose() {
        super.onClose();
        InteractionAPI.getInstance().resetState();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}