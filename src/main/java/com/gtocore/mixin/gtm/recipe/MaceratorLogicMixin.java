package com.gtocore.mixin.gtm.recipe;

import com.gregtechceu.gtceu.common.machine.trait.customlogic.MaceratorLogic;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(MaceratorLogic.class)
public class MaceratorLogicMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void buildRepresentativeRecipes() {}
}
