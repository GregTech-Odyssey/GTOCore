package com.gtocore.mixin.gtm.api.recipe;

import com.gtolib.api.recipe.Recipe;

import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ModifierFunction.FunctionBuilder.class)
public abstract class FunctionBuilderMixin {

    @Shadow(remap = false)
    private int parallels;

    @Shadow(remap = false)
    private ContentModifier durationModifier;

    @Shadow(remap = false)
    private ContentModifier eutModifier;

    @Shadow(remap = false)
    private int addOCs;

    @Shadow(remap = false)
    private ContentModifier inputModifier;

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public ModifierFunction build() {
        if (parallels == 0) return ModifierFunction.NULL;
        return recipe -> {
            Recipe copied = Recipe.of(recipe).modifier(inputModifier, false);
            copied.ocLevel = copied.ocLevel + addOCs;
            if (copied.data.getBoolean("duration_is_total_cwu")) {
                copied.duration = (int) Math.max(1, (copied.duration * (1.0F - 0.025F * addOCs)));
            } else {
                copied.duration = Math.max(1, durationModifier.apply(copied.duration));
            }
            if (eutModifier != ContentModifier.IDENTITY) {
                long preEUt = copied.eut;
                long eut = Math.max(1, eutModifier.apply(Math.abs(preEUt)));
                if (preEUt > 0) {
                    copied.setEut(eut);
                } else {
                    copied.setEut(-eut);
                }
            }
            return copied;
        };
    }
}
