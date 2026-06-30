package com.gtocore.mixin.ae2.blockentity;

import com.gtocore.config.StorageBusBlacklist;

import com.gtolib.api.ae2.ExternalStorageCacheStrategy;
import com.gtolib.api.blockentity.IDirectionCacheBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import appeng.api.behaviors.ExternalStorageStrategy;
import appeng.api.parts.IPartItem;
import appeng.api.stacks.AEKeyType;
import appeng.parts.automation.UpgradeablePart;
import appeng.parts.storagebus.StorageBusPart;
import appeng.util.Platform;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;

@Mixin(StorageBusPart.class)
public abstract class StorageBusPartMixin extends UpgradeablePart {

    @Shadow(remap = false)
    private @Nullable Map<AEKeyType, ExternalStorageStrategy> externalStorageStrategies;

    protected StorageBusPartMixin(IPartItem<?> partItem) {
        super(partItem);
    }

    @Redirect(method = "updateTarget", at = @At(value = "INVOKE", target = "Lappeng/util/Platform;areBlockEntitiesTicking(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z"), remap = false)
    private boolean areBlockEntitiesTicking(Level level, BlockPos pos) {
        var host = getHost().getBlockEntity();
        if (host instanceof IDirectionCacheBlockEntity cacheBlockEntity) {
            var cache = cacheBlockEntity.gtolib$getDirectionCache();
            if (cache == null) return false;
            var n = cache.getAdjacentBlockEntity(level, host.getBlockPos(), getSide());
            if (n == null || StorageBusBlacklist.LIST.contains(n.getClass())) return false;
            return Platform.areBlockEntitiesTicking(level, pos);
        }
        return false;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    private Map<AEKeyType, ExternalStorageStrategy> getExternalStorageStrategies() {
        if (externalStorageStrategies == null) {
            var host = getHost().getBlockEntity();
            this.externalStorageStrategies = ExternalStorageCacheStrategy.createWithManaExternalStorageStrategies(host, host.getBlockPos().relative(getSide()), getSide(), getSide().getOpposite());
        }
        return externalStorageStrategies;
    }
}
