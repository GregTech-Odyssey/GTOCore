package com.gtocore.common.machine.multiblock.part.research;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.gui.GuiTextures;

import net.minecraft.MethodsReturnNonnullByDefault;

import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ExResearchBridgePartMachine extends ExResearchBasePartMachine {

    public ExResearchBridgePartMachine(MetaMachineBlockEntity holder, int tier) {
        super(holder, tier);
    }

    @Override
    public ResourceTexture getComponentIcon() {
        return GuiTextures.HPCA_ICON_BRIDGE_COMPONENT;
    }

    @Override
    public int getUpkeepEUt() {
        return GTValues.VA[GTValues.UHV];
    }

    @Override
    public boolean canBeDamaged() {
        return false;
    }

    @Override
    public boolean doesAllowBridging() {
        return true;
    }
}
