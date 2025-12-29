package com.gtocore.mixin.ae2.gui;

import com.gtolib.api.emi.stack.EmiTagPrefixUtils;
import com.gtolib.api.emi.stack.EmiTagprefixStack;

import appeng.api.stacks.GenericStack;
import appeng.integration.modules.emi.EmiStackHelper;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Set;

@Mixin(targets = "appeng.integration.modules.emi.EmiAeBaseScreenDragDropHandler")
public class DragDropMixin {

    @Redirect(method = "dropStack", at = @At(value = "INVOKE", target = "Lappeng/integration/modules/emi/EmiStackHelper;toGenericStack(Ldev/emi/emi/api/stack/EmiStack;)Lappeng/api/stacks/GenericStack;", remap = false), remap = false)
    private GenericStack redirectToGenericStack(EmiStack emiStack) {
        if (emiStack instanceof EmiTagprefixStack stack) {
            return EmiTagPrefixUtils.toGenericStack(stack);
        } else {
            return EmiStackHelper.toGenericStack(emiStack);
        }
    }

    @ModifyExpressionValue(method = "render", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;collect(Ljava/util/stream/Collector;)Ljava/lang/Object;", remap = false), remap = false)
    private Object modifyToGenericStack(Object original, @Local(argsOnly = true) EmiIngredient dragged) {
        ((Set<GenericStack>) original).addAll(
                dragged.getEmiStacks().stream()
                        .filter(EmiTagprefixStack.class::isInstance)
                        .map(stack -> EmiTagPrefixUtils.toGenericStack(((EmiTagprefixStack) stack))).toList());
        return original;
    }
}
