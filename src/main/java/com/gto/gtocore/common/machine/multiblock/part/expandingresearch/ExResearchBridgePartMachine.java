package com.gto.gtocore.common.machine.multiblock.part.expandingresearch;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import net.minecraft.MethodsReturnNonnullByDefault;

import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import lombok.Getter;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ExResearchBridgePartMachine extends ExResearchBasePartMachine {

    @Getter
    private final int tire;

    public ExResearchBridgePartMachine(IMachineBlockEntity holder, int tire) {
        super(holder);
        this.tire = tire;
    }

    @Override
    public boolean doesAllowBridging() {
        return true;
    }

    @Override
    public ResourceTexture getComponentIcon() {
        return GuiTextures.HPCA_ICON_BRIDGE_COMPONENT;
    }

    @Override
    public int getUpkeepEUt() {
        return GTValues.VA[GTValues.UEV];
    }

    @Override
    public boolean canBeDamaged() {
        return false;
    }
}
