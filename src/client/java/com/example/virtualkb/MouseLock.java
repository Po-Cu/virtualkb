package com.example.virtualkb;

import net.minecraft.client.Minecraft;

public class MouseLock {

    /** 启用时释放鼠标抓取（光标可见），停用时恢复抓取 */
    public static void tick(Minecraft client, ModConfig cfg) {
        boolean noScreen = client.gui.screen() == null;
        boolean grabbed = client.mouseHandler.isMouseGrabbed();

        if (cfg.enabled && noScreen && grabbed) {
            client.mouseHandler.releaseMouse(); // 解锁：光标可见、可自由移动
        } else if (!cfg.enabled && noScreen && !grabbed) {
            client.mouseHandler.grabMouse();    // 恢复原版抓取
        }
    }
}
