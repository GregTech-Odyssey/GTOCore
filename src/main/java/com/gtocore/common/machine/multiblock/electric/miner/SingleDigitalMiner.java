package com.gtocore.common.machine.multiblock.electric.miner;

import com.gtocore.client.forge.ForgeClientEvent;

import com.gtolib.api.machine.feature.IDigitalMiner;
import com.gtolib.api.machine.impl.DigitalMinerLogic;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.cover.filter.ItemFilter;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfiguratorButton;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.SimpleTieredMachine;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.feature.IDataInfoProvider;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.machines.GTMachineUtils;
import com.gregtechceu.gtceu.common.item.PortableScannerBehavior;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;

import com.hepdd.gtmthings.api.gui.widget.SimpleNumberInputWidget;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.side.item.ItemTransferHelper;
import com.lowdragmc.lowdraglib.syncdata.ISubscription;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.mojang.blaze3d.MethodsReturnNonnullByDefault;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SingleDigitalMiner extends SimpleTieredMachine implements IDigitalMiner, IDataInfoProvider {
    // modify from gtmt

    private static final int BORDER_WIDTH = 3;
    @Persisted
    protected final CustomItemStackHandler filterInventory;
    private final int maximumRadius;
    @Nullable
    protected TickableSubscription autoOutputSubs;
    @Nullable
    protected ISubscription exportItemSubs, energySubs;
    @Getter
    protected ItemFilter itemFilter;
    // widget
    protected SlotWidget filterSlot;
    protected ButtonWidget resetButton;
    protected ButtonWidget silkButton;
    private long energyPerTick;
    // miner property
    @Getter
    @Setter
    @Persisted
    @DescSynced
    private int minerRadius;
    @Getter
    @Setter
    @Persisted
    @DescSynced
    private int minHeight;
    @Getter
    @Setter
    @Persisted
    @DescSynced
    private int maxHeight;
    private int silkLevel;

    //////////////////////////////////////
    // ***** Initialization ******//

    public SingleDigitalMiner(MetaMachineBlockEntity holder, int tier, Object... args) {
        super(holder, tier, GTMachineUtils.defaultTankSizeFunction, args);
        this.energyPerTick = GTValues.VEX[tier - 1];
        this.filterInventory = createFilterItemHandler();
        this.silkLevel = 0;
        this.minHeight = 0;
        this.maxHeight = 64;
        this.maximumRadius = (int) (8 * Math.pow(2, tier));
        this.minerRadius = maximumRadius;
    }

    /// ///////////////////////////////////

    protected CustomItemStackHandler createFilterItemHandler() {
        var transfer = new CustomItemStackHandler();
        transfer.setFilter(
                item -> item.is(GTItems.ITEM_FILTER.asItem()) || item.is(GTItems.TAG_FILTER.asItem()));
        return transfer;
    }

    @Override
    public RecipeLogic createRecipeLogic(Object... args) {
        return new DigitalMinerLogic(this);
    }

    @Override
    public void onMachineRemoved() {
        clearInventory(exportItems.storage);
        clearInventory(filterInventory);
    }

    public AABB getMinerArea() {
        var pos = this.getPos();
        return new AABB(pos.getX() - minerRadius, maxHeight, pos.getZ() - minerRadius, pos.getX() + minerRadius, minHeight, pos.getZ() + minerRadius);
    }

    @Override
    public IDigitalMiner.MinerConfig getMinerConfig() {
        return new MinerConfig(getMinerArea(), silkLevel > 0 ? energyPerTick * 4 : energyPerTick, 20, (int) Math.pow(2, tier - 1), silkLevel, itemFilter, null, FluidMode.Ignore);
    }

    @Override
    public DigitalMinerLogic getRecipeLogic() {
        return (DigitalMinerLogic) super.getRecipeLogic();
    }

    @Override
    public void onNeighborChanged(Block block, BlockPos fromPos, boolean isMoving) {
        super.onNeighborChanged(block, fromPos, isMoving);
        updateAutoOutputSubscription();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!isRemote()) {
            filterChange();
            if (getLevel() instanceof ServerLevel serverLevel) {
                serverLevel.getServer().tell(new TickTask(0, this::updateAutoOutputSubscription));
            }
            exportItemSubs = exportItems.addChangedListener(this::updateAutoOutputSubscription);
        }
    }

    //////////////////////////////////////
    // ********** LOGIC **********//

    @Override
    public void onUnload() {
        super.onUnload();
        if (exportItemSubs != null) {
            exportItemSubs.unsubscribe();
            exportItemSubs = null;
        }

        if (energySubs != null) {
            energySubs.unsubscribe();
            energySubs = null;
        }
    }

    /// ///////////////////////////////////
    protected void updateAutoOutputSubscription() {
        var outputFacingItems = getFrontFacing();
        if (!exportItems.isEmpty() && ItemTransferHelper.getItemTransfer(getLevel(),
                getPos().relative(outputFacingItems), outputFacingItems.getOpposite()) != null) {
            autoOutputSubs = subscribeServerTick(autoOutputSubs, this::autoOutput, 20);
        } else if (autoOutputSubs != null) {
            autoOutputSubs.unsubscribe();
            autoOutputSubs = null;
        }
    }

    protected void autoOutput() {
        exportItems.exportToNearby(getFrontFacing());
        updateAutoOutputSubscription();
    }

    @Override
    public boolean drainInput(boolean simulate) {
        long resultEnergy = energyContainer.getEnergyStored() - energyPerTick;
        if (resultEnergy >= 0L && resultEnergy <= energyContainer.getEnergyCapacity()) {
            if (!simulate)
                energyContainer.removeEnergy(energyPerTick);
            return true;
        }
        return false;
    }

    @Override
    public Widget createUIWidget() {
        int rowSize = 3;
        int colSize = 9;
        int width = colSize * 18 + 16;
        int height = rowSize * 18 + 76 + 4;
        int index = 0;

        WidgetGroup group = new WidgetGroup(0, 0, width, height);

        // information screen
        var componentPanel = new ComponentPanelWidget(4, 5, this::addDisplayText).setMaxWidthLimit(110);
        var container = new WidgetGroup(8, 0, 87, 76);
        container.addWidget(new DraggableScrollableWidgetGroup(4, 4, container.getSize().width - 8,
                container.getSize().height - 8)
                .setBackground(GuiTextures.DISPLAY)
                .addWidget(componentPanel));
        container.setBackground(GuiTextures.BACKGROUND_INVERSE);
        group.addWidget(container);

        // output slots
        WidgetGroup slots = new WidgetGroup(8, 76 + 4 / 2, colSize * 18, rowSize * 18);
        for (int y = 0; y < rowSize; y++) {
            for (int x = 0; x < colSize; x++) {
                var slot = new SlotWidget(exportItems, index++, x * 18, y * 18, true, false)
                        .setBackground(GuiTextures.SLOT);
                slots.addWidget(slot);
            }
        }
        group.addWidget(slots);

        // filter slot
        this.filterSlot = new SlotWidget(this.filterInventory, 0, 117, 4, true, true);
        this.filterSlot.setChangeListener(this::filterChange).setBackground(GuiTextures.SLOT, GuiTextures.FILTER_SLOT_OVERLAY);
        group.addWidget(filterSlot);

        // Radius
        group.addWidget(new LabelWidget(99, 26, "水平范围:"));
        group.addWidget(new SimpleNumberInputWidget(140, 24, 24, 12, this::getMinerRadius, this::setMinerRadius)
                .setMin(1).setMax((int) (8 * Math.pow(2, getTier()))));

        // Min height
        group.addWidget(new LabelWidget(99, 44, "最小高度:"));
        group.addWidget(new SimpleNumberInputWidget(140, 42, 24, 12, this::getMinHeight, this::setMinHeight)
                .setMin(getLevel().getMinBuildHeight()).setMax(getLevel().getMaxBuildHeight()));

        // Max height
        group.addWidget(new LabelWidget(99, 62, "最大高度:"));
        group.addWidget(new SimpleNumberInputWidget(140, 60, 24, 12, this::getMaxHeight, this::setMaxHeight)
                .setMin(getLevel().getMinBuildHeight()).setMax(getLevel().getMaxBuildHeight()));

        // reset button
        this.resetButton = new ButtonWidget(16, 46 + BORDER_WIDTH, 18, 16 - BORDER_WIDTH,
                new TextTexture("重置").setDropShadow(false).setColor(ChatFormatting.GRAY.getColor()), this::reset);
        this.resetButton.setHoverTooltips(Component.literal("修改配置后必须重置才能生效。"));
        group.addWidget(this.resetButton);

        // silk button
        this.silkButton = new ButtonWidget(36, 46 + BORDER_WIDTH, 18, 16 - BORDER_WIDTH,
                new TextTexture("精准")
                        .setDropShadow(false)
                        .setColor(silkLevel == 0 ? ChatFormatting.GRAY.getColor() : ChatFormatting.GREEN.getColor()),
                this::setSilk);
        this.silkButton.setHoverTooltips(Component.literal("开启精准采集模式，4倍耗电。"));
        group.addWidget(this.silkButton);

        return group;
    }

    private void resetRecipe() {
        setWorkingEnabled(false);
        getRecipeLogic().resetRecipeLogic();
    }

    private void filterChange() {
        this.itemFilter = null;
        if (!filterInventory.getStackInSlot(0).isEmpty())
            this.itemFilter = ItemFilter.loadFilter(filterInventory.getStackInSlot(0));
        resetRecipe();
    }

    private void reset(ClickData clickData) {
        resetRecipe();
    }

    private void setSilk(ClickData clickData) {
        if (silkLevel == 0) {
            silkLevel = 1;
            this.silkButton.setButtonTexture(new TextTexture("精准").setDropShadow(false).setColor(ChatFormatting.GREEN.getColor()));
            energyPerTick = GTValues.VEX[getTier() - 1] * 4;
        } else {
            silkLevel = 0;
            this.silkButton.setButtonTexture(new TextTexture("精准").setDropShadow(false).setColor(ChatFormatting.GRAY.getColor()));
            energyPerTick = GTValues.VEX[getTier() - 1];
        }
        resetRecipe();
    }

    private void addDisplayText(List<Component> textList) {
        textList.add(Component.literal("挖掘: ").append(String.valueOf(getRecipeLogic().getOreAmount())));
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
    }

    //////////////////////////////////////
    // ******* Interaction *******//

    /// ///////////////////////////////////
    @Override
    protected InteractionResult onScrewdriverClick(Player playerIn, InteractionHand hand, Direction gridSide,
                                                   BlockHitResult hitResult) {
        if (isRemote()) return InteractionResult.SUCCESS;

        if (!this.isActive()) {
            int currentRadius = minerRadius;
            if (currentRadius == 1)
                minerRadius = this.maximumRadius;
            else if (playerIn.isShiftKeyDown())
                minerRadius = Math.max(1, Math.round(currentRadius / 2.0f));
            else
                minerRadius = Math.max(1, currentRadius - 1);

            getRecipeLogic().resetArea(true);

            int workingArea = minerRadius;
            playerIn.sendSystemMessage(
                    Component.translatable("gtceu.universal.tooltip.working_area", workingArea, workingArea));
        } else {
            playerIn.sendSystemMessage(Component.translatable("gtceu.multiblock.large_miner.errorradius"));
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public List<Component> getDataInfo(PortableScannerBehavior.DisplayMode mode) {
        if (mode == PortableScannerBehavior.DisplayMode.SHOW_ALL ||
                mode == PortableScannerBehavior.DisplayMode.SHOW_MACHINE_INFO) {
            int workingArea = minerRadius;
            return Collections.singletonList(
                    Component.translatable("gtceu.universal.tooltip.working_area", workingArea, workingArea));
        }
        return new ArrayList<>();
    }

    @Nullable
    private ForgeClientEvent.HighlightNeed need;

    @Override
    public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
        super.attachConfigurators(configuratorPanel);
        configuratorPanel.attachConfigurators(new IFancyConfiguratorButton.Toggle(
                GuiTextures.LIGHT_ON, GuiTextures.LIGHT_ON, () -> false,
                (clickData, pressed) -> {
                    if (clickData.isRemote && this.self().getLevel() != null) {
                        if (need != null && ForgeClientEvent.CUstomHighlightNeeds.containsKey(need)) {
                            ForgeClientEvent.CUstomHighlightNeeds.removeInt(need);
                            need = null;
                            return;
                        }
                        need = new ForgeClientEvent.HighlightNeed(
                                getPos().east(minerRadius).north(minerRadius).atY(maxHeight),
                                getPos().west(minerRadius).south(minerRadius).atY(minHeight),
                                ChatFormatting.WHITE.getColor());
                        ForgeClientEvent.CUstomHighlightNeeds.computeIfAbsent(
                                need, k -> 20 * 10);
                    }
                })
                .setTooltipsSupplier(pressed -> Collections.singletonList(Component.translatable(SHOW_RANGE_TOOLTIP))));
    }
}
