package com.gtocore.mixin.ae2.eae;

import com.gtocore.config.GTOConfig;
import com.gtocore.eio_travel.logic.TravelSavedData;
import com.gtocore.eio_travel.logic.TravelUtils;
import com.gtocore.integration.ae.hooks.IExtendedPatternContainer;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import appeng.api.parts.IPartItem;
import appeng.helpers.patternprovider.PatternProviderLogic;
import appeng.helpers.patternprovider.PatternProviderLogicHost;
import appeng.parts.AEBasePart;

import com.glodblock.github.extendedae.common.parts.PartExPatternProvider;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.EnumSet;
import java.util.List;

@Mixin(PartExPatternProvider.class)
public abstract class PartExPatternProviderMixin extends AEBasePart implements PatternProviderLogicHost, IExtendedPatternContainer.IPPPC {

    @Unique
    private PartExPatternProvider exae$getSelf() {
        return (PartExPatternProvider) (Object) this;
    }

    @Inject(
            method = "createLogic",
            at = @At("HEAD"),
            cancellable = true,
            remap = false)
    private void modifyCreateLogic(CallbackInfoReturnable<PatternProviderLogic> cir) {
        // This method is intentionally left empty to prevent the original logic from executing.
        // The logic is handled in the XModUtils class.
        if (GTCEu.isDev() && GTOConfig.INSTANCE.devMode.exPatternSize > 36) {
            cir.setReturnValue(new PatternProviderLogic(exae$getSelf().getMainNode(), exae$getSelf(), GTOConfig.INSTANCE.devMode.exPatternSize));
        }
    }

    @Shadow(remap = false)
    public abstract EnumSet<Direction> getTargets();

    protected PartExPatternProviderMixin(IPartItem<?> partItem) {
        super(partItem);
    }

    @Inject(method = "addToWorld", at = @At("RETURN"), remap = false)
    private void addToWorld(CallbackInfo ci) {
        Level level = getLevel();
        if (level instanceof ServerLevel) {
            TravelUtils.removeAndReadd(level, this);
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
