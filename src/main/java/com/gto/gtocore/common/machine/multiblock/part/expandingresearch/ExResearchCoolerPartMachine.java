package com.gto.gtocore.common.machine.multiblock.part.expandingresearch;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.IHPCACoolantProvider;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import net.minecraft.MethodsReturnNonnullByDefault;

import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import lombok.Getter;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.GTValues.UEV;

@Getter
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ExResearchCoolerPartMachine extends ExResearchBasePartMachine implements IHPCACoolantProvider {

    private final int tire;

    public ExResearchCoolerPartMachine(IMachineBlockEntity holder, int tire) {
        super(holder);
        this.tire = tire;
    }

    @Override
    public ResourceTexture getComponentIcon() {
        if (tire == 3) return GuiTextures.HPCA_ICON_ACTIVE_COOLER_COMPONENT;
        else if (tire == 4) return GuiTextures.HPCA_ICON_ACTIVE_COOLER_COMPONENT;
        else if (tire == 5) return GuiTextures.HPCA_ICON_ACTIVE_COOLER_COMPONENT;
        else return GuiTextures.HPCA_ICON_ACTIVE_COOLER_COMPONENT;
    }

    @Override
    public int getUpkeepEUt() {
        if (tire == 3) return GTValues.VA[LuV];
        else if (tire == 4) return GTValues.VA[UV];
        else if (tire == 5) return GTValues.VA[UHV];
        else return GTValues.VA[UEV];
    }

    @Override
    public boolean canBeDamaged() {
        return false;
    }

    @Override
    public int getCoolingAmount() {
        if (tire == 3) return 8;
        else if (tire == 4) return 16;
        else if (tire == 5) return 64;
        else return 128;
    }

    @Override
    public boolean isActiveCooler() {
        return true;
    }

    @Override
    public int getMaxCoolantPerTick() {
        if (tire == 3) return 80;
        else if (tire == 4) return 160;
        else if (tire == 5) return 640;
        else return 1280;
    }
}
