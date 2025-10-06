package com.gtocore.mixin.ae2.blockentity;

import appeng.api.parts.IPartItem;
import appeng.parts.automation.AbstractLevelEmitterPart;
import appeng.parts.automation.UpgradeablePart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractLevelEmitterPart.class)
public abstract class AbstractLevelEmitterPartMixin extends UpgradeablePart {

    protected AbstractLevelEmitterPartMixin(IPartItem<?> partItem) {
        super(partItem);
    }

    @Inject(method = "updateState", at = @At("RETURN"), remap = false)
    private void updateState(CallbackInfo ci) {
        this.getHost().markForSave();
    }
}
