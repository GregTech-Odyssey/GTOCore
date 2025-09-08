package com.gtocore.common.machine.electric;

import com.gtocore.api.report.AffixReporter;
import com.gtocore.api.report.DataCrystalReport;
import com.gtocore.api.report.EnchantmentStorage;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine;

import net.minecraft.network.chat.Component;

import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.*;

import java.util.List;

public class TestReportOutput extends MetaMachine implements IFancyUIMachine {

    public TestReportOutput(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public Widget createUIWidget() {
        var group = new WidgetGroup(0, 0, 182 + 8, 117 + 8);
        group.addWidget(new DraggableScrollableWidgetGroup(4, 4, 182, 117)
                .setBackground(GuiTextures.DISPLAY)
                .addWidget(new LabelWidget(4, 5, self().getBlockState().getBlock().getDescriptionId()))
                .addWidget(new ComponentPanelWidget(4, 17, this::addDisplayText).setMaxWidthLimit(150).clickHandler(this::handleDisplayClick)));

        group.setBackground(GuiTextures.BACKGROUND_INVERSE);
        return group;
    }

    private void addDisplayText(List<Component> textList) {
        textList.add(ComponentPanelWidget.withButton(Component.literal(" [data_crystal]"), "data_crystal"));
        textList.add(ComponentPanelWidget.withButton(Component.literal(" [enchantment]"), "enchantment"));
        textList.add(ComponentPanelWidget.withButton(Component.literal(" [affix_reporter]"), "affix_reporter"));
    }

    private void handleDisplayClick(String componentData, ClickData clickData) {
        if (!clickData.isRemote) {
            if ("data_crystal".equals(componentData)) {
                DataCrystalReport.grtDataCrystalReport();
            } else if ("enchantment".equals(componentData)) {
                EnchantmentStorage.getEnchantmentsReport();
            } else if ("affix_reporter".equals(componentData)) {
                AffixReporter.getAffixReporter();
            }
        }
    }
}
