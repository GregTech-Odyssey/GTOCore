package com.gtocore.mixin.botania;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.api.recipe.RunicAltarRecipe;
import vazkii.botania.client.integration.emi.RunicAltarEmiRecipe;
import vazkii.botania.common.item.material.RuneItem;

import java.util.Objects;

@Mixin(value = RunicAltarEmiRecipe.class, remap = false)
public abstract class RunicAltarEmiRecipeMixin {

    @Inject(method = "<init>(Lvazkii/botania/api/recipe/RunicAltarRecipe;)V", at = @At("TAIL"))
    private void undoRemainderForGtocoreRecipes(RunicAltarRecipe recipe, CallbackInfo ci) {
        // 1. 检查配方的命名空间是否为 "gtocore"
        EmiRecipe thisAsEmiRecipe = (EmiRecipe) this;
        if (Objects.requireNonNull(thisAsEmiRecipe.getId()).getNamespace().equals("gtocore")) {
            // 2. 如果是，遍历所有的输入原料
            for (EmiIngredient ingredient : thisAsEmiRecipe.getInputs()) {
                // 遍历一个原料槽位中所有可能的物品
                for (EmiStack stack : ingredient.getEmiStacks()) {
                    // 3. 检查这个物品是不是符文
                    if (stack.getItemStack().getItem() instanceof RuneItem) {
                        // 4. 撤销/移除它的返还设置
                        // 在EMI中，将返还物设置为空物品堆即可
                        stack.setRemainder(EmiStack.EMPTY);
                    }
                }
            }
        }
    }
}
