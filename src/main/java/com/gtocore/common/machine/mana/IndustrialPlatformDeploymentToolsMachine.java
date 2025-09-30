package com.gtocore.common.machine.mana;

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
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;

import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class IndustrialPlatformDeploymentToolsMachine extends MetaMachine implements IFancyUIMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(IndustrialPlatformDeploymentToolsMachine.class, MetaMachine.MANAGED_FIELD_HOLDER);

    @Persisted
    private final NotifiableItemStackHandler inventory;

    public IndustrialPlatformDeploymentToolsMachine(MetaMachineBlockEntity holder) {
        super(holder);
        inventory = new NotifiableItemStackHandler(this, 9, IO.NONE, IO.BOTH);
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
    // 显示的预设组编号（0表示未选择，1+表示不同的预设组）
    private int checkGroup = 0;
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
    // X方向区块偏移
    @Persisted
    private int offsetX = 0;
    // Z方向区块偏移
    @Persisted
    private int offsetZ = 0;
    // Y方向高度偏移
    @Persisted
    private int offsetY = -1;

    // ------------------- 第三步：确认放置 -------------------
    // 库存的原料量
    @Persisted
    private int[] materialInventory = new int[4];

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

        // 存储信息表
        WidgetGroup group_3 = new DraggableScrollableWidgetGroup(271, 58, 54, 107)
                .setBackground(GuiTextures.CLIPBOARD_PAPER_BACKGROUND);
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
        if (step == PresetSelection) {
            textList.add(createPageNavigation(langWidth, "<" + (checkGroup + 1) + "/" + (checkGroup + 1 + 10) + ">", "group"));
            textList.add(createPageNavigation(langWidth, "<" + (checkId + 1) + "/" + (checkId + 1 + 10) + ">", "id"));
        }
    }

    private void handleDisplayClickPage(String componentData, ClickData clickData) {
        switch (componentData) {
            case "next_group" -> {
                checkGroup = Mth.clamp(checkGroup + 1, 0, checkGroup + 1 + 10);
                checkId = 0;
            }
            case "previous_group" -> {
                checkGroup = Mth.clamp(checkGroup - 1, 0, checkGroup + 1 + 10);
                checkId = 0;
            }
            case "next_id" -> checkId = Mth.clamp(checkId + 1, 0, checkId + 1 + 10);
            case "previous_id" -> checkId = Mth.clamp(checkId - 1, 0, checkId + 1 + 10);
        }
    }

    // 获得图片
    private IGuiTexture getIGuiTexture() {
        if (step == Introduction && checkGroup != 0) {
            String pngs = "template_" + checkGroup + "_" + checkId + ".png";
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
        textList.add(Component.literal("步骤" + step));
        switch (step) {
            case 0 -> GTOMachineTooltips.INSTANCE.getIndustrialPlatformDeploymentToolsIntroduction().apply(textList);
            case 1 -> {
                textList.add(Component.literal("步骤" + step));
                textList.add(Component.literal("步骤" + step));
            }
        }
    }

    private void handleDisplayClickText(String componentData, ClickData clickData) {}

    /////////////////////////////////////
    // ********** UI布局工具 ********** //

    /// //////////////////////////////////

    // 翻页与页标题
    private static Component createPageNavigation(int totalWidth, String title, String string) {
        Font font = Minecraft.getInstance().font;

        Component leftBtn = ComponentPanelWidget.withButton(Component.literal(" [ ← ] "), "previous_" + string);
        Component rightBtn = ComponentPanelWidget.withButton(Component.literal(" [ → ] "), "next_" + string);

        Component titleComp = Component.translatable(title);

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
    // ******** 模板存储验证工具 ******** //
    /////////////////////////////////////

    /**
     * 表示结构块的类型
     */
    public enum BlockType {
        CORE,      // 核心块
        ROAD_X,    // X方向道路
        ROAD_Z,    // Z方向道路
        ROAD_CROSS // 道路交叉点
    }

    /**
     * 单个平台结构块的信息
     * 参考 FactoryBlockPattern 的存储方式
     */
    public record PlatformBlockStructure(
                                         BlockType type,              // 块类型
                                         List<String[]> depth,        // 多层结构（z 方向），每个 String[] 是 y 方向一层
                                         Char2ObjectMap<Block> blockMapping, // 字符 -> 方块映射
                                         int[] materials,             // 整个结构需要的材料数量（顺序与 blockMapping.keySet() 一致）
                                         int xSize,                   // x 方向大小（水平）
                                         int ySize,                   // y 方向大小（高度）
                                         int zSize                    // z 方向大小（水平）
    ) {

        /**
         * 构造方法 - 自动计算尺寸并校验
         */
        public PlatformBlockStructure(
                                      BlockType type,
                                      List<String[]> depth,
                                      Map<Character, Block> blockMapping,
                                      int[] materials) {
            this(
                    type,
                    depth,
                    new Char2ObjectOpenHashMap<>(blockMapping),
                    materials,
                    depth.get(0)[0].length(), // x 大小
                    depth.get(0).length,      // y 大小
                    depth.size()              // z 大小
            );

            // 校验结构
            validateStructure(depth);

            // 校验方块数量在 2~20 之间
            if (blockMapping.size() < 2 || blockMapping.size() > 20) {
                throw new IllegalArgumentException("A structure must contain between 2 and 20 different blocks");
            }
        }

        /**
         * 校验结构合法性
         */
        private static void validateStructure(List<String[]> depth) {
            if (depth.isEmpty()) throw new IllegalArgumentException("Depth cannot be empty");

            int ySize = depth.get(0).length;
            int xSize = depth.get(0)[0].length();
            int zSize = depth.size();

            // 检查所有层高度一致
            for (String[] layer : depth) {
                if (layer.length != ySize) {
                    throw new IllegalArgumentException("All layers must have the same height (y size)");
                }
                // 检查所有行长度一致
                for (String row : layer) {
                    if (row.length() != xSize) {
                        throw new IllegalArgumentException("All rows must have the same width (x size)");
                    }
                }
            }

            // 检查 x/z 必须是 16 的倍数
            if (xSize % 16 != 0) throw new IllegalArgumentException("X size must be multiple of 16");
            if (zSize % 16 != 0) throw new IllegalArgumentException("Z size must be multiple of 16");

            // 检查 x % z == 0 或 z % x == 0
            if (xSize % zSize != 0 && zSize % xSize != 0) {
                throw new IllegalArgumentException("Either X size must be multiple of Z size or vice versa");
            }
        }

        /**
         * 根据相对坐标获取方块
         *
         * @param x 水平方向 X
         * @param y 高度方向 Y
         * @param z 水平方向 Z
         * @return 方块
         */
        public Block getBlockAt(int x, int y, int z) {
            if (z < 0 || z >= zSize) throw new IndexOutOfBoundsException("Z index out of bounds");
            if (y < 0 || y >= ySize) throw new IndexOutOfBoundsException("Y index out of bounds");
            if (x < 0 || x >= xSize) throw new IndexOutOfBoundsException("X index out of bounds");

            char symbol = depth.get(z)[y].charAt(x);
            return blockMapping.get(symbol);
        }
    }

    /**
     * 平台预设（组信息）
     */
    public record PlatformPreset(
                                 int id,
                                 String name,
                                 String description,
                                 List<PlatformBlockStructure> structures) {

        public PlatformPreset {
            // 提取所有类型
            Set<BlockType> types = structures.stream()
                    .map(PlatformBlockStructure::type)
                    .collect(java.util.stream.Collectors.toSet());

            // 检查数量和类型
            switch (structures.size()) {
                case 1 -> {
                    if (!types.contains(BlockType.CORE)) {
                        throw new IllegalArgumentException("1 structure must be CORE type");
                    }
                }
                case 3 -> {
                    if (!types.equals(Set.of(BlockType.ROAD_X, BlockType.ROAD_Z, BlockType.ROAD_CROSS))) {
                        throw new IllegalArgumentException("3 structures must be ROAD_X, ROAD_Z, ROAD_CROSS");
                    }
                }
                case 4 -> {
                    if (!types.equals(Set.of(BlockType.CORE, BlockType.ROAD_X, BlockType.ROAD_Z, BlockType.ROAD_CROSS))) {
                        throw new IllegalArgumentException("4 structures must be CORE, ROAD_X, ROAD_Z, ROAD_CROSS");
                    }
                }
                default -> throw new IllegalArgumentException("Preset must contain exactly 1, 3, or 4 structures");
            }
        }

        /**
         * 根据类型获取结构
         */
        public PlatformBlockStructure getStructure(BlockType type) {
            for (PlatformBlockStructure s : structures) {
                if (s.type() == type) return s;
            }
            return null;
        }
    }

    /////////////////////////////////////
    // *********** 预设模板 *********** //
    /////////////////////////////////////

    /////////////////////////////////////
    // ********* 平台生成主方法 ********* //
    /////////////////////////////////////
}
