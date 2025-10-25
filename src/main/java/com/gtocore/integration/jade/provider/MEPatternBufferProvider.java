package com.gtocore.integration.jade.provider;

import com.gtocore.common.machine.multiblock.part.ae.MEPatternBufferPartMachine;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.client.util.TooltipHelper;
import com.gregtechceu.gtceu.integration.jade.GTElementHelper;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.fluid.JadeFluidObject;
import snownee.jade.api.ui.IElementHelper;

public class MEPatternBufferProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (blockAccessor.getBlockEntity() instanceof MetaMachineBlockEntity blockEntity) {
            if (blockEntity.getMetaMachine() instanceof MEPatternBufferPartMachine) {
                CompoundTag serverData = blockAccessor.getServerData();
                if (!serverData.getBoolean("formed")) return;
                var proxies = serverData.getInt("proxies");
                if (proxies > 0) iTooltip.add(Component.translatable("gtceu.top.proxies_bound", serverData.getInt("proxies")).withStyle(TooltipHelper.RAINBOW_HSL_SLOW));
                readBufferTag(iTooltip, serverData);
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        if (blockAccessor.getBlockEntity() instanceof MetaMachineBlockEntity blockEntity) {
            if (blockEntity.getMetaMachine() instanceof MEPatternBufferPartMachine buffer) {
                if (!buffer.isFormed()) {
                    compoundTag.putBoolean("formed", false);
                    return;
                }
                compoundTag.putBoolean("formed", true);
                var proxies = buffer.getProxies().size();
                if (proxies > 0) compoundTag.putInt("proxies", proxies);
                writeBufferTag(compoundTag, buffer);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return GTCEu.id("me_pattern_buffer");
    }

    private static void writeBufferTag(CompoundTag compoundTag, MEPatternBufferPartMachine buffer) {
        var merged = buffer.mergeInternalSlots();
        var items = merged.items();
        var fluids = merged.fluids();

        ListTag itemsTag = new ListTag();
        for (var entry : items.reference2LongEntrySet()) {
            var ct = entry.getKey().toTag();
            ct.putLong("real", entry.getLongValue());
            itemsTag.add(ct);
        }
        if (!itemsTag.isEmpty()) compoundTag.put("items", itemsTag);

        ListTag fluidsTag = new ListTag();
        for (var entry : fluids.reference2LongEntrySet()) {
            var ct = entry.getKey().toTag();
            ct.putLong("real", entry.getLongValue());
            fluidsTag.add(ct);
        }
        if (!fluidsTag.isEmpty()) compoundTag.put("fluids", fluidsTag);
    }

    private static void readBufferTag(ITooltip iTooltip, CompoundTag serverData) {
        IElementHelper helper = iTooltip.getElementHelper();

        ListTag itemsTag = serverData.getList("items", Tag.TAG_COMPOUND);
        for (Tag t : itemsTag) {
            if (!(t instanceof CompoundTag ct)) continue;
            var stack = AEItemKey.fromTag(ct);
            if (stack == null) continue;
            var amount = ct.getLong("real");
            if (amount > 0) {
                iTooltip.add(helper.smallItem(stack.getReadOnlyStack()));
                Component text = Component.literal(" ")
                        .append(Component.literal(String.valueOf(amount)).withStyle(ChatFormatting.DARK_PURPLE))
                        .append(Component.literal("× ").withStyle(ChatFormatting.WHITE))
                        .append(stack.getDisplayName().copy().withStyle(ChatFormatting.GOLD));
                iTooltip.append(text);
            }
        }
        ListTag fluidsTag = serverData.getList("fluids", Tag.TAG_COMPOUND);
        for (Tag t : fluidsTag) {
            if (!(t instanceof CompoundTag ct)) continue;
            var stack = AEFluidKey.fromTag(ct);
            if (stack == null) continue;
            var amount = ct.getLong("real");
            if (amount > 0) {
                iTooltip.add(GTElementHelper.smallFluid(JadeFluidObject.of(stack.getFluid())));
                Component text = Component.literal(" ")
                        .append(Component.literal(FormattingUtil.formatBuckets(amount)))
                        .withStyle(ChatFormatting.DARK_PURPLE)
                        .append(Component.literal(" ").withStyle(ChatFormatting.WHITE))
                        .append(stack.getDisplayName().copy().withStyle(ChatFormatting.DARK_AQUA));
                iTooltip.append(text);
            }
        }
    }
}
