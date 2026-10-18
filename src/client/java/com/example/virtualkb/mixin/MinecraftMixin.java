package com.example.virtualkb.mixin;

import com.example.virtualkb.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * 原版挖掘/攻击要求“鼠标已抓取”（isMouseGrabbed()==true）。
 * 本模组为了“光标可见”会释放鼠标抓取，导致攻击被原版逻辑拦截。
 * 这里把 handleKeybinds 里的 isMouseGrabbed 判定改成“启用时恒为真”，
 * 让替代按键（小键盘4）在光标可见时也能攻击/挖掘。
 */
@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Redirect(
        method = "handleKeybinds",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MouseHandler;isMouseGrabbed()Z")
    )
    private boolean virtualkb$allowAttackWhenReleased(MouseHandler instance) {
        return ModConfig.get().enabled || instance.isMouseGrabbed();
    }
}
