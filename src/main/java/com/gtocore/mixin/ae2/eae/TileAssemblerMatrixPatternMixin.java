package com.gtocore.mixin.ae2.eae;

import com.gtocore.integration.ae.hooks.IExtendedPatternContainer;

import com.glodblock.github.extendedae.common.tileentities.matrix.TileAssemblerMatrixPattern;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TileAssemblerMatrixPattern.class)
public abstract class TileAssemblerMatrixPatternMixin implements IExtendedPatternContainer {

    @Override
    public boolean gto$isCraftingContainer() {
        return true;
    }
}
