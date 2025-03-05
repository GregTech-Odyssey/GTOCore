package com.gto.gtocore.common.machine.multiblock.part.expandingresearch;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.IHPCAComputationProvider;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import net.minecraft.MethodsReturnNonnullByDefault;

import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import lombok.Getter;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.gregtechceu.gtceu.api.GTValues.*;

@Getter
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ExResearchComputationPartMachine extends ExResearchBasePartMachine implements IHPCAComputationProvider {

    private final int tire;

    public ExResearchComputationPartMachine(IMachineBlockEntity holder, int tire) {
        super(holder);
        this.tire = tire;
    }

    @Override
    public ResourceTexture getComponentIcon() {
        if (isDamaged()) {
            if (tire == 3) return GuiTextures.HPCA_ICON_DAMAGED_ADVANCED_COMPUTATION_COMPONENT;
            else if (tire == 4) return GuiTextures.HPCA_ICON_DAMAGED_ADVANCED_COMPUTATION_COMPONENT;
            else if (tire == 5) return GuiTextures.HPCA_ICON_DAMAGED_ADVANCED_COMPUTATION_COMPONENT;
            else return GuiTextures.HPCA_ICON_DAMAGED_ADVANCED_COMPUTATION_COMPONENT;
        }
        if (tire == 3) return GuiTextures.HPCA_ICON_ADVANCED_COMPUTATION_COMPONENT;
        else if (tire == 4) return GuiTextures.HPCA_ICON_ADVANCED_COMPUTATION_COMPONENT;
        else if (tire == 5) return GuiTextures.HPCA_ICON_ADVANCED_COMPUTATION_COMPONENT;
        else return GuiTextures.HPCA_ICON_ADVANCED_COMPUTATION_COMPONENT;
    }

    @Override
    public int getUpkeepEUt() {
        if (tire == 3) return GTValues.VA[LuV];
        else if (tire == 4) return GTValues.VA[UV];
        else if (tire == 5) return GTValues.VA[UHV];
        else return GTValues.VA[UEV];
    }

    @Override
    public int getMaxEUt() {
        if (tire == 3) return GTValues.VA[UV];
        else if (tire == 4) return GTValues.VA[UEV];
        else if (tire == 5) return GTValues.VA[UIV];
        else return GTValues.VA[OpV];
    }

    @Override
    public int getCWUPerTick() {
        if (isDamaged()) return 0;
        else if (tire == 3) return 64;
        else if (tire == 4) return 256;
        else if (tire == 5) return 1024;
        else return 4096;
    }

    @Override
    public int getCoolingPerTick() {
        if (tire == 3) return 16;
        else if (tire == 4) return 32;
        else if (tire == 5) return 256;
        else return 512;
    }

    @Override
    public boolean canBeDamaged() {
        return true;
    }
}
