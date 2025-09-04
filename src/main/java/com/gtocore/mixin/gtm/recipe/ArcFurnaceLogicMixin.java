package com.gtocore.mixin.gtm.recipe;

import com.gregtechceu.gtceu.common.machine.trait.customlogic.ArcFurnaceLogic;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ArcFurnaceLogic.class)
public class ArcFurnaceLogicMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void buildRepresentativeRecipes() {}
}
