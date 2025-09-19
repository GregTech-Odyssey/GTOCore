package com.gtocore.common.machine.electric;

import com.gtocore.api.report.*;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine;

import net.minecraft.network.chat.Component;

import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.*;

import java.util.Arrays;
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
        textList.add(ComponentPanelWidget.withButton(Component.literal(" [ore_reporter]"), "ore_reporter"));
        textList.add(ComponentPanelWidget.withButton(Component.literal(" [export_all_loot_tables]"), "export_all_loot_tables"));
        textList.add(ComponentPanelWidget.withButton(Component.literal(" [export_loot_tables]"), "export_loot_tables"));
    }

    private void handleDisplayClick(String componentData, ClickData clickData) {
        if (!clickData.isRemote) {
            if ("data_crystal".equals(componentData)) {
                DataCrystalReport.grtDataCrystalReport();
            } else if ("enchantment".equals(componentData)) {
                EnchantmentStorage.getEnchantmentsReport();
            } else if ("affix_reporter".equals(componentData)) {
                AffixReporter.getAffixReporter();
            } else if ("ore_reporter".equals(componentData)) {
                OreReport.generateOreReport();
            } else if ("export_all_loot_tables".equals(componentData)) {
                LootTableExporter.exportAllLootTables();
            } else if ("export_loot_tables".equals(componentData)) {
                List<String> LOOT_TABLES = Arrays.asList(
                        "extrabotany:reward_bags/eins",
                        "extrabotany:reward_bags/zwei",
                        "extrabotany:reward_bags/drei",
                        "extrabotany:reward_bags/vier",
                        "extrabotany:reward_bags/nine_and_three_quarters",
                        "extrabotany:reward_bags/pandoras_box");
                LootTableExporter.exportLootTables(LOOT_TABLES);
            }
        }
    }
}
