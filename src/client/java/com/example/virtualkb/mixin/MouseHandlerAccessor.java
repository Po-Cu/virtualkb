package com.example.virtualkb.mixin;

import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * 访问 MouseHandler 的鼠标坐标字段（26.2 里 xpos()/ypos() 方法被改名，
 * 直接用字段更稳）。若报“找不到字段 xpos”，请按 genSources 里的真实字段名改。
 */
@Mixin(MouseHandler.class)
public interface MouseHandlerAccessor {

    @Accessor("xpos")
    double virtualkb$getXpos();

    @Accessor("ypos")
    double virtualkb$getYpos();
}
