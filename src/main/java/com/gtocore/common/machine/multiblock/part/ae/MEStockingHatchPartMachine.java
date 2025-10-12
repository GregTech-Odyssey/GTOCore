package com.gtocore.common.machine.multiblock.part.ae;

import com.gtocore.common.machine.multiblock.part.ae.slots.ExportOnlyAEFluidList;
import com.gtocore.common.machine.multiblock.part.ae.slots.ExportOnlyAEFluidSlot;
import com.gtocore.common.machine.multiblock.part.ae.slots.ExportOnlyAEStockingFluidList;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;
import com.gregtechceu.gtceu.integration.ae2.machine.feature.multiblock.IMEStockingPart;
import com.gregtechceu.gtceu.integration.ae2.slot.IConfigurableSlotList;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;

import appeng.api.config.Actionable;
import appeng.api.networking.IGrid;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import appeng.api.storage.MEStorage;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.function.Predicate;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MEStockingHatchPartMachine extends MEInputHatchPartMachine implements IMEStockingPart {

    private static final int CONFIG_SIZE = 16;
    @Persisted
    private boolean autoPull;

    public MEStockingHatchPartMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public void addedToController(IMultiController controller) {
        super.addedToController(controller);
        IMEStockingPart.super.addedToController(controller);
    }

    @Override
    public void removedFromController(IMultiController controller) {
        IMEStockingPart.super.removedFromController(controller);
        super.removedFromController(controller);
    }

    @Override
    protected ExportOnlyAEFluidList createTank() {
        return new ExportOnlyAEStockingFluidList(this, CONFIG_SIZE);
    }

    /////////////////////////////////
    // ********** Sync ME *********//
    /////////////////////////////////

    @Override
    void syncME() {
        if (autoPull) {
            refreshList();
        } else {
            IGrid grid = this.getMainNode().getGrid();
            if (grid == null) {
                return;
            }
            MEStorage networkInv = grid.getStorageService().getInventory();
            for (ExportOnlyAEFluidSlot slot : aeFluidHandler.getInventory()) {
                var config = slot.getConfig();
                if (config != null) {
                    var key = config.what();
                    long extracted = networkInv.extract(key, Long.MAX_VALUE, Actionable.SIMULATE, getActionSourceField());
                    if (extracted > 0) {
                        slot.setStock(new GenericStack(key, extracted));
                        continue;
                    }
                }
                slot.setStock(null);
            }
        }
    }

    @Override
    protected void flushInventory() {}

    @Override
    public void setDistinct(boolean isDistinct) {
        super.setDistinct(isDistinct);
        if (!isRemote() && !isDistinct) {
            validateConfig();
        }
    }

    @Override
    public IConfigurableSlotList getSlotList() {
        return aeFluidHandler;
    }

    @Override
    public boolean testConfiguredInOtherPart(@Nullable GenericStack config) {
        if (config == null) return false;
        if (!isFormed()) return false;
        for (IMultiController controller : getControllers()) {
            for (IMultiPart part : controller.getParts()) {
                if (part instanceof MEStockingHatchPartMachine hatch) {
                    if (hatch == this) continue;
                    if (hatch.aeFluidHandler.hasStackInConfig(config, false)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void setAutoPull(boolean autoPull) {
        this.autoPull = autoPull;
        if (!isRemote()) {
            if (!this.autoPull) {
                this.aeFluidHandler.clearInventory(0);
            } else if (updateMEStatus()) {
                this.refreshList();
                updateTankSubscription();
            }
        }
    }

    boolean test(AEKey what) {
        return true;
    }

    private void refreshList() {
        IGrid grid = this.getMainNode().getGrid();
        if (grid == null) {
            aeFluidHandler.clearInventory(0);
            return;
        }
        var counter = grid.getStorageService().getCachedInventory();
        if (counter.isEmpty()) return;
        MEStorage networkStorage = grid.getStorageService().getInventory();

        var queue = new PriorityQueue<>(CONFIG_SIZE, Comparator.comparingLong(GenericStack::amount));

        for (var entry : counter) {
            long amount = entry.getLongValue();
            if (amount <= 0) continue;
            var what = entry.getKey();
            if (!(what instanceof AEFluidKey)) continue;
            boolean free = queue.size() < CONFIG_SIZE;
            if (!free && queue.peek().amount() >= amount) continue;
            if (!test(what)) continue;
            var stack = new GenericStack(what, amount);
            if (testConfiguredInOtherPart(stack)) continue;
            if (free) {
                queue.offer(stack);
            } else {
                queue.poll();
                queue.offer(stack);
            }
        }

        int index;
        int size = queue.size();
        for (index = 0; index < CONFIG_SIZE; index++) {
            if (queue.isEmpty()) break;
            var stack = queue.poll();

            var what = stack.what();
            long amount = stack.amount();

            long request = networkStorage.extract(what, amount, Actionable.SIMULATE, getActionSourceField());

            var slot = this.aeFluidHandler.getInventory()[size - index - 1];
            slot.setConfig(new GenericStack(what, 1));
            slot.setStock(new GenericStack(what, request));
        }

        aeFluidHandler.clearInventory(index);
    }

    ///////////////////////////////
    // ********** GUI ***********//
    ///////////////////////////////
    @Override
    public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
        IMEStockingPart.super.attachConfigurators(configuratorPanel);
        super.attachConfigurators(configuratorPanel);
    }

    ////////////////////////////////
    // ******* Interaction *******//
    ////////////////////////////////
    @Override
    protected InteractionResult onScrewdriverClick(Player playerIn, InteractionHand hand, Direction gridSide, BlockHitResult hitResult) {
        if (!isRemote()) {
            setAutoPull(!autoPull);
            if (autoPull) {
                playerIn.sendSystemMessage(Component.translatable("gtceu.machine.me.stocking_auto_pull_enabled"));
            } else {
                playerIn.sendSystemMessage(Component.translatable("gtceu.machine.me.stocking_auto_pull_disabled"));
            }
        }
        return InteractionResult.sidedSuccess(isRemote());
    }

    ////////////////////////////////
    // ****** Configuration ******//
    ////////////////////////////////
    @Override
    protected CompoundTag writeConfigToTag() {
        if (!autoPull) {
            CompoundTag tag = super.writeConfigToTag();
            tag.putBoolean("AutoPull", false);
            return tag;
        }
        // if in auto-pull, no need to write actual configured slots, but still need to write the ghost circuit
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("AutoPull", true);
        tag.putBoolean("DistinctBuses", isDistinct());
        if (!circuitInventory.storage.getStackInSlot(0).isEmpty()) {
            tag.putByte("GhostCircuit", (byte) IntCircuitBehaviour.getCircuitConfiguration(circuitInventory.storage.getStackInSlot(0)));
        } ;
        return tag;
    }

    @Override
    protected void readConfigFromTag(CompoundTag tag) {
        if (tag.getBoolean("AutoPull")) {
            // if being set to auto-pull, no need to read the configured slots
            this.setAutoPull(true);
            if (tag.contains("DistinctBuses")) {
                setDistinct(tag.getBoolean("DistinctBuses"));
            }
            if (tag.contains("GhostCircuit")) {
                circuitInventory.setStackInSlot(0, IntCircuitBehaviour.stack(tag.getByte("GhostCircuit")));
            } else {
                circuitInventory.setStackInSlot(0, ItemStack.EMPTY);
            }
            return;
        }
        // set auto pull first to avoid issues with clearing the config after reading from the data stick
        this.setAutoPull(false);
        super.readConfigFromTag(tag);
    }

    @Override
    public boolean isAutoPull() {
        return this.autoPull;
    }

    @Override
    public void setAutoPullTest(final Predicate<GenericStack> autoPullTest) {}
}
