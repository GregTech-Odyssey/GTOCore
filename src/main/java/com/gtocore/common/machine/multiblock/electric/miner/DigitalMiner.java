package com.gtocore.common.machine.multiblock.electric.miner;

import com.gtocore.integration.jade.provider.RecipeLogicProvider;

import com.gtolib.api.GTOValues;
import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.NewDataAttributes;
import com.gtolib.api.machine.feature.IDigitalMiner;
import com.gtolib.api.machine.impl.DigitalMinerLogic;
import com.gtolib.api.machine.multiblock.TierCasingMultiblockMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.cover.filter.Filter;
import com.gregtechceu.gtceu.api.cover.filter.FluidFilter;
import com.gregtechceu.gtceu.api.cover.filter.ItemFilter;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.misc.ProspectorMode;
import com.gregtechceu.gtceu.api.gui.widget.ProspectingMapWidget;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.item.ComponentItem;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.item.ItemFilterBehaviour;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;

import com.hepdd.gtmthings.api.gui.widget.SimpleNumberInputWidget;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.gui.widget.layout.Align;
import com.lowdragmc.lowdraglib.syncdata.ISubscription;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.mojang.blaze3d.MethodsReturnNonnullByDefault;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@DataGeneratorScanned
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class DigitalMiner extends TierCasingMultiblockMachine implements IDigitalMiner {

    // ===================== UI相关方法 =====================
    private static final int BORDER_WIDTH = 3;
    @Persisted
    protected final CustomItemStackHandler filterInventory;
    @Persisted
    public IDigitalMiner.FluidMode fluidMode = IDigitalMiner.FluidMode.Harvest;
    @Nullable
    protected ISubscription energySubs;
    protected Filter<?, ?> itemFilter;
    protected Filter<?, ?> fluidFilter;
    // ===================== UI组件 =====================
    protected SlotWidget filterSlot;
    protected ButtonWidget resetButton;
    protected ButtonWidget silkButton;

    // ===================== 构造与初始化 =====================
    protected ButtonWidget fluidModeButton;
    protected DraggableScrollableWidgetGroup mapArea;
    @Persisted
    @DescSynced
    private int xRadialLength;
    @Persisted
    @DescSynced
    private int zRadialLength;
    @Getter
    @Persisted
    @DescSynced
    private int xOffset;
    @Getter
    @Persisted
    @DescSynced
    private int zOffset;
    @Setter
    @Getter
    @Persisted
    @DescSynced
    private int minHeight;
    @Setter
    @Getter
    @Persisted
    @DescSynced
    private int maxHeight;

    // ===================== 逻辑相关方法 =====================
    @Getter
    @Persisted
    private int silkLevel;
    @DescSynced
    private long energyPerTickBase = 0L;
    @Getter
    @DescSynced
    private int parallelMining = 0;
    @DescSynced
    private int prospectorRadius;
    @DescSynced
    @Persisted
    private int maxRadius = 1;
    // ===================== Getter/Setter =====================
    @Getter
    @DescSynced
    @Persisted
    private boolean showRange = false;
    @Getter
    private long energyPerTick;
    private ButtonWidget showRangeButton;

    public DigitalMiner(MetaMachineBlockEntity holder) {
        super(holder, GTOValues.INTEGRAL_FRAMEWORK_TIER);
        this.filterInventory = createFilterItemHandler();
        this.silkLevel = 0;
        this.minHeight = 0;
        this.maxHeight = 64;
        this.xRadialLength = 1;
        this.zRadialLength = 1;
        this.xOffset = 0;
        this.zOffset = 0;
    }

    // ===================== 交互相关方法 =====================

    protected CustomItemStackHandler createFilterItemHandler() {
        var transfer = new CustomItemStackHandler();
        transfer.setFilter(
                item -> item.getItem() instanceof ComponentItem componentItem &&
                        componentItem.getComponents().stream().anyMatch(c -> c instanceof ItemFilterBehaviour));
        return transfer;
    }

    @Override
    public RecipeLogic createRecipeLogic(Object... args) {
        return new DigitalMinerLogic(this);
    }

    // ===================== 生命周期相关 =====================
    @Override
    public void onLoad() {
        super.onLoad();
        if (!isRemote()) {
            filterChange();
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        if (energySubs != null) {
            energySubs.unsubscribe();
            energySubs = null;
        }
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        tier = Math.min(getCasingTier(GTOValues.INTEGRAL_FRAMEWORK_TIER), tier);
        this.energyPerTickBase = (int) Math.pow(4, getTier()) * 2L;
        this.energyPerTick = energyPerTickBase * (silkLevel == 0 ? 1 : 4);
        this.parallelMining = (int) Math.min(4096, 4 * Math.pow(2, getTier()));
        this.maxRadius = (int) Math.min(8 * Math.pow(2, getTier()), 128);
        this.prospectorRadius = Math.min(getTier() / 2 + 1, 6);
        resetRecipe();
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        this.parallelMining = 0;
        resetRecipe();
    }

    @Override
    public void onMachineRemoved() {
        clearInventory(filterInventory);
    }

    @Override
    public void setWorkingEnabled(boolean isWorkingAllowed) {
        if (isWorkingAllowed && getRecipeLogic().isDone()) getRecipeLogic().resetRecipeLogic();
        super.setWorkingEnabled(isWorkingAllowed);
    }

    @Override
    public MinerConfig getMinerConfig() {
        return new MinerConfig(getMinerArea(), energyPerTick, getSpeed(), parallelMining, silkLevel, itemFilter, fluidFilter, fluidMode);
    }

    private void resetRecipe() {
        setWorkingEnabled(false);
        getRecipeLogic().resetRecipeLogic();
    }

    private void filterChange() {
        this.itemFilter = null;
        this.fluidFilter = null;
        var stack = filterInventory.getStackInSlot(0);
        if (!stack.isEmpty()) {
            if (stack.is(GTItems.TAG_FLUID_FILTER.asItem()) || stack.is(GTItems.FLUID_FILTER.asItem()))
                this.fluidFilter = FluidFilter.loadFilter(stack);
            else this.itemFilter = ItemFilter.loadFilter(filterInventory.getStackInSlot(0));
        }
        resetRecipe();
    }

    // ===================== 枚举与语言常量 =====================

    private void reset(ClickData clickData) {
        resetRecipe();
    }

    @SuppressWarnings("ConstantConditions")
    private void setSilk(ClickData clickData) {
        if (silkLevel == 0) {
            silkLevel = 1;
            this.silkButton.setButtonTexture(GuiTextures.BUTTON,
                    new TextTexture(Component.translatable(SILK).getString())
                            .setDropShadow(false)
                            .setColor(ChatFormatting.GREEN.getColor()));
            energyPerTick = energyPerTickBase * 4;
        } else {
            silkLevel = 0;
            this.silkButton.setButtonTexture(GuiTextures.BUTTON,
                    new TextTexture(Component.translatable(SILK).getString())
                            .setDropShadow(false)
                            .setColor(ChatFormatting.GRAY.getColor()));
            energyPerTick = energyPerTickBase;
        }
        resetRecipe();
    }

    public boolean drainInput(boolean simulate) {
        var energyContainer = getEnergyContainer();
        long resultEnergy = energyContainer.getEnergyStored() - energyPerTick;
        if (resultEnergy >= 0L && resultEnergy <= energyContainer.getEnergyCapacity()) {
            if (!simulate)
                energyContainer.removeEnergy(energyPerTick);
            return true;
        }
        return false;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public Widget createUIWidget() {
        int rowSize = 3;
        int colSize = 9;
        int width = colSize * 18 + 16 + 90 + 54;
        int height = rowSize * 18 + 76 + 4;

        WidgetGroup group = new WidgetGroup(0, 0, width, height);

        // information screen
        // var componentPanel = new ComponentPanelWidget(4, 5, this::addDisplayText).setMaxWidthLimit(110);
        var container = new WidgetGroup(8, 66, 87, 76);
        var componentPanel = new DraggableScrollableWidgetGroup();
        container.addWidget(new DraggableScrollableWidgetGroup(4, 4, container.getSize().width - 8,
                container.getSize().height - 8)
                .setBackground(GuiTextures.DISPLAY)
                .addWidget(componentPanel));
        container.setBackground(GuiTextures.BACKGROUND_INVERSE);
        group.addWidget(container);

        WidgetGroup slots = new WidgetGroup(8, 76 + 4 / 2, colSize * 18, rowSize * 18);
        group.addWidget(slots);

        // information screen 2
        var componentPanel2 = new ComponentPanelWidget(4, 5, this::addDisplayText);
        var container2 = new DraggableScrollableWidgetGroup(8, 0, 87, 60);
        container2.addWidget(new WidgetGroup(4, 4, 160, 80)
                .addWidget(componentPanel2)
                .setBackground(GuiTextures.DISPLAY));
        container2.setBackground(GuiTextures.BACKGROUND_INVERSE);
        container2.setYScrollBarWidth(3).setYBarStyle(GuiTextures.BACKGROUND_INVERSE, GuiTextures.BUTTON);
        container2.setXScrollBarHeight(3).setXBarStyle(GuiTextures.BACKGROUND_INVERSE, GuiTextures.BUTTON);
        group.addWidget(container2);

        // filter slot
        this.filterSlot = new SlotWidget(this.filterInventory, 0, 152, 0, true, true);
        this.filterSlot.setChangeListener(this::filterChange).setBackground(GuiTextures.SLOT, GuiTextures.FILTER_SLOT_OVERLAY);
        group.addWidget(filterSlot);
        group.addWidget(new LabelWidget(103, 4, Component.translatable("gtocore.machine.monitor.ae.set_filter"))
                .setClientSideWidget());

        int x1 = 103;
        int x2 = x1 + 41;
        var i = 0;
        // Radius
        group.addWidget(new LabelWidget(x1, 26 + (i) * 18, Component.translatable(XRADIAL_LENGTH)).setClientSideWidget());
        group.addWidget(new SimpleNumberInputWidget(x2, 24 + (i++) * 18, 32, 12, this::getMinerXRadius, this::setMinerXRadius)
                .setMin(0).setMax(maxRadius * 2 + 1));
        group.addWidget(new LabelWidget(x1, 26 + (i) * 18, Component.translatable(ZRADIAL_LENGTH)).setClientSideWidget());
        group.addWidget(new SimpleNumberInputWidget(x2, 24 + (i++) * 18, 32, 12, this::getMinerZRadius, this::setMinerZRadius)
                .setMin(0).setMax(maxRadius * 2 + 1));

        // Offset
        group.addWidget(new LabelWidget(x1, 26 + (i) * 18, Component.translatable(X_OFFSET)).setClientSideWidget());
        group.addWidget(new SimpleNumberInputWidget(x2, 24 + (i++) * 18, 32, 12, this::getXOffset, v -> {
            if (Math.abs(v) <= maxRadius) this.xOffset = v;
            else if (v > 0) this.xOffset = maxRadius;
            else this.xOffset = -maxRadius;
        }).setMin(-maxRadius).setMax(maxRadius));
        group.addWidget(new LabelWidget(x1, 26 + (i) * 18, Component.translatable(Z_OFFSET)).setClientSideWidget());
        group.addWidget(new SimpleNumberInputWidget(x2, 24 + (i++) * 18, 32, 12, this::getZOffset, v -> {
            if (Math.abs(v) <= maxRadius) this.zOffset = v;
            else if (v > 0) this.zOffset = maxRadius;
            else this.zOffset = -maxRadius;
        }).setMin(-maxRadius).setMax(maxRadius));

        // Min height
        group.addWidget(new LabelWidget(x1, 26 + (i) * 18, Component.translatable(MIN_HEIGHT)).setClientSideWidget());
        group.addWidget(new SimpleNumberInputWidget(x2, 24 + (i++) * 18, 32, 12, this::getMinHeight, this::setMinHeight)
                .setMin(getLevel().getMinBuildHeight()).setMax(getLevel().getMaxBuildHeight()));

        // Max height
        group.addWidget(new LabelWidget(x1, 26 + (i) * 18, Component.translatable(MAX_HEIGHT)).setClientSideWidget());
        group.addWidget(new SimpleNumberInputWidget(x2, 24 + (i) * 18, 32, 12, this::getMaxHeight, this::setMaxHeight)
                .setMin(getLevel().getMinBuildHeight()).setMax(getLevel().getMaxBuildHeight()));

        // silk button
        this.silkButton = new ButtonWidget(7, 5 + BORDER_WIDTH, 72, 16 - BORDER_WIDTH,
                this::setSilk);
        this.silkButton.setHoverTooltips(Component.translatable(SILK_TOOLTIP));
        container.addWidget(this.silkButton.setBackground(GuiTextures.BUTTON,
                new TextTexture(Component.translatable(SILK).getString())
                        .setDropShadow(false)
                        .setColor(silkLevel == 0 ? ChatFormatting.GRAY.getColor() : ChatFormatting.GREEN.getColor())));

        // Fluid Mode
        this.fluidModeButton = new ButtonWidget(7, 20 + BORDER_WIDTH, 72, 16 - BORDER_WIDTH,
                (cd -> {
                    fluidMode = fluidMode.next();
                    this.fluidModeButton.setButtonTexture(GuiTextures.BUTTON,
                            new TextTexture(fluidMode.getTitle())
                                    .setDropShadow(false).setColor(fluidMode.color.getColor()))
                            .setHoverTooltips(fluidMode.getTooltip());
                    resetRecipe();
                }));
        this.fluidModeButton.setHoverTooltips(fluidMode.getTooltip());
        container.addWidget(this.fluidModeButton.setBackground(GuiTextures.BUTTON,
                new TextTexture(fluidMode.getTitle())
                        .setDropShadow(false)
                        .setColor(fluidMode.color.getColor())));

        // reset button
        this.resetButton = new ButtonWidget(7, 35 + BORDER_WIDTH, 72, 16 - BORDER_WIDTH, this::reset);
        this.resetButton.setHoverTooltips(Component.translatable(RESET_TOOLTIP));
        container.addWidget(this.resetButton.setBackground(GuiTextures.BUTTON,
                new TextTexture(Component.translatable(RESET).getString()).setDropShadow(false).setColor(ChatFormatting.GRAY.getColor())));

        // Show Range
        showRangeButton = new ButtonWidget(7, 50 + BORDER_WIDTH, 72, 16 - BORDER_WIDTH,
                (cd -> {
                    showRange = !showRange;
                    showRangeButton.setButtonTexture(GuiTextures.BUTTON,
                            new TextTexture(Component.translatable(SHOW_RANGE).getString())
                                    .setDropShadow(false).setColor(showRange ? ChatFormatting.GREEN.getColor() : ChatFormatting.GRAY.getColor()));
                }));
        showRangeButton.setHoverTooltips(Component.translatable(SHOW_RANGE_TOOLTIP));
        container.addWidget(showRangeButton.setBackground(GuiTextures.BUTTON,
                new TextTexture(Component.translatable(SHOW_RANGE).getString())
                        .setDropShadow(false)
                        .setColor(showRange ? ChatFormatting.GREEN.getColor() : ChatFormatting.GRAY.getColor())));

        this.mapArea = new DraggableScrollableWidgetGroup(180, 4, 134, 134).setBackground(GuiTextures.PRIMITIVE_BACKGROUND);
        if (isFormed()) {
            group.addWidget(mapArea.addWidget(new WidgetGroup(1, 1, (prospectorRadius * 2 - 1) * 16 + 13, (prospectorRadius * 2 - 1) * 16 + 21).addWidget(
                    new ProspectorMap(4, 4, (prospectorRadius * 2 - 1) * 16 + 12, (prospectorRadius * 2 - 1) * 16 + 20, prospectorRadius, ProspectorMode.ORE, 1, group))));
            mapArea.setYScrollBarWidth(3).setYBarStyle(GuiTextures.BACKGROUND_INVERSE, GuiTextures.BUTTON);
            mapArea.setXScrollBarHeight(3).setXBarStyle(GuiTextures.BACKGROUND_INVERSE, GuiTextures.BUTTON);
        } else {
            group.addWidget(mapArea);
            mapArea.addWidget(new LabelWidget(0, 0,
                    Component.translatable("gtceu.top.invalid_structure")
                            .setStyle(Style.EMPTY.withColor(ChatFormatting.RED)))
                    .setClientSideWidget()
                    .setAlign(Align.CENTER));
        }

        return group;
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        textList.add(Component.translatable(TO_BE_MINED).append(String.valueOf(getRecipeLogic().getOreAmount())));
        if (!isFormed()) {
            textList.add(Component.translatable("gtceu.top.invalid_structure")
                    .setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
            return;
        }
        if (getRecipeLogic().isDone())
            textList.add(Component.translatable("gtceu.multiblock.large_miner.done")
                    .setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)));
        else if (getRecipeLogic().isWorking())
            textList.add(Component.translatable("gtceu.multiblock.large_miner.working")
                    .setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD)));
        else if (!this.isWorkingEnabled())
            textList.add(Component.translatable("gtceu.multiblock.work_paused"));
        if (getRecipeLogic().isInventoryFull())
            textList.add(Component.translatable("gtceu.multiblock.large_miner.invfull")
                    .setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
        if (!drainInput(true))
            textList.add(Component.translatable("gtceu.multiblock.large_miner.needspower")
                    .setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
        textList.addAll(NewDataAttributes.LEVEL.create(tier).get());
        RecipeLogicProvider.getEUtTooltip(textList, energyPerTick, false);
        textList.add(Component.translatable(PARALLEL, parallelMining));
    }

    @Override
    public boolean canConnectRedstone(Direction side) {
        return side == getFrontFacing() || side == Direction.DOWN || super.canConnectRedstone(side);
    }

    @Override
    public void onNeighborChanged(Block block, BlockPos fromPos, boolean isMoving) {
        super.onNeighborChanged(block, fromPos, isMoving);
        if (getLevel().hasNeighborSignal(fromPos)) {
            setWorkingEnabled(true);
        }
    }

    public int getMinerXRadius() {
        return xRadialLength;
    }

    public void setMinerXRadius(int minerRadius) {
        this.xRadialLength = minerRadius;
    }

    public int getMinerZRadius() {
        return zRadialLength;
    }

    public void setMinerZRadius(int minerRadius) {
        this.zRadialLength = minerRadius;
    }

    @Override
    public DigitalMinerLogic getRecipeLogic() {
        return (DigitalMinerLogic) super.getRecipeLogic();
    }

    public int getSpeed() {
        return 40;
    }

    public AABB getMinerArea() {
        if (xRadialLength > maxRadius * 2 + 1) xRadialLength = maxRadius * 2 + 1;
        if (zRadialLength > maxRadius * 2 + 1) zRadialLength = maxRadius * 2 + 1;
        if (minHeight > maxHeight) {
            int temp = minHeight;
            minHeight = maxHeight;
            maxHeight = temp;
        }
        BlockPos pos = getPos();
        BlockPos pos1 = pos.offset(xOffset, 0, zOffset).atY(minHeight);
        BlockPos pos2 = pos1.offset((xRadialLength), 0, (zRadialLength)).atY(maxHeight);
        return new AABB(pos1, pos2);
    }

    private class ProspectorMap extends ProspectingMapWidget {

        final Widget parent;
        boolean isDragging = false;
        double startX = 0, startY = 0;
        double lastX = 0, lastY = 0;
        WaypointItem startWaypoint = null;

        ProspectorMap(int x, int y, int width, int height, int radius, ProspectorMode mode, int scale, Widget parent) {
            super(x, y, width, height, radius, mode, scale);
            this.parent = parent;
            this.itemList.setVisible(false).setActive(false);
            this.getContainedWidgets(false).stream()
                    .filter(w -> !(w instanceof ImageWidget))
                    .forEach(w -> w.setVisible(false).setActive(false));
            this.getContainedWidgets(false).stream()
                    .filter(w -> w instanceof ImageWidget)
                    .forEach(w -> {
                        w.setSelfPosition(0, 0);
                        w.setSize((radius * 2 - 1) * 16 + 8, (radius * 2 - 1) * 16 + 16);
                    });
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            var clickedItem = getClickedVein(mouseX, mouseY);
            if (clickedItem == null) {
                return super.mouseClicked(mouseX, mouseY, button);
            }
            startWaypoint = clickedItem;
            isDragging = true;
            startX = mouseX;
            startY = mouseY;
            lastX = mouseX;
            lastY = mouseY;
            return true;
        }

        @Override
        public void drawInForeground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            super.drawInForeground(graphics, mouseX, mouseY, partialTicks);
            graphics.fill((int) startX, (int) startY, (int) lastX, (int) lastY, 0x80FFFFFF);
        }

        @Override
        public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
            if (isDragging && startWaypoint != null) {
                lastX = mouseX;
                lastY = mouseY;
            }
            return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }

        @Override
        public boolean mouseReleased(double mouseX, double mouseY, int button) {
            if (isDragging && startWaypoint != null) {
                isDragging = false;
                var endWaypoint = getClickedVein(mouseX, mouseY);
                if (endWaypoint != null && endWaypoint != startWaypoint) {
                    var startBlockPos = startWaypoint.position();
                    var endBlockPos = endWaypoint.position();
                    int xOffset = Math.min(startBlockPos.getX(), endBlockPos.getX()) - getPos().getX();
                    int zOffset = Math.min(startBlockPos.getZ(), endBlockPos.getZ()) - getPos().getZ();
                    int xRadialLength = Math.abs(startBlockPos.getX() - endBlockPos.getX());
                    int zRadialLength = Math.abs(startBlockPos.getZ() - endBlockPos.getZ());
                    if (xRadialLength > maxRadius * 2 + 1) xRadialLength = maxRadius * 2 + 1;
                    if (zRadialLength > maxRadius * 2 + 1) zRadialLength = maxRadius * 2 + 1;
                    if (Math.abs(xOffset) > maxRadius) xOffset = xOffset > 0 ? maxRadius : -maxRadius;
                    if (Math.abs(zOffset) > maxRadius) zOffset = zOffset > 0 ? maxRadius : -maxRadius;
                    final int finalXOffset = xOffset;
                    final int finalZOffset = zOffset;
                    final int finalXRadialLength = xRadialLength;
                    final int finalZRadialLength = zRadialLength;
                    writeClientAction(16, buf -> {
                        buf.writeInt(finalXOffset);
                        buf.writeInt(finalZOffset);
                        buf.writeInt(finalXRadialLength);
                        buf.writeInt(finalZRadialLength);
                    });
                    startWaypoint = null;
                    return true;
                }
            }
            return super.mouseReleased(mouseX, mouseY, button);
        }

        @Override
        public void handleClientAction(int id, FriendlyByteBuf buffer) {
            if (id == 16) {
                xOffset = buffer.readInt();
                zOffset = buffer.readInt();
                xRadialLength = buffer.readInt();
                zRadialLength = buffer.readInt();
                resetRecipe();
                parent.detectAndSendChanges();
                return;
            }
            super.readUpdateInfo(id, buffer);
        }
    }
}
