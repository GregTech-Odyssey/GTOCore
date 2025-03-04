package com.gto.gtocore.common.wireless;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;

import com.hepdd.gtmthings.api.misc.ITransferData;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;

import java.util.UUID;

import static com.hepdd.gtmthings.utils.TeamUtil.GetName;

public record ExtendTransferData(UUID UUID, long Throughput, long loss, MetaMachine machine) implements ITransferData {

    @Override
    public Component getInfo() {
        MetaMachine machine = machine();
        long eut = Throughput();
        String pos = machine.getPos().toShortString();
        if (eut > 0) {
            return Component.translatable(machine.getBlockState().getBlock().getDescriptionId())
                    .withStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("recipe.condition.dimension.tooltip", machine.getLevel().dimension().location()).append(" [").append(pos).append("] ").append(Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.0", GetName(machine.getLevel(), UUID()))))))
                    .append(" +").append(FormattingUtil.formatNumbers(eut)).append(" EU/t (").append(GTValues.VNF[GTUtil.getFloorTierByVoltage(eut)]).append(") ")
                    .append(Component.translatable("gtocore.machine.energy_loss", Component.literal(FormattingUtil.formatNumbers(loss())).append(" EU/t (").append(GTValues.VNF[GTUtil.getFloorTierByVoltage(loss())]).append(")")))
                    .append(ComponentPanelWidget.withButton(Component.literal(" [ ] "), pos));
        } else {
            return Component.translatable(machine.getBlockState().getBlock().getDescriptionId())
                    .withStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("recipe.condition.dimension.tooltip", machine.getLevel().dimension().location()).append(" [").append(pos).append("] ").append(Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.0", GetName(machine.getLevel(), UUID()))))))
                    .append(" -").append(FormattingUtil.formatNumbers(-eut)).append(" EU/t (").append(GTValues.VNF[GTUtil.getFloorTierByVoltage(-eut)]).append(")")
                    .append(ComponentPanelWidget.withButton(Component.literal(" [ ] "), pos));
        }
    }
}
