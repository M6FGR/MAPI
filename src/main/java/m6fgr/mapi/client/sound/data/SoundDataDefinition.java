package m6fgr.mapi.client.sound.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;

public record SoundDataDefinition(
        ResourceLocation soundEvent,
        String category,
        float volume,
        float pitch,
        float speed,
        float fadeInSpeed,
        double loopStart,
        double loopEnd
) {
    public SoundSource getSoundSource() {
        if (category == null) return SoundSource.MASTER;
        try {
            return SoundSource.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            return SoundSource.MASTER;
        }
    }
}