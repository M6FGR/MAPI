package m6fgr.mapi.client.sound.api.impl;

import m6fgr.mapi.client.sound.api.IConfigurableSound;
import m6fgr.mapi.client.sound.api.ISoundChannel;
import m6fgr.mapi.client.sound.api.ISoundInstanceBuilder;
import m6fgr.mapi.client.sound.api.SoundUtilsAPI;
import m6fgr.mapi.client.sound.data.SoundDataDefinition;
import m6fgr.mapi.client.sound.data.SoundDataRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SoundUtilsAPIImpl implements SoundUtilsAPI {

    private final Map<String, ISoundChannel> channels = new ConcurrentHashMap<>();

    public SoundUtilsAPIImpl() {

    }

    @Override
    public ISoundChannel getChannel(String channelName) {
        return this.channels.computeIfAbsent(channelName, SoundChannelImpl::new);
    }

    @Override
    public ISoundInstanceBuilder create(SoundEvent soundEvent) {
        return new SoundInstanceBuilderImpl(soundEvent);
    }

    @Override
    public ISoundInstanceBuilder createFromData(ResourceLocation dataId) {
        SoundDataDefinition def = SoundDataRegistry.get(dataId);
        if (def == null) {
            throw new IllegalArgumentException("SoundUtils API: No JSON definition found for data ID: " + dataId);
        }

        SoundEvent soundEvent = BuiltInRegistries.SOUND_EVENT.get(def.soundEvent());
        if (soundEvent == null) {
            throw new IllegalArgumentException("SoundUtils API: SoundEvent not found: " + def.soundEvent());
        }

        return create(soundEvent)
                .setCategory(def.getSoundSource())
                .setVolume(def.volume())
                .setPitch(def.pitch())
                .setSpeed(def.speed())
                .enableFadeIn(def.fadeInSpeed())
                .setLoopBounds(def.loopStart(), def.loopEnd());
    }

    @Override
    public IConfigurableSound playFromData(ResourceLocation dataId) {
        return createFromData(dataId).play();
    }
}