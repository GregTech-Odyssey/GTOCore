package com.gto.gtocore.common.machine.multiblock.electric;

import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.utils.MachineUtil;
import com.gto.gtocore.utils.StringUtil;

import com.gregtechceu.gtceu.api.capability.IControllable;
import com.gregtechceu.gtceu.api.capability.IOpticalComputationProvider;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockDisplayText;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;

import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ComputationProviderMachine extends WorkableElectricMultiblockMachine
                                        implements IOpticalComputationProvider, IControllable {

    public int allocatedCWUt = 0;
    public long totalCWU = 0;
    public int maxCWUt = 0;
    @Nullable
    protected TickableSubscription tickSubs;

    private int lastAllocatedCWUt = 0;
    private boolean canProvideCWUt = true;
    private final boolean inf;

    public ComputationProviderMachine(IMachineBlockEntity holder, boolean inf, Object... args) {
        super(holder, args);
        this.inf = inf;
    }

    @Override
    public int requestCWUt(int cwut, boolean simulate, @NotNull Collection<IOpticalComputationProvider> seen) {
        seen.add(this);
        if (!canProvideCWUt) return 0;
        return allocatedCWUt(cwut, simulate);
    }

    private int allocatedCWUt(int cwut, boolean simulate) {
        if (totalCWU < getMaxCWUt()) {
            if (inf && MachineUtil.inputEU(this, Integer.MAX_VALUE)) return Integer.MAX_VALUE;
            if (MachineUtil.inputEU(this, getOverclockVoltage()))
                totalCWU += (long) Math.pow(2, getTier());
            maxCWUt = 0;
        }
        int maxCWUt = getMaxCWUt();
        int availableCWUt = maxCWUt - this.allocatedCWUt;
        int toAllocate = Math.min(cwut, (int) Math.min(availableCWUt, totalCWU));
        if (!simulate) {
            this.allocatedCWUt += toAllocate;
        }
        return toAllocate;
    }

    @Override
    public int getMaxCWUt(@NotNull Collection<IOpticalComputationProvider> seen) {
        seen.add(this);
        if (inf) return Integer.MAX_VALUE;
        if (maxCWUt == 0) {
            if (getTier() < 12 && MachineUtil.notConsumableItem(this, GTOItems.OPTICAL_MAINFRAME.asStack())) {
                return 1024;
            }
            if (getTier() < 13 && MachineUtil.notConsumableItem(this, GTOItems.EXOTIC_MAINFRAME.asStack())) {
                return 2048;
            }
            if (getTier() < 14 && MachineUtil.notConsumableItem(this, GTOItems.COSMIC_MAINFRAME.asStack())) {
                return 4096;
            }
            if (MachineUtil.notConsumableItem(this, GTOItems.SUPRACAUSAL_MAINFRAME.asStack())) {
                return 8192;
            }
            return 0;
        } else return maxCWUt;
    }

    @Override
    public boolean canBridge(@NotNull Collection<IOpticalComputationProvider> seen) {
        seen.add(this);
        return true;
    }

    public void tick() {
        totalCWU -= allocatedCWUt;
        lastAllocatedCWUt = allocatedCWUt;
        if (getRecipeLogic().isSuspend()) {
            allocatedCWUt = 0;
            canProvideCWUt = false;
            return;
        } else {
            canProvideCWUt = true;
        }
        if (allocatedCWUt != 0) {
            getRecipeLogic().setStatus(RecipeLogic.Status.WORKING);
            allocatedCWUt = 0;
        } else {
            getRecipeLogic().setStatus(RecipeLogic.Status.IDLE);
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (getLevel() instanceof ServerLevel serverLevel) {
            serverLevel.getServer().tell(new TickTask(0, this::updateTickSubscription));
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        if (tickSubs != null) {
            tickSubs.unsubscribe();
            tickSubs = null;
        }
    }

    protected void updateTickSubscription() {
        if (isFormed) {
            tickSubs = subscribeServerTick(tickSubs, this::tick);
        } else if (tickSubs != null) {
            tickSubs.unsubscribe();
            tickSubs = null;
        }
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        if (getLevel() instanceof ServerLevel serverLevel) {
            serverLevel.getServer().tell(new TickTask(0, this::updateTickSubscription));
        }
    }

    @Override
    public void onChanged() {
        maxCWUt = 0;
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        MultiblockDisplayText.builder(textList, isFormed())
                .setWorkingStatus(true, allocatedCWUt > 0)
                .setWorkingStatusKeys(
                        "gtocore.machine.idling",
                        "gtocore.machine.idling",
                        "gtocore.machine.data_bank.providing")
                .addCustom(tl -> {
                    if (isFormed()) {
                        Component cwutInfo = Component.literal(
                                lastAllocatedCWUt + " / " +
                                        (inf ? StringUtil.full_color("infinity") : getMaxCWUt()))
                                .append(Component.literal(" CWU/t"))
                                .withStyle(ChatFormatting.AQUA);
                        tl.add(Component.translatable(
                                "gtceu.multiblock.hpca.computation",
                                cwutInfo).withStyle(ChatFormatting.GRAY));
                    }
                })
                .addWorkingStatusLine();
    }
}
