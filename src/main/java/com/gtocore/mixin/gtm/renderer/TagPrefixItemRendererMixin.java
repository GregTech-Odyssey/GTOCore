package com.gtocore.mixin.gtm.renderer;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.data.pack.GTDynamicResourcePack;
import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconType;
import com.gregtechceu.gtceu.client.renderer.item.TagPrefixItemRenderer;

import com.gtolib.GTOCore;
import net.minecraft.data.models.model.DelegatedModel;
import net.minecraft.world.item.Item;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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
    @Inject(method = "reinitModels", at = @At("TAIL"), remap = false)
    private static void reinitModels(CallbackInfo ci) {
        for (var tagPrefix : TagPrefix.values()) {
            var iconType = tagPrefix.materialIconType();
            if (iconType == null) {
                continue;
            }
            GTDynamicResourcePack.addItemModel(GTOCore.id(tagPrefix.getLowerCaseName()),
                    () -> new DelegatedModel(iconType.getItemModelPath(MaterialIconSet.DULL, true)).get());
        }
    }
}
