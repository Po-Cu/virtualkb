package com.example.virtualkb.mixin;

import com.example.virtualkb.ModConfig;
import com.example.virtualkb.VisualKeysClick;
import io.github.dimaskama.visualkeys.client.KeyEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.input.MouseButtonInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 鼠标锁定：拦截 MouseHandler 的世界操作。
 * - onButton：不直接操作世界，改为“点击 VisualKeys 键盘上的键”
 * - onScroll / turnPlayer：直接拦截
 */
@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

    @Inject(method = "onButton", at = @At("HEAD"), cancellable = true)
    private void virtualkb$onButton(long window, MouseButtonInfo input, int action, CallbackInfo ci) {
        if (!shouldBlock()) return;
        ci.cancel(); // 吃掉这次点击，避免影响世界

        if (action != 0) { // 按下
            KeyEntry key = VisualKeysClick.findKey(scaledX(), scaledY());
            if (key != null && key.code >= 0) {
                VisualKeysClick.setHeldScancode(key.code);
                VisualKeysClick.trigger(key.code, true);
            }
        } else { // 松开
            if (VisualKeysClick.getHeldScancode() >= 0) {
                VisualKeysClick.trigger(VisualKeysClick.getHeldScancode(), false);
                VisualKeysClick.setHeldScancode(-1);
            }
        }
    }

    @Inject(method = "onScroll", at = @At("HEAD"), cancellable = true)
    private void virtualkb$onScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (shouldBlock()) ci.cancel();
    }

    @Inject(method = "turnPlayer", at = @At("HEAD"), cancellable = true)
    private void virtualkb$turnPlayer(double timeDelta, CallbackInfo ci) {
        if (shouldBlock()) ci.cancel();
    }

    private static boolean shouldBlock() {
        return ModConfig.get().enabled && Minecraft.getInstance().gui.screen() == null;
    }

    // 把 MouseHandler 的原始窗口坐标换算成 GUI 缩放坐标（与 HUD 键盘同一坐标系）
    private double scaledX() {
        Minecraft mc = Minecraft.getInstance();
        return ((MouseHandlerAccessor) this).virtualkb$getXpos()
            * (double) mc.getWindow().getGuiScaledWidth() / (double) mc.getWindow().getScreenWidth();
    }

    private double scaledY() {
        Minecraft mc = Minecraft.getInstance();
        return ((MouseHandlerAccessor) this).virtualkb$getYpos()
            * (double) mc.getWindow().getGuiScaledHeight() / (double) mc.getWindow().getScreenHeight();
    }
}
