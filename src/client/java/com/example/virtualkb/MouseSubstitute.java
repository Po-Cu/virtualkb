package com.example.virtualkb;

import com.example.virtualkb.mixin.KeyMappingAccessor;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

public class MouseSubstitute {

    // 记录小键盘4（左键）上一帧是否按下，用于“边缘触发”
    private static boolean wasLeftPressed = false;

    public static void tick(Minecraft client, ModConfig cfg) {
        LocalPlayer player = client.player;
        if (player == null) return;
        Options options = client.options;

        // 左键（小键盘4）：直接写 isDown 字段（绕过 ToggleKeyMapping 的翻转语义），
        // 每 tick 同步按住状态；按下瞬间额外 click 一次触发初始挥动/开始挖掘。
        boolean leftPressed = KeyBindings.SUB_LEFT.isDown();
        ((KeyMappingAccessor) options.keyAttack).virtualkb$setIsDown(leftPressed);
        if (leftPressed && !wasLeftPressed) {
            KeyMapping.click(((KeyMappingAccessor) options.keyAttack).virtualkb$getBoundKey());
        }
        wasLeftPressed = leftPressed;

        // 右键（小键盘6）：单击使用/交互
        while (KeyBindings.SUB_RIGHT.consumeClick()) {
            SimulatedInput.tap(options.keyUse);
        }

        // 中键（小键盘5）：单击拾取方块
        while (KeyBindings.SUB_MIDDLE.consumeClick()) {
            SimulatedInput.tap(options.keyPickItem);
        }

        // 滚轮（小键盘8=上滚，2=下滚）
        while (KeyBindings.SCROLL_UP.consumeClick()) {
            scrollHotbar(client, -1);
        }
        while (KeyBindings.SCROLL_DOWN.consumeClick()) {
            scrollHotbar(client, +1);
        }

        // 视角移动（方向键）
        look(client, player);
    }

    private static void scrollHotbar(Minecraft client, int direction) {
        Inventory inv = client.player.getInventory();
        int current = inv.getSelectedSlot();              // 当前选中槽位 0~8
        int next = Math.floorMod(current + direction, 9); // floorMod 保证 0~8 循环
        inv.setSelectedSlot(next);
    }

    private static void look(Minecraft client, LocalPlayer player) {
        // 转动速度由原版鼠标灵敏度决定（0.0 ~ 1.0，默认 0.5）
        double sensitivity = client.options.sensitivity().get();
        float step = (float) (sensitivity * 10.0); // 每游戏 tick 旋转的角度

        if (KeyBindings.LOOK_UP.isDown())    player.setXRot(player.getXRot() - step);
        if (KeyBindings.LOOK_DOWN.isDown())  player.setXRot(player.getXRot() + step);
        if (KeyBindings.LOOK_LEFT.isDown())  player.setYRot(player.getYRot() - step);
        if (KeyBindings.LOOK_RIGHT.isDown()) player.setYRot(player.getYRot() + step);

        // 钳制俯仰角到 [-90, 90]，防止视角翻转；偏航角归一化
        player.setXRot(Mth.clamp(player.getXRot(), -90.0f, 90.0f));
        player.setYRot(Mth.wrapDegrees(player.getYRot()));
    }
}
