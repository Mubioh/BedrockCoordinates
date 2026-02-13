package me.mubioh.bedrockcoordinates.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public final class CoordinateConfig {

    public enum DisplayMode {
        BLOCK,
        XYZ
    }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("bedrockcoordinates.json");

    private static CoordinateConfig INSTANCE = new CoordinateConfig();

    private boolean showCoordinates = true;

    private DisplayMode displayMode = DisplayMode.BLOCK;

    public static CoordinateConfig instance() { return INSTANCE; }

    public boolean showCoordinates() { return showCoordinates; }

    public void setShowCoordinates(boolean value) { this.showCoordinates = value; }

    public DisplayMode displayMode() { return displayMode == null ? DisplayMode.BLOCK : displayMode; }

    public void setDisplayMode(DisplayMode mode) { this.displayMode = (mode == null) ? DisplayMode.BLOCK : mode; }

    public DisplayMode toggleDisplayMode() {
        DisplayMode next = (displayMode() == DisplayMode.BLOCK) ? DisplayMode.XYZ : DisplayMode.BLOCK;
        setDisplayMode(next);
        return next;
    }

    public static void load() {
        if (!Files.exists(CONFIG_PATH)) {
            save();
            return;
        }

        try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
            CoordinateConfig loaded = GSON.fromJson(reader, CoordinateConfig.class);
            if (loaded != null) {
                if (loaded.displayMode == null) loaded.displayMode = DisplayMode.BLOCK;
                INSTANCE = loaded;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                GSON.toJson(INSTANCE, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}