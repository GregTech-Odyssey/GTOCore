package com.gtocore.common.machine.mana;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.translation.GTOMachineTooltips;

import com.gtolib.GTOCore;
import com.gtolib.utils.holder.IntObjectHolder;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.gtocore.common.item.CoordinateCardBehavior.getStoredCoordinates;
import static com.gtocore.common.machine.mana.PlatformCreators.PlatformCreationAsync;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PlatformDeploymentMachine extends MetaMachine implements IFancyUIMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(PlatformDeploymentMachine.class, MetaMachine.MANAGED_FIELD_HOLDER);

    @Persisted
    private final NotifiableItemStackHandler inventory;

    private final List<PlatformBlockType.PlatformPreset> presets = PlatformTemplateStorage.initializePresets();
    private final int maxGroup;

    public PlatformDeploymentMachine(MetaMachineBlockEntity holder) {
        super(holder);
        inventory = new NotifiableItemStackHandler(this, 27, IO.NONE, IO.BOTH);
        inventory.addChangedListener(this::examineMaterial);
        maxGroup = presets.size();
    }

    @Override
    @NotNull
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        inventory.notifyListeners();
    }

    @Override
    public void onUnload() {
        super.onUnload();
    }

    /////////////////////////////////////
    // *********** 信息存储 *********** //
    /// //////////////////////////////////

    // 当前所处的步骤
    @Persisted
    private int step = 0;

    // 总步骤数
    private final int totalStep = 3;

    // 步骤编号常量定义
    private final int Introduction = 0;        // 第零步：简介
    private final int PresetSelection = 1;     // 第一步：选择预设
    private final int ConfirmConsumables = 2;  // 第二步：确认耗材
    private final int AdjustSettings = 3;      // 第三步：调整设置

    // ------------------- 第一步：选择预设 -------------------
    // 是否已完成预设选择
    @Persisted
    private boolean presetConfirm = false;
    // 当前查看的预设组索引
    @Persisted
    private int checkGroup = 0;
    // 显示的预设编号
    private int checkId = 0;
    // 保存的预设组编号
    @Persisted
    private int saveGroup = 0;
    // 保存的预设编号
    @Persisted
    private int saveId = 0;

    // ------------------- 第二步：选择偏移 -------------------
    // X方向区块偏移
    @Persisted
    private int offsetX = 0;
    // Z方向区块偏移
    @Persisted
    private int offsetZ = 0;
    // Y方向高度偏移
    @Persisted
    private int offsetY = -1;

    // X方向区块偏移修改大小
    @Persisted
    private int adjustX = 0;
    // Z方向区块偏移修改大小
    @Persisted
    private int adjustZ = 0;
    // Y方向偏移修改大小
    @Persisted
    private int adjustY = 0;

    // ------------------- 第三步：确认放置 -------------------
    // 库存的原料量
    @Persisted
    private int[] materialInventory = new int[] { 0, 0, 0 };
    // 库存是否充足
    @Persisted
    private boolean insufficient = true;
    // 原料物品
    private static final List<List<IntObjectHolder<Item>>> ITEM_VALUE_HOLDERS = List.of(
            List.of(
                    new IntObjectHolder<>(5000, GTOItems.BIOWARE_MAINFRAME.asItem()),
                    new IntObjectHolder<>(2000, GTOItems.BIOWARE_COMPUTER.asItem()),
                    new IntObjectHolder<>(500, GTOItems.BIOWARE_ASSEMBLY.asItem()),
                    new IntObjectHolder<>(200, GTOItems.BIOWARE_PROCESSOR.asItem())),
            List.of(
                    new IntObjectHolder<>(5000, GTOItems.OPTICAL_MAINFRAME.asItem()),
                    new IntObjectHolder<>(2000, GTOItems.OPTICAL_COMPUTER.asItem()),
                    new IntObjectHolder<>(500, GTOItems.OPTICAL_ASSEMBLY.asItem()),
                    new IntObjectHolder<>(200, GTOItems.OPTICAL_PROCESSOR.asItem())),
            List.of(
                    new IntObjectHolder<>(5000, GTOItems.EXOTIC_MAINFRAME.asItem()),
                    new IntObjectHolder<>(2000, GTOItems.EXOTIC_COMPUTER.asItem()),
                    new IntObjectHolder<>(500, GTOItems.EXOTIC_ASSEMBLY.asItem()),
                    new IntObjectHolder<>(200, GTOItems.EXOTIC_PROCESSOR.asItem())));

    // ------------------- 第四步：运行中 -------------------
    // 任务是否完成
    @Persisted
    private boolean taskCompleted = true;
    // 破坏方块
    @Persisted
    private boolean breakBlocks = true;
    // 跳过空气
    @Persisted
    private boolean skipAir = true;
    // 光照更新
    @Persisted
    private boolean updateLight = true;
    // 光照更新
    @Persisted
    private int speed = 50;
    // 可导出
    @Persisted
    private boolean canExport = false;

    private int progress = 0;

    /////////////////////////////////////
    // ************ UI组件 ************ //
    /// //////////////////////////////////

    int totalLangWidth = 266;
    int langWidth = 266 - 8;

    // 创建UI组件
    @Override
    public Widget createUIWidget() {
        var group = new WidgetGroup(0, 0, 320 + 8, 160 + 8);

        // 步骤标题
        {
            WidgetGroup group_title = new DraggableScrollableWidgetGroup(4, 4, totalLangWidth, 160)
                    .setBackground(GuiTextures.DISPLAY);
            group_title.addWidget(new ComponentPanelWidget(4, 5, this::addDisplayTextTitle));
            group.addWidget(group_title);
        }

        // 主页
        {
            WidgetGroup mainContentGroup = new DraggableScrollableWidgetGroup(4, 18, totalLangWidth, 146);

            ComponentPanelWidget mainTextPanel = new ComponentPanelWidget(4, 0, this::addDisplayText)
                    .clickHandler(this::handleDisplayClick)
                    .setMaxWidthLimit(langWidth);
            mainContentGroup.addWidget(mainTextPanel);

            // 预设选择步骤时添加预览图
            mainContentGroup.addWidget(new ImageWidget(166, 46, 100, 100, this::getIGuiTexture));

            group.addWidget(mainContentGroup);
        }

        // 启动区
        {
            WidgetGroup group_start = new DraggableScrollableWidgetGroup(271, 4, 54, 105)
                    .setBackground(GuiTextures.CLIPBOARD_PAPER_BACKGROUND);
            group_start.addWidget(new ComponentPanelWidget(13, 4, this::addDisplayTextStep)
                    .clickHandler(this::handleDisplayClickStep));
            group_start.addWidget(new ComponentPanelWidget(2, 20, this::addDisplayTextStart)
                    .clickHandler(this::handleDisplayClickStart)
                    .setMaxWidthLimit(50));
            group.addWidget(group_start);
        }

        // 物品槽
        {
            WidgetGroup group_slot = new DraggableScrollableWidgetGroup(271, 110, 54, 54);
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 3; x++) {
                    int slotIndex = y * 3 + x;
                    group_slot.addWidget(new SlotWidget(inventory, slotIndex, x * 18, y * 18, true, true)
                            .setBackground(GuiTextures.SLOT));
                }
            }
            group.addWidget(group_slot);
        }

        group.setBackground(GuiTextures.BACKGROUND_INVERSE);
        return group;
    }

    // 步骤标题显示
    private void addDisplayTextTitle(List<Component> textList) {
        textList.add(centerComponent(langWidth, Component.translatable("gtocore.machine.industrial_platform_deployment_tools.title." + step)));
    }

    // 步骤控制工具
    private void addDisplayTextStep(List<Component> textList) {
        MutableComponent result = Component.empty();
        for (int i = 0; i <= totalStep; i++) {
            if (step == i)
                result.append(ComponentPanelWidget.withButton(Component.literal("§b⭕§r"), "step_" + i));
            else result.append(ComponentPanelWidget.withButton(Component.literal("§1⭕§r"), "step_" + i));
        }
        textList.add(result);
    }

    private void handleDisplayClickStep(String componentData, ClickData clickData) {
        String[] parts = componentData.split("_", 2);
        if (parts[0].equals("step")) {
            step = Mth.clamp(Integer.parseInt(parts[1]), 0, totalStep);
        }
    }

    // 页面主文本
    private void addDisplayText(List<Component> textList) {
        switch (step) {
            case Introduction -> GTOMachineTooltips.INSTANCE.getIndustrialPlatformDeploymentToolsIntroduction().apply(textList);
            case PresetSelection -> {
                if (!ensurePresetsReady()) return;

                textList.add(createPageNavigation(langWidth,
                        createPageNavigation(langWidth - 60, Component.literal("<" + (checkGroup + 1) + "/" + maxGroup + ">"), "group"),
                        "group_plas"));

                int totalIds = getPlatformPreset(checkGroup).getStructures().size();
                textList.add(createPageNavigation(langWidth,
                        createPageNavigation(langWidth - 60, Component.literal("<" + (checkId + 1) + "/" + totalIds + ">"), "id"),
                        "id_plas"));

                textList.add(createEqualColumns(langWidth, Component.translatable("gtocore.machine.industrial_platform_deployment_tools.text.choose_this")
                        .append(ComponentPanelWidget.withButton(Component.literal("[§b⭕§r]"), "choose_this")),
                        presetConfirm ?
                                Component.translatable("gtocore.machine.industrial_platform_deployment_tools.text.selected", saveGroup + 1, saveId + 1) :
                                Component.translatable("gtocore.machine.industrial_platform_deployment_tools.text.unselected")));

                String displayName = getPlatformPreset(checkGroup).getDisplayName();
                String description = getPlatformPreset(checkGroup).getDescription();
                String source = getPlatformPreset(checkGroup).getSource();
                if (displayName != null) textList.add(Component.translatable(displayName));
                if (description != null) textList.add(Component.translatable(description));
                if (source != null) textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.text.source", source));

                String structDisplayName = getPlatformBlockStructure(checkGroup, checkId).getDisplayName();
                String type = getPlatformBlockStructure(checkGroup, checkId).getType();
                String structDescription = getPlatformBlockStructure(checkGroup, checkId).getDescription();
                String structSource = getPlatformBlockStructure(checkGroup, checkId).getSource();
                if (structDisplayName != null) textList.add(Component.translatable(structDisplayName));
                if (type != null) textList.add(Component.translatable(type));
                if (structDescription != null) textList.add(Component.translatable(structDescription));
                if (structSource != null) textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.text.source", structSource));
            }
            case ConfirmConsumables -> {
                textList.add(createEqualColumns(langWidth,
                        ComponentPanelWidget.withButton(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.material.loading"), "loading"),
                        ComponentPanelWidget.withButton(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.material.unloading"), "unloading")));

                textList.add(createEqualColumns(langWidth, Component.translatable("gtocore.machine.industrial_platform_deployment_tools.material.reserves"),
                        Component.translatable("gtocore.machine.industrial_platform_deployment_tools.material.0"),
                        Component.translatable("gtocore.machine.industrial_platform_deployment_tools.material.1"),
                        Component.translatable("gtocore.machine.industrial_platform_deployment_tools.material.2")));
                textList.add(createEqualColumns(langWidth, Component.empty(),
                        Component.literal(String.valueOf(materialInventory[0])),
                        Component.literal(String.valueOf(materialInventory[1])),
                        Component.literal(String.valueOf(materialInventory[2]))));

                if (!presetConfirm) textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.text.unselected"));
                else {
                    int[] costMaterial = getPlatformBlockStructure(saveGroup, saveId).getMaterials();
                    textList.add(createEqualColumns(langWidth, Component.translatable("gtocore.machine.industrial_platform_deployment_tools.material.demand"),
                            Component.translatable("gtocore.machine.industrial_platform_deployment_tools.material.0"),
                            Component.translatable("gtocore.machine.industrial_platform_deployment_tools.material.1"),
                            Component.translatable("gtocore.machine.industrial_platform_deployment_tools.material.2")));
                    textList.add(createEqualColumns(langWidth, Component.empty(),
                            Component.literal(String.valueOf(costMaterial[0])),
                            Component.literal(String.valueOf(costMaterial[1])),
                            Component.literal(String.valueOf(costMaterial[2]))));

                    List<IntObjectHolder<ItemStack>> extraMaterials = getPlatformBlockStructure(saveGroup, saveId).getExtraMaterials();
                    if (!extraMaterials.isEmpty()) {
                        textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.material.extra_demand"));
                        extraMaterials.forEach(e -> textList.add(Component.literal("[").append(e.obj.getDisplayName()).append("×").append(String.valueOf(e.number)).append("]")));
                    }
                    if (!insufficient) textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.material.insufficient"));
                    else textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.material.adequate"));
                }
            }
            case AdjustSettings -> {
                Component empty = Component.empty();

                Component X_change_1 = Component.empty()
                        .append(ComponentPanelWidget.withButton(Component.literal("[-]"), "x_minus"))
                        .append(ComponentPanelWidget.withButton(Component.literal("[+]"), "x_add"));
                Component Z_change_1 = Component.empty()
                        .append(ComponentPanelWidget.withButton(Component.literal("[-]"), "z_minus"))
                        .append(ComponentPanelWidget.withButton(Component.literal("[+]"), "z_add"));
                Component Y_change_1 = Component.empty()
                        .append(ComponentPanelWidget.withButton(Component.literal("[-]"), "y_minus"))
                        .append(ComponentPanelWidget.withButton(Component.literal("[+]"), "y_add"));

                Component X_change_2 = Component.empty()
                        .append(ComponentPanelWidget.withButton(Component.literal("[-]"), "adjust_x_minus"))
                        .append(ComponentPanelWidget.withButton(Component.literal("[-" + adjustX + "]"), "x_minus_plas"))
                        .append(ComponentPanelWidget.withButton(Component.literal("[+" + adjustX + "]"), "x_add_plas"))
                        .append(ComponentPanelWidget.withButton(Component.literal("[+]"), "adjust_x_add"));

                Component Z_change_2 = Component.empty()
                        .append(ComponentPanelWidget.withButton(Component.literal("[-]"), "adjust_z_minus"))
                        .append(ComponentPanelWidget.withButton(Component.literal("[-" + adjustZ + "]"), "z_minus_plas"))
                        .append(ComponentPanelWidget.withButton(Component.literal("[+" + adjustZ + "]"), "z_add_plas"))
                        .append(ComponentPanelWidget.withButton(Component.literal("[+]"), "adjust_z_add"));

                Component Y_change_2 = Component.empty()
                        .append(ComponentPanelWidget.withButton(Component.literal("[-]"), "adjust_y_minus"))
                        .append(ComponentPanelWidget.withButton(Component.literal("[-" + adjustY + "]"), "y_minus_plas"))
                        .append(ComponentPanelWidget.withButton(Component.literal("[+" + adjustY + "]"), "y_add_plas"))
                        .append(ComponentPanelWidget.withButton(Component.literal("[+]"), "adjust_y_add"));

                Component x_offset = Component.translatable("gtocore.machine.industrial_platform_deployment_tools.offset.x", offsetX);
                Component z_offset = Component.translatable("gtocore.machine.industrial_platform_deployment_tools.offset.z", offsetZ);
                Component y_offset = Component.translatable("gtocore.machine.industrial_platform_deployment_tools.offset.y", offsetY);

                textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.offset"));
                textList.add(createEqualColumns(langWidth - 20, x_offset, X_change_1, X_change_2));
                textList.add(createEqualColumns(langWidth - 20, z_offset, Z_change_1, Z_change_2));
                textList.add(createEqualColumns(langWidth - 20, y_offset, Y_change_1, Y_change_2));

                textList.add(empty);

                if (!presetConfirm) textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.text.unselected"));
                else {
                    BlockPos pos = getPos();
                    int minChunkX = (pos.getX() >> 4) + offsetX;
                    int minChunkZ = (pos.getZ() >> 4) + offsetZ;
                    int maxChunkX = minChunkX + getPlatformBlockStructure(saveGroup, saveId).getXSize() / 16;
                    int maxChunkZ = minChunkZ + getPlatformBlockStructure(saveGroup, saveId).getZSize() / 16;

                    int minX = minChunkX << 4;
                    int maxX = (maxChunkX << 4) + 15;
                    int minZ = minChunkZ << 4;
                    int maxZ = (maxChunkZ << 4) + 15;

                    int minY = pos.getY() + offsetY;
                    int maxY = minY + getPlatformBlockStructure(saveGroup, saveId).getYSize();

                    textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.boundary"));

                    textList.add(createEqualColumns(langWidth,
                            Component.translatable("gtocore.machine.industrial_platform_deployment_tools.offset.x", maxX),
                            Component.literal(String.valueOf(minX))));
                    textList.add(createEqualColumns(langWidth,
                            Component.translatable("gtocore.machine.industrial_platform_deployment_tools.offset.y", maxY),
                            Component.literal(String.valueOf(minY))));
                    textList.add(createEqualColumns(langWidth,
                            Component.translatable("gtocore.machine.industrial_platform_deployment_tools.offset.z", maxZ),
                            Component.literal(String.valueOf(minZ))));
                }
                textList.add(empty);
                textList.add(createEqualColumns(langWidth,
                        Component.translatable("gtocore.machine.industrial_platform_deployment_tools.breakBlocks")
                                .append(ComponentPanelWidget.withButton(breakBlocks ?
                                        Component.translatable("gtocore.machine.on") :
                                        Component.translatable("gtocore.machine.off"), "breakBlocks")),
                        Component.translatable("gtocore.machine.industrial_platform_deployment_tools.skipAir")
                                .append(ComponentPanelWidget.withButton(skipAir ?
                                        Component.translatable("gtocore.machine.on") :
                                        Component.translatable("gtocore.machine.off"), "skipAir"))));

                textList.add(createEqualColumns(langWidth,
                        Component.translatable("gtocore.machine.industrial_platform_deployment_tools.updateLight")
                                .append(ComponentPanelWidget.withButton(updateLight ?
                                        Component.translatable("gtocore.machine.on") :
                                        Component.translatable("gtocore.machine.off"), "updateLight")),
                        Component.translatable("gtocore.machine.industrial_platform_deployment_tools.speed")
                                .append(String.format("%.1f", speed / 10.0))
                                .append(ComponentPanelWidget.withButton(Component.literal("[+]"), "+speed"))
                                .append(ComponentPanelWidget.withButton(Component.literal("[-]"), "-speed"))));
            }
        }
    }

    private void handleDisplayClick(String componentData, ClickData clickData) {
        switch (step) {
            case PresetSelection -> {
                if (!ensurePresetsReady()) return;
                int maxId = getPlatformPreset(checkGroup).getStructures().size() - 1;
                switch (componentData) {
                    case "next_group" -> {
                        checkGroup = Mth.clamp(checkGroup + 1, 0, maxGroup - 1);
                        checkId = 0;
                    }
                    case "previous_group" -> {
                        checkGroup = Mth.clamp(checkGroup - 1, 0, maxGroup - 1);
                        checkId = 0;
                    }
                    case "next_group_plas" -> {
                        checkGroup = Mth.clamp(checkGroup + 10, 0, maxGroup - 1);
                        checkId = 0;
                    }
                    case "previous_group_plas" -> {
                        checkGroup = Mth.clamp(checkGroup - 10, 0, maxGroup - 1);
                        checkId = 0;
                    }

                    case "next_id" -> checkId = Mth.clamp(checkId + 1, 0, maxId);
                    case "previous_id" -> checkId = Mth.clamp(checkId - 1, 0, maxId);
                    case "next_id_plas" -> checkId = Mth.clamp(checkId + 5, 0, maxId);
                    case "previous_id_plas" -> checkId = Mth.clamp(checkId - 5, 0, maxId);

                    case "choose_this" -> {
                        saveGroup = checkGroup;
                        saveId = checkId;
                        presetConfirm = true;
                        examineMaterial();
                    }
                }
            }
            case ConfirmConsumables -> {
                if (componentData.equals("loading")) {
                    loadingMaterial();
                    examineMaterial();
                } else if (componentData.equals("unloading")) {
                    unloadingMaterial();
                    examineMaterial();
                }
            }
            case AdjustSettings -> {
                switch (componentData) {
                    case "x_add" -> offsetX++;
                    case "x_minus" -> offsetX--;
                    case "z_add" -> offsetZ++;
                    case "z_minus" -> offsetZ--;
                    case "y_add" -> offsetY++;
                    case "y_minus" -> offsetY--;

                    case "x_add_plas" -> offsetX += adjustX;
                    case "x_minus_plas" -> offsetX -= adjustX;
                    case "adjust_x_add" -> adjustX = Math.max(0, adjustX + 1);
                    case "adjust_x_minus" -> adjustX = Math.max(0, adjustX - 1);

                    case "z_add_plas" -> offsetZ += adjustZ;
                    case "z_minus_plas" -> offsetZ -= adjustZ;
                    case "adjust_z_add" -> adjustZ = Math.max(0, adjustZ + 1);
                    case "adjust_z_minus" -> adjustZ = Math.max(0, adjustZ - 1);

                    case "y_add_plas" -> offsetY += adjustY;
                    case "y_minus_plas" -> offsetY -= adjustY;
                    case "adjust_y_add" -> adjustY = Math.max(0, adjustY + 1);
                    case "adjust_y_minus" -> adjustY = Math.max(0, adjustY - 1);

                    case "breakBlocks" -> breakBlocks = !breakBlocks;
                    case "skipAir" -> skipAir = !skipAir;
                    case "updateLight" -> updateLight = !updateLight;
                    case "+speed" -> speed = Mth.clamp(checkId + 5, 10, 100);
                    case "-speed" -> speed = Mth.clamp(checkId - 5, 10, 100);
                }
            }
        }
    }

    private boolean ensurePresetsReady() {
        if (maxGroup == 0) return false;
        checkGroup = Mth.clamp(checkGroup, 0, maxGroup - 1);
        PlatformBlockType.PlatformPreset preset = getPlatformPreset(checkGroup);
        if (preset.getStructures().isEmpty()) return false;
        checkId = Mth.clamp(checkId, 0, preset.getStructures().size() - 1);
        return true;
    }

    private IGuiTexture getIGuiTexture() {
        if (step == PresetSelection && ensurePresetsReady()) {
            if (!getPlatformBlockStructure(checkGroup, checkId).getPreview()) return IGuiTexture.EMPTY;
            String pngs = getPlatformPreset(checkGroup).getName() + "/" + getPlatformBlockStructure(checkGroup, checkId).getName() + ".png";
            ResourceLocation imageLocation = GTOCore.id("textures/gui/industrial_platform_deployment_tools/" + pngs);
            return new ResourceTexture(imageLocation);
        }
        return IGuiTexture.EMPTY;
    }

    // 启动控制工具
    private void addDisplayTextStart(List<Component> textList) {
        if (!presetConfirm) {
            textList.add(centerComponent(50, Component.translatable("gtocore.machine.industrial_platform_deployment_tools.text.unselected")));
        } else {
            if (!insufficient) {
                textList.add(centerComponent(50, Component.translatable("gtocore.machine.industrial_platform_deployment_tools.material.insufficient")));
            } else {
                if (!taskCompleted) {
                    textList.add(centerComponent(50, Component.translatable("gtocore.machine.industrial_platform_deployment_tools.doing", progress)));
                } else {
                    textList.add(centerComponent(50, ComponentPanelWidget.withButton(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.start"), "start")));
                }
            }
        }
        if (canExport) textList.add(centerComponent(50, ComponentPanelWidget.withButton(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.export"), "export")));
    }

    private void handleDisplayClickStart(String componentData, ClickData clickData) {
        if (componentData.equals("start")) start();
        else if (componentData.equals("export")) getPlatform();
    }

    /////////////////////////////////////
    // ********** UI布局工具 ********** //
    /////////////////////////////////////

    // 翻页与页标题
    private static Component createPageNavigation(int totalWidth, Component titleComp, String string) {
        Font font = Minecraft.getInstance().font;
        Component leftBtn = ComponentPanelWidget.withButton(Component.literal(" [ ← ] "), "previous_" + string);
        Component rightBtn = ComponentPanelWidget.withButton(Component.literal(" [ → ] "), "next_" + string);
        int middleSpace = totalWidth - font.width(leftBtn) - font.width(titleComp) - font.width(rightBtn);
        int leftSpace = middleSpace / 2;
        int rightSpace = middleSpace - leftSpace;
        int spacePixel = font.width(" ");
        Component leftPad = Component.literal(" ".repeat(spacePixel > 0 ? leftSpace / spacePixel : leftSpace));
        Component rightPad = Component.literal(" ".repeat(spacePixel > 0 ? rightSpace / spacePixel : rightSpace));
        return Component.empty().append(leftBtn).append(leftPad).append(titleComp).append(rightPad).append(rightBtn);
    }

    // 自动分栏
    private static Component createEqualColumns(int totalWidth, Component... components) {
        if (components.length == 0) return Component.empty();
        Font font = Minecraft.getInstance().font;
        int columnCount = components.length;
        int baseColWidth = totalWidth / columnCount;
        int remainder = totalWidth % columnCount;
        MutableComponent result = Component.empty();
        for (int i = 0; i < columnCount; i++) {
            Component col = components[i];
            int spacePixel = font.width(" ");
            int padPixels = (baseColWidth + (i == columnCount - 1 ? remainder : 0)) - font.width(col);
            result = result.append(col);
            if (padPixels > 0 && spacePixel > 0) {
                result = result.append(Component.literal(" ".repeat(padPixels / spacePixel)));
            }
        }
        return result;
    }

    // 自动居中
    private static Component centerComponent(int totalWidth, Component component) {
        Font font = Minecraft.getInstance().font;
        int contentWidth = font.width(component);
        if (contentWidth >= totalWidth) return component;
        int leftSpace = (totalWidth - contentWidth) / 2;
        int rightSpace = totalWidth - contentWidth - leftSpace;
        Component leftPad = Component.literal(" ".repeat(leftSpace / font.width(" ")));
        Component rightPad = Component.literal(" ".repeat(rightSpace / font.width(" ")));
        return Component.empty().append(leftPad).append(component).append(rightPad);
    }

    /////////////////////////////////////
    // *********** 辅助方法 *********** //
    /////////////////////////////////////

    private PlatformBlockType.PlatformPreset getPlatformPreset(int group) {
        return presets.get(group);
    }

    private PlatformBlockType.PlatformBlockStructure getPlatformBlockStructure(int group, int id) {
        return getPlatformPreset(group).getStructures().get(id);
    }

    private void loadingMaterial() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            for (int k = 0; k < ITEM_VALUE_HOLDERS.size(); k++) {
                for (IntObjectHolder<Item> holder : ITEM_VALUE_HOLDERS.get(k)) {
                    if (holder.obj.equals(stack.getItem())) {
                        materialInventory[k] += holder.number * stack.getCount();
                        inventory.setStackInSlot(i, ItemStack.EMPTY);
                        break;
                    }
                }
            }
        }
    }

    private void unloadingMaterial() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty()) continue;
            boolean filled = false;
            for (int k = 0; k < ITEM_VALUE_HOLDERS.size() && !filled; k++) {
                for (IntObjectHolder<Item> holder : ITEM_VALUE_HOLDERS.get(k)) {
                    int count = Math.min(materialInventory[k] / holder.number, 64);
                    if (count > 0) {
                        inventory.setStackInSlot(i, new ItemStack(holder.obj, count));
                        materialInventory[k] -= holder.number * count;
                        filled = true;
                        break;
                    }
                }
            }
        }
    }

    private void examineMaterial() {
        if (!presetConfirm) {
            insufficient = false;
            return;
        }
        int[] costMaterial = getPlatformBlockStructure(saveGroup, saveId).getMaterials();
        boolean materialsSufficient = true;
        canExport = false;
        for (int i = 0; i < materialInventory.length; i++) {
            if (materialInventory[i] < costMaterial[i]) {
                materialsSufficient = false;
                break;
            }
        }
        List<IntObjectHolder<ItemStack>> extraMaterials = getPlatformBlockStructure(saveGroup, saveId).getExtraMaterials();
        Map<Item, Integer> inventoryCount = new HashMap<>();
        int coordinateCards = 0;
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            inventoryCount.put(stack.getItem(), inventoryCount.getOrDefault(stack.getItem(), 0) + stack.getCount());
            if (stack.getItem() == GTOItems.COORDINATE_CARD.asItem()) coordinateCards++;
        }
        for (IntObjectHolder<ItemStack> holder : extraMaterials) {
            Item item = holder.obj.getItem();
            int required = holder.number;
            int available = inventoryCount.getOrDefault(item, 0);
            if (available < required) {
                materialsSufficient = false;
                break;
            }
        }
        canExport = coordinateCards > 1;
        insufficient = materialsSufficient;
    }

    private boolean consumeResources() {
        if (!presetConfirm || !insufficient) return false;
        int[] costMaterial = getPlatformBlockStructure(saveGroup, saveId).getMaterials();
        for (int i = 0; i < materialInventory.length; i++) materialInventory[i] -= costMaterial[i];
        List<IntObjectHolder<ItemStack>> extraMaterials = getPlatformBlockStructure(saveGroup, saveId).getExtraMaterials();
        for (IntObjectHolder<ItemStack> holder : extraMaterials) {
            Item item = holder.obj.getItem();
            int remaining = holder.number;
            for (int i = 0; i < inventory.getSize() && remaining > 0; i++) {
                ItemStack stack = inventory.getStackInSlot(i);
                if (stack.getItem() == item) {
                    int take = Math.min(stack.getCount(), remaining);
                    stack.shrink(take);
                    remaining -= take;
                }
            }
            if (remaining > 0) {
                GTOCore.LOGGER.error("Failed to consume all required resources for platform deployment");
                return false;
            }
        }
        return true;
    }

    private void start() {
        if (!taskCompleted) return;
        Level level = getLevel();
        BlockPos pos = getPos();
        if (level == null) return;
        if (!(level instanceof ServerLevel serverLevel)) return;
        if (!consumeResources()) return;
        progress = 0;
        taskCompleted = false;
        try {
            PlatformStructurePlacer.placeStructureAsync(
                    serverLevel,
                    new BlockPos(((pos.getX() >> 4) + offsetX) << 4, pos.getY() + offsetY, ((pos.getZ() >> 4) + offsetZ) << 4),
                    getPlatformBlockStructure(saveGroup, saveId),
                    speed * 1000,
                    breakBlocks,
                    skipAir,
                    updateLight,
                    progress -> this.progress = progress,
                    () -> taskCompleted = true);
        } catch (IOException e) {
            GTOCore.LOGGER.error("The industrial platform deployment tool cannot deploy the platform, platform error {} {}, file location {}",
                    getPlatformPreset(saveGroup).getName(),
                    getPlatformBlockStructure(saveGroup, saveId).getName(),
                    getPlatformBlockStructure(saveGroup, saveId).getResource());
            taskCompleted = true;
        }
        examineMaterial();
    }

    private void getPlatform() {
        BlockPos pos1 = null;
        BlockPos pos2 = null;
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack.getItem() == GTOItems.COORDINATE_CARD.asItem()) {
                if (pos1 == null) pos1 = getStoredCoordinates(stack);
                else pos2 = getStoredCoordinates(stack);
            }
        }
        if (pos1 != null && pos2 != null) {
            PlatformCreationAsync(getLevel(), pos1, pos2);
        }
    }
}
