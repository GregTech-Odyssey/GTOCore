package com.gtocore.mixin.ae2.pattern;

import com.gtocore.api.ae2.pattern.IEncodingLogic;

import com.gtolib.api.ae2.pattern.IDetails;

import net.minecraft.nbt.CompoundTag;

import appeng.crafting.pattern.AEProcessingPattern;
import appeng.parts.encoding.PatternEncodingLogic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PatternEncodingLogic.class)
public abstract class PatternEncodingLogicMixin implements IEncodingLogic {

    @Shadow(remap = false)
    public abstract void saveChanges();

    @Unique
    private String gtocore$recipe = "";

    @Inject(method = "loadProcessingPattern", at = @At("RETURN"), remap = false)
    private void loadProcessingPattern(AEProcessingPattern pattern, CallbackInfo ci) {
        if (((IDetails) pattern).getRecipe() != null) {
            gtocore$recipe = ((IDetails) pattern).getRecipe().id.toString();
        } else {
            gtocore$recipe = "";
        }
    }

    @Inject(method = "writeToNBT", at = @At("TAIL"), remap = false)
    void writeToNBT(CompoundTag data, CallbackInfo ci) {
        data.putString("gto$recipe", gtocore$recipe);
    }

    @Inject(method = "readFromNBT", at = @At("TAIL"), remap = false)
    void readFromNBT(CompoundTag data, CallbackInfo ci) {
        gtocore$recipe = data.getString("gto$recipe");
    }

    @Override
    public String gtocore$getRecipe() {
        return gtocore$recipe;
    }

    @Override
    public void gtocore$setRecipe(String recipe) {
        this.gtocore$recipe = recipe;
        saveChanges();
    }

    @Override
    public void gtocore$clearExtraRecipeInfo() {
        this.gtocore$recipe = "";
    }
}
