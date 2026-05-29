package dev.fix85.gracejump;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static Config INSTANCE;

    public boolean enabled = true;
    public int graceTicks = 5;

    public static Config get() {
        if (INSTANCE == null) {
            INSTANCE = new Config();
        }
        return INSTANCE;
    }

    private static Path getFilePath() {
        return FabricLoader.getInstance().getConfigDir().resolve("gracejump.json");
    }

    public static void load() {
        Path path = getFilePath();
        try {
            if (Files.exists(path)) {
                String json = Files.readString(path);
                Config loaded = GSON.fromJson(json, Config.class);
                if (loaded != null) {
                    INSTANCE = loaded;
                }
            } else {
                INSTANCE = new Config();
                save();
            }
        } catch (IOException e) {
            INSTANCE = new Config();
        }
    }

    public static void save() {
        try {
            Files.createDirectories(getFilePath().getParent());
            Files.writeString(getFilePath(), GSON.toJson(get()));
        } catch (IOException ignored) {}
    }
}
