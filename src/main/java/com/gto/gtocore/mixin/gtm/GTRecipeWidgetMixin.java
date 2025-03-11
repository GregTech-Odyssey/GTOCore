package com.gto.gtocore.mixin.gtm;

import com.gto.gtocore.common.data.GTORecipeModifiers;

import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import com.gregtechceu.gtceu.integration.xei.widgets.GTRecipeWidget;

import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GTRecipeWidget.class)
public abstract class GTRecipeWidgetMixin {

    @Shadow(remap = false)
    protected abstract void setTier(int tier);

    @Shadow(remap = false)
    private int tier;

    @Shadow(remap = false)
    protected abstract void setTierToMin();

    @Shadow(remap = false)
    protected abstract void setRecipeTextWidget(OverclockingLogic logic);

    @Shadow(remap = false)
    protected abstract void setRecipeWidget();

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void setRecipeOC(int button, boolean isShiftClick) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            setTier(tier + 1);
        } else if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            setTier(tier - 1);
        } else if (button == GLFW.GLFW_MOUSE_BUTTON_MIDDLE) {
            setTierToMin();
        }
        setRecipeTextWidget(new GTORecipeModifiers.Overclocking(isShiftClick));
        setRecipeWidget();
    }
}
