package com.gto.gtocore.mixin.emi;

import com.gregtechceu.gtceu.api.item.MetaMachineItem;

import net.minecraft.network.chat.Component;

import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.ItemEmiStack;
import dev.emi.emi.search.EmiSearch;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(EmiSearch.class)
public class EmiSearchMixin {

    @Redirect(method = "bake", at = @At(value = "INVOKE", target = "Ldev/emi/emi/api/stack/EmiStack;getTooltipText()Ljava/util/List;"), remap = false)
    private static List<Component> getTooltipText(EmiStack instance) {
        if (instance instanceof ItemEmiStack itemEmiStack && itemEmiStack.getKey() instanceof MetaMachineItem) {
            return instance.getTooltipText();
        }
        return null;
    }
}
