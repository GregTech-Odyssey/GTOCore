package com.gto.gtocore.mixin.emi;

import com.gto.gtocore.api.misc.PlayerUUID;
import com.gto.gtocore.client.ClientUtil;

import dev.emi.emi.api.stack.EmiStackInteraction;
import dev.emi.emi.config.EmiConfig;
import dev.emi.emi.input.EmiBind;
import dev.emi.emi.runtime.EmiReloadManager;
import dev.emi.emi.screen.EmiScreenManager;
import earth.terrarium.adastra.common.menus.PlanetsMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;

@Mixin(EmiScreenManager.class)
public final class EmiScreenManagerMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static boolean isDisabled() {
        return !EmiReloadManager.isLoaded() || !EmiConfig.enabled || ClientUtil.getPlayer().containerMenu instanceof PlanetsMenu;
    }

    @Inject(method = "stackInteraction", at = @At(value = "INVOKE", target = "Ldev/emi/emi/screen/EmiScreenManager;craftInteraction(Ldev/emi/emi/api/stack/EmiIngredient;Ljava/util/function/Supplier;Ldev/emi/emi/api/stack/EmiStackInteraction;Ljava/util/function/Function;)Z", ordinal = 0), remap = false)
    private static void head(EmiStackInteraction stack, Function<EmiBind, Boolean> function, CallbackInfoReturnable<Boolean> cir) {
        if (PlayerUUID.isDeveloper(ClientUtil.getPlayer())) EmiConfig.cheatMode = true;
    }

    @Inject(method = "stackInteraction", at = @At(value = "INVOKE", target = "Ljava/util/function/Function;apply(Ljava/lang/Object;)Ljava/lang/Object;", ordinal = 6), remap = false)
    private static void tail(EmiStackInteraction stack, Function<EmiBind, Boolean> function, CallbackInfoReturnable<Boolean> cir) {
        if (PlayerUUID.isDeveloper(ClientUtil.getPlayer())) EmiConfig.cheatMode = false;
    }
}
