package com.gto.gtocore.common.machine.multiblock.electric;

import com.gto.gtocore.api.machine.multiblock.CrossRecipeMultiblockMachine;
import com.gto.gtocore.utils.MachineUtils;

import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;
import com.gregtechceu.gtceu.api.misc.EnergyContainerList;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.longs.Long2ObjectMaps;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    @Nullable
    private EnergyContainerList inputEnergyContainers;
    @Persisted
    private long heat = 0;
    @Persisted
    private final NotifiableEnergyContainer energyContainer;
    @Nullable
    private TickableSubscription preHeatSubs;

    public AdvancedFusionReactorMachine(IMachineBlockEntity holder, int tier) {
        super(holder, false, true, MachineUtils::getHatchParallel);
        this.tier = tier;
        this.energyContainer = createEnergyContainer();
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    private NotifiableEnergyContainer createEnergyContainer() {
        var container = new NotifiableEnergyContainer(this, 0, 0, 0, 0, 0);
        container.setCapabilityValidator(Objects::isNull);
        return container;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!isRemote()) {
            updatePreHeatSubscription();
        }
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        List<IEnergyContainer> energyContainers = new ArrayList<>();
        Map<Long, IO> ioMap = getMultiblockState().getMatchContext().getOrCreate("ioMap", Long2ObjectMaps::emptyMap);
        for (IMultiPart part : getParts()) {
            IO io = ioMap.getOrDefault(part.self().getPos().asLong(), IO.BOTH);
            if (io == IO.NONE || io == IO.OUT) continue;
            var handlerLists = part.getRecipeHandlers();
            for (var handlerList : handlerLists) {
                if (!handlerList.isValid(io)) continue;
                handlerList.getCapability(EURecipeCapability.CAP).stream().filter(IEnergyContainer.class::isInstance).map(IEnergyContainer.class::cast).forEach(energyContainers::add);
                traitSubscriptions.add(handlerList.subscribe(this::updatePreHeatSubscription, EURecipeCapability.CAP));
            }
        }
        this.inputEnergyContainers = new EnergyContainerList(energyContainers);
        energyContainer.resetBasicInfo(calculateEnergyStorageFactor(tier, energyContainers.size()), 0, 0, 0, 0);
        updatePreHeatSubscription();
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        this.inputEnergyContainers = null;
        heat = 0;
        energyContainer.resetBasicInfo(0, 0, 0, 0, 0);
        energyContainer.setEnergyStored(0);
        updatePreHeatSubscription();
    }

    private void updatePreHeatSubscription() {
        if (heat > 0 || (inputEnergyContainers != null && inputEnergyContainers.getEnergyStored() > 0 && energyContainer.getEnergyStored() < energyContainer.getEnergyCapacity())) {
            preHeatSubs = subscribeServerTick(preHeatSubs, this::updateHeat);
        } else if (preHeatSubs != null) {
            preHeatSubs.unsubscribe();
            preHeatSubs = null;
        }
    }

    @Override
    public @Nullable GTRecipe getRealRecipe(@NotNull GTRecipe recipe) {
        if (!recipe.data.contains("eu_to_start") || recipe.data.getLong("eu_to_start") > energyContainer.getEnergyCapacity()) return null;
        long heatDiff = recipe.data.getLong("eu_to_start") - heat;
        if (heatDiff < 0) return null;
        if (energyContainer.getEnergyStored() < heatDiff) return null;
        energyContainer.removeEnergy(heatDiff);
        heat += heatDiff;
        updatePreHeatSubscription();
        return super.getRealRecipe(recipe);
    }

    @Override
    public boolean onWorking() {
        if (getOffsetTimer() % 5 == 0) {
            GTRecipe recipe = recipeLogic.getLastRecipe();
            assert recipe != null;
            if (recipe.data.contains("eu_to_start")) {
                long heatDiff = recipe.data.getLong("eu_to_start") - this.heat;
                if (heatDiff > 0) {
                    recipeLogic.setWaiting(Component.translatable("gtceu.recipe_logic.insufficient_fuel"));
                    if (this.energyContainer.getEnergyStored() < heatDiff) return super.onWorking();
                    this.energyContainer.removeEnergy(heatDiff);
                    this.heat += heatDiff;
                    this.updatePreHeatSubscription();
                }
            }
        }
        return super.onWorking();
    }

    public void updateHeat() {
        if ((getRecipeLogic().isIdle() || !isWorkingEnabled() || (getRecipeLogic().isWaiting() && getRecipeLogic().getProgress() == 0)) && heat > 0) {
            heat = heat <= 10000 ? 0 : (heat - 10000);
        }
        var leftStorage = energyContainer.getEnergyCapacity() - energyContainer.getEnergyStored();
        if (inputEnergyContainers != null && leftStorage > 0) {
            energyContainer.addEnergy(inputEnergyContainers.removeEnergy(leftStorage));
        }
        updatePreHeatSubscription();
    }

    @Override
    public void customText(List<Component> textList) {
        super.customText(textList);
        textList.add(Component.translatable("gtceu.multiblock.fusion_reactor.energy", this.energyContainer.getEnergyStored(), this.energyContainer.getEnergyCapacity()));
        textList.add(Component.translatable("gtceu.multiblock.fusion_reactor.heat", heat));
    }
}
