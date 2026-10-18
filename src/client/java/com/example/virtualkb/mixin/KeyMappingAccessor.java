package com.example.virtualkb.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import java.util.Map;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Mixin accessor：访问 KeyMapping 的两个私有成员。
 * - ALL：全局按键注册表（key 名称 → KeyMapping），用于“按扫描码找到对应按键”
 * - key：某个 KeyMapping 当前绑定的 InputConstants.Key
 */
@Mixin(KeyMapping.class)
public interface KeyMappingAccessor {

    @Accessor("ALL")
    static Map<String, KeyMapping> virtualkb$getAllKeyMappings() {
        throw new AssertionError();
    }

    @Accessor("key")
    InputConstants.Key virtualkb$getBoundKey();

    // 直接读写 isDown 字段（绕过 ToggleKeyMapping 的翻转语义）
    @Accessor("isDown")
    void virtualkb$setIsDown(boolean value);
}
