package m6fgr.mapi.client.sound.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import m6fgr.mapi.cls.ILoadableClass;
import m6fgr.mapi.main.MAPI;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

import java.util.HashMap;
import java.util.Map;

public class SoundDataRegistry extends SimpleJsonResourceReloadListener implements ILoadableClass {
    private static final Gson GSON = new Gson();
    private static final Map<ResourceLocation, SoundDataDefinition> REGISTRY = new HashMap<>();

    public SoundDataRegistry() {
        super(GSON, "advanced_sounds");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectMap, ResourceManager resourceManager, ProfilerFiller profiler) {
        REGISTRY.clear();

        objectMap.forEach((location, jsonElement) -> {
            try {
                if (!jsonElement.isJsonObject()) return;
                JsonObject root = jsonElement.getAsJsonObject();

                if (root.has("sounds") && root.get("sounds").isJsonObject()) {
                    JsonObject soundsObj = root.getAsJsonObject("sounds");

                    for (String key : soundsObj.keySet()) {
                        if (!soundsObj.get(key).isJsonObject()) continue;

                        ResourceLocation soundId = ResourceLocation.tryParse(key);
                        if (soundId == null) {
                            MAPI.LOGGER.warn("Invalid sound entry key '{}' in file {}", key, location);
                            continue;
                        }

                        this.parseAndRegister(soundId, soundsObj.getAsJsonObject(key));
                    }
                } else {
                    this.parseAndRegister(location, root);
                }
            } catch (Exception e) {
                MAPI.LOGGER.error("Failed to load Sound API data file: {} ", location, e);
            }
        });
    }

    private void parseAndRegister(ResourceLocation soundId, JsonObject soundEntry) {
        if (!soundEntry.has("sound_event")) {
            MAPI.LOGGER.warn("Missing 'sound_event' field for sound entry '{}'", soundId);
            return;
        }

        ResourceLocation event = ResourceLocation.tryParse(soundEntry.get("sound_event").getAsString());
        if (event == null) {
            MAPI.LOGGER.warn("Invalid 'sound_event' ResourceLocation in sound entry '{}'", soundId);
            return;
        }

        String category = soundEntry.has("category") ? soundEntry.get("category").getAsString() : "master";
        float volume = soundEntry.has("volume") ? soundEntry.get("volume").getAsFloat() : 1.0f;
        float pitch = soundEntry.has("pitch") ? soundEntry.get("pitch").getAsFloat() : 1.0f;
        float speed = soundEntry.has("speed") ? soundEntry.get("speed").getAsFloat() : 1.0f;
        float fadeInSpeed = soundEntry.has("fade_in_speed") ? soundEntry.get("fade_in_speed").getAsFloat() : 0.05f;

        double start = 0.0;
        double end = -1.0;

        if (soundEntry.has("loop_bounds") && soundEntry.get("loop_bounds").isJsonObject()) {
            JsonObject bounds = soundEntry.getAsJsonObject("loop_bounds");
            start = bounds.has("start_seconds") ? bounds.get("start_seconds").getAsDouble() : 0.0;
            end = bounds.has("end_seconds") ? bounds.get("end_seconds").getAsDouble() : -1.0;
        }

        SoundDataDefinition def = new SoundDataDefinition(event, category, volume, pitch, speed, fadeInSpeed, start, end);
        REGISTRY.put(soundId, def);
    }

    public static SoundDataDefinition get(ResourceLocation id) {
        return REGISTRY.get(id);
    }

    public static boolean contains(ResourceLocation id) {
        return REGISTRY.containsKey(id);
    }

    @Override
    public void onGameConstructor(IEventBus gameBus) {
        gameBus.<AddReloadListenerEvent>addListener(event -> event.addListener(new SoundDataRegistry()));
    }
}