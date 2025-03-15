package com.gto.gtocore.mixin.gtm.item;

import com.gto.gtocore.api.data.chemical.material.GTOMaterial;
import com.gto.gtocore.api.data.chemical.material.info.GTOMaterialIconSet;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.block.MaterialBlock;
import com.gregtechceu.gtceu.api.item.MaterialBlockItem;
import com.gregtechceu.gtceu.api.item.component.ICustomRenderer;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
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
    private ICustomRenderer gtocore$customRenderer;

    protected MaterialBlockItemMixin(Block block, Properties properties) {
        super(block, properties);
    }

    @ModifyVariable(method = "<init>", at = @At("HEAD"), index = 2, argsOnly = true, remap = false)
    private static Item.Properties init(Item.Properties value, @Local(argsOnly = true) MaterialBlock block) {
        if (block.material instanceof GTOMaterial material) {
            Rarity rarity = material.gtocore$rarity();
            if (rarity != null) value.rarity(rarity);
        }
        return value;
    }

    @Inject(method = "<init>", at = @At("RETURN"), remap = false)
    private void MaterialBlockItem(MaterialBlock block, Item.Properties properties, CallbackInfo ci) {
        if (GTCEu.isClientSide()) {
            if (block.material.getMaterialIconSet() instanceof GTOMaterialIconSet iconSet) {
                gtocore$customRenderer = iconSet.getCustomRenderer();
            }
        }
    }

    @Inject(method = "getRenderer", at = @At("HEAD"), remap = false, cancellable = true)
    private void getRenderer(ItemStack stack, CallbackInfoReturnable<IRenderer> cir) {
        if (gtocore$customRenderer != null) {
            cir.setReturnValue(gtocore$customRenderer.getRenderer());
        }
    }

    @Override
    public boolean isFoil(@NotNull ItemStack itemStack) {
        if (getBlock().material instanceof GTOMaterial gtoMaterial && gtoMaterial.gtocore$glow()) return true;
        return super.isFoil(itemStack);
    }
}
