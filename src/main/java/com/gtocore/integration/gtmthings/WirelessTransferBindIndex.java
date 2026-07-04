package com.gtocore.integration.gtmthings;

import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
import com.gregtechceu.gtceu.api.capability.ICoverable;
import com.gregtechceu.gtceu.api.cover.CoverBehavior;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import com.hepdd.gtmthings.common.cover.AdvancedWirelessTransferCover;
import com.hepdd.gtmthings.common.cover.WirelessTransferCover;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Server-side reverse index for gtmthings wireless transfer covers.
 * <p>
 * A cover stores the position of the block it was bound to ({@code targetPos}) but the bound block has no
 * back-reference, so this transient index maps {@code (targetDimension, targetPos) -> covers pointing at it},
 * letting the Jade HUD show, when looking at a bound block, which cover(s) point at it.
 * <p>
 * Populated from cover {@code onLoad}/{@code onAttached}, cleared on {@code onRemoved}; only loaded covers appear.
 */
public final class WirelessTransferBindIndex {

    private WirelessTransferBindIndex() {}

    /** coverDim/coverPos/coverFace = where the cover physically sits; transferType 1=item 2=fluid. */
    public record Binding(ResourceKey<Level> coverDim, BlockPos coverPos, Direction coverFace, int transferType,
                          String coverBlockId) {}

    // targetDimension -> (packed targetPos -> bindings)
    private static final Map<ResourceKey<Level>, Map<Long, Set<Binding>>> MAP = new HashMap<>();

    public static void register(CoverBehavior cover, @Nullable BlockPos targetPos, @Nullable String dimensionId, int transferType) {
        ResourceKey<Level> targetDim = resolve(cover, targetPos, dimensionId);
        if (targetDim == null) return;
        ICoverable holder = cover.coverHolder;
        BlockPos coverPos = holder.getPos();
        String coverBlockId = holder.getLevel().getBlockState(coverPos).getBlock().getDescriptionId();
        add(targetDim, targetPos, new Binding(holder.getLevel().dimension(), coverPos, cover.attachedSide, transferType, coverBlockId));
    }

    public static void unregister(CoverBehavior cover, @Nullable BlockPos targetPos, @Nullable String dimensionId) {
        ResourceKey<Level> targetDim = resolve(cover, targetPos, dimensionId);
        if (targetDim == null) return;
        remove(targetDim, targetPos, cover.coverHolder.getLevel().dimension(), cover.coverHolder.getPos(), cover.attachedSide);
    }

    /** Validate common preconditions and return the target dimension key, or null to skip. */
    @Nullable
    private static ResourceKey<Level> resolve(CoverBehavior cover, @Nullable BlockPos targetPos, @Nullable String dimensionId) {
        if (targetPos == null || dimensionId == null || dimensionId.isEmpty()) return null;
        Level level = cover.coverHolder.getLevel();
        if (level == null || level.isClientSide) return null;
        ResourceLocation loc = ResourceLocation.tryParse(dimensionId);
        return loc == null ? null : ResourceKey.create(Registries.DIMENSION, loc);
    }

    private static synchronized void add(ResourceKey<Level> targetDim, BlockPos targetPos, Binding binding) {
        MAP.computeIfAbsent(targetDim, k -> new HashMap<>())
                .computeIfAbsent(targetPos.asLong(), k -> new LinkedHashSet<>())
                .add(binding);
    }

    private static synchronized void remove(ResourceKey<Level> targetDim, BlockPos targetPos,
                                            ResourceKey<Level> coverDim, BlockPos coverPos, Direction coverFace) {
        Map<Long, Set<Binding>> byPos = MAP.get(targetDim);
        if (byPos == null) return;
        long key = targetPos.asLong();
        Set<Binding> set = byPos.get(key);
        if (set == null) return;
        set.removeIf(b -> b.coverFace() == coverFace && b.coverPos().equals(coverPos) && b.coverDim().equals(coverDim));
        if (set.isEmpty()) byPos.remove(key);
        if (byPos.isEmpty()) MAP.remove(targetDim);
    }

    /**
     * Bindings pointing at the given block, or an empty list.
     * <p>
     * Covers have no {@code onUnload} hook we can inject, so their source chunk unloading (or a different world
     * being loaded in the same JVM) would otherwise leave stale entries behind. Each candidate is therefore
     * validated against the live server here and dropped if its source cover is no longer loaded/present; only
     * currently-loaded covers survive. onLoad re-adds a cover when its chunk loads again.
     */
    public static synchronized List<Binding> get(ResourceKey<Level> targetDim, BlockPos targetPos, MinecraftServer server) {
        Map<Long, Set<Binding>> byPos = MAP.get(targetDim);
        if (byPos == null) return List.of();
        long key = targetPos.asLong();
        Set<Binding> set = byPos.get(key);
        if (set == null || set.isEmpty()) return List.of();
        set.removeIf(b -> !isLive(server, b));
        if (set.isEmpty()) {
            byPos.remove(key);
            if (byPos.isEmpty()) MAP.remove(targetDim);
            return List.of();
        }
        return new ArrayList<>(set);
    }

    /** True if the binding's source cover is currently loaded and still a wireless transfer cover on that face. */
    private static boolean isLive(MinecraftServer server, Binding b) {
        ServerLevel level = server.getLevel(b.coverDim());
        if (level == null || !level.isLoaded(b.coverPos())) return false;
        ICoverable coverable = GTCapabilityHelper.getCoverable(level.getBlockEntity(b.coverPos()), b.coverFace());
        if (coverable == null) return false;
        CoverBehavior cover = coverable.getCoverAtSide(b.coverFace());
        return cover instanceof WirelessTransferCover || cover instanceof AdvancedWirelessTransferCover;
    }
}
