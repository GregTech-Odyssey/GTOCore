package com.gto.gtocore.mixin.gtm.renderer;

import com.gto.gtocore.api.data.chemical.material.info.GTOMaterialIconSet;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.item.TagPrefixItem;
import com.gregtechceu.gtceu.api.item.component.ICustomRenderer;

import com.lowdragmc.lowdraglib.Platform;
import com.lowdragmc.lowdraglib.client.renderer.IItemRendererProvider;
import com.lowdragmc.lowdraglib.client.renderer.IRenderer;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TagPrefixItem.class)
public class TagPrefixItemMixin implements IItemRendererProvider {

    @Unique
    private ICustomRenderer gtocore$customRenderer;

    @Inject(method = "<init>(Lnet/minecraft/world/item/Item$Properties;Lcom/gregtechceu/gtceu/api/data/tag/TagPrefix;Lcom/gregtechceu/gtceu/api/data/chemical/material/Material;)V", at = @At(value = "RETURN"), remap = false)
    private void TagPrefixItem(Item.Properties properties, TagPrefix tagPrefix, Material material, CallbackInfo ci) {
        if (Platform.isClient()) {
            if (material.getMaterialIconSet() instanceof GTOMaterialIconSet iconSet) {
                this.gtocore$customRenderer = iconSet.getCustomRenderer();
            }
        }
    }

    @Nullable
    @Override
    public IRenderer getRenderer(ItemStack stack) {
        if (gtocore$customRenderer != null) {
            return gtocore$customRenderer.getRenderer();
        }
        return null;
    }
}
