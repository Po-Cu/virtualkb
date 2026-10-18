package com.example.virtualkb.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 给物品栏 / 容器界面（箱子、工作台等）/ 聊天栏添加一个右上角的 X 关闭按钮，
 * 点击后用鼠标关闭当前界面（不必按 Esc / E / T）。
 */
@Mixin(Screen.class)
public abstract class ScreenCloseButtonMixin {

    // addRenderableWidget 是 protected，用 @Shadow 声明以便调用
    @Shadow
    protected abstract <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(T widget);

    @Inject(method = "init", at = @At("TAIL"))
    private void virtualkb$addCloseButton(CallbackInfo ci) {
        // 编译期 this 是 ScreenCloseButtonMixin，实际运行时是 Screen，需显式转换
        Screen self = (Screen) (Object) this;
        if (self instanceof AbstractContainerScreen || self instanceof ChatScreen) {
            int x = Minecraft.getInstance().getWindow().getGuiScaledWidth() - 24;
            this.addRenderableWidget(
                Button.builder(Component.literal("✕"), b -> self.onClose())
                    .bounds(x, 4, 20, 20)
                    .build()
            );
        }
    }
}
