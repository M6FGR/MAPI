package m6fgr.mapi.builders;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.function.Predicate;

public class ConfigBuilder {

    private final ModConfigSpec.Builder builder;
    private ModConfigSpec builtSpec;

    private ConfigBuilder() {
        this.builder = new ModConfigSpec.Builder();
    }

    public static ConfigBuilder create() {
        return new ConfigBuilder();
    }

    public ConfigBuilder push(String category) {
        this.builder.push(category);
        return this;
    }

    public ConfigBuilder pop() {
        this.builder.pop();
        return this;
    }

    public ConfigBuilder comment(String... comments) {
        this.builder.comment(comments);
        return this;
    }

    public ConfigBuilder newBoolean(String path, boolean defaultValue) {
        this.builder.define(path, defaultValue);
        return this;
    }

    public ConfigBuilder newInt(String path, int defaultValue, int min, int max) {
        this.builder.defineInRange(path, defaultValue, min, max);
        return this;
    }

    public ConfigBuilder newDouble(String path, double defaultValue, double min, double max) {
        this.builder.defineInRange(path, defaultValue, min, max);
        return this;
    }

    public ConfigBuilder newString(String path, String defaultValue) {
        this.builder.define(path, defaultValue);
        return this;
    }

    public <T extends Enum<T>> ConfigBuilder newEnum(String path, T defaultValue) {
        this.builder.defineEnum(path, defaultValue);
        return this;
    }

    public <T> ConfigBuilder newList(String path, List<T> defaultValue, Predicate<Object> elementValidator) {
        this.builder.defineList(path, () -> defaultValue, elementValidator);
        return this;
    }

    public <T> ConfigBuilder newList(String path, List<T> defaultValue) {
        return this.newList(path, defaultValue, object -> true);
    }

    public ModConfigSpec build() {
        if (this.builtSpec == null) {
            this.builtSpec = this.builder.build();
        }
        return this.builtSpec;
    }


    public ModConfigSpec register(ModContainer container, ModConfig.Type type) {
        ModConfigSpec spec = this.build();
        container.registerConfig(type, spec);
        return spec;
    }


    public ModConfigSpec register(ModContainer container, ModConfig.Type type, String fileName) {
        ModConfigSpec spec = this.build();
        container.registerConfig(type, spec, fileName);
        return spec;
    }
}