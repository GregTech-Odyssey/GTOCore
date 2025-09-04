package com.gtocore.mixin.ae2.blockentity;

import com.gtolib.api.ae2.ExternalStorageCacheStrategy;

import appeng.api.behaviors.ExternalStorageStrategy;
import appeng.api.parts.IPartItem;
import appeng.api.stacks.AEKeyType;
import appeng.parts.automation.UpgradeablePart;
import appeng.parts.storagebus.StorageBusPart;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(StorageBusPart.class)
public abstract class StorageBusPartMixin extends UpgradeablePart {

    @Shadow(remap = false)
    private @Nullable Map<AEKeyType, ExternalStorageStrategy> externalStorageStrategies;

    protected StorageBusPartMixin(IPartItem<?> partItem) {
        super(partItem);
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
