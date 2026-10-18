package com.example.virtualkb;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public class KeyBindings {

    // 分类：按键设置里的分组。对应翻译 key 为 "key.category.virtualkb.main"
    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
        Identifier.fromNamespaceAndPath("virtualkb", "main")
    );

    // 视角移动（方向键）
    public static final KeyMapping LOOK_UP    = new KeyMapping("key.virtualkb.lookup",    InputConstants.Type.KEYSYM, InputConstants.KEY_UP,      CATEGORY);
    public static final KeyMapping LOOK_DOWN  = new KeyMapping("key.virtualkb.lookdown",  InputConstants.Type.KEYSYM, InputConstants.KEY_DOWN,    CATEGORY);
    public static final KeyMapping LOOK_LEFT  = new KeyMapping("key.virtualkb.lookleft",  InputConstants.Type.KEYSYM, InputConstants.KEY_LEFT,    CATEGORY);
    public static final KeyMapping LOOK_RIGHT = new KeyMapping("key.virtualkb.lookright", InputConstants.Type.KEYSYM, InputConstants.KEY_RIGHT,   CATEGORY);

    // 小键盘代替鼠标按键：4=左键 5=中键 6=右键
    // 注意：翻译 key 用 click_ 前缀，避免和旧版本的 left/right 重名导致 options.txt 里的旧绑定继续生效
    public static final KeyMapping SUB_LEFT   = new KeyMapping("key.virtualkb.click_left",   InputConstants.Type.KEYSYM, InputConstants.KEY_NUMPAD4, CATEGORY);
    public static final KeyMapping SUB_MIDDLE = new KeyMapping("key.virtualkb.click_middle", InputConstants.Type.KEYSYM, InputConstants.KEY_NUMPAD5, CATEGORY);
    public static final KeyMapping SUB_RIGHT  = new KeyMapping("key.virtualkb.click_right",  InputConstants.Type.KEYSYM, InputConstants.KEY_NUMPAD6, CATEGORY);

    // 小键盘代替滚轮：8=上滚 2=下滚
    public static final KeyMapping SCROLL_UP   = new KeyMapping("key.virtualkb.scroll_up",   InputConstants.Type.KEYSYM, InputConstants.KEY_NUMPAD8, CATEGORY);
    public static final KeyMapping SCROLL_DOWN = new KeyMapping("key.virtualkb.scroll_down", InputConstants.Type.KEYSYM, InputConstants.KEY_NUMPAD2, CATEGORY);

    /** 把所有按键交给 Fabric 注册进游戏按键系统（这样就能在原版按键设置里改键） */
    public static void register() {
        KeyMappingHelper.registerKeyMapping(LOOK_UP);
        KeyMappingHelper.registerKeyMapping(LOOK_DOWN);
        KeyMappingHelper.registerKeyMapping(LOOK_LEFT);
        KeyMappingHelper.registerKeyMapping(LOOK_RIGHT);
        KeyMappingHelper.registerKeyMapping(SUB_LEFT);
        KeyMappingHelper.registerKeyMapping(SUB_MIDDLE);
        KeyMappingHelper.registerKeyMapping(SUB_RIGHT);
        KeyMappingHelper.registerKeyMapping(SCROLL_UP);
        KeyMappingHelper.registerKeyMapping(SCROLL_DOWN);
    }
}
