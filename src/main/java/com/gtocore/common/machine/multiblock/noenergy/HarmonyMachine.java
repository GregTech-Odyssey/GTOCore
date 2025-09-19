package com.gtocore.common.machine.multiblock.noenergy;

import com.gtolib.api.capability.IExtendWirelessEnergyContainerHolder;
import com.gtolib.api.machine.multiblock.NoEnergyMultiblockMachine;
import com.gtolib.api.recipe.IdleReason;
import com.gtolib.api.recipe.Recipe;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Fluid;

import com.hepdd.gtmthings.api.misc.WirelessEnergyContainer;
import com.hepdd.gtmthings.utils.TeamUtil;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class HarmonyMachine extends NoEnergyMultiblockMachine implements IExtendWirelessEnergyContainerHolder {

    private static final BigInteger BASE = BigInteger.valueOf(5277655810867200L);

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(HarmonyMachine.class, NoEnergyMultiblockMachine.MANAGED_FIELD_HOLDER);
    private static final Fluid HYDROGEN = GTMaterials.Hydrogen.getFluid();
    private static final Fluid HELIUM = GTMaterials.Helium.getFluid();
    private WirelessEnergyContainer WirelessEnergyContainerCache;
    @Persisted
    private int tier = 1;
    @Persisted
    private int count;
    @Persisted
    private int oc;
    @Persisted
    private long hydrogen;
    @Persisted
    private long helium;
    private final ConditionalSubscriptionHandler tickSubs;

    public HarmonyMachine(MetaMachineBlockEntity holder) {
        super(holder);
        tickSubs = new ConditionalSubscriptionHandler(this, this::StartupUpdate, this::isFormed);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    private void StartupUpdate() {
        if (getOffsetTimer() % 20 == 0) {
            oc = 0;
            long[] a = getFluidAmount(HYDROGEN, HELIUM);
            if (inputFluid(HYDROGEN, a[0])) {
                hydrogen += a[0];
            }
            if (inputFluid(HELIUM, a[1])) {
                helium += a[1];
            }
            if (notConsumableCircuit(4)) {
                oc = 4;
            } else if (notConsumableCircuit(3)) {
                oc = 3;
            } else if (notConsumableCircuit(2)) {
                oc = 2;
            } else if (notConsumableCircuit(1)) {
                oc = 1;
            }
            tickSubs.updateSubscription();
        }
    }

    private BigInteger getStartupEnergy() {
        if (oc == 0) return BigInteger.ZERO;
        return BASE.multiply(BigInteger.ONE.shiftLeft(3 * oc - 1));
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        tickSubs.initialize(getLevel());
    }

    @Nullable
    @Override
    protected Recipe getRealRecipe(Recipe recipe) {
        if (getUUID() != null && tier <= recipe.data.getInt("tier") && hydrogen >= 1024000000 && helium >= 1024000000 && oc > 0) {
            hydrogen -= 1024000000;
            helium -= 1024000000;
            var container = getWirelessEnergyContainer();
            if (container == null) return null;
            BigInteger storage = container.getStorage();
            BigInteger energy = getStartupEnergy().multiply(BigInteger.valueOf(Math.max(1, (recipe.data.getInt("tier") - 1) << 2)));
            if (storage.compareTo(energy) > 0) {
                container.setStorage(storage.subtract(energy));
                if (tier == recipe.data.getInt("tier")) {
                    count++;
                    if (count > 16 + (tier << 2)) {
                        count = 0;
                        tier++;
                    }
                }
                recipe.duration = recipe.duration >> (oc - 1);
                return recipe;
            }
            setIdleReason(IdleReason.NO_EU);
        }
        return null;
    }

    @Override
    public void customText(List<Component> textList) {
        super.customText(textList);
        textList.add(Component.translatable("tooltip.avaritia.tier", tier));
        textList.add(Component.translatable("behaviour.lighter.uses", 16 + (tier << 2) - count));
        if (getUUID() != null) {
            var container = getWirelessEnergyContainer();
            textList.add(Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.0", TeamUtil.GetName(getLevel(), getUUID())));
            if (container != null) textList.add(Component.translatable("gtmthings.machine.wireless_energy_monitor.tooltip.1", FormattingUtil.formatNumbers(container.getStorage())));
        }
        textList.add(Component.translatable("gtocore.machine.eye_of_harmony.eu", FormattingUtil.formatNumbers(getStartupEnergy())));
        textList.add(Component.translatable("gtocore.machine.eye_of_harmony.hydrogen", FormattingUtil.formatNumbers(hydrogen)));
        textList.add(Component.translatable("gtocore.machine.eye_of_harmony.helium", FormattingUtil.formatNumbers(helium)));
    }

    @Override
    @Nullable
    public UUID getUUID() {
        return getOwnerUUID();
    }

    @Override
    public void setWirelessEnergyContainerCache(final WirelessEnergyContainer WirelessEnergyContainerCache) {
        this.WirelessEnergyContainerCache = WirelessEnergyContainerCache;
    }

    @Override
    public WirelessEnergyContainer getWirelessEnergyContainerCache() {
        return this.WirelessEnergyContainerCache;
    }
}
