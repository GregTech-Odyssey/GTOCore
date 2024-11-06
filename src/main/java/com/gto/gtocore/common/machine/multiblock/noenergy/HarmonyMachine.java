package com.gto.gtocore.common.machine.multiblock.noenergy;

import com.gto.gtocore.api.machine.multiblock.NoEnergyMultiblockMachine;
import com.gto.gtocore.utils.MachineUtil;

import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.logic.OCParams;
import com.gregtechceu.gtceu.api.recipe.logic.OCResult;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;

import com.hepdd.gtmthings.api.misc.WirelessEnergyManager;
import com.hepdd.gtmthings.utils.TeamUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class HarmonyMachine extends NoEnergyMultiblockMachine {

    public static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            HarmonyMachine.class, WorkableMultiblockMachine.MANAGED_FIELD_HOLDER);

    private static final Fluid HYDROGEN = GTMaterials.Hydrogen.getFluid();
    private static final Fluid HELIUM = GTMaterials.Helium.getFluid();

    @Persisted
    private int oc = 0;
    @Persisted
    private long hydrogen = 0, helium = 0;
    @Persisted
    private UUID userid;

    private final ConditionalSubscriptionHandler StartupSubs;

    public HarmonyMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
        this.StartupSubs = new ConditionalSubscriptionHandler(this, this::StartupUpdate, this::isFormed);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    private void StartupUpdate() {
        if (getOffsetTimer() % 20 == 0) {
            oc = 0;
            long[] a = MachineUtil.getFluidAmount(this, HYDROGEN, HELIUM);
            if (MachineUtil.inputFluid(this, FluidStack.create(HYDROGEN, a[0]))) {
                hydrogen += a[0];
            }
            if (MachineUtil.inputFluid(this, FluidStack.create(HELIUM, a[1]))) {
                helium += a[1];
            }
            if (MachineUtil.notConsumableCircuit(this, 4)) {
                oc = 4;
            } else if (MachineUtil.notConsumableCircuit(this, 3)) {
                oc = 3;
            } else if (MachineUtil.notConsumableCircuit(this, 2)) {
                oc = 2;
            } else if (MachineUtil.notConsumableCircuit(this, 1)) {
                oc = 1;
            }
        }
    }

    private long getStartupEnergy() {
        return oc == 0 ? 0 : (long) (5277655810867200L * Math.pow(8, oc - 1));
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        StartupSubs.initialize(getLevel());
    }

    @Nullable
    public static GTRecipe recipeModifier(MetaMachine machine, @NotNull GTRecipe recipe, @NotNull OCParams params,
                                          @NotNull OCResult result) {
        if (machine instanceof HarmonyMachine harmonyMachine && harmonyMachine.userid != null && harmonyMachine.hydrogen >= 1024000000 && harmonyMachine.helium >= 1024000000 && harmonyMachine.oc > 0) {
            harmonyMachine.hydrogen -= 1024000000;
            harmonyMachine.helium -= 1024000000;
            if (WirelessEnergyManager.addEUToGlobalEnergyMap(harmonyMachine.userid, -harmonyMachine.getStartupEnergy(), machine)) {
                GTRecipe recipe1 = recipe.copy();
                recipe1.duration = (int) (4800 / Math.pow(2, harmonyMachine.oc));
                return recipe1;
            }
        }
        return null;
    }

    @Override
    public boolean shouldOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
        if (this.userid == null || !this.userid.equals(player.getUUID())) {
            this.userid = player.getUUID();
        }
        return true;
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        if (this.isFormed()) {
            if (userid != null) {
                textList.add(Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.0",
                        TeamUtil.GetName(getLevel(), userid)));
                textList.add(Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.1",
                        FormattingUtil.formatNumbers(WirelessEnergyManager.getUserEU(userid))));
            }
            textList.add(Component.translatable("gtocore.machine.eye_of_harmony.eu", FormattingUtil.formatNumbers(getStartupEnergy())));
            textList.add(Component.translatable("gtocore.machine.eye_of_harmony.hydrogen", FormattingUtil.formatNumbers(hydrogen)));
            textList.add(Component.translatable("gtocore.machine.eye_of_harmony.helium", FormattingUtil.formatNumbers(helium)));
        }
    }
}
