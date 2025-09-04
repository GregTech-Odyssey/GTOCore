package com.gtocore.mixin.gtm.item;

import com.gtocore.api.data.material.GTOMaterialIconSet;
import com.gtocore.api.data.tag.GTOTagPrefix;

import com.gtolib.api.data.chemical.material.GTOMaterial;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.item.TagPrefixItem;
import com.gregtechceu.gtceu.api.item.component.ICustomRenderer;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import com.lowdragmc.lowdraglib.client.renderer.IItemRendererProvider;
import com.lowdragmc.lowdraglib.client.renderer.IRenderer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TagPrefixItem.class)
public class TagPrefixItemMixin extends Item implements IItemRendererProvider {

    @Shadow(remap = false)
    @Final
    public Material material;

    @Unique
    private ICustomRenderer gtolib$customRenderer;

    public TagPrefixItemMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "<init>(Lnet/minecraft/world/item/Item$Properties;Lcom/gregtechceu/gtceu/api/data/tag/TagPrefix;Lcom/gregtechceu/gtceu/api/data/chemical/material/Material;)V", at = @At(value = "RETURN"), remap = false)
    private void TagPrefixItem(Properties properties, TagPrefix tagPrefix, Material material, CallbackInfo ci) {
        if (GTCEu.isClientSide()) {
            if (material.getMaterialIconSet() instanceof GTOMaterialIconSet iconSet) {
                gtolib$customRenderer = iconSet.getCustomRenderer();
            } else if (tagPrefix instanceof GTOTagPrefix tagPrefix1 && tagPrefix1.customRenderer() != null) {
                gtolib$customRenderer = tagPrefix1.customRenderer();
            }
        }
    }

    @Override
    public boolean isFoil(@NotNull ItemStack itemStack) {
        if (material instanceof GTOMaterial gtoMaterial && gtoMaterial.gtolib$glow()) return true;
        return super.isFoil(itemStack);
    }

    @Nullable
    @Override
    public IRenderer getRenderer(ItemStack stack) {
        if (gtolib$customRenderer != null) {
            return gtolib$customRenderer.getRenderer();
        }
        return null;
    }
}
