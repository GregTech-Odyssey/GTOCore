package com.gtocore.common.machine.multiblock.electric.miner;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.cover.filter.Filter;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.SimpleTieredMachine;
import com.gregtechceu.gtceu.api.machine.TieredMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.ISubscription;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SingleDigitalMiner extends SimpleTieredMachine {

    @Persisted
    protected final CustomItemStackHandler filterInventory;
    @Persisted
    public IDigitalMiner.FluidMode fluidMode = IDigitalMiner.FluidMode.Harvest;
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
    @Nullable
    protected ISubscription energySubs;

    protected Filter<?, ?> filter;
    @Getter
    private long energyPerTick;

    // ===================== UI组件 =====================
    protected SlotWidget filterSlot;
    protected ButtonWidget resetButton;
    protected ButtonWidget silkButton;
    protected ButtonWidget fluidModeButton;
    private ButtonWidget showRangeButton;
    protected DraggableScrollableWidgetGroup mapArea;

    // ===================== 构造与初始化 =====================

    public SingleDigitalMiner(MetaMachineBlockEntity holder, int tier, Int2IntFunction tankScalingFunction, Object... args) {
        super(holder, tier, tankScalingFunction, args);
        filterInventory=new CustomItemStackHandler();
        this.silkLevel = 0;
        this.minHeight = 0;
        this.maxHeight = 64;
        this.xRadialLength = 1;
        this.zRadialLength = 1;
        this.xOffset = 0;
        this.zOffset = 0;
    }

    @Override
    public @NotNull DigitalMinerLogic getRecipeLogic() {
        return (DigitalMinerLogic) super.getRecipeLogic();
    }
}
