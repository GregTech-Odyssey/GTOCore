package com.gtocore.mixin.ae2;

import appeng.api.stacks.AEKey;
import appeng.helpers.InterfaceLogic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(InterfaceLogic.class)
public abstract class InterfaceLogicMixin {

    @Shadow(remap = false)
    protected abstract boolean handleCrafting(int x, AEKey key, long amount);

    @Unique
    private int gtolib$delay = 0;

    @Redirect(method = "tryUsePlan", at = @At(value = "INVOKE", target = "Lappeng/helpers/InterfaceLogic;handleCrafting(ILappeng/api/stacks/AEKey;J)Z", ordinal = 1), remap = false)
    private boolean handleCrafting(InterfaceLogic instance, int x, AEKey key, long amount) {
        if (gtolib$delay == 0) {
            gtolib$delay = 20;
            return handleCrafting(x, key, amount);
        }
        gtolib$delay--;
        return false;
    }
}
