package com.gtocore.integration.jade.provider;

import com.gtolib.GTOCore;
import com.gtolib.utils.NumberUtils;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.LiquidBlock;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class DestroyTimeProvider implements IBlockComponentProvider {

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (blockAccessor.getBlock() instanceof LiquidBlock) return;
        iTooltip.add(Component.translatable("behavior.portable_scanner.block_hardness", NumberUtils.numberText(blockAccessor.getBlock().defaultDestroyTime()).withStyle(ChatFormatting.BLUE), NumberUtils.numberText(blockAccessor.getBlock().getExplosionResistance()).withStyle(ChatFormatting.DARK_BLUE)).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public ResourceLocation getUid() {
        return GTOCore.id("destroy_time_provider");
    }

    @Override
    public boolean enabledByDefault() {
        return false;
    }
}
