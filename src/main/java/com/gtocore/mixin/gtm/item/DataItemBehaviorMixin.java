package com.gtocore.mixin.gtm.item;

import com.gregtechceu.gtceu.common.item.DataItemBehavior;
import com.gregtechceu.gtceu.utils.ResearchManager;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

import com.lowdragmc.lowdraglib.utils.LocalizationUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DataItemBehavior.class)
public abstract class DataItemBehaviorMixin {

    @Redirect(method = "appendHoverText", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/Component;translatable(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;", ordinal = 0))
    private MutableComponent replaceTitleComponent(String key, ItemStack stack) {
        ResearchManager.ResearchItem researchData = ResearchManager.readResearchId(stack);
        if (researchData != null) {
            return Component.translatable("item.gtocore.data_item.type.title", LocalizationUtils.format(researchData.recipeType().toString().replace(":", ".")));
        }
        return Component.translatable(key);
    }
}
