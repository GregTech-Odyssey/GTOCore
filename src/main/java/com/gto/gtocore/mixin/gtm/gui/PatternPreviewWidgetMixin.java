package com.gto.gtocore.mixin.gtm.gui;

import com.gregtechceu.gtceu.api.gui.widget.PatternPreviewWidget;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(PatternPreviewWidget.class)
public class PatternPreviewWidgetMixin {

    @ModifyConstant(method = "setPage", remap = false, constant = @Constant(intValue = 18, ordinal = 0))
    private int modifyContainer(int constant) {
        return 36;
    }
}
