package com.gtocore.mixin.ae2.stacks;

import com.gtolib.api.fluid.IFluid;
import com.gtolib.api.recipe.lookup.IIngredientConvertible;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;

import appeng.api.stacks.AEFluidKey;

import com.fast.recipesearch.IntLongMap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AEFluidKey.class)
public class AEFluidKeyMixin implements IIngredientConvertible {

    @Shadow(remap = false)
    @Final
    private Fluid fluid;

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public ResourceLocation getId() {
        return ((IFluid) fluid).gtolib$getIdLocation();
    }

    @Override
    public void gtolib$convert(long amount, IntLongMap map) {
        map.add(((IFluid) fluid).gtolib$getMapFluid(), amount);
    }
}
