package com.gtocore.mixin.ae2.pattern;

import com.gtolib.api.ae2.pattern.IDetails;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.registry.GTRegistries;

import net.minecraft.nbt.StringTag;

import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.KeyCounter;
import appeng.crafting.pattern.AEProcessingPattern;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AEProcessingPattern.class)
public abstract class AEProcessingPatternMixin implements IDetails {

    @Unique
    private KeyCounter[] gtolib$inputHolder;

    @Unique
    private GTRecipeType gtolib$recipeType;

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void gtolib$init(AEItemKey definition, CallbackInfo ci) {
        if (definition.getTag().tags.get("type") instanceof StringTag stringTag) {
            gtolib$recipeType = GTRegistries.RECIPE_TYPES.get(GTCEu.id(stringTag.getAsString()));
        }
    }

    @Override
    public KeyCounter[] gtolib$getInputHolder() {
        if (gtolib$inputHolder == null) {
            var length = getInputs().length;
            gtolib$inputHolder = new KeyCounter[length];
            for (int i = 0; i < length; i++) {
                gtolib$inputHolder[i] = new KeyCounter();
            }
        }
        return gtolib$inputHolder;
    }

    @Override
    public GTRecipeType getRecipeType() {
        return gtolib$recipeType;
    }
}
