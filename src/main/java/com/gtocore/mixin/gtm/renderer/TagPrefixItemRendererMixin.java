package com.gtocore.mixin.gtm.renderer;

import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconType;
import com.gregtechceu.gtceu.client.renderer.item.TagPrefixItemRenderer;

import net.minecraft.world.item.Item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TagPrefixItemRenderer.class)
public class TagPrefixItemRendererMixin {

    @Inject(method = "create", at = @At("HEAD"), remap = false, cancellable = true)
    private static void create(Item item, MaterialIconType type, MaterialIconSet iconSet, CallbackInfo ci) {
        if (iconSet == GTOMaterials.Cosmic.getMaterialIconSet()) {
            ci.cancel();
        }
    }
}
