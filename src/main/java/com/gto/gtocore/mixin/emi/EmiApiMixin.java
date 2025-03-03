package com.gto.gtocore.mixin.emi;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import dev.emi.emi.api.EmiApi;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.ItemEmiStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * @author EasterFG on 2025/3/2
 */
@Mixin(EmiApi.class)
public abstract class EmiApiMixin {

    @ModifyVariable(method = "displayUses",
                    at = @At(value = "INVOKE",
                             target = "Ldev/emi/emi/api/stack/EmiIngredient;isEmpty()Z"),
                    remap = false,
                    argsOnly = true)
    private static EmiIngredient modifyDisplayUses(EmiIngredient stack) {
        return stack.isEmpty() ? stack : gtocore$getBucketFluid(stack);
    }

    @ModifyVariable(method = "displayRecipes",
                    at = @At(value = "INVOKE",
                             target = "Ljava/util/List;size()I"),
                    remap = false,
                    argsOnly = true)
    private static EmiIngredient modifyDisplayRecipes(EmiIngredient stack) {
        return stack.getEmiStacks().size() != 1 ? stack : gtocore$getBucketFluid(stack);
    }

    @Unique
    private static EmiIngredient gtocore$getBucketFluid(EmiIngredient stack) {
        if (stack instanceof ItemEmiStack itemEmiStack && itemEmiStack.getKey() instanceof BucketItem bucketItem) {
            Fluid fluid = bucketItem.getFluid();
            return fluid == Fluids.EMPTY ? stack : EmiStack.of(fluid);
        }
        return stack;
    }
}
