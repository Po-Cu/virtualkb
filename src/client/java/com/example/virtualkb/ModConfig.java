package com.example.virtualkb;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;

/**
 * 配置数据类。现在只有一个总开关。
 * 替代按键不再存配置，而是注册成真正的 KeyMapping，玩家可在原版“按键设置”里改键。
 */
public class ModConfig {

    // 总开关：启用后鼠标锁定 + 键盘代替鼠标 + 点击 VisualKeys 键盘操作
    public boolean enabled = false;

    // 虚拟键盘透明度（0.0 = 全透明，1.0 = 不透明）
    public float keyboardOpacity = 1.0f;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static ModConfig INSTANCE;

    public static ModConfig get() {
        if (INSTANCE == null) {
            INSTANCE = load();
        }
        return INSTANCE;
    }

    public static void save() {
        if (INSTANCE == null) return; // 尚未初始化时不写
        writeToDisk(INSTANCE);
    }

    private static void writeToDisk(ModConfig cfg) {
        Path path = configPath();
        try {
            Files.createDirectories(path.getParent());
            Files.writeString(path, GSON.toJson(cfg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ModConfig load() {
        Path path = configPath();
        if (Files.exists(path)) {
            try {
                ModConfig cfg = GSON.fromJson(Files.readString(path), ModConfig.class);
                if (cfg != null) return cfg; // 内容为 "null" 时返回 null，回退默认值
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ModConfig fresh = new ModConfig();
        writeToDisk(fresh);
        return fresh;
    }

    private static Path configPath() {
        return FabricLoader.getInstance().getConfigDir().resolve("virtualkb.json");
    }
}
