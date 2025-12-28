package com.gtocore.common.machine.noenergy.PlatformDeployment;

import com.gtocore.client.forge.ForgeClientEvent;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.translation.GTOMachineTooltips;

import com.gtolib.GTOCore;
import com.gtolib.api.network.NetworkPack;
import com.gtolib.utils.ServerUtils;
import com.gtolib.utils.holder.IntObjectHolder;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.gtocore.common.item.CoordinateCardBehavior.getStoredCoordinates;
import static com.gtocore.common.machine.noenergy.PlatformDeployment.PlatformCreators.PlatformCreationAsync;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PlatformDeploymentMachine extends MetaMachine implements IFancyUIMachine, IMachineLife {

    private static final NetworkPack HIGHLIGHT_REGION = NetworkPack.registerS2C("platformDeploymentMachineHighlight", (p, b) -> {
        var dimension = b.readResourceKey(Registries.DIMENSION);
        var start = b.readBlockPos();
        var end = b.readBlockPos();
        var color = b.readInt();
        var durationTicks = b.readInt();
        ForgeClientEvent.highlightRegion(dimension, start, end, color, durationTicks);
    });

    private static final NetworkPack STOP_HIGHLIGHT = NetworkPack.registerS2C("platformDeploymentMachineStopHighlight", (p, b) -> {
        var start = b.readBlockPos();
        var end = b.readBlockPos();
        ForgeClientEvent.stopHighlight(start, end);
    });

    public static void highlightRegion(ResourceKey<Level> dimension, BlockPos start, BlockPos end, int color, int durationTicks) {
        HIGHLIGHT_REGION.send(buf -> {
            buf.writeResourceKey(dimension);
            buf.writeBlockPos(start);
            buf.writeBlockPos(end);
            buf.writeInt(color);
            buf.writeInt(durationTicks);
        }, ServerUtils.getServer());
    }

    public static void stopHighlight(BlockPos start, BlockPos end) {
        STOP_HIGHLIGHT.send(buf -> {
            buf.writeBlockPos(start);
            buf.writeBlockPos(end);
        }, ServerUtils.getServer());
    }

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
    public void onLoad() {
        super.onLoad();
        inventory.notifyListeners();
        posChanged();
    }

    @Override
    public void onUnload() {
        super.onUnload();
    }

    @Override
    public void onMachineRemoved() {
        unloadingMaterial();
        clearInventory(inventory.storage);
    }

    /////////////////////////////////////
    // *********** 信息存储 *********** //
    /// //////////////////////////////////

    // 当前所处的步骤
    private int step = 0;

    // 总步骤数
    private static final int totalStep = 3;

    // 步骤编号常量定义
    private static final int Introduction = 0;        // 第零步：简介
    private static final int PresetSelection = 1;     // 第一步：选择预设
    private static final int ConfirmConsumables = 2;  // 第二步：确认耗材
    private static final int AdjustSettings = 3;      // 第三步：调整设置

    // ------------------- 第一步：选择预设 -------------------
    // 是否已完成预设选择
    @Persisted
    private boolean presetConfirm = false;
    // 当前查看的预设组索引
    @Persisted
    private int checkGroup = 0;
    // 显示的预设编号
    @Persisted
    private int checkId = 0;
    // 保存的预设组编号
    @Persisted
    private int saveGroup = 0;
    // 保存的预设编号
    @Persisted
    private int saveId = 0;
    // 是否显示预览
    @Persisted
    private boolean preview = false;
    // 是否高亮
    @Persisted
    private boolean highlight = false;

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
    // 坐标点
    @Persisted
    private BlockPos pos1 = new BlockPos(0, 0, 0);
    @Persisted
    private BlockPos pos2 = new BlockPos(0, 0, 0);

    // ------------------- 第三步：确认放置 -------------------
    // 库存的原料量
    @Persisted
    private final int[] materialInventory = new int[] { 0, 0, 0 };
    // 库存是否充足
    @Persisted
    private boolean insufficient = false;
    // 原料物品
    private static final List<List<IntObjectHolder<Item>>> ITEM_VALUE_HOLDERS = List.of(
            List.of(
                    new IntObjectHolder<>(5000, GTOItems.INDUSTRIAL_COMPONENTS[0][2].asItem()),
                    new IntObjectHolder<>(1000, GTOItems.INDUSTRIAL_COMPONENTS[0][1].asItem()),
                    new IntObjectHolder<>(200, GTOItems.INDUSTRIAL_COMPONENTS[0][0].asItem())),
            List.of(
                    new IntObjectHolder<>(5000, GTOItems.INDUSTRIAL_COMPONENTS[1][2].asItem()),
                    new IntObjectHolder<>(1000, GTOItems.INDUSTRIAL_COMPONENTS[1][1].asItem()),
                    new IntObjectHolder<>(200, GTOItems.INDUSTRIAL_COMPONENTS[1][0].asItem())),
            List.of(
                    new IntObjectHolder<>(5000, GTOItems.INDUSTRIAL_COMPONENTS[2][2].asItem()),
                    new IntObjectHolder<>(1000, GTOItems.INDUSTRIAL_COMPONENTS[2][1].asItem()),
                    new IntObjectHolder<>(200, GTOItems.INDUSTRIAL_COMPONENTS[2][0].asItem())));

    // ------------------- 第四步：运行中 -------------------
    // 任务是否完成
    @Persisted
    private boolean taskCompleted = true;
    // 跳过空气
    @Persisted
    private boolean skipAir = true;
    // 光照更新
    @Persisted
    private boolean updateLight = true;
    // 速度
    @Persisted
    private int speed = 50;
    // X轴对称
    @Persisted
    private boolean xMirror = false;
    // Z轴对称
    @Persisted
    private boolean zMirror = false;
    // Y轴旋转
    @Persisted
    private int rotation = 0;
    // 可导出
    @Persisted
    private boolean canExport = false;

    private int progress = 0;

    /////////////////////////////////////
    // ************ UI组件 ************ //
    /////////////////////////////////////
    private static final int langWidth = 282 - 8;

    // 创建UI组件
    @Override
    public Widget createUIWidget() {
        int width = 336;
        int height = 160;
        var group = new WidgetGroup(0, 0, width + 8, height + 8);

        // 步骤标题
        int totalLangWidth = 282;

        WidgetGroup group_title = new DraggableScrollableWidgetGroup(4, 4, totalLangWidth, height)
                .setBackground(GuiTextures.DISPLAY);
        group_title.addWidget(new ComponentPanelWidget(4, 5, this::addDisplayTextTitle));
        group.addWidget(group_title);

        // 主页
        DraggableScrollableWidgetGroup mainContentGroup = new DraggableScrollableWidgetGroup(4, 20, totalLangWidth, height - 16);

        mainContentGroup.addWidget(new ComponentPanelWidget(4, 0, this::addDisplayText)
                .clickHandler(this::handleDisplayClick)
                .setMaxWidthLimit(langWidth));

        mainContentGroup.addWidget(new ComponentPanelWidget(4 + langWidth / 2, 0, this::addDisplayText2)
                .clickHandler(this::handleDisplayClick)
                .setMaxWidthLimit(langWidth / 2));

        mainContentGroup.addWidget(new ComponentPanelWidget(4 + langWidth / 4, 0, this::addDisplayText3)
                .clickHandler(this::handleDisplayClick)
                .setMaxWidthLimit(langWidth / 4 * 3));

        mainContentGroup.addWidget(new ComponentPanelWidget(4 + langWidth / 4 * 3, 0, this::addDisplayText4)
                .clickHandler(this::handleDisplayClick)
                .setMaxWidthLimit(langWidth / 4));

        mainContentGroup.addWidget(new ComponentPanelWidget(4 + 88, 0, this::addDisplayText5)
                .clickHandler(this::handleDisplayClick)
                .setMaxWidthLimit(langWidth / 2));

        mainContentGroup.addWidget(new ComponentPanelWidget(4 + langWidth / 2, 0, this::addDisplayText6)
                .clickHandler(this::handleDisplayClick)
                .setMaxWidthLimit(langWidth / 2));

        mainContentGroup.addWidget(new ComponentPanelWidget(4 + langWidth - 62, 0, this::addDisplayText7)
                .clickHandler(this::handleDisplayClick)
                .setMaxWidthLimit(langWidth / 4));

        mainContentGroup.addWidget(new ImageWidget(totalLangWidth - 100, 46, 100, 100, this::getIGuiTexture));

        group.addWidget(mainContentGroup);

        // 启动区
        WidgetGroup group_start = new DraggableScrollableWidgetGroup(width - 49, 4, 54, 105)
                .setBackground(GuiTextures.CLIPBOARD_PAPER_BACKGROUND);
        group_start.addWidget(new ComponentPanelWidget(8, 4, this::addDisplayTextStep)
                .clickHandler((a, b) -> handleDisplayClickStep(a, mainContentGroup)));
        group_start.addWidget(new ComponentPanelWidget(8, 20, this::addDisplayTextStart)
                .clickHandler(this::handleDisplayClickStart)
                .setMaxWidthLimit(38));
        group.addWidget(group_start);

        // 物品槽
        WidgetGroup group_slot = new DraggableScrollableWidgetGroup(width - 49, 110, 54, 54);
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                int slotIndex = y * 3 + x;
                group_slot.addWidget(new SlotWidget(inventory, slotIndex, x * 18, y * 18, true, true)
                        .setBackground(GuiTextures.SLOT));
            }
        }
        group.addWidget(group_slot);

        group.setBackground(GuiTextures.BACKGROUND_INVERSE);
        return group;
    }

    private static final Component empty = Component.empty();

    // 步骤标题显示
    private void addDisplayTextTitle(List<Component> textList) {
        textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.title." + step));
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

    private void handleDisplayClickStep(String componentData, DraggableScrollableWidgetGroup group) {
        String[] parts = componentData.split("_", 2);
        if (parts[0].equals("step")) {
            group.setScrollYOffset(0);
            step = Mth.clamp(Integer.parseInt(parts[1]), 0, totalStep);
        }
    }

    // 页面主文本
    private void addDisplayText(List<Component> textList) {
        switch (step) {
            case Introduction -> GTOMachineTooltips.INSTANCE.getIndustrialPlatformDeploymentToolsIntroduction().apply(textList);
            case PresetSelection -> {
                PlatformBlockType.PlatformPreset group = getPlatformPreset(checkGroup);
                PlatformBlockType.PlatformBlockStructure structure = getPlatformBlockStructure(checkGroup, checkId);

                Component leftBtn1 = ComponentPanelWidget.withButton(Component.literal(" [ ← ] "), "previous_group_plas");
                Component leftBtn2 = ComponentPanelWidget.withButton(Component.literal(" [ ← ] "), "previous_group");
                Component empty1 = Component.literal(" ".repeat(15 - ((checkGroup + 1) / 10 + maxGroup / 10 + 5) / 2));
                textList.add(Component.empty().append(leftBtn1).append(leftBtn2).append(empty1).append(Component.literal("<" + (checkGroup + 1) + "/" + maxGroup + ">")));

                int totalIds = getPlatformPreset(checkGroup).structures().size();
                Component leftBtn3 = ComponentPanelWidget.withButton(Component.literal(" [ ← ] "), "previous_id_plas");
                Component leftBtn4 = ComponentPanelWidget.withButton(Component.literal(" [ ← ] "), "previous_id");
                Component empty2 = Component.literal(" ".repeat(15 - ((checkId + 1) / 10 + totalIds / 10 + 5) / 2));
                textList.add(Component.empty().append(leftBtn3).append(leftBtn4).append(empty2).append(Component.literal("<" + (checkId + 1) + "/" + totalIds + ">")));

                textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.text.choose_this")
                        .append(ComponentPanelWidget.withButton(Component.literal("[§b⭕§r]"), "choose_this")));

                textList.add(structure.preview() ? Component.translatable("gtocore.machine.industrial_platform_deployment_tools.text.preview")
                        .append(ComponentPanelWidget.withButton(Component.literal("[§b🖼§r]"), "preview")) : empty);
                textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.text.size", structure.xSize(), structure.ySize(), structure.zSize(),
                        structure.xSize() >> 4, structure.zSize() >> 4));

                String displayName = group.displayName();
                String description = group.description();
                String source = group.source();
                if (displayName != null) textList.add(Component.translatable(displayName));
                if (description != null) textList.add(Component.translatable(description));
                if (source != null) textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.text.source", source));

                String structDisplayName = structure.displayName();
                String type = structure.type();
                String structDescription = structure.description();
                String structSource = structure.source();
                if (structDisplayName != null) textList.add(Component.translatable(structDisplayName));
                if (type != null) textList.add(Component.translatable(type));
                if (structDescription != null) textList.add(Component.translatable(structDescription));
                if (structSource != null) textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.text.source", structSource));
            }
            case ConfirmConsumables -> {
                textList.add(ComponentPanelWidget.withButton(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.material.loading"), "loading"));

                textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.material.reserves"));
                textList.add(empty);

                if (!presetConfirm) textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.text.unselected"));
                else {
                    PlatformBlockType.PlatformBlockStructure structure = getPlatformBlockStructure(saveGroup, saveId);

                    textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.material.demand"));
                    textList.add(empty);

                    List<IntObjectHolder<ItemStack>> extraMaterials = structure.extraMaterials();
                    if (!extraMaterials.isEmpty()) {
                        textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.material.extra_demand"));
                        extraMaterials.forEach(e -> textList.add(
                                Component.literal("[").append(e.obj.getDisplayName()).append("×").append(String.valueOf(e.number)).append("]")));
                    }
                    if (!insufficient) textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.material.insufficient"));
                    else textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.material.adequate"));
                }
            }
            case AdjustSettings -> {

                Component x_offset = Component.translatable("gtocore.machine.industrial_platform_deployment_tools.offset.x", offsetX);
                Component y_offset = Component.translatable("gtocore.machine.industrial_platform_deployment_tools.offset.y", offsetY);
                Component z_offset = Component.translatable("gtocore.machine.industrial_platform_deployment_tools.offset.z", offsetZ);

                textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.offset"));
                textList.add(x_offset);
                textList.add(y_offset);
                textList.add(z_offset);

                textList.add(empty);

                if (!presetConfirm) textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.text.unselected"));
                else {
                    textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.boundary"));

                    textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.offset.x", pos2.getX()));
                    textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.offset.y", pos2.getY()));
                    textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.offset.z", pos2.getZ()));
                }
                textList.add(empty);
                textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.skipAir")
                        .append(ComponentPanelWidget.withButton(skipAir ?
                                Component.translatable("gtocore.machine.on") :
                                Component.translatable("gtocore.machine.off"), "skipAir")));

                textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.xMirror")
                        .append(ComponentPanelWidget.withButton(xMirror ?
                                Component.translatable("gtocore.machine.on") :
                                Component.translatable("gtocore.machine.off"), "xMirror")));

                textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.rotation")
                        .append(ComponentPanelWidget.withButton(
                                Component.literal(String.valueOf(rotation)), "rotation")));
            }
        }
    }

    private void addDisplayText2(List<Component> textList) {
        switch (step) {
            case PresetSelection -> {
                textList.add(empty);
                textList.add(empty);
                textList.add(presetConfirm ?
                        Component.translatable("gtocore.machine.industrial_platform_deployment_tools.text.selected", saveGroup + 1, saveId + 1) :
                        Component.translatable("gtocore.machine.industrial_platform_deployment_tools.text.unselected"));

                textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.text.highlight")
                        .append(ComponentPanelWidget.withButton(Component.literal("[§b🔆§r]"), "highlight")));

            }
            case ConfirmConsumables -> {
                textList.add(ComponentPanelWidget.withButton(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.material.unloading"), "unloading"));

                textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.material.1"));
                textList.add(Component.literal(String.valueOf(materialInventory[1])));

                if (!presetConfirm) textList.add(empty);
                else {
                    PlatformBlockType.PlatformBlockStructure structure = getPlatformBlockStructure(saveGroup, saveId);

                    int[] costMaterial = structure.materials();
                    textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.material.1"));
                    textList.add(Component.literal(String.valueOf(costMaterial[1])));
                }
            }
            case AdjustSettings -> {
                textList.add(empty);

                textList.add(empty);
                textList.add(empty);
                textList.add(empty);

                textList.add(empty);

                if (!presetConfirm) textList.add(empty);
                else {
                    textList.add(empty);

                    textList.add(Component.literal(String.valueOf(pos1.getX())));
                    textList.add(Component.literal(String.valueOf(pos1.getY())));
                    textList.add(Component.literal(String.valueOf(pos1.getZ())));
                }
                textList.add(empty);
                textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.updateLight")
                        .append(ComponentPanelWidget.withButton(updateLight ?
                                Component.translatable("gtocore.machine.on") :
                                Component.translatable("gtocore.machine.off"), "updateLight")));

                textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.zMirror")
                        .append(ComponentPanelWidget.withButton(zMirror ?
                                Component.translatable("gtocore.machine.on") :
                                Component.translatable("gtocore.machine.off"), "zMirror")));

                textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.speed")
                        .append(String.valueOf(speed))
                        .append(ComponentPanelWidget.withButton(Component.literal("[+]"), "+speed"))
                        .append(ComponentPanelWidget.withButton(Component.literal("[-]"), "-speed")));
            }
        }
    }

    private void addDisplayText3(List<Component> textList) {
        if (step == ConfirmConsumables) {
            textList.add(empty);

            textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.material.0"));
            textList.add(Component.literal(String.valueOf(materialInventory[0])));

            if (!presetConfirm) textList.add(empty);
            else {
                PlatformBlockType.PlatformBlockStructure structure = getPlatformBlockStructure(saveGroup, saveId);

                int[] costMaterial = structure.materials();
                textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.material.0"));
                textList.add(Component.literal(String.valueOf(costMaterial[0])));
            }
        }
    }

    private void addDisplayText4(List<Component> textList) {
        if (step == ConfirmConsumables) {
            textList.add(empty);

            textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.material.2"));
            textList.add(Component.literal(String.valueOf(materialInventory[2])));

            if (!presetConfirm) textList.add(empty);
            else {
                PlatformBlockType.PlatformBlockStructure structure = getPlatformBlockStructure(saveGroup, saveId);

                int[] costMaterial = structure.materials();
                textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.material.2"));
                textList.add(Component.literal(String.valueOf(costMaterial[2])));
            }
        }
    }

    private void addDisplayText5(List<Component> textList) {
        if (step == AdjustSettings) {
            textList.add(empty);

            Component X_change_1 = Component.empty()
                    .append(ComponentPanelWidget.withButton(Component.literal("[-]"), "x_minus"))
                    .append(ComponentPanelWidget.withButton(Component.literal("[+]"), "x_add"));
            Component Y_change_1 = Component.empty()
                    .append(ComponentPanelWidget.withButton(Component.literal("[-]"), "y_minus"))
                    .append(ComponentPanelWidget.withButton(Component.literal("[+]"), "y_add"));
            Component Z_change_1 = Component.empty()
                    .append(ComponentPanelWidget.withButton(Component.literal("[-]"), "z_minus"))
                    .append(ComponentPanelWidget.withButton(Component.literal("[+]"), "z_add"));

            textList.add(X_change_1);
            textList.add(Y_change_1);
            textList.add(Z_change_1);
        }
    }

    private void addDisplayText6(List<Component> textList) {
        if (step == AdjustSettings) {
            textList.add(empty);

            Component X_change_2 = Component.empty()
                    .append(ComponentPanelWidget.withButton(Component.literal("[-]"), "adjust_x_minus"))
                    .append(ComponentPanelWidget.withButton(Component.literal("[-" + adjustX + "]"), "x_minus_plas"))
                    .append(ComponentPanelWidget.withButton(Component.literal("[+" + adjustX + "]"), "x_add_plas"))
                    .append(ComponentPanelWidget.withButton(Component.literal("[+]"), "adjust_x_add"));
            Component Y_change_2 = Component.empty()
                    .append(ComponentPanelWidget.withButton(Component.literal("[-]"), "adjust_y_minus"))
                    .append(ComponentPanelWidget.withButton(Component.literal("[-" + adjustY + "]"), "y_minus_plas"))
                    .append(ComponentPanelWidget.withButton(Component.literal("[+" + adjustY + "]"), "y_add_plas"))
                    .append(ComponentPanelWidget.withButton(Component.literal("[+]"), "adjust_y_add"));
            Component Z_change_2 = Component.empty()
                    .append(ComponentPanelWidget.withButton(Component.literal("[-]"), "adjust_z_minus"))
                    .append(ComponentPanelWidget.withButton(Component.literal("[-" + adjustZ + "]"), "z_minus_plas"))
                    .append(ComponentPanelWidget.withButton(Component.literal("[+" + adjustZ + "]"), "z_add_plas"))
                    .append(ComponentPanelWidget.withButton(Component.literal("[+]"), "adjust_z_add"));

            textList.add(X_change_2);
            textList.add(Y_change_2);
            textList.add(Z_change_2);
        }
    }

    private void addDisplayText7(List<Component> textList) {
        if (step == PresetSelection) {
            Component rightBtn1 = ComponentPanelWidget.withButton(Component.literal(" [ → ] "), "next_group");
            Component rightBtn2 = ComponentPanelWidget.withButton(Component.literal(" [ → ] "), "next_group_plas");
            textList.add(Component.empty().append(rightBtn1).append(rightBtn2));

            Component rightBtn3 = ComponentPanelWidget.withButton(Component.literal(" [ → ] "), "next_id");
            Component rightBtn4 = ComponentPanelWidget.withButton(Component.literal(" [ → ] "), "next_id_plas");
            textList.add(Component.empty().append(rightBtn3).append(rightBtn4));
        }
    }

    private void handleDisplayClick(String componentData, ClickData clickData) {
        switch (step) {
            case PresetSelection -> {
                int maxId = getPlatformPreset(checkGroup).structures().size() - 1;
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
                        posChanged();
                    }
                    case "preview" -> preview = !preview;
                    case "highlight" -> {
                        highlight = !highlight;
                        highlightArea(highlight);
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
                    case "x_add" -> {
                        offsetX++;
                        posChanged();
                    }
                    case "x_minus" -> {
                        offsetX--;
                        posChanged();
                    }
                    case "z_add" -> {
                        offsetZ++;
                        posChanged();
                    }
                    case "z_minus" -> {
                        offsetZ--;
                        posChanged();
                    }
                    case "y_add" -> {
                        offsetY++;
                        posChanged();
                    }
                    case "y_minus" -> {
                        offsetY--;
                        posChanged();
                    }

                    case "x_add_plas" -> {
                        offsetX += adjustX;
                        posChanged();
                    }
                    case "x_minus_plas" -> {
                        offsetX -= adjustX;
                        posChanged();
                    }
                    case "adjust_x_add" -> adjustX = Math.max(0, adjustX + 1);
                    case "adjust_x_minus" -> adjustX = Math.max(0, adjustX - 1);

                    case "z_add_plas" -> {
                        offsetZ += adjustZ;
                        posChanged();
                    }
                    case "z_minus_plas" -> {
                        offsetZ -= adjustZ;
                        posChanged();
                    }
                    case "adjust_z_add" -> adjustZ = Math.max(0, adjustZ + 1);
                    case "adjust_z_minus" -> adjustZ = Math.max(0, adjustZ - 1);

                    case "y_add_plas" -> {
                        offsetY += adjustY;
                        posChanged();
                    }
                    case "y_minus_plas" -> {
                        offsetY -= adjustY;
                        posChanged();
                    }
                    case "adjust_y_add" -> adjustY = Math.max(0, adjustY + 1);
                    case "adjust_y_minus" -> adjustY = Math.max(0, adjustY - 1);

                    case "skipAir" -> skipAir = !skipAir;
                    case "updateLight" -> updateLight = !updateLight;
                    case "xMirror" -> xMirror = !xMirror;
                    case "zMirror" -> zMirror = !zMirror;
                    case "rotation" -> rotation = (rotation + 90) % 360;
                    case "+speed" -> speed = Mth.clamp(speed + 5, 10, 100);
                    case "-speed" -> speed = Mth.clamp(speed - 5, 10, 100);
                }
            }
        }
    }

    private IGuiTexture getIGuiTexture() {
        if (step == PresetSelection && preview) {
            PlatformBlockType.PlatformBlockStructure structure = getPlatformBlockStructure(checkGroup, checkId);
            if (!structure.preview()) return IGuiTexture.EMPTY;
            String pngs = structure.name() + ".png";
            ResourceLocation imageLocation = GTOCore.id("textures/gui/industrial_platform_deployment_tools/" + pngs);
            return new ResourceTexture(imageLocation);
        }
        return IGuiTexture.EMPTY;
    }

    // 启动控制工具
    private void addDisplayTextStart(List<Component> textList) {
        if (canExport) textList.add(ComponentPanelWidget.withButton(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.export"), "export"));
        else if (!presetConfirm) {
            textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.text.unselected"));
        } else {
            if (!insufficient) {
                textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.material.insufficient"));
            } else {
                if (!taskCompleted) {
                    textList.add(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.doing", progress));
                } else {
                    textList.add(ComponentPanelWidget.withButton(Component.translatable("gtocore.machine.industrial_platform_deployment_tools.start"), "start"));
                }
            }
        }
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
        Component leftBtn = ComponentPanelWidget.withButton(Component.literal(" [ ← ] "), "previous_" + string);
        Component rightBtn = ComponentPanelWidget.withButton(Component.literal(" [ → ] "), "next_" + string);
        int middleSpace = totalWidth - 60 - widthOfString(titleComp);
        if (middleSpace <= 0) return Component.empty().append(leftBtn).append(titleComp).append(rightBtn);
        int leftSpace = middleSpace / 2;
        int rightSpace = middleSpace - leftSpace;
        int spacePixel = 6;
        Component leftPad = Component.literal(" ".repeat(leftSpace / spacePixel));
        Component rightPad = Component.literal(" ".repeat(rightSpace / spacePixel));
        return Component.empty().append(leftBtn).append(leftPad).append(titleComp).append(rightPad).append(rightBtn);
    }

    // 自动分栏
    private static Component createEqualColumns(int totalWidth, Component... components) {
        if (components.length == 0) return Component.empty();
        int columnCount = components.length;
        int baseColWidth = totalWidth / columnCount;
        int remainder = totalWidth % columnCount;
        MutableComponent result = Component.empty();
        for (int i = 0; i < columnCount; i++) {
            Component col = components[i];
            int spacePixel = charWidth;
            int padPixels = (baseColWidth + (i == columnCount - 1 ? remainder : 0)) - widthOfString(col);
            result = result.append(col);
            if (padPixels > 0 && spacePixel > 0) {
                result = result.append(Component.literal(" ".repeat(padPixels / spacePixel)));
            }
        }
        return result;
    }

    private static final int charWidth = 6;

    private static int widthOfString(Component component) {
        return component.getString().length() * charWidth;
    }

    // 自动居中
    private static Component centerComponent(int totalWidth, Component component) {
        int contentWidth = charWidth;
        if (contentWidth >= totalWidth) return component;
        int leftSpace = (totalWidth - contentWidth) / 2;
        int rightSpace = totalWidth - contentWidth - leftSpace;
        Component leftPad = Component.literal(" ".repeat(leftSpace / charWidth));
        Component rightPad = Component.literal(" ".repeat(rightSpace / charWidth));
        return Component.empty().append(leftPad).append(component).append(rightPad);
    }

    /////////////////////////////////////
    // *********** 辅助方法 *********** //
    /////////////////////////////////////

    private PlatformBlockType.PlatformPreset getPlatformPreset(int group) {
        try {
            return presets.get(group);
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            checkGroup = 0;
            saveGroup = 0;
            return presets.getFirst();
        }
    }

    private PlatformBlockType.PlatformBlockStructure getPlatformBlockStructure(int group, int id) {
        try {
            return getPlatformPreset(group).structures().get(id);
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            checkId = 0;
            saveId = 0;
            return getPlatformPreset(group).structures().getFirst();
        }
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

    private void posChanged() {
        BlockPos pos = getPos();
        if (highlight) {
            highlight = false;
            highlightArea(false);
        }
        PlatformBlockType.PlatformBlockStructure structure = getPlatformBlockStructure(saveGroup, saveId);

        int sizeX = structure.xSize();
        int sizeZ = structure.zSize();
        int sizeY = structure.ySize();

        int chunkMinX = (pos.getX() >> 4) << 4;
        int chunkMinZ = (pos.getZ() >> 4) << 4;

        int centerOffsetX = (sizeX - 1) / 32;
        int centerOffsetZ = (sizeZ - 1) / 32;

        int startX = chunkMinX - centerOffsetX * 16 + offsetX * 16;
        int startZ = chunkMinZ - centerOffsetZ * 16 + offsetZ * 16;
        int startY = pos.getY() + offsetY;

        int maxX = startX + sizeX - 1;
        int maxZ = startZ + sizeZ - 1;
        int maxY = startY + sizeY - 1;

        pos1 = new BlockPos(startX, startY, startZ);
        pos2 = new BlockPos(maxX, maxY, maxZ);
    }

    private void examineMaterial() {
        if (!presetConfirm) {
            insufficient = false;
            return;
        }
        PlatformBlockType.PlatformBlockStructure structure = getPlatformBlockStructure(saveGroup, saveId);
        int[] costMaterial = structure.materials();
        boolean materialsSufficient = true;
        canExport = false;
        for (int i = 0; i < materialInventory.length; i++) {
            if (materialInventory[i] < costMaterial[i]) {
                materialsSufficient = false;
                break;
            }
        }
        List<IntObjectHolder<ItemStack>> extraMaterials = structure.extraMaterials();
        Map<Item, Integer> inventoryCount = new HashMap<>();
        int coordinateCards = 0;
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            inventoryCount.put(stack.getItem(), inventoryCount.getOrDefault(stack.getItem(), 0) + stack.getCount());
            if (stack.getItem() == GTOItems.COORDINATE_CARD.asItem()) {
                if (coordinateCards == 0) pos1 = getStoredCoordinates(stack);
                else pos2 = getStoredCoordinates(stack);
                coordinateCards++;
            }
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
        PlatformBlockType.PlatformBlockStructure structure = getPlatformBlockStructure(saveGroup, saveId);
        int[] costMaterial = structure.materials();
        for (int i = 0; i < materialInventory.length; i++) materialInventory[i] -= costMaterial[i];
        List<IntObjectHolder<ItemStack>> extraMaterials = structure.extraMaterials();
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

    private static int[] transform(int lx, int ly, int lz, int sx, int sz, int rot, boolean zMir, boolean xMir) {
        int rx = lx, rz = lz;
        switch (rot) {
            case 90 -> {
                int t = rx;
                rx = sz - 1 - rz;
                rz = t;
            }
            case 180 -> {
                rx = sx - 1 - rx;
                rz = sz - 1 - rz;
            }
            case 270 -> {
                int t = rx;
                rx = rz;
                rz = sx - 1 - t;
            }
        }
        if (xMir) rx = sx - 1 - rx;
        if (zMir) rz = sz - 1 - rz;
        return new int[] { rx, ly, rz };
    }

    private static int[] calcOffsetsBy8Points(int sx, int sy, int sz, int rot, boolean zMir, boolean xMir) {
        int[][] corners = { { 0, 0, 0 }, { sx - 1, 0, 0 }, { 0, sy - 1, 0 }, { sx - 1, sy - 1, 0 }, { 0, 0, sz - 1 }, { sx - 1, 0, sz - 1 }, { 0, sy - 1, sz - 1 }, { sx - 1, sy - 1, sz - 1 } };
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, minZ = Integer.MAX_VALUE;
        for (int[] c : corners) {
            int[] t = transform(c[0], c[1], c[2], sx, sz, rot, zMir, xMir);
            minX = Math.min(minX, t[0]);
            minY = Math.min(minY, t[1]);
            minZ = Math.min(minZ, t[2]);
        }
        return new int[] { -minX, -minY, -minZ };
    }

    private void highlightArea(boolean light) {
        if (!(getLevel() instanceof ServerLevel)) return;
        ResourceKey<Level> dimension = getLevel().dimension();

        if (canExport) {
            BlockPos p1 = null, p2 = null;
            for (int i = 0; i < inventory.getSize() && (p1 == null || p2 == null); i++) {
                ItemStack stack = inventory.getStackInSlot(i);
                if (stack.is(GTOItems.COORDINATE_CARD.asItem())) {
                    if (p1 == null) p1 = getStoredCoordinates(stack);
                    else p2 = getStoredCoordinates(stack);
                }
            }
            if (p1 != null && p2 != null) {
                BlockPos min = new BlockPos(Math.min(p1.getX(), p2.getX()), Math.min(p1.getY(), p2.getY()), Math.min(p1.getZ(), p2.getZ()));
                BlockPos max = new BlockPos(Math.max(p1.getX(), p2.getX()), Math.max(p1.getY(), p2.getY()), Math.max(p1.getZ(), p2.getZ()));
                if (light) highlightRegion(dimension, min, max, 0x660099CC, 1200);
                else stopHighlight(min, max);
            }
            return;
        }

        if (!presetConfirm) return;
        PlatformBlockType.PlatformBlockStructure struct = getPlatformBlockStructure(saveGroup, saveId);
        int sx = struct.xSize(), sy = struct.ySize(), sz = struct.zSize();
        BlockPos start = pos1;

        boolean zMir = this.xMirror, xMir = this.zMirror;
        int rot = this.rotation;

        int[] offsets = calcOffsetsBy8Points(sx, sy, sz, rot, zMir, xMir);
        int ox = offsets[0], oy = offsets[1], oz = offsets[2];

        int[][] corners = { { 0, 0, 0 }, { sx - 1, 0, 0 }, { 0, sy - 1, 0 }, { sx - 1, sy - 1, 0 }, { 0, 0, sz - 1 }, { sx - 1, 0, sz - 1 }, { 0, sy - 1, sz - 1 }, { sx - 1, sy - 1, sz - 1 } };

        int minWX = Integer.MAX_VALUE, minWY = Integer.MAX_VALUE, minWZ = Integer.MAX_VALUE;
        int maxWX = Integer.MIN_VALUE, maxWY = Integer.MIN_VALUE, maxWZ = Integer.MIN_VALUE;
        for (int[] c : corners) {
            int[] t = transform(c[0], c[1], c[2], sx, sz, rot, zMir, xMir);
            int wx = start.getX() + t[0] + ox;
            int wy = start.getY() + t[1] + oy;
            int wz = start.getZ() + t[2] + oz;
            minWX = Math.min(minWX, wx);
            maxWX = Math.max(maxWX, wx);
            minWY = Math.min(minWY, wy);
            maxWY = Math.max(maxWY, wy);
            minWZ = Math.min(minWZ, wz);
            maxWZ = Math.max(maxWZ, wz);
        }

        BlockPos minPos = new BlockPos(minWX, minWY, minWZ);
        BlockPos maxPos = new BlockPos(maxWX, maxWY, maxWZ);
        if (light) highlightRegion(dimension, minPos, maxPos, 0x2277FF77, 600);
        else stopHighlight(minPos, maxPos);
    }

    private void start() {
        if (!taskCompleted) return;
        Level level = getLevel();
        if (level == null) return;
        if (!(level instanceof ServerLevel serverLevel)) return;
        if (!consumeResources()) return;
        posChanged();
        PlatformBlockType.PlatformBlockStructure structure = getPlatformBlockStructure(saveGroup, saveId);
        progress = 0;
        taskCompleted = false;
        try {
            PlatformStructurePlacer.placeStructureAsync(
                    serverLevel,
                    pos1,
                    structure,
                    speed * 1000,
                    true,
                    skipAir,
                    updateLight,
                    zMirror,
                    xMirror,
                    rotation,
                    progress -> this.progress = progress,
                    () -> taskCompleted = true);
        } catch (IOException e) {
            GTOCore.LOGGER.error("The industrial platform deployment tool cannot deploy the platform, platform error {} {}, file location {}",
                    getPlatformPreset(saveGroup).name(),
                    structure.name(),
                    structure.resource());
            taskCompleted = true;
        }
        examineMaterial();
    }

    private void getPlatform() {
        if (!(getLevel() instanceof ServerLevel serverLevel)) return;
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
            PlatformCreationAsync(serverLevel, pos1, pos2, xMirror, zMirror, rotation);
        }
    }
}
