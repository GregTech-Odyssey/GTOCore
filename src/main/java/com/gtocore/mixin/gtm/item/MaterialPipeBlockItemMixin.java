package com.gtocore.mixin.gtm.item;

import com.gtolib.api.data.chemical.material.GTOMaterial;

import com.gregtechceu.gtceu.api.block.MaterialPipeBlock;
import com.gregtechceu.gtceu.api.block.PipeBlock;
import com.gregtechceu.gtceu.api.item.MaterialPipeBlockItem;
import com.gregtechceu.gtceu.api.item.PipeBlockItem;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import com.llamalad7.mixinextras.sugar.Local;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(MaterialPipeBlockItem.class)
public abstract class MaterialPipeBlockItemMixin extends PipeBlockItem {

    @Shadow(remap = false)
    public abstract @NotNull MaterialPipeBlock getBlock();

    protected MaterialPipeBlockItemMixin(PipeBlock block, Properties properties) {
        super(block, properties);
    }

    @ModifyVariable(method = "<init>", at = @At("HEAD"), index = 2, argsOnly = true, remap = false)
    private static Properties init(Properties value, @Local(argsOnly = true) MaterialPipeBlock block) {
        if (block.material instanceof GTOMaterial material) {
            Rarity rarity = material.gtolib$rarity();
            if (rarity != null) value.rarity(rarity);
        }
        return value;
    }

    @Override
    public boolean isFoil(@NotNull ItemStack itemStack) {
        if (getBlock().material instanceof GTOMaterial gtoMaterial && gtoMaterial.gtolib$glow()) return true;
        return super.isFoil(itemStack);
    }
}
