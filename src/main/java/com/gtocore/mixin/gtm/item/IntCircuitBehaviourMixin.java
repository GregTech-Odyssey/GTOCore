package com.gtocore.mixin.gtm.item;

import com.gregtechceu.gtceu.api.recipe.ingredient.IntCircuitIngredient;
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(IntCircuitBehaviour.class)
public final class IntCircuitBehaviourMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static void setCircuitConfiguration(ItemStack itemStack, int configuration) {
        var tagCompound = itemStack.getOrCreateTag();
        tagCompound.putInt(IntCircuitIngredient.Configuration, configuration);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static int getCircuitConfiguration(ItemStack itemStack) {
        if (!itemStack.is(IntCircuitIngredient.PROGRAMMED_CIRCUIT)) return 0;
        var tagCompound = itemStack.getTag();
        if (tagCompound != null && tagCompound.tags.get(IntCircuitIngredient.Configuration) instanceof IntTag intTag) {
            return intTag.getAsInt();
        }
        return 0;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static boolean isIntegratedCircuit(ItemStack itemStack) {
        boolean isCircuit = itemStack.is(IntCircuitIngredient.PROGRAMMED_CIRCUIT);
        if (isCircuit && itemStack.getTag() == null) {
            var compound = new CompoundTag();
            compound.putInt(IntCircuitIngredient.Configuration, 0);
            itemStack.setTag(compound);
        }
        return isCircuit;
    }
}
