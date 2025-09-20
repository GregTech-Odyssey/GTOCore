package com.gtocore.common.machine.multiblock.noenergy;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.misc.EnergyContainerList;
import com.gtocore.common.machine.multiblock.part.ae.MEEnergyAccessPartMachine;
import com.gtolib.api.machine.feature.multiblock.IMultiblockTraitHolder;
import com.gtolib.api.machine.feature.multiblock.ITierCasingMachine;
import com.gtolib.api.machine.multiblock.NoRecipeLogicMultiblockMachine;
import com.gtolib.api.machine.trait.TierCasingTrait;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.List;

import static com.gtolib.api.GTOValues.GLASS_TIER;

public class MEEnergySubstationMachine extends NoRecipeLogicMultiblockMachine implements ITierCasingMachine, IMultiblockTraitHolder {
    private final TierCasingTrait tierCasingTrait;
    private EnergyContainerList inputHatches=null;

    public MEEnergySubstationMachine(MetaMachineBlockEntity holder) {
        super(holder);
        tierCasingTrait = new TierCasingTrait(this, GLASS_TIER);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        List<IEnergyContainer> inputs = new ObjectArrayList<>();
        for(IMultiPart part : this.getParts()) {
            for (var handlerList : part.getRecipeHandlers()) {
                    var containers = handlerList.getCapability(EURecipeCapability.CAP).stream().filter(IEnergyContainer.class::isInstance).map(IEnergyContainer.class::cast).toList();
                    if (handlerList.getHandlerIO() == IO.IN) {
                        inputs.addAll(containers);
                    }
                }
        }
        this.inputHatches = new EnergyContainerList(inputs);
        for(IMultiPart part : this.getParts()){
            if(part instanceof MEEnergyAccessPartMachine me_part)me_part.onFormatted(this);
        }
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        this.inputHatches=null;
    }

    @Override
    public Object2IntMap<String> getCasingTiers() {
        return tierCasingTrait.getCasingTiers();
    }


    public EnergyContainerList getEnergyContainer() {
        return inputHatches;
    }
}
