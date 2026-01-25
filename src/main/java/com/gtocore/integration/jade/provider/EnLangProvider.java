package com.gtocore.integration.jade.provider;

import com.gtocore.client.forge.GTOComponentHandler;
import com.gtocore.client.forge.GTOComponentHandlerKt;
import com.gtocore.config.GTOConfig;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.api.block.MaterialBlock;
import com.gregtechceu.gtceu.api.block.MaterialPipeBlock;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;

import appeng.block.networking.CableBusBlock;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;

public enum EnLangProvider implements IBlockComponentProvider, IEntityComponentProvider {

    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (!GTOConfig.INSTANCE.showEnglishName) return;
        var enLang = GTOComponentHandler.INSTANCE.getEnglishLanguage();
        if (enLang == null) return;
        if (accessor.getBlock() instanceof MaterialBlock ||
                accessor.getBlock() instanceof MaterialPipeBlock<?, ?, ?> || accessor.getBlock() instanceof CableBusBlock) {
            GTOComponentHandlerKt.getEnglish(accessor.getPickedResult()).ifPresent(tooltip::add);
            return;
        }
        tooltip.add(Component.literal(enLang.getOrDefault(accessor.getBlock().getDescriptionId(), accessor.getBlock().getDescriptionId())));
    }

    @Override
    public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
        if (!GTOConfig.INSTANCE.showEnglishName) return;
        var enLang = GTOComponentHandler.INSTANCE.getEnglishLanguage();
        if (enLang == null) return;
        switch (accessor.getEntity()) {
            case ItemEntity itemEntity -> GTOComponentHandlerKt.getEnglish(itemEntity.getItem()).ifPresent(tooltip::add);
            case ItemFrame itemFrame -> GTOComponentHandlerKt.getEnglish(itemFrame.getItem()).ifPresent(tooltip::add);
            default -> tooltip.add(Component.literal(enLang.getOrDefault(accessor.getEntity().getType().getDescriptionId(), accessor.getEntity().getType().getDescriptionId())));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return GTOCore.id("en_lang");
    }
}
