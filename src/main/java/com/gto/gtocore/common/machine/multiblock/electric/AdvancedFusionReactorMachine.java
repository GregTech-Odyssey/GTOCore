package com.gto.gtocore.common.machine.multiblock.electric;

import com.gto.gtocore.api.machine.multiblock.CrossRecipeMultiblockMachine;
import com.gto.gtocore.api.machine.trait.EnergyContainerTrait;
import com.gto.gtocore.utils.MachineUtils;

import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler;
import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.gregtechceu.gtceu.common.machine.multiblock.electric.FusionReactorMachine.calculateEnergyStorageFactor;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class AdvancedFusionReactorMachine extends CrossRecipeMultiblockMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            AdvancedFusionReactorMachine.class, CrossRecipeMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Getter
    private final int tier;
    @Persisted
    private long heat = 0;
    @Persisted
    private final EnergyContainerTrait energyContainer;
    private final ConditionalSubscriptionHandler preHeatSubs;

    public AdvancedFusionReactorMachine(IMachineBlockEntity holder, int tier) {
        super(holder, false, true, MachineUtils::getHatchParallel);
        this.tier = tier;
        this.energyContainer = createEnergyContainer();
        preHeatSubs = new ConditionalSubscriptionHandler(this, this::updateHeat, () -> isFormed || heat > 0);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    private EnergyContainerTrait createEnergyContainer() {
        var container = new EnergyContainerTrait(this, 0);
        container.setCapabilityValidator(Objects::isNull);
        return container;
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        int size = 0;
        for (IRecipeHandler<?> handler : getCapabilitiesFlat(IO.IN, EURecipeCapability.CAP)) {
            if (handler instanceof IEnergyContainer) {
                size++;
            }
        }
        energyContainer.resetBasicInfo(calculateEnergyStorageFactor(tier, size));
        preHeatSubs.initialize(getLevel());
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        heat = 0;
        energyContainer.resetBasicInfo(0);
        energyContainer.setEnergyStored(0);
    }

    @Override
    public @Nullable GTRecipe getRealRecipe(@NotNull GTRecipe recipe) {
        if (!recipe.data.contains("eu_to_start") || recipe.data.getLong("eu_to_start") > energyContainer.getEnergyCapacity()) return null;
        long heatDiff = recipe.data.getLong("eu_to_start") - heat;
        if (heatDiff < 0) return null;
        if (energyContainer.getEnergyStored() < heatDiff) return null;
        energyContainer.removeEnergy(heatDiff);
        heat += heatDiff;
        return super.getRealRecipe(recipe);
    }

    private void updateHeat() {
        if (heat > 0 && (getRecipeLogic().isIdle() || !isWorkingEnabled() || (getRecipeLogic().isWaiting() && getRecipeLogic().getProgress() == 0))) {
            heat = heat <= 10000 ? 0 : (heat - 10000);
        }
        if (isFormed() && getEnergyContainer().getEnergyStored() > 0) {
            var leftStorage = energyContainer.getEnergyCapacity() - energyContainer.getEnergyStored();
            if (leftStorage > 0) {
                energyContainer.addEnergy(getEnergyContainer().removeEnergy(leftStorage));
            }
        }
        preHeatSubs.updateSubscription();
    }

    @Override
    public void customText(List<Component> textList) {
        super.customText(textList);
        textList.add(Component.translatable("gtceu.multiblock.fusion_reactor.energy", this.energyContainer.getEnergyStored(), this.energyContainer.getEnergyCapacity()));
        textList.add(Component.translatable("gtceu.multiblock.fusion_reactor.heat", heat));
    }
}
