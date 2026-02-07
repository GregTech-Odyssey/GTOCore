package com.gtocore.mixin.eae;

import appeng.api.networking.storage.IStorageService;
import appeng.api.parts.IPartItem;
import appeng.api.storage.AEKeyFilter;
import appeng.core.settings.TickRates;
import appeng.parts.automation.IOBusPart;
import com.glodblock.github.extendedae.common.parts.base.PartSpecialExportBus;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Set;

@Mixin(PartSpecialExportBus.class)
public abstract class PartSpecialExportBusMixin extends IOBusPart implements IStorageService.UpdateRequester {

    public PartSpecialExportBusMixin(TickRates tickRates, @Nullable AEKeyFilter filter, IPartItem<?> partItem) {
        super(tickRates, filter, partItem);
    }

    @Override
    protected void onUpgradesChanged() {
        super.onUpgradesChanged();
        runListener();
    }

    @Override
    public boolean isUpdateRequested(IStorageService service) {
        return true;
    }

    @Override
    public Set<Runnable> getListener() {
        return new ReferenceOpenHashSet<>();
    }
}
