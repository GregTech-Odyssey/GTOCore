package com.gto.gtocore.common.machine.electric;

import com.gto.gtocore.api.machine.IVacuumMachine;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyTooltip;
import com.gregtechceu.gtceu.api.gui.fancy.TooltipsPanel;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.SimpleTieredMachine;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.logic.OCParams;
import com.gregtechceu.gtceu.api.recipe.logic.OCResult;
import com.gregtechceu.gtceu.common.data.GTMachines;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.network.chat.Component;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public class VacuumPumpMachine extends SimpleTieredMachine implements IVacuumMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            VacuumPumpMachine.class, SimpleTieredMachine.MANAGED_FIELD_HOLDER);

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

    protected TickableSubscription tickSubs;

    public VacuumPumpMachine(IMachineBlockEntity holder, int tier, Object... args) {
        super(holder, tier, GTMachines.defaultTankSizeFunction, args);
        this.tickSubs = subscribeServerTick(tickSubs, this::tick);
    }

    private void tick() {
        if (getOffsetTimer() % 20 != 0) return;
        if (getRecipeLogic().isWorking()) {
            if (totalEU < 12000) totalEU += 2 * GTValues.VA[getTier()];
        } else if (totalEU > 0) {
            totalEU -= 4 * GTValues.VA[getTier()];
        }
        vacuumTier = Math.min(getTier() + 1, (int) Math.ceil(totalEU / 1200D));
    }

    public static GTRecipe recipeModifier(MetaMachine machine, @NotNull GTRecipe recipe, OCParams params, OCResult result) {
        if (machine instanceof VacuumPumpMachine pumpMachine && pumpMachine.getTier() == recipe.data.getInt("tier")) {
            return recipe;
        }
        return null;
    }

    @Override
    public void attachTooltips(TooltipsPanel tooltipsPanel) {
        super.attachTooltips(tooltipsPanel);
        tooltipsPanel.attachTooltips(new IFancyTooltip.Basic(() -> GuiTextures.INFO_ICON, () -> List.of(Component.translatable("gtocore.recipe.vacuum.tier", getVacuumTier())), () -> true, () -> null));
    }
}
