package com.gtocore.common.machine.noenergy;

import com.gtocore.integration.jade.provider.AEGridProvider;
import com.gtocore.integration.jade.provider.TickTimeProvider;

import com.gtolib.api.ae2.IExpandedGrid;
import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.api.data.GTODimensions;
import com.gtolib.utils.RLUtils;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import appeng.parts.AEBasePart;
import com.glodblock.github.extendedae.client.render.EAEHighlightHandler;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Pattern;

@DataGeneratorScanned
public final class PerformanceMonitorMachine extends MetaMachine implements IFancyUIMachine {

    @RegisterLanguage(cn = "AE网络", en = "AE Grid")
    private static final String GRID = "gtocore.performance_monitor.grid";

    private static final Pattern PATTERN = Pattern.compile(", ");

    private List<Component> textListCache;
    private boolean grid = false;

    public PerformanceMonitorMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    private void handleDisplayClick(String componentData, ClickData clickData) {
        if (componentData.equals("grid")) {
            grid = !grid;
            textListCache = null;
        } else if (clickData.isRemote) {
            if (componentData.isEmpty()) return;
            String[] parts = PATTERN.split(componentData);
            if (parts.length == 4) {
                BlockPos pos = new BlockPos(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                EAEHighlightHandler.highlight(pos, GTODimensions.getDimensionKey(RLUtils.parse(parts[3])), System.currentTimeMillis() + 15000);
            }
        }
    }

    @Override
    public Widget createUIWidget() {
        WidgetGroup group = new WidgetGroup(0, 0, 182 + 8, 117 + 8);
        group.addWidget(new DraggableScrollableWidgetGroup(4, 4, 182, 117).setBackground(GuiTextures.DISPLAY)
                .addWidget(new LabelWidget(4, 5, self().getBlockState().getBlock().getDescriptionId()))
                .addWidget(new ComponentPanelWidget(4, 17, this::addDisplayText).setMaxWidthLimit(150).clickHandler(this::handleDisplayClick)));
        group.setBackground(GuiTextures.BACKGROUND_INVERSE);
        return group;
    }

    private void addDisplayText(@NotNull List<Component> textList) {
        if (isRemote()) return;
        textList.add(Component.translatable("gui.enderio.range.show").append(ComponentPanelWidget.withButton(Component.translatable(grid ? GRID : "config.gtceu.option.machines"), "grid")));
        if (grid) {
            AEGridProvider.OBSERVE = true;
            if (textListCache == null || getOffsetTimer() % 160 == 0) {
                textListCache = new ArrayList<>();
                Map<IExpandedGrid, Long> sortedMap = new TreeMap<>(Comparator.comparing(IExpandedGrid::getLatency).reversed());
                sortedMap.putAll(IExpandedGrid.PERFORMANCE_MAP);
                IExpandedGrid.PERFORMANCE_MAP.clear();
                for (Map.Entry<IExpandedGrid, Long> entry : sortedMap.entrySet()) {
                    IExpandedGrid key = entry.getKey();
                    Object o;
                    String pos = "";
                    Level level = null;
                    if (key.getPivot() != null) {
                        level = key.getPivot().getLevel();
                        if ((o = key.getPivot().getOwner()) != null) {
                            if (o instanceof AEBasePart part) {
                                var host = part.getHost();
                                if (host != null) {
                                    pos = host.getBlockEntity().getBlockPos().toShortString();
                                }
                            } else if (o instanceof BlockEntity be) {
                                pos = be.getBlockPos().toShortString();
                            } else if (o instanceof MetaMachine machine) {
                                pos = machine.getPos().toShortString();
                            }
                        }
                    }
                    textListCache.add(Component.translatable(key.toString()).append(" ")
                            .withStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("recipe.condition.dimension.tooltip", level == null ? " " : level.dimension().location()).append(" [").append(pos).append("] "))))
                            .append(Component.translatable(AEGridProvider.LATENCY, entry.getValue()).append(" μs"))
                            .append(ComponentPanelWidget.withButton(Component.literal(" [ ] "), pos + ", " + (level == null ? "" : level.dimension().location()))));
                }
            }
            textList.addAll(textListCache);
        } else {
            OBSERVE = true;
            if (textListCache == null || getOffsetTimer() % 160 == 0) {
                textListCache = new ArrayList<>();
                Map<MetaMachine, Integer> sortedMap = new TreeMap<>(Comparator.comparing(MetaMachine::getTickTime).reversed());
                sortedMap.putAll(PERFORMANCE_MAP);
                PERFORMANCE_MAP.clear();
                for (Map.Entry<MetaMachine, Integer> entry : sortedMap.entrySet()) {
                    MetaMachine machine = entry.getKey();
                    String pos = machine.getPos().toShortString();
                    Level level = machine.getLevel();
                    if (level == null) continue;
                    textListCache.add(Component.translatable(machine.getBlockState().getBlock().getDescriptionId()).append(" ")
                            .withStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("recipe.condition.dimension.tooltip", level.dimension().location()).append(" [").append(pos).append("] "))))
                            .append(Component.translatable(TickTimeProvider.LATENCY, entry.getValue()).append(" μs"))
                            .append(ComponentPanelWidget.withButton(Component.literal(" [ ] "), pos + ", " + level.dimension().location())));
                }
            }
            textList.addAll(textListCache);
        }
    }
}
