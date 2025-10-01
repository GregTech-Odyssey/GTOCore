package com.gtocore.mixin.ae2.blockentity;

import com.gtocore.integration.eio.ITravelHandlerHook;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import appeng.blockentity.crafting.PatternProviderBlockEntity;
import appeng.blockentity.grid.AENetworkBlockEntity;
import appeng.helpers.patternprovider.PatternProviderLogicHost;
import com.enderio.base.common.travel.TravelSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PatternProviderBlockEntity.class)
public class PatternProviderBlockEntityMixin extends AENetworkBlockEntity {

    public PatternProviderBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState) {
        super(blockEntityType, pos, blockState);
    }

    @Inject(method = "onReady", at = @At("RETURN"), remap = false)
    private void onReady(CallbackInfo ci) {
        Level level = getLevel();
        if (level != null) {
            ITravelHandlerHook.removeAndReadd(level, (PatternProviderLogicHost) this);
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        Level level = getLevel();
        if (level != null) {
            TravelSavedData.getTravelData(level).removeTravelTargetAt(level, getBlockPos());
        }
    }
}
