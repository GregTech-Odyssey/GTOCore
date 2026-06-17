package com.gtocore.integration.jade.provider;

import com.gtocore.common.item.TimeTwisterBehavior;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.integration.jade.provider.CapabilityBlockProvider;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import org.jetbrains.annotations.Nullable;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.api.ui.IElementHelper;

public final class AccelerateBlockProvider extends CapabilityBlockProvider<Integer> {

    public AccelerateBlockProvider() {
        super(GTOCore.id("accelerate_provider"));
    }

    @Override
    public void appendServerData(CompoundTag data, BlockAccessor blockAccessor) {
        super.appendServerData(data, blockAccessor);
        TimeTwisterBehavior.appendWailaData(data, blockAccessor);
    }

    @Nullable
    @Override
    protected Integer getCapability(Level level, BlockPos pos, BlockEntity blockEntity, @Nullable Direction side) {
        if (blockEntity != null && blockEntity.getPersistentData().contains("accelerate_tick")) {
            return blockEntity.getPersistentData().getInt("accelerate_tick");
        }
        return null;
    }

    @Override
    protected void write(CompoundTag data, Integer capability) {
        if (capability != null) data.putInt("accelerate_tick", capability);
    }

    @Override
    protected void addTooltip(CompoundTag capData, ITooltip tooltip, Player player, BlockAccessor block,
                              BlockEntity blockEntity, IPluginConfig config) {
        int tick = capData.getInt("accelerate_tick");
        if (tick == 0) {
            TimeTwisterBehavior.appendWailaTooltip(block.getServerData(), tooltip, block, config);
            return;
        }
        IElementHelper helper = tooltip.getElementHelper();
        tooltip.add(helper.progress(getProgress(tick, 100), Component.literal(tick + " / " + 100 + " Tick"), helper.progressStyle().color(0xFFFFFFFF, 0xFFADD8E6).textColor(-1), Util.make(BoxStyle.DEFAULT, style -> style.borderColor = 0xFF888888), true));
    }
}
