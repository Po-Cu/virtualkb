package com.example.virtualkb;

import com.example.virtualkb.mixin.KeyMappingAccessor;
import java.util.ArrayDeque;
import java.util.Queue;
import net.minecraft.client.KeyMapping;

/**
 * 模拟“按下一次”的辅助类：先 setDown(true) 并增加点击计数，本 tick 结束后再 setDown(false)。
 */
public class SimulatedInput {

    private static final Queue<KeyMapping> PENDING_RELEASE = new ArrayDeque<>();

    public static void tap(KeyMapping mapping) {
        mapping.setDown(true);
        // 增加点击计数，让 consumeClick() 型动作（使用/拾取）也能触发
        KeyMapping.click(((KeyMappingAccessor) mapping).virtualkb$getBoundKey());
        PENDING_RELEASE.add(mapping);
    }

    public static void endTick() {
        while (!PENDING_RELEASE.isEmpty()) {
            PENDING_RELEASE.poll().setDown(false);
        }
    }
}
