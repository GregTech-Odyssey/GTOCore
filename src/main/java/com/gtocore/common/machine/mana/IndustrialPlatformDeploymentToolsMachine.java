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

import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class IndustrialPlatformDeploymentToolsMachine extends MetaMachine implements IFancyUIMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(IndustrialPlatformDeploymentToolsMachine.class, MetaMachine.MANAGED_FIELD_HOLDER);

    @Persisted
    private final NotifiableItemStackHandler inventory;

    public IndustrialPlatformDeploymentToolsMachine(MetaMachineBlockEntity holder) {
        super(holder);
        inventory = new NotifiableItemStackHandler(this, 81, IO.NONE, IO.BOTH);
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
    /////////////////////////////////////

    // 第零步 - 简介
    // 第一步 - 选择预设
    // 第二步 - 选择偏移 - 显示四角的坐标 - (x方向区块)(Z方向区块)(Y方向)
    // ------- 输入材料 - (高级/中级/初级/基础压缩工业平台组件)
    // 第三步 - 确认放置 - (消耗材料) - (开启放置任务)
    // 第四步 - 返回第一步


    // 通过一些步骤决定平台的生成
    // 第零步 - 简介
    // 第一步 - 选择基础块预览模型/使用自定义模型
    // 第二步 - 是否添加块间的道路 - 选择基础道路预览模型/使用自定义模型
    // 第三步 - 选择层数(最大4)-层间隔(最大10)
    // 第四步 - 选择层间连接柱(是否有)(使用什么方块)(连接柱在块的角处存在)
    // 第五步 - 选择中心底层块的区块偏移与高度偏移，(x方向区块)(Z方向区块)(Y方向)
    // 第六步 - 选择单元拓展数量，(x+方向)(x-方向)(Z+方向)(Z-方向)
    // 第七步 - 是否清除层间的方块和最高层向上数层方块(最大60)
    // 第八步 - 是否填充底层向下的数层方块(最大200) (替换所有空气/流体)
    // 第九步 - 步骤完全检查 信息确认(可修改) - 安全检查 - 清单整理 - 最终确认(锁死)
    // ---------- 检查需求步骤是否全部正确完成 - 出现错误则提示错误信息
    // ---------- 点击按钮确认信息，并锁定，当 step 返回之前步骤时自动解除
    // ---------- 安全检查 确认平台底层与顶层全部在世界高度内 确认操作范围全部在世界内
    // ---------- 平台部署清单 展示全部参数与信息
    // ------------- 使用样式 大小 范围 四角坐标 等等。。。。。
    // ---------- 材料清单(根据方块量转化为需要一定数量的[物质量？][破坏量？]) 混凝土/常见石料/无法回收的建筑方块等等
    // ------------- 采用预设 需消耗 高级/中级/初级/基础压缩工业平台组件 提供[物质量][破坏量]
    // ------------- 或 物质球/奇点 提供[物质量] ---- 区块破坏器*1 + 一定量爆炸物 提供[破坏量]
    // ------------- 自定义部分的无法替换方块仍旧需要提供原方块
    // ---------- 最终确认 - 确认后无法返回需改
    // 第十步 - 材料输入 - 输入需要的材料并消耗 - 点击按钮消耗/订阅每秒自动消耗全部库存
    // ---------- 完成全部后点击开始任务 - 并显示任务进度

    // 当前所处的步骤(0-10)，用于控制流程进度
    @Persisted
    private int step = 0;
    // 总步骤数
    private final int totalStep = 11;

    // 步骤编号常量定义
    private final int Introduction = 0;                // 第零步：简介
    private final int BasicBlockSelection = 1;         // 第一步：基础块选择
    private final int RoadSelection = 2;               // 第二步：块间道路选择
    private final int LayersAndInterval = 3;           // 第三步：层数和间隔
    private final int PillarConfiguration = 4;         // 第四步：层间连接柱
    private final int OffsetConfiguration = 5;         // 第五步：中心底层块偏移
    private final int UnitExpansion = 6;               // 第六步：单元拓展数量
    private final int ClearConfiguration = 7;          // 第七步：清除层间方块
    private final int FillConfiguration = 8;           // 第八步：填充底层下方方块
    private final int FinalConfirmation = 9;           // 第九步：最终确认
    private final int MaterialInput = 10;              // 第十步：材料输入与消耗
    private final int OnWorking = 11;                  // 第十步：运行中

    // 当前页面索引，用于在步骤内分页显示内容
    @Persisted
    private int page = 0;
    // 每个步骤对应的最大页面数，索引对应step
    private final int[] maxPage = { 0, 33, 33, 1, 0, 0, 0, 0, 0, 3, 0, 0 };

    // ------------------- 第一步：基础块选择 -------------------
    // 是否已完成基础块选择
    @Persisted
    private boolean platformConfirm = false;
    // 选择的基础块预设编号(0表示未选择，1为自定义模型，1+表示不同的预设模型)
    @Persisted
    private int platformPresets = 0;

    // ------------------- 第二步：块间道路选择 -------------------
    // 是否已完成道路选择
    @Persisted
    private boolean roadConfirm = false;
    // 是否需要在块间添加道路
    @Persisted
    private boolean needRoad = false;
    // 选择的道路预设编号(0表示未选择，1为自定义模型，1+表示不同的预设模型)
    @Persisted
    private int roadPresets = 0;

    // ------------------- 第三步：层数和间隔 -------------------
    // 是否已完成层数和间隔配置
    @Persisted
    private boolean layersConfirm = false;
    // 平台的总层数(最大4层)
    @Persisted
    private int layers = 0;
    // 每层之间的垂直间隔(从上到下，单位为方块)(layers - 1)
    @Persisted
    private int[] layerInterval;

    // ------------------- 第四步：层间连接柱 -------------------
    // 是否已完成连接柱配置
    @Persisted
    private boolean pillarConfirm = false;
    // 是否需要层间连接柱
    @Persisted
    private boolean needPillar = false;
    // 连接柱预设编号(0表示未选择，1为自定义方块，1+表示不同的预设方块)
    @Persisted
    private int pillarPresets = 0;

    // ------------------- 第五步：中心底层块偏移 -------------------
    // 是否已完成偏移配置
    @Persisted
    private boolean offsetConfirm = false;
    // 中心底层块在X方向的区块偏移
    @Persisted
    private int offsetX = 0;
    // 中心底层块在Z方向的区块偏移
    @Persisted
    private int offsetZ = 0;
    // 中心底层块在Y方向的高度偏移
    @Persisted
    private int offsetY = 0;

    // ------------------- 第六步：单元拓展数量 -------------------
    // 是否已完成拓展配置
    @Persisted
    private boolean expandConfirm = false;
    // 沿X+方向的单元拓展数量
    @Persisted
    private int expandXPos = 0;
    // 沿X-方向的单元拓展数量
    @Persisted
    private int expandXNeg = 0;
    // 沿Z+方向的单元拓展数量
    @Persisted
    private int expandZPos = 0;
    // 沿Z-方向的单元拓展数量
    @Persisted
    private int expandZNeg = 0;

    // ------------------- 第七步：清除层间方块 -------------------
    // 是否已完成清除配置
    @Persisted
    private boolean clearConfirm = false;
    // 是否需要清除层间方块
    @Persisted
    private boolean needClear = false;
    // 清除的高度范围(从最高层向上数的层数，最大60)
    @Persisted
    private int clearHeight = 0;

    // ------------------- 第八步：填充底层下方方块 -------------------
    // 是否已完成填充配置
    @Persisted
    private boolean fillConfirm = false;
    // 是否需要填充底层下方方块
    @Persisted
    private boolean needFill = false;
    // 填充的层数(从底层向下数的层数，最大200)
    @Persisted
    private int fillLayers = 0;
    // 填充使用的方块预设编号(0表示未选择，1为自定义方块，1+表示不同的预设方块)
    @Persisted
    private int fillBlockPresets = 0;

    // ------------------- 第九步：最终确认 -------------------
    // 是否已完成初步确认
    @Persisted
    private boolean preliminaryConfirm = false;
    // 安全检查是否通过(平台是否在世界高度范围内)
    @Persisted
    private boolean safetyCheckPassed = false;
    // 需要的物质量
    @Persisted
    private long substanceAmount = 0;
    // 需要的破环量
    @Persisted
    private long destructionAmount = 0;
    // 需要的材料及数量(键为物品ID，值为数量)
    // @Persisted
    // private Object2IntMap<Item> requiredBlocksMap = new Object2IntOpenHashMap<>();
    // 是否已完成最终确认
    @Persisted
    private boolean finalConfirm = false;

    // ------------------- 第十步：材料输入与消耗 -------------------
    // 材料是否全部输入
    @Persisted
    private boolean blocksEntered = false;
    // 是否启用自动消耗库存材料(每秒自动消耗)
    @Persisted
    private boolean autoConsume = false;

    // ------------------- 第十一步：运行中 -------------------

    /////////////////////////////////////
    // ************ UI组件 ************ //
    /////////////////////////////////////

    int totalLangWidth = 266;
    int langWidth = 266 - 8;

    // 创建UI组件
    @Override
    public Widget createUIWidget() {
        var group = new WidgetGroup(0, 0, 320 + 8, 160 + 8);

        WidgetGroup group_1 = new DraggableScrollableWidgetGroup(4, 4, totalLangWidth, 160)
                .setBackground(GuiTextures.DISPLAY);

        // 步骤标题
        group_1.addWidget(new ComponentPanelWidget(4, 5, this::addDisplayTextTitle)
                .setMaxWidthLimit(langWidth));

        // 步骤选择
        group_1.addWidget(new ComponentPanelWidget(166, 17, this::addDisplayTextStep)
                .clickHandler(this::handleDisplayClickStep));

        // 翻页与页显示
        group_1.addWidget(new ComponentPanelWidget(4, 29, this::addDisplayTextPage)
                .setMaxWidthLimit(langWidth)
                .clickHandler(this::handleDisplayClickPage));

        // 图片展示
        group_1.addWidget(new ImageWidget(166, 60, 100, 100, this::getIGuiTexture));

        // 页面主文本
        group_1.addWidget(new ComponentPanelWidget(4, 29, this::addDisplayText)
                .setMaxWidthLimit(langWidth));

        group.addWidget(group_1);

        // 物品槽
        WidgetGroup group_2 = new DraggableScrollableWidgetGroup(271, 3, 54, 162);
        for (int y = 0; y < 27; y++) {
            for (int x = 0; x < 3; x++) {
                int slotIndex = y * 3 + x;
                group_2.addWidget(new SlotWidget(inventory, slotIndex, x * 18, y * 18, true, true).setBackground(GuiTextures.SLOT));
            }
        }
        group.addWidget(group_2);

        group.setBackground(GuiTextures.BACKGROUND_INVERSE);
        return group;
    }

    // 步骤标题显示
    private void addDisplayTextTitle(List<Component> textList) {
        textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.title." + step));
    }

    // 步骤控制工具
    private void addDisplayTextStep(List<Component> textList) {
        String in_progress = "⏳";
        String completed = "✅";
        MutableComponent result = Component.empty();
        for (int i = 0; i <= totalStep; i++) {
            if (step == i) result.append(ComponentPanelWidget.withButton(Component.literal("§b" + (confirmStep(i) ? completed : in_progress) + "§r"), "step_" + i));
            else result.append(ComponentPanelWidget.withButton(Component.literal(confirmStep(i) ? completed : in_progress), "step_" + i));
        }
        textList.add(result);
    }

    private void handleDisplayClickStep(String componentData, ClickData clickData) {
        String[] parts = componentData.split("_", 2);
        if (parts[0].equals("step")) {
            step = Mth.clamp(Integer.parseInt(parts[1]), 0, totalStep);
            page = 0;
        }
    }

    // 页控制工具
    private void addDisplayTextPage(List<Component> textList) {
        if (maxPage[step] != 0) {
            textList.add(createPageNavigation(langWidth, "<" + (page + 1) + "/" + (maxPage[step] + 1) + ">"));
        }
    }

    private void handleDisplayClickPage(String componentData, ClickData clickData) {
        page = Mth.clamp(page + ("next_page".equals(componentData) ? 1 : -1), 0, maxPage[step]);
    }

    // 获得图片
    private IGuiTexture getIGuiTexture() {
        if (step == Introduction && page != 0) {
            ResourceLocation imageLocation = GTOCore.id("textures/gui/industrial_platform_deployment_tools/template_" + page + ".png");
            return new ResourceTexture(imageLocation);
        }
        return IGuiTexture.EMPTY;
    }

    // 页面主文本
    private void addDisplayText(List<Component> textList) {
        if (maxPage[step] != 0) textList.add(Component.literal(""));
        textList.add(Component.literal("步骤" + step));
        switch (step) {
            case 0 -> GTOMachineTooltips.INSTANCE.getIndustrialPlatformDeploymentToolsIntroduction().apply(textList);
            case 1 -> {
                if (page == 0){

                }
                textList.add(Component.literal("步骤" + step));
                textList.add(Component.literal("步骤" + step));
            }
        }
    }

    private void handleDisplayClickText(String componentData, ClickData clickData) {

    }

    /////////////////////////////////////
    // ********** UI逻辑工具 ********** //
    /////////////////////////////////////

    // 步骤完成检查工具
    private boolean confirmStep(int step) {
        boolean confirm = false;
        switch (step) {
            case 0 -> confirm = true;
            case 1 -> confirm = platformConfirm;
            case 2 -> confirm = roadConfirm;
            case 3 -> confirm = layersConfirm;
            case 4 -> confirm = pillarConfirm;
            case 5 -> confirm = offsetConfirm;
            case 6 -> confirm = expandConfirm;
            case 7 -> confirm = clearConfirm;
            case 8 -> confirm = fillConfirm;
            case 9 -> confirm = finalConfirm;
            case 10 -> confirm = blocksEntered;
        }
        return confirm;
    }

    /////////////////////////////////////
    // ********** UI布局工具 ********** //
    /////////////////////////////////////

    // 翻页与页标题
    private static Component createPageNavigation(int totalWidth, String title) {
        Font font = Minecraft.getInstance().font;

        Component leftBtn = ComponentPanelWidget.withButton(Component.literal(" [ ← ] "), "previous_page");
        Component rightBtn = ComponentPanelWidget.withButton(Component.literal(" [ → ] "), "next_page");

        Component leftText = Component.translatable("gtocore.machine.previous_page");
        Component rightText = Component.translatable("gtocore.machine.next_page");
        Component titleComp = Component.translatable(title);

        int leftBtnWidth = font.width(leftBtn);
        int leftTextWidth = font.width(leftText);
        int rightBtnWidth = font.width(rightBtn);
        int rightTextWidth = font.width(rightText);
        int titleWidth = font.width(titleComp);

        int leftSegmentWidth = leftBtnWidth + leftTextWidth;
        int rightSegmentWidth = rightTextWidth + rightBtnWidth;

        int middleSpace = totalWidth - leftSegmentWidth - titleWidth - rightSegmentWidth;
        int leftSpace = middleSpace / 2;
        int rightSpace = middleSpace - leftSpace;

        int spacePixel = font.width(" ");

        Component leftPad = Component.literal(" ".repeat(spacePixel > 0 ? leftSpace / spacePixel : leftSpace));
        Component rightPad = Component.literal(" ".repeat(spacePixel > 0 ? rightSpace / spacePixel : rightSpace));

        MutableComponent result = Component.empty();
        result = result.append(leftBtn);
        result = result.append(leftText);
        result = result.append(leftPad);
        result = result.append(titleComp);
        result = result.append(rightPad);
        result = result.append(rightText);
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
    // ******** 模板存储读取工具 ******** //
    /////////////////////////////////////

    /////////////////////////////////////
    // *********** 基础模板 *********** //
    /////////////////////////////////////

    /////////////////////////////////////
    // ********* 自定义模板解析 ********* //
    /////////////////////////////////////

    /////////////////////////////////////
    // ********** 材料计算工具 ********** //
    /////////////////////////////////////

    /////////////////////////////////////
    // ********* 分单元放置方法 ********* //
    /////////////////////////////////////

    /////////////////////////////////////
    // ********* 平台生成主方法 ********* //
    /////////////////////////////////////
}
