package com.gto.gtocore.integration.jade.provider;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.capability.IManaContainer;
import com.gto.gtocore.api.machine.mana.feature.IManaMachine;
import com.gto.gtocore.common.machine.mana.multiblock.ManaDistributorMachine;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.integration.jade.provider.CapabilityBlockProvider;
import com.gregtechceu.gtceu.utils.FormattingUtil;

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

public final class ManaContainerBlockProvider extends CapabilityBlockProvider<IManaContainer> {

    public ManaContainerBlockProvider() {
        super(GTOCore.id("mana_container_provider"));
    }

    @Nullable
    @Override
    protected IManaContainer getCapability(Level level, BlockPos pos, @Nullable Direction side) {
        MetaMachine machine = MetaMachine.getMachine(level, pos);
        if (machine instanceof IManaMachine manaMachine) {
            return manaMachine.getManaContainer();
        }
        return null;
    }

    @Override
    protected void write(CompoundTag data, IManaContainer capability) {
        data.putLong("Mana", capability.getCurrentMana());
        data.putLong("MaxMana", capability.getMaxMana());
        ManaDistributorMachine distributor = capability.getDistributor();
        if (distributor == null) return;
        data.putString("pos", Component.translatable(distributor.getDefinition().getDescriptionId()).append("[").append(distributor.getPos().toShortString()).append("]").getString());
    }

    @Override
    protected void addTooltip(CompoundTag capData, ITooltip tooltip, Player player, BlockAccessor block,
                              BlockEntity blockEntity, IPluginConfig config) {
        String pos = capData.getString("pos");
        if (!pos.isEmpty()) {
            tooltip.add(Component.translatable("gtmthings.machine.wireless_energy_hatch.tooltip.bind", pos));
        }
        long maxStorage = capData.getLong("MaxMana");
        if (maxStorage == 0) return;
        long stored = capData.getLong("Mana");
        IElementHelper helper = tooltip.getElementHelper();
        tooltip.add(helper.progress(getProgress(stored, maxStorage), Component.literal(FormattingUtil.formatNumbers(stored) + " / " + FormattingUtil.formatNumbers(maxStorage) + " Mana"), helper.progressStyle().color(0xFF00B1FF, 0xFF00B1FF).textColor(-1), Util.make(BoxStyle.DEFAULT, style -> style.borderColor = 0xFF888888), true));
    }
}
