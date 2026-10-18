package com.example.virtualkb;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ClothConfigScreen {

    public static Screen create(Screen parent) {
        ModConfig cfg = ModConfig.get();

        ConfigBuilder builder = ConfigBuilder.create()
            .setParentScreen(parent)
            .setTitle(Component.translatable("title.virtualkb.config"))
            .setSavingRunnable(ModConfig::save);

        ConfigEntryBuilder eb = builder.entryBuilder();

        var category = builder.getOrCreateCategory(Component.translatable("category.virtualkb.general"));
        category.addEntry(eb.startBooleanToggle(
                Component.translatable("option.virtualkb.enabled"), cfg.enabled)
            .setDefaultValue(false)
            .setTooltip(Component.translatable("tooltip.virtualkb.enabled"))
            .setSaveConsumer(v -> cfg.enabled = v)
            .build());
        category.addEntry(eb.startFloatField(
                Component.translatable("option.virtualkb.opacity"), cfg.keyboardOpacity)
            .setMin(0.0f)
            .setMax(1.0f)
            .setDefaultValue(1.0f)
            .setTooltip(Component.translatable("tooltip.virtualkb.opacity"))
            .setSaveConsumer(v -> cfg.keyboardOpacity = v)
            .build());

        return builder.build();
    }
}
