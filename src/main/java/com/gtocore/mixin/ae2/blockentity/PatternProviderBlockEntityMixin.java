package com.gtocore.mixin.ae2.blockentity;

import com.gtocore.eio_travel.logic.TravelSavedData;
import com.gtocore.eio_travel.logic.TravelUtils;
import com.gtocore.integration.ae.hooks.IExtendedPatternContainer;

import com.gregtechceu.gtceu.api.recipe.GTRecipeType;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import appeng.blockentity.crafting.PatternProviderBlockEntity;
import appeng.blockentity.grid.AENetworkBlockEntity;
import appeng.helpers.patternprovider.PatternProviderLogicHost;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.EnumSet;
import java.util.List;

@Mixin(PatternProviderBlockEntity.class)
public abstract class PatternProviderBlockEntityMixin extends AENetworkBlockEntity implements IExtendedPatternContainer.IPPPC, PatternProviderLogicHost {

    @Shadow(remap = false)
    public abstract EnumSet<Direction> getTargets();

    public PatternProviderBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState) {
        super(blockEntityType, pos, blockState);
    }

    @Inject(method = "onReady", at = @At("RETURN"), remap = false)
    private void onReady(CallbackInfo ci) {
        Level level = getLevel();
        if (level instanceof ServerLevel) {
            TravelUtils.removeAndReadd(level, this);
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        Level level = getLevel();
        if (level instanceof ServerLevel) {
            TravelSavedData.getTravelData(level).removeTravelTargetAt(level, getBlockPos());
        }
    }

    @Override
    public Level gto$getLevel() {
        return level;
    }

    @Override
    public BlockPos gto$getBlockPos() {
        return getBlockPos();
    }

    @Override
    public BlockEntity gto$getBlockEntity() {
        return this;
    }

    @Override
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
