package com.example.virtualkb;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

/**
 * Mod Menu 入口：实现 ModMenuApi 接口，告诉 Mod Menu
 * “我的配置界面怎么造”。getModConfigScreenFactory 返回一个工厂函数。
 */
public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        // parent -> ... 是 lambda：参数是上一层界面，返回值是新 Screen
        return parent -> ClothConfigScreen.create(parent);
    }
}
