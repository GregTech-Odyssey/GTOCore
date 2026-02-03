package com.gtocore.mixin.ae2.blockentity;

import com.gtocore.eio_travel.api.ITravelHandlerHook;
import com.gtocore.eio_travel.api.TravelSavedData;
import com.gtocore.integration.ae.hooks.IExtendedPatternContainer;

import com.gregtechceu.gtceu.api.recipe.GTRecipeType;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import appeng.api.parts.IPartItem;
import appeng.helpers.patternprovider.PatternProviderLogicHost;
import appeng.parts.AEBasePart;
import appeng.parts.crafting.PatternProviderPart;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.EnumSet;
import java.util.List;

@Mixin(PatternProviderPart.class)
public abstract class PatternProviderPartMixin extends AEBasePart implements PatternProviderLogicHost, IExtendedPatternContainer.IPPPC {

    @Shadow(remap = false)
    public abstract EnumSet<Direction> getTargets();

    protected PatternProviderPartMixin(IPartItem<?> partItem) {
        super(partItem);
    }

    @Inject(method = "addToWorld", at = @At("RETURN"), remap = false)
    private void addToWorld(CallbackInfo ci) {
        Level level = getLevel();
        if (level instanceof ServerLevel) {
            ITravelHandlerHook.removeAndReadd(level, this);
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

    @Override
    public Level gto$getLevel() {
        return getLevel();
    }

    @Override
    public BlockPos gto$getBlockPos() {
        return getBlockEntity().getBlockPos();
    }

    @Override
    public BlockEntity gto$getBlockEntity() {
        return getBlockEntity();
    }

    public EnumSet<Direction> gto$getPushDirection() {
        return getTargets();
    }

    @Override
    public @Nullable GTRecipeType gto$getRecipeType() {
        return IExtendedPatternContainer.gto$getRecipeType(this);
    }

    @Override
    public @Nullable List<GTRecipeType> gto$getRecipeTypes() {
        return IExtendedPatternContainer.gto$getRecipeTypes(this);
    }

    @Override
    public boolean gto$isCraftingContainer() {
        return IExtendedPatternContainer.gto$isCraftingContainer(this);
    }
}
