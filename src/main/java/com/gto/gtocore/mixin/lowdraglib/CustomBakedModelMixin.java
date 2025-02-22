package com.gto.gtocore.mixin.lowdraglib;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import com.google.common.collect.Table;
import com.llamalad7.mixinextras.sugar.Local;
import com.lowdragmc.lowdraglib.client.model.custommodel.Connections;
import com.lowdragmc.lowdraglib.client.model.custommodel.CustomBakedModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

@Mixin(CustomBakedModel.class)
public abstract class CustomBakedModelMixin {

    @Shadow(remap = false)
    @Final
    private Table<Direction, Connections, List<BakedQuad>> sideCache;

    @Shadow(remap = false)
    @Nonnull
    public abstract List<BakedQuad> getCustomQuads(BlockAndTintGetter level, BlockPos pos, @NotNull BlockState state, @Nullable Direction side, RandomSource rand);

    @Inject(method = "getCustomQuads", at = @At(value = "INVOKE", target = "Ljava/util/Objects;requireNonNull(Ljava/lang/Object;)Ljava/lang/Object;"), remap = false, cancellable = true)
    private void requireNonNull(BlockAndTintGetter level, BlockPos pos, BlockState state, Direction side, RandomSource rand, CallbackInfoReturnable<List<BakedQuad>> cir, @Local Connections connections) {
        List<BakedQuad> customQuads = sideCache.get(side, connections);
        if (customQuads == null) {
            customQuads = sideCache.cellSet().stream()
                    .map(Table.Cell::getValue)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
        }
        if (customQuads == null) {
            customQuads = getCustomQuads(level, pos, state, side, rand);
        }
        cir.setReturnValue(customQuads);
    }
}
