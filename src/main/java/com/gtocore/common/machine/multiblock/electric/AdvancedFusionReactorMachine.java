package com.gtocore.common.machine.multiblock.electric;

import com.gtolib.api.machine.multiblock.CrossRecipeMultiblockMachine;
import com.gtolib.api.machine.trait.EnergyContainerTrait;
import com.gtolib.api.recipe.IdleReason;
import com.gtolib.utils.MachineUtils;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.handler.IO;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;
import com.gregtechceu.gtceu.common.data.GTRecipeDataKeys;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;

import com.gto.datasynclib.annotations.SaveToDisk;
import com.gto.datasynclib.annotations.SyncToClient;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.gregtechceu.gtceu.api.GTValues.LuV;
import static com.gregtechceu.gtceu.common.machine.multiblock.electric.FusionReactorMachine.calculateEnergyStorageFactor;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class AdvancedFusionReactorMachine extends CrossRecipeMultiblockMachine {

    @Getter
    @SyncToClient
    private int color = -1;
    private static final int tier = LuV;
    @SaveToDisk
    private long heat = 0;
    @SaveToDisk
    private final EnergyContainerTrait energyContainer;
    private final ConditionalSubscriptionHandler preHeatSubs;

    public AdvancedFusionReactorMachine(MetaMachineBlockEntity holder) {
        super(holder, false, true, MachineUtils::getHatchParallel);
        this.energyContainer = createEnergyContainer();
        preHeatSubs = new ConditionalSubscriptionHandler(this, this::updateHeat, 0, () -> isFormed || heat > 0);
    }

    private EnergyContainerTrait createEnergyContainer() {
        return new EnergyContainerTrait(this, 0);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        int size = 0;
        for (var handler : getCapabilitiesFlat(IO.IN, IEnergyContainer.class)) {
            size++;
        }
        var bonusTier = calculateBonusTier();
        energyContainer.resetBasicInfo(calculateEnergyStorageFactor(tier + bonusTier, size));
        preHeatSubs.initialize(getLevel());
    }

    private int calculateBonusTier() {
        if (formeds == null || getSubFormed().length <= 1) {
            return 0;
        }
        int bonusTier;
        for (bonusTier = 0; bonusTier < getSubFormed().length - 1; bonusTier++) {
            // the last index is for special uses, ignore it
            if (!getSubFormed()[bonusTier]) {
                break;
            }
        }
        return bonusTier;
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        heat = 0;
        energyContainer.resetBasicInfo(0);
        energyContainer.setEnergyStored(0);
    }

    @Override
    @Nullable
    public GTRecipe getRealRecipe(RecipeHandlerUnit unit, GTRecipe recipe) {
        long eu_to_start = recipe.data.getLong(GTRecipeDataKeys.EU_TO_START);
        if (eu_to_start > energyContainer.getEnergyCapacity()) {
            setIdleReason(IdleReason.INSUFFICIENT_ENERGY_BUFFER);
            return null;
        }
        long heatDiff = eu_to_start - heat;
        if (heatDiff > 0) {
            if (energyContainer.getEnergyStored() < heatDiff) {
                setIdleReason(IdleReason.INSUFFICIENT_ENERGY_BUFFER);
                return null;
            }
            energyContainer.removeEnergy(heatDiff);
            heat += heatDiff;
        }
        return super.getRealRecipe(unit, recipe);
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
    public void onWorking() {
        if (color == -1) {
            GTRecipe recipe = recipeLogic.getLastRecipe();
            assert recipe != null;
            if (!recipe.fluidOutputs.isEmpty()) {
                var fluid = recipe.fluidOutputs.getFirst().inner.getFluid();
                if (fluid != null) {
                    int newColor = -16777216 | GTUtil.getFluidColor(fluid);
                    if (color != newColor) {
                        color = newColor;
                    }
                }
            }
        }
        super.onWorking();
    }

    @Override
    public void onWaiting() {
        super.onWaiting();
        color = -1;
    }

    @Override
    public void afterWorking() {
        super.afterWorking();
        color = -1;
    }

    @Override
    public void customText(List<Component> textList) {
        super.customText(textList);
        textList.add(Component.translatable("gtceu.multiblock.fusion_reactor.energy", this.energyContainer.getEnergyStored(), this.energyContainer.getEnergyCapacity()));
        textList.add(Component.translatable("gtceu.multiblock.fusion_reactor.heat", heat));
    }

    @Override
    public int getTier() {
        return tier + calculateBonusTier();
    }
}
