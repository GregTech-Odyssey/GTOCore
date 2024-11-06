package com.gto.gtocore.api.machine;

import com.gregtechceu.gtceu.api.capability.recipe.*;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.editor.EditableMachineUI;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.TieredMachine;
import com.gregtechceu.gtceu.api.machine.fancyconfigurator.CircuitFancyConfigurator;
import com.gregtechceu.gtceu.api.machine.feature.*;
import com.gregtechceu.gtceu.api.machine.trait.*;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.ui.GTRecipeTypeUI;
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;

import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.side.fluid.FluidTransferHelper;
import com.lowdragmc.lowdraglib.side.item.ItemTransferHelper;
import com.lowdragmc.lowdraglib.syncdata.ISubscription;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.annotation.RequireRerender;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import com.lowdragmc.lowdraglib.utils.Position;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import it.unimi.dsi.fastutil.ints.Int2LongFunction;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SimpleNoEnergyMachine extends TieredMachine implements IRecipeLogicMachine, IMachineLife, IMufflableMachine, ITieredMachine, IAutoOutputBoth, IFancyUIMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            SimpleNoEnergyMachine.class, TieredMachine.MANAGED_FIELD_HOLDER);

    @Persisted
    @DescSynced
    @RequireRerender
    protected Direction outputFacingItems;
    @Persisted
    @DescSynced
    @RequireRerender
    protected Direction outputFacingFluids;
    @Getter
    @Persisted
    @DescSynced
    @RequireRerender
    protected boolean autoOutputItems;
    @Getter
    @Persisted
    @DescSynced
    @RequireRerender
    protected boolean autoOutputFluids;
    @Getter
    @Setter
    @Persisted
    protected boolean allowInputFromOutputSideItems;
    @Getter
    @Setter
    @Persisted
    protected boolean allowInputFromOutputSideFluids;
    @Getter
    @Persisted
    protected final NotifiableItemStackHandler circuitInventory;
    @Nullable
    protected TickableSubscription autoOutputSubs;
    @Nullable
    protected ISubscription exportItemSubs;
    @Nullable
    protected ISubscription exportFluidSubs;
    @Getter
    @Persisted
    @DescSynced
    public final RecipeLogic recipeLogic;
    @Getter
    public final GTRecipeType[] recipeTypes;
    @Setter
    @Getter
    @Persisted
    public int activeRecipeType;
    @Getter
    public final Int2LongFunction tankScalingFunction;
    @Nullable
    private ICleanroomProvider cleanroom;
    @Persisted
    public final NotifiableItemStackHandler importItems;
    @Persisted
    public final NotifiableItemStackHandler exportItems;
    @Persisted
    public final NotifiableFluidTank importFluids;
    @Persisted
    public final NotifiableFluidTank exportFluids;
    @Persisted
    public final NotifiableComputationContainer importComputation;
    @Persisted
    public final NotifiableComputationContainer exportComputation;
    @Getter
    protected final Table<IO, RecipeCapability<?>, List<IRecipeHandler<?>>> capabilitiesProxy;
    protected final List<ISubscription> traitSubscriptions;
    @Persisted
    @DescSynced
    protected boolean isMuffled;
    protected boolean previouslyMuffled = true;

    @SuppressWarnings("UnstableApiUsage")
    public SimpleNoEnergyMachine(IMachineBlockEntity holder, int tier, Int2LongFunction tankScalingFunction, Object... args) {
        super(holder, tier);
        this.recipeTypes = getDefinition().getRecipeTypes();
        this.activeRecipeType = 0;
        this.tankScalingFunction = tankScalingFunction;
        this.capabilitiesProxy = Tables.newCustomTable(new EnumMap<>(IO.class), IdentityHashMap::new);
        this.traitSubscriptions = new ArrayList<>();
        this.recipeLogic = createRecipeLogic();
        this.importItems = createImportItemHandler();
        this.exportItems = createExportItemHandler();
        this.importFluids = createImportFluidHandler();
        this.exportFluids = createExportFluidHandler();
        this.importComputation = createImportComputationContainer(args);
        this.exportComputation = createExportComputationContainer();
        this.outputFacingItems = hasFrontFacing() ? getFrontFacing().getOpposite() : Direction.UP;
        this.outputFacingFluids = outputFacingItems;
        this.circuitInventory = createCircuitItemHandler();
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    protected NotifiableItemStackHandler createCircuitItemHandler() {
        return new NotifiableItemStackHandler(this, 1, IO.IN, IO.NONE).setFilter(IntCircuitBehaviour::isIntegratedCircuit);
    }

    protected NotifiableItemStackHandler createImportItemHandler() {
        return new NotifiableItemStackHandler(this, getRecipeType().getMaxInputs(ItemRecipeCapability.CAP), IO.IN);
    }

    protected NotifiableItemStackHandler createExportItemHandler() {
        return new NotifiableItemStackHandler(this, getRecipeType().getMaxOutputs(ItemRecipeCapability.CAP), IO.OUT);
    }

    protected NotifiableFluidTank createImportFluidHandler() {
        return new NotifiableFluidTank(this, getRecipeType().getMaxInputs(FluidRecipeCapability.CAP), this.tankScalingFunction.apply(this.getTier()), IO.IN);
    }

    protected NotifiableFluidTank createExportFluidHandler() {
        return new NotifiableFluidTank(this, getRecipeType().getMaxOutputs(FluidRecipeCapability.CAP), this.tankScalingFunction.apply(this.getTier()), IO.OUT);
    }

    protected NotifiableComputationContainer createImportComputationContainer(Object... args) {
        boolean transmitter = true;
        if (args.length > 0 && args[args.length - 1] instanceof Boolean isTransmitter) {
            transmitter = isTransmitter;
        }
        return new NotifiableComputationContainer(this, IO.IN, transmitter);
    }

    protected NotifiableComputationContainer createExportComputationContainer() {
        return new NotifiableComputationContainer(this, IO.OUT, false);
    }

    protected RecipeLogic createRecipeLogic() {
        return new RecipeLogic(this);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        for (MachineTrait trait : getTraits()) {
            if (trait instanceof IRecipeHandlerTrait<?> handlerTrait) {
                if (!capabilitiesProxy.contains(handlerTrait.getHandlerIO(), handlerTrait.getCapability())) {
                    capabilitiesProxy.put(handlerTrait.getHandlerIO(), handlerTrait.getCapability(), new ArrayList<>());
                }
                Objects.requireNonNull(capabilitiesProxy.get(handlerTrait.getHandlerIO(), handlerTrait.getCapability())).add(handlerTrait);
                traitSubscriptions.add(handlerTrait.addChangedListener(recipeLogic::updateTickSubscription));
            }
        }
        if (!isRemote()) {
            if (getLevel() instanceof ServerLevel serverLevel) {
                serverLevel.getServer().tell(new TickTask(0, this::updateAutoOutputSubscription));
            }
            exportItemSubs = exportItems.addChangedListener(this::updateAutoOutputSubscription);
            exportFluidSubs = exportFluids.addChangedListener(this::updateAutoOutputSubscription);
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        traitSubscriptions.forEach(ISubscription::unsubscribe);
        traitSubscriptions.clear();
        recipeLogic.inValid();
        if (exportItemSubs != null) {
            exportItemSubs.unsubscribe();
            exportItemSubs = null;
        }
        if (exportFluidSubs != null) {
            exportFluidSubs.unsubscribe();
            exportFluidSubs = null;
        }
    }

    @Override
    public void onMachineRemoved() {
        clearInventory(importItems.storage);
        clearInventory(exportItems.storage);
    }

    @Override
    public void clientTick() {
        super.clientTick();
        if (previouslyMuffled != isMuffled) {
            previouslyMuffled = isMuffled;
            if (recipeLogic != null) recipeLogic.updateSound();
        }
    }

    @Override
    public boolean keepSubscribing() {
        return false;
    }

    @NotNull
    public GTRecipeType getRecipeType() {
        return recipeTypes[activeRecipeType];
    }

    @Nullable
    public ICleanroomProvider getCleanroom() {
        return this.cleanroom;
    }

    public void setCleanroom(@Nullable final ICleanroomProvider cleanroom) {
        this.cleanroom = cleanroom;
    }

    public boolean isMuffled() {
        return this.isMuffled;
    }

    public void setMuffled(final boolean isMuffled) {
        this.isMuffled = isMuffled;
    }

    @Override
    public boolean hasAutoOutputFluid() {
        return exportFluids.getTanks() > 0;
    }

    @Override
    public boolean hasAutoOutputItem() {
        return exportItems.getSlots() > 0;
    }

    @Override
    @Nullable
    public Direction getOutputFacingFluids() {
        if (hasAutoOutputFluid()) {
            return outputFacingFluids;
        }
        return null;
    }

    @Override
    @Nullable
    public Direction getOutputFacingItems() {
        if (hasAutoOutputItem()) {
            return outputFacingItems;
        }
        return null;
    }

    @Override
    public void setAutoOutputItems(boolean allow) {
        if (hasAutoOutputItem()) {
            this.autoOutputItems = allow;
            updateAutoOutputSubscription();
        }
    }

    @Override
    public void setAutoOutputFluids(boolean allow) {
        if (hasAutoOutputFluid()) {
            this.autoOutputFluids = allow;
            updateAutoOutputSubscription();
        }
    }

    @Override
    public void setOutputFacingFluids(@Nullable Direction outputFacing) {
        if (hasAutoOutputFluid()) {
            this.outputFacingFluids = outputFacing;
            updateAutoOutputSubscription();
        }
    }

    @Override
    public void setOutputFacingItems(@Nullable Direction outputFacing) {
        if (hasAutoOutputItem()) {
            this.outputFacingItems = outputFacing;
            updateAutoOutputSubscription();
        }
    }

    @Override
    public void onNeighborChanged(Block block, BlockPos fromPos, boolean isMoving) {
        super.onNeighborChanged(block, fromPos, isMoving);
        updateAutoOutputSubscription();
    }

    protected void updateAutoOutputSubscription() {
        if (getLevel() == null) return;
        Direction outputFacingItems = getOutputFacingItems();
        Direction outputFacingFluids = getOutputFacingFluids();
        if ((isAutoOutputItems() && !exportItems.isEmpty()) && outputFacingItems != null && ItemTransferHelper.getItemTransfer(getLevel(), getPos().relative(outputFacingItems), outputFacingItems.getOpposite()) != null || (isAutoOutputFluids() && !exportFluids.isEmpty()) && outputFacingFluids != null && FluidTransferHelper.getFluidTransfer(getLevel(), getPos().relative(outputFacingFluids), outputFacingFluids.getOpposite()) != null) {
            autoOutputSubs = subscribeServerTick(autoOutputSubs, this::autoOutput);
        } else if (autoOutputSubs != null) {
            autoOutputSubs.unsubscribe();
            autoOutputSubs = null;
        }
    }

    protected void autoOutput() {
        if (getOffsetTimer() % 5 == 0) {
            if (isAutoOutputFluids() && getOutputFacingFluids() != null) {
                exportFluids.exportToNearby(getOutputFacingFluids());
            }
            if (isAutoOutputItems() && getOutputFacingItems() != null) {
                exportItems.exportToNearby(getOutputFacingItems());
            }
        }
        updateAutoOutputSubscription();
    }

    @Override
    public boolean isFacingValid(Direction facing) {
        if (facing == getOutputFacingItems() || facing == getOutputFacingFluids()) {
            return false;
        }
        return super.isFacingValid(facing);
    }

    //////////////////////////////////////
    // *********** GUI ***********//
    //////////////////////////////////////
    @Override
    public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
        IFancyUIMachine.super.attachConfigurators(configuratorPanel);
        configuratorPanel.attachConfigurators(new CircuitFancyConfigurator(circuitInventory.storage));
    }

    @SuppressWarnings("UnstableApiUsage")
    public static BiFunction<ResourceLocation, GTRecipeType, EditableMachineUI> EDITABLE_UI_CREATOR = Util.memoize((path, recipeType) -> new EditableMachineUI("simple", path, () -> {
        WidgetGroup template = recipeType.getRecipeUI().createEditableUITemplate(false, false).createDefault();

        WidgetGroup group = new WidgetGroup(0, 0, template.getSize().width, Math.max(template.getSize().height, 78));
        template.setSelfPosition(new Position(0, (group.getSize().height - template.getSize().height) / 2));
        group.addWidget(template);
        return group;
    }, (template, machine) -> {
        if (machine instanceof SimpleNoEnergyMachine magicMachine) {
            var storages = Tables.newCustomTable(new EnumMap<>(IO.class), LinkedHashMap<RecipeCapability<?>, Object>::new);
            storages.put(IO.IN, ItemRecipeCapability.CAP, magicMachine.importItems.storage);
            storages.put(IO.OUT, ItemRecipeCapability.CAP, magicMachine.exportItems.storage);
            storages.put(IO.IN, FluidRecipeCapability.CAP, magicMachine.importFluids);
            storages.put(IO.OUT, FluidRecipeCapability.CAP, magicMachine.exportFluids);
            storages.put(IO.IN, CWURecipeCapability.CAP, magicMachine.importComputation);
            storages.put(IO.OUT, CWURecipeCapability.CAP, magicMachine.exportComputation);
            magicMachine.getRecipeType().getRecipeUI().createEditableUITemplate(false, false).setupUI(template, new GTRecipeTypeUI.RecipeHolder(magicMachine.recipeLogic::getProgressPercent, storages, new CompoundTag(), Collections.emptyList(), false, false));
        }
    }));

    @Override
    public ResourceTexture sideTips(Player player, BlockPos pos, BlockState state, Set<GTToolType> toolTypes, Direction side) {
        if (toolTypes.contains(GTToolType.WRENCH)) {
            if (!player.isShiftKeyDown()) {
                if (!hasFrontFacing() || side != getFrontFacing()) {
                    return GuiTextures.TOOL_IO_FACING_ROTATION;
                }
            }
        }
        if (toolTypes.contains(GTToolType.SCREWDRIVER)) {
            if (side == getOutputFacingItems() || side == getOutputFacingFluids()) {
                return GuiTextures.TOOL_ALLOW_INPUT;
            }
        }
        return super.sideTips(player, pos, state, toolTypes, side);
    }
}
