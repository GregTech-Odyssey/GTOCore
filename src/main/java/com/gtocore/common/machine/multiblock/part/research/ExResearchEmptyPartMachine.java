package com.gtocore.common.machine.multiblock.part.research;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.gui.GuiTextures;

import net.minecraft.MethodsReturnNonnullByDefault;

import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ExResearchEmptyPartMachine extends ExResearchBasePartMachine {

    public ExResearchEmptyPartMachine(MetaMachineBlockEntity holder) {
        super(holder, 3);
    }

    @Override
    public ResourceTexture getComponentIcon() {
        return GuiTextures.HPCA_ICON_EMPTY_COMPONENT;
    }

    @Override
    public int getUpkeepEUt() {
        return 0;
    }

    @Override
    public boolean canBeDamaged() {
        return false;
    }
}
