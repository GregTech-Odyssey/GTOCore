package com.gto.gtocore.common.machine.multiblock.noenergy;

import com.gto.gtocore.api.machine.feature.IExtendWirelessEnergyContainerHolder;
import com.gto.gtocore.api.machine.multiblock.NoEnergyMultiblockMachine;
import com.gto.gtocore.common.wireless.ExtendWirelessEnergyContainer;
import com.gto.gtocore.utils.MachineUtils;

import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;

import com.hepdd.gtmthings.api.misc.WirelessEnergyContainer;
import com.hepdd.gtmthings.utils.TeamUtil;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class HarmonyMachine extends NoEnergyMultiblockMachine implements IExtendWirelessEnergyContainerHolder {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            HarmonyMachine.class, WorkableMultiblockMachine.MANAGED_FIELD_HOLDER);

    private static final Fluid HYDROGEN = GTMaterials.Hydrogen.getFluid();
    private static final Fluid HELIUM = GTMaterials.Helium.getFluid();

    @Getter
    @Setter
    private WirelessEnergyContainer WirelessEnergyContainerCache;

    @Persisted
    private int oc;
    @Persisted
    private long hydrogen, helium;
    @Persisted
    private UUID userid;

    private final ConditionalSubscriptionHandler StartupSubs;

    public HarmonyMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
        StartupSubs = new ConditionalSubscriptionHandler(this, this::StartupUpdate, this::isFormed);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    private void StartupUpdate() {
        if (getOffsetTimer() % 20 == 0) {
            oc = 0;
            int[] a = MachineUtils.getFluidAmount(this, HYDROGEN, HELIUM);
            if (MachineUtils.inputFluid(this, HYDROGEN, a[0])) {
                hydrogen += a[0];
            }
            if (MachineUtils.inputFluid(this, HELIUM, a[1])) {
                helium += a[1];
            }
            if (MachineUtils.notConsumableCircuit(this, 4)) {
                oc = 4;
            } else if (MachineUtils.notConsumableCircuit(this, 3)) {
                oc = 3;
            } else if (MachineUtils.notConsumableCircuit(this, 2)) {
                oc = 2;
            } else if (MachineUtils.notConsumableCircuit(this, 1)) {
                oc = 1;
            }
        }
    }

    private long getStartupEnergy() {
        return oc == 0 ? 0 : (5277655810867200L * (1L << (4 * oc - 1)));
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        StartupSubs.initialize(getLevel());
    }

    @Nullable
    @Override
    protected GTRecipe getRealRecipe(GTRecipe recipe) {
        if (userid != null && hydrogen >= 1024000000 && helium >= 1024000000 && oc > 0) {
            hydrogen -= 1024000000;
            helium -= 1024000000;
            ExtendWirelessEnergyContainer container = getWirelessEnergyContainer();
            if (container != null && container.removeEnergy(getStartupEnergy(), this) == -getStartupEnergy()) {
                recipe.duration = 4800 / (1 << (oc));
                return recipe;
            }
        }
        return null;
    }

    @Override
    public boolean shouldOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
        if (userid == null || !userid.equals(player.getUUID())) {
            userid = player.getUUID();
        }
        return true;
    }

    @Override
    protected void customText(List<Component> textList) {
        super.customText(textList);
        if (userid != null) {
            ExtendWirelessEnergyContainer container = getWirelessEnergyContainer();
            textList.add(Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.0",
                    TeamUtil.GetName(getLevel(), userid)));
            if (container != null) textList.add(Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.1",
                    FormattingUtil.formatNumbers(container.getStorage())));
        }
        textList.add(Component.translatable("gtocore.machine.eye_of_harmony.eu", FormattingUtil.formatNumbers(getStartupEnergy())));
        textList.add(Component.translatable("gtocore.machine.eye_of_harmony.hydrogen", FormattingUtil.formatNumbers(hydrogen)));
        textList.add(Component.translatable("gtocore.machine.eye_of_harmony.helium", FormattingUtil.formatNumbers(helium)));
    }

    @Override
    public UUID getUUID() {
        return userid;
    }
}
