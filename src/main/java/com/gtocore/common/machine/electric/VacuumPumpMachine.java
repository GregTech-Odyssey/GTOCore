package com.gtocore.common.machine.electric;

import com.gtolib.api.machine.feature.IVacuumMachine;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyTooltip;
import com.gregtechceu.gtceu.api.gui.fancy.TooltipsPanel;
import com.gregtechceu.gtceu.api.machine.SimpleTieredMachine;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.data.machines.GTMachineUtils;

import net.minecraft.network.chat.Component;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class VacuumPumpMachine extends SimpleTieredMachine implements IVacuumMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(VacuumPumpMachine.class, SimpleTieredMachine.MANAGED_FIELD_HOLDER);

    @Override
    @NotNull
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Persisted
    @DescSynced
    private int vacuumTier;
    @Persisted
    private int totalEU;
    private TickableSubscription tickSubs;

    public VacuumPumpMachine(MetaMachineBlockEntity holder, int tier, Object... args) {
        super(holder, tier, GTMachineUtils.defaultTankSizeFunction, args);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!isRemote()) {
            tickSubs = subscribeServerTick(tickSubs, this::tick);
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

    private void tick() {
        if (getOffsetTimer() % 20 != 0) return;
        if (getRecipeLogic().isWorking()) {
            if (totalEU < 12000) totalEU += 2 * GTValues.VA[getTier()];
        } else if (totalEU > 0) {
            totalEU -= 4 * GTValues.VA[getTier()];
        }
        vacuumTier = Math.min(getTier() + 1, (int) Math.ceil(totalEU / 1200.0));
    }

    @Override
    public void afterWorking() {
        super.afterWorking();
        update();
    }

    @Nullable
    @Override
    public GTRecipe doModifyRecipe(@NotNull GTRecipe recipe) {
        if (getTier() == recipe.data.getInt("tier")) {
            return recipe;
        }
        return null;
    }

    @Override
    public void attachTooltips(TooltipsPanel tooltipsPanel) {
        super.attachTooltips(tooltipsPanel);
        tooltipsPanel.attachTooltips(new IFancyTooltip.Basic(() -> GuiTextures.INFO_ICON, () -> List.of(Component.translatable("gtocore.recipe.vacuum.tier", vacuumTier)), () -> true, () -> null));
    }

    @Override
    public int getVacuumTier() {
        return this.vacuumTier;
    }

    public int getTotalEU() {
        return this.totalEU;
    }

    public TickableSubscription getTickSubs() {
        return this.tickSubs;
    }
}
