package com.gtocore.common.machine.multiblock.part.research;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.IHPCACoolantProvider;
import com.gregtechceu.gtceu.api.gui.GuiTextures;

import net.minecraft.MethodsReturnNonnullByDefault;

import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.gregtechceu.gtceu.api.GTValues.*;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ExResearchCoolerPartMachine extends ExResearchBasePartMachine implements IHPCACoolantProvider {

    public ExResearchCoolerPartMachine(MetaMachineBlockEntity holder, int tier) {
        super(holder, tier);
    }

    @Override
    public ResourceTexture getComponentIcon() {
        if (tier == 3) return GuiTextures.HPCA_ICON_ACTIVE_COOLER_COMPONENT;
        else if (tier == 4) return GuiTextures.HPCA_ICON_ACTIVE_COOLER_COMPONENT;
        else if (tier == 5) return GuiTextures.HPCA_ICON_ACTIVE_COOLER_COMPONENT;
        else return GuiTextures.HPCA_ICON_ACTIVE_COOLER_COMPONENT;
    }

    @Override
    public int getUpkeepEUt() {
        if (tier == 3) return GTValues.VA[UV];
        else if (tier == 4) return GTValues.VA[UHV];
        else return GTValues.VA[UIV];
    }

    @Override
    public boolean canBeDamaged() {
        return false;
    }

    @Override
    public int getCoolingAmount() {
        if (tier == 3) return 8;
        else if (tier == 4) return 64;
        else return 256;
    }

    @Override
    public boolean isActiveCooler() {
        return true;
    }

    @Override
    public int getMaxCoolantPerTick() {
        if (tier == 3) return 80;
        else if (tier == 4) return 320;
        else return 1280;
    }
}
