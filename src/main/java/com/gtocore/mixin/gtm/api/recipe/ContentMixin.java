package com.gtocore.mixin.gtm.api.recipe;

import com.gregtechceu.gtceu.api.recipe.content.Content;

import com.lowdragmc.lowdraglib.utils.ColorUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Content.class)
public class ContentMixin {

    /**
     * @return color with alpha using sin function
     * @author Kovrax
     * @reason add fade animation to not-consumed item in recipe viewer GUI
     */
    @ModifyVariable(method = "drawChance", at = @At("STORE"), name = "color", remap = false)
    private int drawChance(int color) {
        if (color != 0xFF0000) return color;
        double progress = Math.abs(System.currentTimeMillis() % 4000) / 4000.0d;
        float alpha = (float) ((Math.cos(progress * 2 * Math.PI) + 1) / 2.2 + 0.05);
        return ColorUtils.color(alpha, 1f, 0.0f, 0.0f);
    }
}
