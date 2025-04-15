package com.gto.gtocore.common.machine.multiblock.electric;

import com.gto.gtocore.api.machine.multiblock.ElectricMultiblockMachine;
import com.gto.gtocore.common.machine.multiblock.part.ae.MECraftPatternPartMachine;
import com.gto.gtocore.common.machine.trait.CraftProcessLogic;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CraftProcessMachine extends ElectricMultiblockMachine {

    private final List<MECraftPatternPartMachine> craftPatternPartMachines = new ArrayList<>();

    public CraftProcessMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        int machineIndex = 0;
        for (IMultiPart part : getParts()) {
            if (part instanceof MECraftPatternPartMachine machine) {
                int finalMachineIndex = machineIndex;
                machine.setUpdateCallback(index -> getRecipeLogic().generateRecipes(finalMachineIndex));
                craftPatternPartMachines.add(machine);
                machineIndex++;
            }
        }
        getRecipeLogic().generateRecipes(-1);
    }

    @Override
    protected @NotNull CraftProcessLogic createRecipeLogic(Object @NotNull... args) {
        return new CraftProcessLogic(this);
    }

    @Override
    public @NotNull CraftProcessLogic getRecipeLogic() {
        return (CraftProcessLogic) super.getRecipeLogic();
    }
}
