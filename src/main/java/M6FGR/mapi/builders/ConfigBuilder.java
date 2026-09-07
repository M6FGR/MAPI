package M6FGR.mapi.builders;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import java.util.List;
import java.util.function.Predicate;

public class ConfigBuilder {

    private final ForgeConfigSpec.Builder builder;
    private ForgeConfigSpec builtSpec;

    private ConfigBuilder() {
        this.builder = new ForgeConfigSpec.Builder();
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

    public ForgeConfigSpec build() {
        if (this.builtSpec == null) {
            this.builtSpec = this.builder.build();
        }
        return this.builtSpec;
    }

    public ForgeConfigSpec register(ModConfig.Type type, ModLoadingContext context) {
        ForgeConfigSpec spec = this.build();
        context.registerConfig(type, spec);
        return spec;
    }

    public ForgeConfigSpec register(ModConfig.Type type, ModLoadingContext context, String fileName) {
        ForgeConfigSpec spec = this.build();
        context.registerConfig(type, spec, fileName);
        return spec;
    }

}