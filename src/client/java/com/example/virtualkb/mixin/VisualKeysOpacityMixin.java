package com.example.virtualkb.mixin;

import com.example.virtualkb.ModConfig;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import io.github.dimaskama.visualkeys.client.KeyboardRenderer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * 给 VisualKeys 键盘加透明度：
 * - 平面模式（keyboardTextured=false）用 fill(…, color)，修改颜色的 alpha。
 * - 纹理模式用 blit(…)，重定向到带 tint 颜色的 13 参 blit。
 */
@Mixin(KeyboardRenderer.class)
public class VisualKeysOpacityMixin {

    @ModifyArg(
        method = "render",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;fill(IIIII)V"),
        index = 4
    )
    private static int virtualkb$modifyFillColor(int color) {
        int alpha = Math.round(ModConfig.get().keyboardOpacity * 255.0f);
        return (color & 0x00FFFFFF) | (alpha << 24);
    }

    @Redirect(
        method = "render",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blit(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIFFIIIIII)V")
    )
    private static void virtualkb$blitWithOpacity(GuiGraphicsExtractor ctx, RenderPipeline pipeline, Identifier id,
            int x, int y, float u, float v, int w, int h, int texW, int texH, int imgW, int imgH) {
        int alpha = Math.round(ModConfig.get().keyboardOpacity * 255.0f);
        int color = (alpha << 24) | 0xFFFFFF;
        ctx.blit(pipeline, id, x, y, u, v, w, h, texW, texH, imgW, imgH, color);
    }
}
