package com.gtocore.integration.jade.provider;

import com.gtocore.client.ClientCache;
import com.gtocore.common.item.StructureDetectBehavior;

import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.pattern.MultiblockState;
import com.gregtechceu.gtceu.api.pattern.error.PatternError;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import com.glodblock.github.extendedae.client.render.EAEHighlightHandler;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

@DataGeneratorScanned
public final class MultiblockStructureProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    @RegisterLanguage(cn = "正在检查结构", en = "Checking structure")
    private static final String CHECKING = "gtocore.top.checking";

    @RegisterLanguage(cn = "排队等待检查结构", en = "Waiting in line for check structure")
    private static final String WAITING = "gtocore.top.waiting";

    @RegisterLanguage(cn = "可能的错误 ", en = "Probable errors ")
    private static final String ERRORS = "gtocore.top.errors";

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (blockAccessor.getServerData().contains("hasError")) {
            if (blockAccessor.getServerData().getBoolean("hasError")) {
                iTooltip.add(Component.translatable("gtceu.top.invalid_structure").withStyle(ChatFormatting.RED));
                if (blockAccessor.getServerData().getBoolean("checking")) {
                    iTooltip.add(Component.translatable(CHECKING).withStyle(ChatFormatting.YELLOW));
                } else if (blockAccessor.getServerData().getBoolean("waiting")) {
                    iTooltip.add(Component.translatable(WAITING).withStyle(ChatFormatting.DARK_AQUA));
                } else {
                    boolean highlight = ClientCache.highlightTime < 1;
                    boolean isHighlight = false;
                    int i = 0;
                    for (var error : blockAccessor.getServerData().getList("error", Tag.TAG_COMPOUND)) {
                        if (error instanceof CompoundTag compoundTag) {
                            var infos = compoundTag.getList("info", Tag.TAG_STRING);
                            iTooltip.add(Component.translatable(ERRORS).append(String.valueOf(++i)).withStyle(ChatFormatting.GOLD));
                            for (var info : infos) {
                                var c = Component.Serializer.fromJson(info.getAsString());
                                if (c != null) {
                                    iTooltip.add(c);
                                }
                            }
                            var errorPos = compoundTag.getLong("pos");
                            if (errorPos != 0) {
                                if (highlight) {
                                    isHighlight = true;
                                    EAEHighlightHandler.highlight(BlockPos.of(errorPos), blockAccessor.getLevel().dimension(), System.currentTimeMillis() + 10000);
                                }
                            }
                        }
                    }
                    if (isHighlight) {
                        ClientCache.highlightTime = 200;
                    }
                }
            } else {
                iTooltip.add(Component.translatable("gtceu.top.valid_structure").withStyle(ChatFormatting.GREEN));
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        if (blockAccessor.getBlockEntity() instanceof MetaMachineBlockEntity blockEntity && blockEntity.getMetaMachine() instanceof IMultiController controller) {
            if (controller.isFormed()) {
                compoundTag.putBoolean("hasError", false);
            } else {
                compoundTag.putBoolean("hasError", true);
                if (controller.checking()) {
                    compoundTag.putBoolean("checking", true);
                } else if (controller.getMultiblockState().hasError()) {
                    if (controller.getMultiblockState().error == MultiblockState.UNINIT_ERROR && controller.getWaitingTime() == 0) {
                        compoundTag.putBoolean("waiting", true);
                    } else {
                        var errors = new ListTag();
                        LongSet posSet = new LongOpenHashSet();
                        for (var error : controller.getMultiblockState().errorRecord) {
                            var tag = toTag(error, posSet);
                            if (tag != null) {
                                errors.add(tag);
                            }
                        }
                        var tag = toTag(controller.getMultiblockState().error, posSet);
                        if (tag != null) {
                            errors.add(tag);
                        }
                        compoundTag.put("error", errors);
                    }
                } else {
                    compoundTag.putBoolean("waiting", true);
                }
            }
        }
    }

    private static Tag toTag(PatternError error, LongSet set) {
        var infos = new ListTag();
        for (var err : StructureDetectBehavior.analysis(error)) {
            infos.add(StringTag.valueOf(Component.Serializer.toJson(err)));
        }
        if (!infos.isEmpty()) {
            var tag = new CompoundTag();
            var pos = error.getPos();
            if (pos != null) {
                long posLong = pos.asLong();
                if (set.contains(posLong)) return null;
                tag.putLong("pos", posLong);
                set.add(posLong);
            }
            tag.put("info", infos);
            return tag;
        }
        return null;
    }

    @Override
    public ResourceLocation getUid() {
        return GTCEu.id("multiblock_structure");
    }
}
