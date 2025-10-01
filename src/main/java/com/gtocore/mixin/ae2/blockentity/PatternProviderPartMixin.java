package com.gtocore.mixin.ae2.blockentity;

import com.gtocore.integration.eio.ITravelHandlerHook;

import net.minecraft.world.level.Level;

import appeng.api.parts.IPartItem;
import appeng.helpers.patternprovider.PatternProviderLogicHost;
import appeng.parts.AEBasePart;
import appeng.parts.crafting.PatternProviderPart;
import com.enderio.base.common.travel.TravelSavedData;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PatternProviderPart.class)
public abstract class PatternProviderPartMixin extends AEBasePart {

    protected PatternProviderPartMixin(IPartItem<?> partItem) {
        super(partItem);
    }

    @Override
    public void addToWorld() {
        super.addToWorld();
        Level level = getLevel();
        if (level != null) {
            ITravelHandlerHook.removeAndReadd(level, (PatternProviderLogicHost) this);
        }
    }

    @Override
    public void removeFromWorld() {
        super.removeFromWorld();
        Level level = getLevel();
        if (level != null) {
            TravelSavedData.getTravelData(level).removeTravelTargetAt(level, getBlockEntity().getBlockPos());
        }
    }
}
