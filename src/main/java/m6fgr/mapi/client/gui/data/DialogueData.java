package m6fgr.mapi.client.gui.data;

import m6fgr.mapi.client.gui.InteractionType;
import m6fgr.mapi.client.gui.screens.InteractionMiniScreen.OptionStyle;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class DialogueData {

    private final Component speakerName;
    private final String text;
    private final int charsPerTick;
    private final OptionStyle optionStyle;
    private final List<DialogueOption> options = new ArrayList<>();

    public DialogueData(Component speakerName, String text, int charsPerTick, OptionStyle optionStyle) {
        this.speakerName = speakerName;
        this.text = text;
        this.charsPerTick = charsPerTick;
        this.optionStyle = optionStyle;
    }

    public static Builder builder(Component speakerName, String text) {
        return new Builder(speakerName, text);
    }

    public Component getSpeakerName() {
        return this.speakerName;
    }

    public String getText() {
        return this.text;
    }

    public int getCharsPerTick() {
        return this.charsPerTick;
    }

    public OptionStyle getOptionStyle() {
        return this.optionStyle;
    }

    public List<DialogueOption> getOptions() {
        return this.options;
    }

    public record DialogueOption(
            Component label,
            InteractionType interactionType,
            Runnable clientCallback
    ) {}

    public static class Builder {
        private final Component speakerName;
        private final String text;
        private int charsPerTick = 2;
        private OptionStyle optionStyle = OptionStyle.CUSTOM;
        private final List<DialogueOption> options = new ArrayList<>();

        public Builder(Component speakerName, String text) {
            this.speakerName = speakerName;
            this.text = text;
        }

        public Builder speed(int charsPerTick) {
            this.charsPerTick = Math.max(1, charsPerTick);
            return this;
        }

        public Builder style(OptionStyle style) {
            this.optionStyle = style;
            return this;
        }

        public Builder addOption(Component label, InteractionType interactionType, Runnable clientCallback) {
            this.options.add(new DialogueOption(label, interactionType, clientCallback));
            return this;
        }

        public Builder addOption(String labelText, InteractionType interactionType, Runnable clientCallback) {
            return addOption(Component.literal(labelText), interactionType, clientCallback);
        }

        public Builder addOption(Component label, InteractionType interactionType) {
            return addOption(label, interactionType, () -> {});
        }

        public Builder addOption(String labelText, InteractionType interactionType) {
            return addOption(Component.literal(labelText), interactionType, () -> {});
        }

        public Builder addClientOption(Component label, Runnable clientCallback) {
            return addOption(label, null, clientCallback);
        }

        public Builder addClientOption(String labelText, Runnable clientCallback) {
            return addOption(Component.literal(labelText), null, clientCallback);
        }

        public DialogueData build() {
            DialogueData data = new DialogueData(this.speakerName, this.text, this.charsPerTick, this.optionStyle);
            data.options.addAll(this.options);
            return data;
        }
    }
}