package com.gto.gtocore.mixin.farmersdelight;

import com.gto.gtocore.api.item.IContinuousCraftingItem;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeItem;

import org.spongepowered.asm.mixin.Mixin;
import vectorwing.farmersdelight.common.item.KnifeItem;

@Mixin(KnifeItem.class)
public abstract class KnifeItemMixin implements IContinuousCraftingItem, IForgeItem {

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        return definition$getCraftingRemainingItem(itemStack);
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }
}
