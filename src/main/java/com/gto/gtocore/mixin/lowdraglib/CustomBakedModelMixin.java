package com.gto.gtocore.mixin.lowdraglib;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import com.lowdragmc.lowdraglib.client.model.custommodel.Connections;
import com.lowdragmc.lowdraglib.client.model.custommodel.CustomBakedModel;
import org.spongepowered.asm.mixin.*;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.Nonnull;

@Mixin(CustomBakedModel.class)
public abstract class CustomBakedModelMixin {

    @Unique
    private final ConcurrentMap<Direction, ConcurrentMap<Connections, List<BakedQuad>>> gtocore$sideCache = new ConcurrentHashMap<>();

    @Shadow(remap = false)
    @Final
    private List<BakedQuad> noSideCache;

    @Shadow(remap = false)
    @Final
    private BakedModel parent;

    /**
     * @author .
     * @reason .
     */
    @Nonnull
    @Overwrite(remap = false)
    public List<BakedQuad> getCustomQuads(BlockAndTintGetter level, BlockPos pos, @Nonnull BlockState state, @javax.annotation.Nullable Direction side, RandomSource rand) {
        if (side == null) {
            if (noSideCache.isEmpty()) {
                synchronized (noSideCache) {
                    if (noSideCache.isEmpty()) {
                        noSideCache.addAll(CustomBakedModel.buildCustomQuads(Connections.checkConnections(level, pos, state, side), parent.getQuads(state, null, rand), 0.0f));
                    }
                }
            }
            return noSideCache;
        }
        Connections connections = Connections.checkConnections(level, pos, state, side);
        return gtocore$sideCache.computeIfAbsent(side, key -> new ConcurrentHashMap<>()).computeIfAbsent(connections, key -> CustomBakedModel.buildCustomQuads(connections, parent.getQuads(state, side, rand), 0.0f));
    }
}
