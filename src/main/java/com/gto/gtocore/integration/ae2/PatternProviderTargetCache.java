package com.gto.gtocore.integration.ae2;

import com.gregtechceu.gtceu.common.data.GTItems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;

import appeng.api.behaviors.ExternalStorageStrategy;
import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.AEKeyType;
import appeng.api.storage.MEStorage;
import appeng.capabilities.Capabilities;
import appeng.helpers.patternprovider.PatternProviderTarget;
import appeng.me.storage.CompositeStorage;
import appeng.parts.automation.StackWorldBehaviors;
import appeng.util.BlockApiCache;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

public final class PatternProviderTargetCache {

    private final IPatternProviderLogic logic;
    private final BlockApiCache<MEStorage> cache;
    private final Direction direction;
    private final IActionSource src;
    private final Map<AEKeyType, ExternalStorageStrategy> strategies;

    public PatternProviderTargetCache(IPatternProviderLogic logic, ServerLevel l, BlockPos pos, Direction direction, IActionSource src) {
        this.logic = logic;
        this.cache = BlockApiCache.create(Capabilities.STORAGE, l, pos);
        this.direction = direction;
        this.src = src;
        this.strategies = StackWorldBehaviors.createExternalStorageStrategies(l, pos, direction);
    }

    @Nullable
    public PatternProviderTarget find() {
        var meStorage = cache.find(direction);
        if (meStorage != null) {
            return new WrapMeStorage(meStorage, src, logic);
        }
        var externalStorages = new IdentityHashMap<AEKeyType, MEStorage>(2);
        for (var entry : strategies.entrySet()) {
            var wrapper = entry.getValue().createWrapper(false, () -> {});
            if (wrapper != null) {
                externalStorages.put(entry.getKey(), wrapper);
            }
        }
        if (!externalStorages.isEmpty()) {
            return new WrapMeStorage(new CompositeStorage(externalStorages), src, logic);
        }
        return null;
    }

    private record WrapMeStorage(MEStorage storage, IActionSource src, IPatternProviderLogic logic) implements PatternProviderTarget {

        @Override
        public long insert(AEKey what, long amount, Actionable type) {
            return storage.insert(what, amount, type, src);
        }

        @Override
        public boolean containsPatternInput(Set<AEKey> patternInputs) {
            switch (logic.gtocore$getBlocking()) {
                case ALL:
                    for (var stack : storage.getAvailableStacks()) {
                        if (stack.getKey() instanceof AEItemKey itemKey && itemKey.getItem() == GTItems.PROGRAMMED_CIRCUIT.asItem()) continue;
                        return true;
                    }
                    break;
                case NON_CONTAIN:
                    for (var stack : storage.getAvailableStacks()) {
                        if (patternInputs.contains(stack.getKey().dropSecondary())) continue;
                        return true;
                    }
                    break;
                case CONTAIN:
                    for (var stack : storage.getAvailableStacks()) {
                        if (patternInputs.contains(stack.getKey().dropSecondary())) return true;
                    }
            }
            return false;
        }
    }
}
