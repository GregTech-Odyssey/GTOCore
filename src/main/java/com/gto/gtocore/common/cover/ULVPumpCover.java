package com.gto.gtocore.common.cover;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.IControllable;
import com.gregtechceu.gtceu.api.capability.ICoverable;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.cover.CoverBehavior;
import com.gregtechceu.gtceu.api.cover.CoverDefinition;
import com.gregtechceu.gtceu.api.cover.IUICover;
import com.gregtechceu.gtceu.api.cover.filter.FilterHandler;
import com.gregtechceu.gtceu.api.cover.filter.FilterHandlers;
import com.gregtechceu.gtceu.api.cover.filter.FluidFilter;
import com.gregtechceu.gtceu.api.gui.widget.EnumSelectorWidget;
import com.gregtechceu.gtceu.api.gui.widget.IntInputWidget;
import com.gregtechceu.gtceu.api.gui.widget.NumberInputWidget;
import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;
import com.gregtechceu.gtceu.api.transfer.fluid.FluidHandlerDelegate;
import com.gregtechceu.gtceu.api.transfer.fluid.IFluidHandlerModifiable;
import com.gregtechceu.gtceu.api.transfer.fluid.ModifiableFluidHandlerWrapper;
import com.gregtechceu.gtceu.common.cover.data.BucketMode;
import com.gregtechceu.gtceu.common.cover.data.ManualIOMode;
import com.gregtechceu.gtceu.utils.FluidStackHashStrategy;
import com.gregtechceu.gtceu.utils.GTTransferUtils;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.annotation.RequireRerender;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import com.lowdragmc.lowdraglib.utils.LocalizationUtils;
import it.unimi.dsi.fastutil.objects.Object2IntOpenCustomHashMap;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ULVPumpCover extends CoverBehavior implements IUICover, IControllable {

    static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(ULVPumpCover.class,
            CoverBehavior.MANAGED_FIELD_HOLDER);

    private static final int tier = 0;
    private static final int maxMilliBucketsPerTick = 32;

    @Persisted
    @DescSynced
    @Getter
    protected int currentMilliBucketsPerTick;

    @Persisted
    @DescSynced
    @Getter
    @RequireRerender
    protected IO io = IO.OUT;

    @Persisted
    @DescSynced
    @Getter
    protected BucketMode bucketMode = BucketMode.MILLI_BUCKET;

    @Persisted
    @DescSynced
    @Getter
    protected ManualIOMode manualIOMode = ManualIOMode.DISABLED;

    @Persisted
    @Getter
    protected boolean isWorkingEnabled = true;
    private int milliBucketsLeftToTransferLastSecond;

    @Persisted
    @DescSynced
    final FilterHandler<FluidStack, FluidFilter> filterHandler;
    private final ConditionalSubscriptionHandler subscriptionHandler;
    private NumberInputWidget<Integer> transferRateWidget;

    public ULVPumpCover(CoverDefinition definition, ICoverable coverHolder, Direction attachedSide) {
        super(definition, coverHolder, attachedSide);
        this.currentMilliBucketsPerTick = maxMilliBucketsPerTick;
        this.milliBucketsLeftToTransferLastSecond = currentMilliBucketsPerTick * 20;
        subscriptionHandler = new ConditionalSubscriptionHandler(coverHolder, this::update, this::isSubscriptionActive);
        filterHandler = FilterHandlers.fluid(this)
                .onFilterLoaded(f -> configureFilter())
                .onFilterUpdated(f -> configureFilter())
                .onFilterRemoved(f -> configureFilter());
    }

    private boolean isSubscriptionActive() {
        return isWorkingEnabled && getAdjacentFluidHandler() != null;
    }

    @Nullable
    private IFluidHandlerModifiable getOwnFluidHandler() {
        return coverHolder.getFluidHandlerCap(attachedSide, false);
    }

    @Nullable
    private IFluidHandler getAdjacentFluidHandler() {
        return GTTransferUtils.getAdjacentFluidHandler(coverHolder.getLevel(), coverHolder.getPos(), attachedSide)
                .resolve()
                .orElse(null);
    }

    // Initialization

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public boolean canAttach() {
        return getOwnFluidHandler() != null;
    }

    private void setIo(IO io) {
        if (io == IO.IN || io == IO.OUT) {
            this.io = io;
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        subscriptionHandler.initialize(coverHolder.getLevel());
    }

    @Override
    public void onRemoved() {
        super.onRemoved();
        subscriptionHandler.unsubscribe();
    }

    @Override
    public List<ItemStack> getAdditionalDrops() {
        var list = super.getAdditionalDrops();
        if (!filterHandler.getFilterItem().isEmpty()) {
            list.add(filterHandler.getFilterItem());
        }
        return list;
    }

    @Override
    public void onNeighborChanged(Block block, BlockPos fromPos, boolean isMoving) {
        subscriptionHandler.updateSubscription();
    }

    @Override
    public void setWorkingEnabled(boolean isWorkingAllowed) {
        if (this.isWorkingEnabled != isWorkingAllowed) {
            this.isWorkingEnabled = isWorkingAllowed;
            subscriptionHandler.updateSubscription();
        }
    }

    // Transfer Logic

    private void setTransferRate(int milliBucketsPerTick) {
        this.currentMilliBucketsPerTick = Math.min(Math.max(milliBucketsPerTick, 0), maxMilliBucketsPerTick);
    }

    private void setBucketMode(BucketMode bucketMode) {
        var oldMultiplier = this.bucketMode.multiplier;
        var newMultiplier = bucketMode.multiplier;

        this.bucketMode = bucketMode;

        if (transferRateWidget == null) return;

        if (oldMultiplier > newMultiplier) {
            transferRateWidget.setValue(getCurrentBucketModeTransferRate());
        }

        transferRateWidget.setMax(maxMilliBucketsPerTick / bucketMode.multiplier);

        if (newMultiplier > oldMultiplier) {
            transferRateWidget.setValue(getCurrentBucketModeTransferRate());
        }
    }

    private void setManualIOMode(ManualIOMode manualIOMode) {
        this.manualIOMode = manualIOMode;
        coverHolder.markDirty();
    }

    private void update() {
        long timer = coverHolder.getOffsetTimer();
        if (timer % 5 != 0)
            return;

        if (milliBucketsLeftToTransferLastSecond > 0) {
            int platformTransferredFluid = doTransferFluids(milliBucketsLeftToTransferLastSecond);
            this.milliBucketsLeftToTransferLastSecond -= platformTransferredFluid;
        }

        if (timer % 20 == 0) {
            this.milliBucketsLeftToTransferLastSecond = currentMilliBucketsPerTick * 20;
        }

        subscriptionHandler.updateSubscription();
    }

    private int doTransferFluids(int platformTransferLimit) {
        var afh = getAdjacentFluidHandler();
        var am = afh instanceof IFluidHandlerModifiable modifiable ? modifiable : new ModifiableFluidHandlerWrapper(afh);
        var ownfh = getOwnFluidHandler();

        if (afh != null && ownfh != null) {
            return switch (io) {
                case IN -> doTransferFluidsInternal(am, ownfh, platformTransferLimit);
                case OUT -> doTransferFluidsInternal(ownfh, am, platformTransferLimit);
                default -> 0;
            };
        }
        return 0;
    }

    int doTransferFluidsInternal(IFluidHandlerModifiable source, IFluidHandlerModifiable destination, int platformTransferLimit) {
        return transferAny(source, destination, platformTransferLimit);
    }

    int transferAny(IFluidHandlerModifiable source, IFluidHandlerModifiable destination, int platformTransferLimit) {
        return GTTransferUtils.transferFluidsFiltered(source, destination, filterHandler.getFilter(), platformTransferLimit);
    }

    protected enum TransferDirection {
        INSERT,
        EXTRACT
    }

    static Map<FluidStack, Integer> enumerateDistinctFluids(IFluidHandlerModifiable fluidHandler, TransferDirection direction) {
        final Map<FluidStack, Integer> summedFluids = new Object2IntOpenCustomHashMap<>(
                FluidStackHashStrategy.comparingAllButAmount());

        for (int tank = 0; tank < fluidHandler.getTanks(); tank++) {
            if (!canTransfer(fluidHandler, direction, tank))
                continue;

            FluidStack fluidStack = fluidHandler.getFluidInTank(tank);
            if (fluidStack.isEmpty())
                continue;

            summedFluids.putIfAbsent(fluidStack, 0);
            summedFluids.computeIfPresent(fluidStack, (stack, totalAmount) -> totalAmount + stack.getAmount());
        }

        return summedFluids;
    }

    private static boolean canTransfer(IFluidHandlerModifiable fluidHandler, TransferDirection direction, int tank) {
        return switch (direction) {
            case INSERT -> fluidHandler.supportsFill(tank);
            case EXTRACT -> fluidHandler.supportsDrain(tank);
        };
    }

    //////////////////////////////////////
    // *********** GUI ***********//
    //////////////////////////////////////

    @Override
    public Widget createUIWidget() {
        final var group = new WidgetGroup(0, 0, 176, 137);

        group.addWidget(new LabelWidget(10, 5, LocalizationUtils.format(getUITitle(), GTValues.VN[tier])));

        transferRateWidget = new IntInputWidget(10, 20, 134, 20,
                this::getCurrentBucketModeTransferRate,
                this::setCurrentBucketModeTransferRate).setMin(0);
        setBucketMode(this.bucketMode); // initial input widget config happens here
        group.addWidget(transferRateWidget);

        group.addWidget(new EnumSelectorWidget<>(
                146, 20, 20, 20,
                Arrays.stream(BucketMode.values()).filter(m -> m.multiplier <= maxMilliBucketsPerTick).toList(),
                bucketMode, this::setBucketMode).setTooltipSupplier(ULVPumpCover::getBucketModeTooltip));

        group.addWidget(new EnumSelectorWidget<>(10, 45, 20, 20, List.of(IO.IN, IO.OUT), io, this::setIo));

        group.addWidget(new EnumSelectorWidget<>(146, 107, 20, 20,
                ManualIOMode.VALUES, manualIOMode, this::setManualIOMode)
                .setHoverTooltips("cover.universal.manual_import_export.mode.description"));

        group.addWidget(filterHandler.createFilterSlotUI(125, 108));
        group.addWidget(filterHandler.createFilterConfigUI(10, 72, 156, 60));

        buildAdditionalUI(group);

        return group;
    }

    private static List<Component> getBucketModeTooltip(BucketMode mode, String langKey) {
        return List.of(
                Component.translatable(langKey).append(Component.translatable("gtceu.gui.content.units.per_tick")));
    }

    private int getCurrentBucketModeTransferRate() {
        return this.currentMilliBucketsPerTick / this.bucketMode.multiplier;
    }

    private void setCurrentBucketModeTransferRate(int transferRate) {
        this.setTransferRate(transferRate * this.bucketMode.multiplier);
    }

    @NotNull
    String getUITitle() {
        return "cover.pump.title";
    }

    void buildAdditionalUI(WidgetGroup group) {}

    void configureFilter() {}

    private CoverableFluidHandlerWrapper fluidHandlerWrapper;

    @Nullable
    @Override
    public IFluidHandlerModifiable getFluidHandlerCap(@Nullable IFluidHandlerModifiable defaultValue) {
        if (defaultValue == null) {
            return null;
        }
        if (fluidHandlerWrapper == null || fluidHandlerWrapper.delegate != defaultValue) {
            this.fluidHandlerWrapper = new CoverableFluidHandlerWrapper(defaultValue);
        }
        return fluidHandlerWrapper;
    }

    private class CoverableFluidHandlerWrapper extends FluidHandlerDelegate {

        CoverableFluidHandlerWrapper(IFluidHandlerModifiable delegate) {
            super(delegate);
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            if (io == IO.OUT && manualIOMode == ManualIOMode.DISABLED) {
                return 0;
            }
            if (!filterHandler.test(resource) && manualIOMode == ManualIOMode.FILTERED) {
                return 0;
            }
            return super.fill(resource, action);
        }

        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {
            if (io == IO.IN && manualIOMode == ManualIOMode.DISABLED) {
                return FluidStack.EMPTY;
            }
            if (manualIOMode == ManualIOMode.FILTERED && !filterHandler.test(resource)) {
                return FluidStack.EMPTY;
            }
            return super.drain(resource, action);
        }
    }
}
