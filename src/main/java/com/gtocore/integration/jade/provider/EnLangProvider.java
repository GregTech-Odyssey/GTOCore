package com.gtocore.integration.jade.provider;

import com.gtocore.client.forge.GTOComponentHandler;
import com.gtocore.config.GTOConfig;

import com.gtolib.GTOCore;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;

public enum EnLangProvider implements IBlockComponentProvider, IEntityComponentProvider {

    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (!GTOConfig.INSTANCE.gamePlay.showEnglishName) return;
        var enLang = GTOComponentHandler.INSTANCE.getEnglishLanguage();
        if (enLang == null) return;
        tooltip.add(Component.literal(enLang.getOrDefault(accessor.getBlock().getDescriptionId(), accessor.getBlock().getDescriptionId())));
    }

    @Override
    public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
        if (!GTOConfig.INSTANCE.gamePlay.showEnglishName) return;
        var enLang = GTOComponentHandler.INSTANCE.getEnglishLanguage();
        if (enLang == null) return;
        tooltip.add(Component.literal(enLang.getOrDefault(accessor.getEntity().getType().getDescriptionId(), accessor.getEntity().getType().getDescriptionId())));
    }

    @Override
    public ResourceLocation getUid() {
        return GTOCore.id("en_lang");
    }
}
