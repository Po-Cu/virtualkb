package com.example.virtualkb;

import com.example.virtualkb.mixin.KeyMappingAccessor;
import com.mojang.blaze3d.platform.InputConstants;
import io.github.dimaskama.visualkeys.client.KeyEntry;
import io.github.dimaskama.visualkeys.client.KeyboardRenderOptions;
import io.github.dimaskama.visualkeys.client.VisualKeys;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.PauseScreen;

public class VisualKeysClick {

    /** 根据屏幕坐标找到被点击的虚拟键；没点到或键盘未显示返回 null */
    public static KeyEntry findKey(double mouseX, double mouseY) {
        if (!VisualKeys.CONFIG.getData().enabled) return null; // VisualKeys 键盘没显示
        KeyboardRenderOptions o = VisualKeys.CONFIG.getData().hudRenderOptions;

        int mx = (int) ((mouseX - o.keyboardX) / o.keyboardScale);
        int my = (int) ((mouseY - o.keyboardY) / o.keyboardScale);

        for (KeyEntry key : VisualKeys.QWERTY.map.values()) {
            if (!key.type.isVisible(o)) continue;
            int kx = key.getX(o);
            int ky = key.getY(o);
            if (mx >= kx && my >= ky && mx < kx + key.width && my < ky + key.height) {
                return key;
            }
        }
        return null;
    }

    /** 触发/释放某个物理键（扫描码）对应的操作 */
    public static void trigger(int scancode, boolean pressed) {
        if (scancode < 0) return;
        // ESC / F3 在 26.2 里不走普通 KeyMapping 的 set/click，需要单独处理：
        // ESC 打开暂停菜单/关闭界面；F3 切换调试界面（DebugScreenEntryList.toggleDebugOverlay）。
        if (pressed && scancode == InputConstants.KEY_ESCAPE) { handleEscape(); return; }
        if (pressed && scancode == InputConstants.KEY_F3) { handleF3(); return; }
        // F2 截图 / F11 全屏 / O 好友列表：都走原版 handleGlobalKeyPress（事件匹配式）
        if (pressed && (scancode == InputConstants.KEY_F2
                || scancode == InputConstants.KEY_F11
                || scancode == InputConstants.KEY_O)) {
            handleGlobalKey(scancode);
            return;
        }

        InputConstants.Key boundKey = findBoundKey(scancode);
        if (boundKey != null) {
            KeyMapping.set(boundKey, pressed); // 按住状态（isDown）
            if (pressed) KeyMapping.click(boundKey); // 点击计数（consumeClick）
        }
    }

    /** 按住时重复“点击”，模拟系统按键重复（用于按住 Q 连续丢弃等） */
    public static void repeatClick(int scancode) {
        InputConstants.Key boundKey = findBoundKey(scancode);
        if (boundKey != null) {
            KeyMapping.click(boundKey);
        }
    }

    private static InputConstants.Key findBoundKey(int scancode) {
        for (KeyMapping km : KeyMappingAccessor.virtualkb$getAllKeyMappings().values()) {
            InputConstants.Key boundKey = ((KeyMappingAccessor) km).virtualkb$getBoundKey();
            if (boundKey.getValue() == scancode) {
                return boundKey;
            }
        }
        return null;
    }

    private static void handleEscape() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.gui.screen() != null) {
            mc.gui.screen().onClose(); // 关闭当前界面
        } else if (mc.player != null) {
            mc.gui.setScreen(new PauseScreen(true));
        }
    }

    private static void handleF3() {
        // 26.2 的调试界面切换：直接调用 DebugScreenEntryList.toggleDebugOverlay()
        Minecraft.getInstance().debugEntries.toggleDebugOverlay();
    }

    private static void handleGlobalKey(int scancode) {
        // 把 scancode 转成 InputConstants.Key，交给原版 handleGlobalKeyPress 处理
        // （F2=截图、F11=全屏、O=好友列表；toggleFullscreen/toggleFriendsScreen 都是 private）
        Minecraft mc = Minecraft.getInstance();
        mc.handleGlobalKeyPress(InputConstants.Type.KEYSYM.getOrCreate(scancode), true);
    }

    // —— 按住状态与重复 ——
    private static int heldScancode = -1;
    private static int repeatCooldown = 0;

    public static int getHeldScancode() {
        return heldScancode;
    }

    public static void setHeldScancode(int code) {
        heldScancode = code;
        repeatCooldown = 0;
    }

    /** 释放当前按住的键（例如打开界面时调用，防止退出界面后被重复触发） */
    public static void releaseHeld() {
        if (heldScancode >= 0) {
            trigger(heldScancode, false);
            heldScancode = -1;
            repeatCooldown = 0;
        }
    }

    /** 每 tick 调用：处理“按住重复” */
    public static void tick() {
        if (heldScancode < 0) return;
        if (++repeatCooldown >= 4) { // 每 4 tick（约 0.2 秒）重复一次
            repeatCooldown = 0;
            repeatClick(heldScancode);
        }
    }
}
