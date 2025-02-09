package com.gto.gtocore.api.machine;

import com.gto.gtocore.api.machine.feature.IEnhancedMultiblockMachine;
import com.gto.gtocore.api.machine.feature.ILockableRecipe;

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
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SimpleNoEnergyMachine extends TieredMachine implements IRecipeLogicMachine, IMachineLife, IMufflableMachine, IAutoOutputBoth, IFancyUIMachine, IEnhancedMultiblockMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            SimpleNoEnergyMachine.class, TieredMachine.MANAGED_FIELD_HOLDER);

    @Persisted
    @DescSynced
    @RequireRerender
    private Direction outputFacingItems;
    @Persisted
    @DescSynced
    @RequireRerender
    private Direction outputFacingFluids;
    @Getter
    @Persisted
    @DescSynced
    @RequireRerender
    private boolean autoOutputItems;
    @Getter
    @Persisted
    @DescSynced
    @RequireRerender
    private boolean autoOutputFluids;
    @Getter
    @Setter
    @Persisted
    private boolean allowInputFromOutputSideItems;
    @Getter
    @Setter
    @Persisted
    private boolean allowInputFromOutputSideFluids;
    @Getter
    @Persisted
    private final NotifiableItemStackHandler circuitInventory;
    @Nullable
    private TickableSubscription autoOutputSubs;
    @Nullable
    private ISubscription exportItemSubs;
    @Nullable
    private ISubscription exportFluidSubs;
    @Getter
    @Persisted
    @DescSynced
    private final RecipeLogic recipeLogic;
    @Getter
    private final GTRecipeType[] recipeTypes;
    @Setter
    @Getter
    @Persisted
    private int activeRecipeType;
    @Getter
    private final Int2IntFunction tankScalingFunction;
    @Nullable
    private ICleanroomProvider cleanroom;
    @Persisted
    private final NotifiableItemStackHandler importItems;
    @Persisted
    private final NotifiableItemStackHandler exportItems;
    @Persisted
    private final NotifiableFluidTank importFluids;
    @Persisted
    private final NotifiableFluidTank exportFluids;
    @Persisted
    private final NotifiableComputationContainer importComputation;
    @Persisted
    private final NotifiableComputationContainer exportComputation;
    @Getter
    protected final Table<IO, RecipeCapability<?>, List<IRecipeHandler<?>>> capabilitiesProxy;
    private final List<ISubscription> traitSubscriptions;
    @Persisted
    @DescSynced
    private boolean isMuffled;
    private boolean previouslyMuffled = true;

    public SimpleNoEnergyMachine(IMachineBlockEntity holder, int tier, Int2IntFunction tankScalingFunction, Object... args) {
        super(holder, tier);
        recipeTypes = getDefinition().getRecipeTypes();
        activeRecipeType = 0;
        this.tankScalingFunction = tankScalingFunction;
        capabilitiesProxy = Tables.newCustomTable(new EnumMap<>(IO.class), IdentityHashMap::new);
        traitSubscriptions = new ArrayList<>();
        recipeLogic = createRecipeLogic();
        importItems = createImportItemHandler();
        exportItems = createExportItemHandler();
        importFluids = createImportFluidHandler();
        exportFluids = createExportFluidHandler();
        importComputation = createImportComputationContainer(args);
        exportComputation = createExportComputationContainer();
        outputFacingItems = hasFrontFacing() ? getFrontFacing().getOpposite() : Direction.UP;
        outputFacingFluids = outputFacingItems;
        circuitInventory = createCircuitItemHandler();
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    private NotifiableItemStackHandler createCircuitItemHandler() {
        return new NotifiableItemStackHandler(this, 1, IO.IN, IO.NONE).setFilter(IntCircuitBehaviour::isIntegratedCircuit);
    }

    private NotifiableItemStackHandler createImportItemHandler() {
        return new NotifiableItemStackHandler(this, getRecipeType().getMaxInputs(ItemRecipeCapability.CAP), IO.IN);
    }

    private NotifiableItemStackHandler createExportItemHandler() {
        return new NotifiableItemStackHandler(this, getRecipeType().getMaxOutputs(ItemRecipeCapability.CAP), IO.OUT);
    }

    private NotifiableFluidTank createImportFluidHandler() {
        return new NotifiableFluidTank(this, getRecipeType().getMaxInputs(FluidRecipeCapability.CAP), tankScalingFunction.apply(getTier()), IO.IN);
    }

    private NotifiableFluidTank createExportFluidHandler() {
        return new NotifiableFluidTank(this, getRecipeType().getMaxOutputs(FluidRecipeCapability.CAP), tankScalingFunction.apply(getTier()), IO.OUT);
    }

    private NotifiableComputationContainer createImportComputationContainer(Object... args) {
        boolean transmitter = true;
        if (args.length > 0 && args[args.length - 1] instanceof Boolean isTransmitter) {
            transmitter = isTransmitter;
        }
        return new NotifiableComputationContainer(this, IO.IN, transmitter);
    }

    private NotifiableComputationContainer createExportComputationContainer() {
        return new NotifiableComputationContainer(this, IO.OUT, false);
    }

    private RecipeLogic createRecipeLogic() {
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

    @Override
    @NotNull
    public GTRecipeType getRecipeType() {
        return recipeTypes[activeRecipeType];
    }

    @Override
    @Nullable
    public ICleanroomProvider getCleanroom() {
        return cleanroom;
    }

    @Override
    public void setCleanroom(@Nullable ICleanroomProvider cleanroom) {
        this.cleanroom = cleanroom;
    }

    @Override
    public boolean isMuffled() {
        return isMuffled;
    }

    @Override
    public void setMuffled(boolean isMuffled) {
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
            autoOutputItems = allow;
            updateAutoOutputSubscription();
        }
    }

    @Override
    public void setAutoOutputFluids(boolean allow) {
        if (hasAutoOutputFluid()) {
            autoOutputFluids = allow;
            updateAutoOutputSubscription();
        }
    }

    @Override
    public void setOutputFacingFluids(@Nullable Direction outputFacing) {
        if (hasAutoOutputFluid()) {
            outputFacingFluids = outputFacing;
            updateAutoOutputSubscription();
        }
    }

    @Override
    public void setOutputFacingItems(@Nullable Direction outputFacing) {
        if (hasAutoOutputItem()) {
            outputFacingItems = outputFacing;
            updateAutoOutputSubscription();
        }
    }

    @Override
    public void onNeighborChanged(Block block, BlockPos fromPos, boolean isMoving) {
        super.onNeighborChanged(block, fromPos, isMoving);
        updateAutoOutputSubscription();
    }

    private void updateAutoOutputSubscription() {
        if (getLevel() == null) return;
        Direction outputFacingItems = getOutputFacingItems();
        Direction outputFacingFluids = getOutputFacingFluids();
        if ((autoOutputItems && !exportItems.isEmpty()) && outputFacingItems != null && ItemTransferHelper.getItemTransfer(getLevel(), getPos().relative(outputFacingItems), outputFacingItems.getOpposite()) != null || (autoOutputFluids && !exportFluids.isEmpty()) && outputFacingFluids != null && FluidTransferHelper.getFluidTransfer(getLevel(), getPos().relative(outputFacingFluids), outputFacingFluids.getOpposite()) != null) {
            autoOutputSubs = subscribeServerTick(autoOutputSubs, this::autoOutput);
        } else if (autoOutputSubs != null) {
            autoOutputSubs.unsubscribe();
            autoOutputSubs = null;
        }
    }

    private void autoOutput() {
        if (getOffsetTimer() % 5 == 0) {
            if (autoOutputFluids && getOutputFacingFluids() != null) {
                exportFluids.exportToNearby(getOutputFacingFluids());
            }
            if (autoOutputItems && getOutputFacingItems() != null) {
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
        ILockableRecipe.attachRecipeLockable(configuratorPanel, recipeLogic);
    }

    public static final BiFunction<ResourceLocation, GTRecipeType, EditableMachineUI> EDITABLE_UI_CREATOR = Util.memoize((path, recipeType) -> new EditableMachineUI("simple", path, () -> {
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
