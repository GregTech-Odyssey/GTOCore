package com.gtocore.mixin.ae2.blockentity;

import com.gtocore.eio_travel.api.ITravelHandlerHook;
import com.gtocore.eio_travel.api.TravelSavedData;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import appeng.api.parts.IPartItem;
import appeng.helpers.patternprovider.PatternProviderLogicHost;
import appeng.parts.AEBasePart;
import appeng.parts.crafting.PatternProviderPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PatternProviderPart.class)
public abstract class PatternProviderPartMixin extends AEBasePart {

    protected PatternProviderPartMixin(IPartItem<?> partItem) {
        super(partItem);
    }

    @Inject(method = "addToWorld", at = @At("RETURN"), remap = false)
    private void addToWorld(CallbackInfo ci) {
        Level level = getLevel();
        if (level instanceof ServerLevel) {
            ITravelHandlerHook.removeAndReadd(level, (PatternProviderLogicHost) this);
        }
    }

    @Override
    public void removeFromWorld() {
        super.removeFromWorld();
        Level level = getLevel();
        if (level instanceof ServerLevel) {
            TravelSavedData.getTravelData(level).removeTravelTargetAt(level, getBlockEntity().getBlockPos());
        }
    }
}
