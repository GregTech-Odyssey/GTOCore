package com.gto.gtocore.mixin.emi;

import net.minecraft.network.chat.Component;

import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.search.EmiSearch;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(EmiSearch.class)
public class EmiSearchMixin {

    @Redirect(method = "bake", at = @At(value = "INVOKE", target = "Ldev/emi/emi/api/stack/EmiStack;getTooltipText()Ljava/util/List;"), remap = false)
    private static List<Component> bake(EmiStack instance) {
        return null;
    }
}
