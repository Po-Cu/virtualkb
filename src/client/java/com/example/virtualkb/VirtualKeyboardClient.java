package com.example.virtualkb;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class VirtualKeyboardClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModConfig.get();        // 读配置
        KeyBindings.register(); // 注册按键

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ModConfig cfg = ModConfig.get();

            // 鼠标锁定：光标可见，但无法直接操作世界
            MouseLock.tick(client, cfg);

            // 键盘代替鼠标
            if (cfg.enabled) {
                if (client.gui.screen() == null) {
                    MouseSubstitute.tick(client, cfg);
                    VisualKeysClick.tick(); // 按住重复触发
                } else {
                    VisualKeysClick.releaseHeld(); // 界面打开时释放按住的键，防止退出界面后被重新触发
                }
            }

            // 松开排队的模拟按键
            SimulatedInput.endTick();
        });
    }
}
