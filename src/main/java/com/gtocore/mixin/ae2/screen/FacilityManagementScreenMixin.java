package com.gtocore.mixin.ae2.screen;

import com.gtocore.integration.jech.PinYinUtils;

import gto_ae.client.gui.me.facility_management.FacilityManagementScreen;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FacilityManagementScreen.class)
public class FacilityManagementScreenMixin {

    @Redirect(method = "refreshList", at = @At(value = "INVOKE", target = "Ljava/lang/String;contains(Ljava/lang/CharSequence;)Z"), remap = false)
    private boolean onReturnToMainMenu(String instance, CharSequence s) {
        return PinYinUtils.match(instance, s);
    }
}
