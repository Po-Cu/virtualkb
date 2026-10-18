package com.example.virtualkb.mixin;

import com.example.virtualkb.VisualKeysClick;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import io.github.dimaskama.visualkeys.client.KeyboardRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * 让 VisualKeys 渲染键盘时，把我“按住”的那个键也高亮。
 * VisualKeys 原本用 InputConstants.isKeyDown(window, 扫描码) 检测物理按键；
 * 我们模拟点击只改了 KeyMapping，所以额外把“当前按住的扫描码”也算作按下。
 */
@Mixin(KeyboardRenderer.class)
public class VisualKeysRendererMixin {

    @Redirect(
        method = "render",
        at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/InputConstants;isKeyDown(Lcom/mojang/blaze3d/platform/Window;I)Z")
    )
    private static boolean virtualkb$highlightHeldKey(Window window, int code) {
        return InputConstants.isKeyDown(window, code) || code == VisualKeysClick.getHeldScancode();
    }
}
