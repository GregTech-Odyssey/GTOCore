package com.gto.gtocore.mixin.gtm.api.machine;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.steam.SteamEnergyRecipeHandler;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.utils.GTMath;

import org.spongepowered.asm.mixin.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mixin(SteamEnergyRecipeHandler.class)
public class SteamEnergyRecipeHandlerMixin {

    @Shadow(remap = false)
    @Final
    private double conversionRate;

    @Shadow(remap = false)
    @Final
    private NotifiableFluidTank steamTank;

    @Unique
    private static final FluidIngredient gtocore$INGREDIENT = FluidIngredient.of(GTMaterials.Steam.getFluid(1));

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public List<Long> handleRecipeInner(IO io, GTRecipe recipe, List<Long> left, boolean simulate) {
        long eut = left.stream().reduce(0L, Long::sum);
        int totalSteam = GTMath.saturatedCast((long) Math.ceil(eut * conversionRate));
        if (totalSteam > 0) {
            var steam = gtocore$INGREDIENT.copy();
            steam.setAmount(totalSteam);
            var list = new ArrayList<FluidIngredient>(1);
            list.add(steam);
            var leftSteam = steamTank.handleRecipeInner(io, recipe, list, simulate);
            if (leftSteam == null || leftSteam.isEmpty()) return null;
            eut = (long) (leftSteam.get(0).getAmount() / conversionRate);
        }
        return eut <= 0 ? null : Collections.singletonList(eut);
    }
}
