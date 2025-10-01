package com.gtocore.common.machine.mana;

import com.gtocore.api.lang.OffsetGradientColor;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.translation.GTOMachineTooltips;

import com.gtolib.GTOCore;

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

import java.util.*;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class IndustrialPlatformDeploymentToolsMachine extends MetaMachine implements IFancyUIMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(IndustrialPlatformDeploymentToolsMachine.class, MetaMachine.MANAGED_FIELD_HOLDER);

    @Persisted
    private final NotifiableItemStackHandler inventory;

    private final Map<String, PlatformBlockType.PlatformPreset> presetMap = new HashMap<>();
    private final List<String> presetNames = new ArrayList<>();

    public IndustrialPlatformDeploymentToolsMachine(MetaMachineBlockEntity holder) {
        super(holder);
        inventory = new NotifiableItemStackHandler(this, 9, IO.NONE, IO.BOTH);
        initializePresets();
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
        BlockPos pos = getPos();
        Level level = getLevel();
        if (level != null) {
            centerX = pos.getX() >> 4;
            centerZ = pos.getZ() >> 4;
            centerY = pos.getY();
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        presetMap.clear();
    }

    /**
     * 初始化预设模板
     */
    private void initializePresets() {
        List<PlatformBlockType.PlatformPreset> presets = PlatformTemplateStorage.initializePresets();
        for (PlatformBlockType.PlatformPreset preset : presets) {
            presetMap.put(preset.getName(), preset);
        }
        presetNames.addAll(presetMap.keySet());
        if (!presetNames.isEmpty()) {
            currentPresetName = presetNames.get(0);
        }
    }

    /////////////////////////////////////
    // *********** 信息存储 *********** //
    /// //////////////////////////////////

    // 第零步 - 简介
    // 第一步 - 选择预设
    // 第二步 - 选择偏移 - 显示四角的坐标 - (x方向区块)(Z方向区块)(Y方向)
    // ------- 输入材料 - (高级/中级/初级/基础压缩工业平台组件)
    // 第三步 - 确认放置 - (消耗材料) - (开启放置任务)
    // 第四步 - 返回第一步

    // 当前所处的步骤（0-4）
    @Persisted
    private int step = 0;

    // 总步骤数
    private final int totalStep = 4;

    // 步骤编号常量定义
    private final int Introduction = 0;        // 第零步：简介
    private final int PresetSelection = 1;     // 第一步：选择预设
    private final int OffsetConfiguration = 2; // 第二步：选择偏移 确认材料
    private final int ConfirmPlacement = 3;    // 第三步：确认放置

    // ------------------- 第一步：选择预设 -------------------
    // 是否已完成预设选择
    @Persisted
    private boolean presetConfirm = false;
    @Persisted
    private String currentPresetName = null;
    // 显示的预设编号
    private int checkId = 0;
    // 保存的预设组编号（0表示未选择，1+表示不同的预设组）
    @Persisted
    private int saveGroup = 0;
    // 保存的预设编号
    @Persisted
    private int saveId = 0;

    // ------------------- 第二步：选择偏移 -------------------
    // 是否已完成偏移配置
    @Persisted
    private boolean offsetConfirm = false;
    // X中心区块坐标
    @Persisted
    private int centerX = 0;
    // Z中心区块坐标
    @Persisted
    private int centerZ = 0;
    // Y中心坐标
    @Persisted
    private int centerY = 0;

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
    private int materialInventory = 0;

    // 定义物品与价值的映射关系
    private static final Map<Item, Integer> ITEM_VALUE_MAP;

    static {
        ITEM_VALUE_MAP = new LinkedHashMap<>();
        ITEM_VALUE_MAP.put(GTOItems.BIOWARE_MAINFRAME.asItem(), 5000);
        ITEM_VALUE_MAP.put(GTOItems.BIOWARE_COMPUTER.asItem(), 2000);
        ITEM_VALUE_MAP.put(GTOItems.BIOWARE_ASSEMBLY.asItem(), 500);
        ITEM_VALUE_MAP.put(GTOItems.BIOWARE_PROCESSOR.asItem(), 200);
    }

    // ------------------- 第四步：运行中 -------------------
    // 任务是否完成
    @Persisted
    private boolean taskCompleted = true;

    /////////////////////////////////////
    // ************ UI组件 ************ //
    /// //////////////////////////////////

    int totalLangWidth = 266;
    int langWidth = 266 - 8;

    // 创建UI组件
    @Override
    public Widget createUIWidget() {
        var group = new WidgetGroup(0, 0, 320 + 8, 160 + 8);

        WidgetGroup group_1 = new DraggableScrollableWidgetGroup(4, 4, totalLangWidth, 160)
                .setBackground(GuiTextures.DISPLAY);

        // 步骤标题
        group_1.addWidget(new ComponentPanelWidget(4, 5, this::addDisplayTextTitle));

        // 步骤选择
        group_1.addWidget(new ComponentPanelWidget(220, 17, this::addDisplayTextStep)
                .clickHandler(this::handleDisplayClickStep));

        // 预设组控制工具
        group_1.addWidget(new ComponentPanelWidget(4, 29, this::addDisplayTextPage)
                .clickHandler(this::handleDisplayClickPage));

        // 图片展示
        group_1.addWidget(new ImageWidget(166, 60, 100, 100, this::getIGuiTexture));

        // 页面主文本
        group_1.addWidget(new ComponentPanelWidget(4, 29, this::addDisplayText)
                .clickHandler(this::handleDisplayClickText)
                .setMaxWidthLimit(langWidth));

        group.addWidget(group_1);

        // 物品槽
        WidgetGroup group_2 = new DraggableScrollableWidgetGroup(271, 3, 54, 54);
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                int slotIndex = y * 3 + x;
                group_2.addWidget(new SlotWidget(inventory, slotIndex, x * 18, y * 18, true, true).setBackground(GuiTextures.SLOT));
            }
        }
        group.addWidget(group_2);

        // 原料信息表
        WidgetGroup group_3 = new DraggableScrollableWidgetGroup(271, 58, 54, 107)
                .setBackground(GuiTextures.CLIPBOARD_PAPER_BACKGROUND);
        group_3.addWidget(new ComponentPanelWidget(4, 4, this::addDisplayTextMaterial)
                .clickHandler(this::handleDisplayClickMaterial)
                .setMaxWidthLimit(46));
        group.addWidget(group_3);

        group.setBackground(GuiTextures.BACKGROUND_INVERSE);
        return group;
    }

    // 步骤标题显示
    private void addDisplayTextTitle(List<Component> textList) {
        textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.title." + step));
    }

    // 步骤控制工具
    private void addDisplayTextStep(List<Component> textList) {
        MutableComponent result = Component.empty();
        for (int i = 0; i <= totalStep; i++) {
            if (step == i)
                result.append(ComponentPanelWidget.withButton(Component.literal("§b" + "⭕" + "§r"), "step_" + i));
            else result.append(ComponentPanelWidget.withButton(Component.literal("⭕"), "step_" + i));
        }
        textList.add(result);
    }

    private void handleDisplayClickStep(String componentData, ClickData clickData) {
        String[] parts = componentData.split("_", 2);
        if (parts[0].equals("step")) {
            step = Mth.clamp(Integer.parseInt(parts[1]), 0, totalStep);
        }
    }

    // 预设组控制工具
    private void addDisplayTextPage(List<Component> textList) {
        if (step == PresetSelection && ensurePresetsReady()) {
            int currentIndex = presetNames.indexOf(currentPresetName);
            int totalGroups = presetNames.size();

            textList.add(createPageNavigation(
                    langWidth,
                    createPageNavigation(langWidth - 80,
                            Component.literal("<" + (currentIndex + 1) + "/" + totalGroups + ">"),
                            "group"),
                    "group_plas"));

            PlatformBlockType.PlatformPreset currentPreset = presetMap.get(currentPresetName);
            textList.add(createPageNavigation(
                    langWidth,
                    Component.literal("<" + (checkId + 1) + "/" + currentPreset.getStructures().size() + ">"),
                    "id"));
        }
    }

    private void handleDisplayClickPage(String componentData, ClickData clickData) {
        if (!ensurePresetsReady()) return;

        int currentIndex = presetNames.indexOf(currentPresetName);
        int maxGroup = presetNames.size() - 1;

        PlatformBlockType.PlatformPreset currentPreset = presetMap.get(currentPresetName);
        int maxId = currentPreset.getStructures().size() - 1;

        switch (componentData) {
            case "next_group" -> {
                currentIndex = (currentIndex + 1) % (maxGroup + 1);
                currentPresetName = presetNames.get(currentIndex);
                checkId = 0;
            }
            case "previous_group" -> {
                currentIndex = (currentIndex - 1 + (maxGroup + 1)) % (maxGroup + 1);
                currentPresetName = presetNames.get(currentIndex);
                checkId = 0;
            }
            case "next_group_plas" -> {
                currentIndex = Math.min(currentIndex + 10, maxGroup);
                currentPresetName = presetNames.get(currentIndex);
                checkId = 0;
            }
            case "previous_group_plas" -> {
                currentIndex = Math.max(currentIndex - 10, 0);
                currentPresetName = presetNames.get(currentIndex);
                checkId = 0;
            }
            case "next_id" -> checkId = Mth.clamp(checkId + 1, 0, maxId);
            case "previous_id" -> checkId = Mth.clamp(checkId - 1, 0, maxId);
        }
    }

    private boolean ensurePresetsReady() {
        if (presetMap.isEmpty()) return false;

        if (currentPresetName == null || !presetMap.containsKey(currentPresetName)) {
            currentPresetName = presetNames.get(0);
        }

        PlatformBlockType.PlatformPreset preset = presetMap.get(currentPresetName);
        if (preset == null || preset.getStructures().isEmpty()) {
            return false;
        }

        checkId = Mth.clamp(checkId, 0, preset.getStructures().size() - 1);
        return true;
    }

    // 获得图片
    private IGuiTexture getIGuiTexture() {
        if (step == PresetSelection && currentPresetName != null) {
            String pngs = currentPresetName + ".png";
            ResourceLocation imageLocation = GTOCore.id("textures/gui/industrial_platform_deployment_tools/" + pngs);
            return new ResourceTexture(imageLocation);
        }
        return IGuiTexture.EMPTY;
    }

    // 页面主文本
    private void addDisplayText(List<Component> textList) {
        if (step == PresetSelection) {
            textList.add(Component.literal(""));
            textList.add(Component.literal(""));
        }
        switch (step) {
            case Introduction -> GTOMachineTooltips.INSTANCE.getIndustrialPlatformDeploymentToolsIntroduction().apply(textList);
            case PresetSelection -> {
                textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.text.choose_this")
                        .append(ComponentPanelWidget.withButton(Component.literal("[⭕]").withStyle(s -> s.withColor(new OffsetGradientColor(0.5f))), "choose_this")));
                if (presetConfirm) {
                    textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.text.selected", saveGroup + 1, saveId + 1));
                } else {
                    textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.text.unselected"));
                }
            }
            case OffsetConfiguration -> {
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

                if (presetConfirm) {
                    int maxChunkX = centerX + offsetX;
                    int maxChunkZ = centerZ + offsetZ;
                    int minChunkX = maxChunkX - 3;
                    int minChunkZ = maxChunkZ - 3;

                    int minX = minChunkX << 4;
                    int maxX = (maxChunkX << 4) + 15;
                    int minZ = minChunkZ << 4;
                    int maxZ = (maxChunkZ << 4) + 15;

                    Component coordinate_1 = Component.literal("(" + minX + "," + (centerY + offsetY) + "," + maxZ + ")");
                    Component coordinate_2 = Component.literal("(" + maxX + "," + (centerY + offsetY) + "," + maxZ + ")");
                    Component coordinate_3 = Component.literal("(" + minX + "," + (centerY + offsetY) + "," + minZ + ")");
                    Component coordinate_4 = Component.literal("(" + maxX + "," + (centerY + offsetY) + "," + minZ + ")");

                    textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.coordinate"));
                    textList.add(createEqualColumns(langWidth, coordinate_1, coordinate_2));
                    textList.add(createEqualColumns(langWidth, coordinate_3, coordinate_4));
                } else {
                    textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.text.unselected"));
                }
            }
            case ConfirmPlacement -> {
                textList.add(Component.translatable("步骤" + step));
            }
        }
    }

    private void handleDisplayClickText(String componentData, ClickData clickData) {
        switch (componentData) {
            case "choose_this" -> {
                saveGroup = presetNames.indexOf(currentPresetName);
                saveId = checkId;
                presetConfirm = true;
            }
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
        }
    }

    // 原料控制工具
    private void addDisplayTextMaterial(List<Component> textList) {
        Component input = ComponentPanelWidget.withButton(Component.literal(" [ ↓ ] "), "input");
        Component output = ComponentPanelWidget.withButton(Component.literal(" [ ↑ ] "), "output");

        textList.add(createEqualColumns(46, input, output));

        if (presetConfirm) {
            int costMaterial = getPlatformBlockStructure(saveGroup, saveId).getMaterials();
            if (materialInventory > costMaterial) textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.material.adequate"));
            else textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.material.insufficient"));
        }

        Component materialReserves = Component.translatable("gtocore.machine.industrial_platform_deployment_tools.material.reserves", materialInventory);

        textList.add(materialReserves);
    }

    private void handleDisplayClickMaterial(String componentData, ClickData clickData) {
        if (Objects.equals(componentData, "input")) {
            for (int i = 0; i < inventory.getSlots(); i++) {
                ItemStack stack = inventory.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    Integer value = ITEM_VALUE_MAP.get(stack.getItem());
                    if (value != null) {
                        materialInventory += value * stack.getCount();
                        inventory.setStackInSlot(i, ItemStack.EMPTY);
                    }
                }
            }
        } else if (Objects.equals(componentData, "output")) {
            for (int i = 0; i < inventory.getSlots(); i++) {
                if (inventory.getStackInSlot(i).isEmpty()) {
                    for (Map.Entry<Item, Integer> entry : ITEM_VALUE_MAP.entrySet()) {
                        int count = Math.min(materialInventory / entry.getValue(), 64);
                        if (count > 0) {
                            inventory.setStackInSlot(i, new ItemStack(entry.getKey(), count));
                            materialInventory -= entry.getValue() * count;
                            break;
                        }
                    }
                }
            }
        }
    }

    /////////////////////////////////////
    // ********** UI布局工具 ********** //
    /////////////////////////////////////

    // 翻页与页标题
    private static Component createPageNavigation(int totalWidth, Component titleComp, String string) {
        Font font = Minecraft.getInstance().font;

        Component leftBtn = ComponentPanelWidget.withButton(Component.literal(" [ ← ] "), "previous_" + string);
        Component rightBtn = ComponentPanelWidget.withButton(Component.literal(" [ → ] "), "next_" + string);

        int leftBtnWidth = font.width(leftBtn);
        int rightBtnWidth = font.width(rightBtn);
        int titleWidth = font.width(titleComp);

        int middleSpace = totalWidth - leftBtnWidth - titleWidth - rightBtnWidth;
        int leftSpace = middleSpace / 2;
        int rightSpace = middleSpace - leftSpace;

        int spacePixel = font.width(" ");

        Component leftPad = Component.literal(" ".repeat(spacePixel > 0 ? leftSpace / spacePixel : leftSpace));
        Component rightPad = Component.literal(" ".repeat(spacePixel > 0 ? rightSpace / spacePixel : rightSpace));

        MutableComponent result = Component.empty();
        result = result.append(leftBtn);
        result = result.append(leftPad);
        result = result.append(titleComp);
        result = result.append(rightPad);
        result = result.append(rightBtn);

        return result;
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
            int colWidth = font.width(col);
            int currentColTotalWidth = baseColWidth + (i == columnCount - 1 ? remainder : 0);
            int spacePixel = font.width(" ");
            int padPixels = currentColTotalWidth - colWidth;
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

        MutableComponent result = Component.empty();
        result = result.append(leftPad);
        result = result.append(component);
        result = result.append(rightPad);

        return result;
    }

    /////////////////////////////////////
    // ********* 辅助获取方法 ********* //
    /////////////////////////////////////

    private PlatformBlockType.PlatformPreset getPlatformPreset(int group) {
        return presetMap.get(presetNames.get(group));
    }

    private PlatformBlockType.PlatformBlockStructure getPlatformBlockStructure(int group, int id) {
        return getPlatformPreset(group).getStructures().get(id);
    }

    /////////////////////////////////////
    // ********* 平台生成主方法 ********* //
    /////////////////////////////////////
}
