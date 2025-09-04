package com.gtocore.mixin.gtm.item;

import com.gtocore.api.data.material.GTOMaterialIconSet;

import com.gtolib.api.data.chemical.material.GTOMaterial;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.block.MaterialBlock;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.item.MaterialBlockItem;
import com.gregtechceu.gtceu.api.item.component.ICustomRenderer;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;

import com.llamalad7.mixinextras.sugar.Local;
import com.lowdragmc.lowdraglib.client.renderer.IRenderer;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MaterialBlockItem.class)
public abstract class MaterialBlockItemMixin extends BlockItem {

    @Shadow(remap = false)
    public abstract @NotNull MaterialBlock getBlock();

    @Unique
    private ICustomRenderer gtolib$customRenderer;

    protected MaterialBlockItemMixin(Block block, Properties properties) {
        super(block, properties);
    }

    @ModifyVariable(method = "<init>", at = @At("HEAD"), index = 2, argsOnly = true, remap = false)
    private static Properties init(Properties value, @Local(argsOnly = true) Material mat) {
        if (mat instanceof GTOMaterial material) {
            Rarity rarity = material.gtolib$rarity();
            if (rarity != null) value.rarity(rarity);
        }
        return value;
    }

    @Inject(method = "<init>", at = @At("RETURN"), remap = false)
    private void MaterialBlockItem(Block block, Properties properties, TagPrefix tagPrefix, Material material, CallbackInfo ci) {
        if (GTCEu.isClientSide()) {
            if (material.getMaterialIconSet() instanceof GTOMaterialIconSet iconSet) {
                gtolib$customRenderer = iconSet.getCustomRenderer();
            }
        }
    }

    @Inject(method = "getRenderer", at = @At("HEAD"), remap = false, cancellable = true)
    private void getRenderer(ItemStack stack, CallbackInfoReturnable<IRenderer> cir) {
        if (gtolib$customRenderer != null) {
            cir.setReturnValue(gtolib$customRenderer.getRenderer());
        }
    }

    @Override
    public boolean isFoil(@NotNull ItemStack itemStack) {
        if (getBlock().material instanceof GTOMaterial gtoMaterial && gtoMaterial.gtolib$glow()) return true;
        return super.isFoil(itemStack);
    }
}
