package com.gtocore.mixin.gtm.registry;

import com.gtolib.api.recipe.modifier.RecipeModifierFunction;
import com.gtolib.api.recipe.modifier.RecipeModifierFunctionList;

import com.gregtechceu.gtceu.api.recipe.modifier.RecipeModifier;
import com.gregtechceu.gtceu.api.registry.registrate.MachineBuilder;

import com.tterrag.registrate.Registrate;
import org.apache.commons.lang3.function.TriFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.BiFunction;
import java.util.function.Function;

@Mixin(MachineBuilder.class)
public class MachineBuilderMixin {

    @Shadow(remap = false)
    private RecipeModifier recipeModifier;

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void init(Registrate registrate, String name, Function definition, Function machine, BiFunction blockFactory, BiFunction itemFactory, TriFunction blockEntityFactory, CallbackInfo ci) {
        recipeModifier = RecipeModifierFunction.OVERCLOCKING;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public MachineBuilder recipeModifier(RecipeModifier recipeModifier) {
        this.recipeModifier = recipeModifier instanceof RecipeModifierFunctionList list ? list : new RecipeModifierFunctionList(recipeModifier);
        return (MachineBuilder) (Object) this;
    }

    @Inject(method = "recipeModifiers([Lcom/gregtechceu/gtceu/api/recipe/modifier/RecipeModifier;)Lcom/gregtechceu/gtceu/api/registry/registrate/MachineBuilder;", at = @At("HEAD"), remap = false, cancellable = true)
    private void injectRecipeModifiers(RecipeModifier[] recipeModifiers, CallbackInfoReturnable<MachineBuilder> cir) {
        this.recipeModifier = new RecipeModifierFunctionList(recipeModifiers);
        cir.setReturnValue((MachineBuilder) (Object) this);
    }
}
