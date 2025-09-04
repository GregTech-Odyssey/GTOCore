package com.gtocore.mixin.ae2.blockentity;

import com.gtolib.api.ae2.IExpandedStorageService;
import com.gtolib.api.ae2.StorageExportCacheStrategy;

import appeng.api.behaviors.StackExportStrategy;
import appeng.api.behaviors.StackTransferContext;
import appeng.api.config.SchedulingMode;
import appeng.api.config.Settings;
import appeng.api.config.YesNo;
import appeng.api.networking.IGrid;
import appeng.api.networking.crafting.ICraftingRequester;
import appeng.api.networking.crafting.ICraftingService;
import appeng.api.networking.energy.IEnergyService;
import appeng.api.networking.storage.IStorageService;
import appeng.api.parts.IPartItem;
import appeng.api.stacks.AEKey;
import appeng.api.storage.AEKeyFilter;
import appeng.core.definitions.AEItems;
import appeng.core.settings.TickRates;
import appeng.parts.automation.ExportBusPart;
import appeng.parts.automation.IOBusPart;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExportBusPart.class)
public abstract class ExportBusPartMixin extends IOBusPart implements ICraftingRequester {

    @Shadow(remap = false)
    protected abstract StackTransferContext createTransferContext(IStorageService storageService, IEnergyService energyService);

    @Shadow(remap = false)
    protected abstract int getStartingSlot(SchedulingMode schedulingMode, int x);

    @Shadow(remap = false)
    protected abstract void attemptCrafting(StackTransferContext context, ICraftingService cg, int slotToExport, AEKey what);

    @Shadow(remap = false)
    protected abstract void updateSchedulingMode(SchedulingMode schedulingMode, int x);

    @Shadow(remap = false)
    private @Nullable StackExportStrategy exportStrategy;

    protected ExportBusPartMixin(TickRates tickRates, @Nullable AEKeyFilter filter, IPartItem<?> partItem) {
        super(tickRates, filter, partItem);
    }

    @Unique
    private int gtolib$delay = 0;

    @Inject(method = "attemptCrafting", at = @At("HEAD"), remap = false, cancellable = true)
    private void attemptCrafting(StackTransferContext context, ICraftingService cg, int slotToExport, AEKey what, CallbackInfo ci) {
        if (gtolib$delay == 0) {
            gtolib$delay = 20;
            ci.cancel();
        } else gtolib$delay--;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    protected final StackExportStrategy getExportStrategy() {
        if (exportStrategy == null) {
            var self = this.getHost().getBlockEntity();
            exportStrategy = StorageExportCacheStrategy.createExportFacade(self, self.getBlockPos().relative(getSide()), getSide(), getSide().getOpposite());
        }
        return exportStrategy;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    protected boolean doBusWork(IGrid grid) {
        var storageService = grid.getStorageService();
        var cg = grid.getCraftingService();
        var fzMode = this.getConfigManager().getSetting(Settings.FUZZY_MODE);
        var schedulingMode = this.getConfigManager().getSetting(Settings.SCHEDULING_MODE);

        var context = createTransferContext(storageService, grid.getEnergyService());

        int x;
        long amount;
        var strategy = getExportStrategy();
        boolean isFuzzy = isUpgradedWith(AEItems.FUZZY_CARD);
        boolean isCraftingEnabled = isUpgradedWith(AEItems.CRAFTING_CARD);
        for (x = 0; x < this.availableSlots(); x++) {
            final int slotToExport = this.getStartingSlot(schedulingMode, x);
            var what = getConfig().getKey(slotToExport);

            if (what == null) continue;

            if (isCraftingEnabled && this.getConfigManager().getSetting(Settings.CRAFT_ONLY) == YesNo.YES) {
                attemptCrafting(context, cg, slotToExport, what);
                continue;
            }

            var before = context.getOperationsRemaining();

            if (isFuzzy) {
                for (var fuzzyWhat : IExpandedStorageService.of(storageService).getFuzzyKeyCounter().findFuzzy(what, fzMode)) {
                    amount = fuzzyWhat.getLongValue();
                    if (amount > 0) {
                        amount = strategy.transfer(context, fuzzyWhat.getKey(), amount);
                        if (amount > 0) {
                            context.reduceOperationsRemaining(1);
                        }
                    }
                }
            } else {
                amount = storageService.getCachedInventory().get(what);
                if (amount > 0) {
                    amount = strategy.transfer(context, what, amount);
                    if (amount > 0) {
                        context.reduceOperationsRemaining(1);
                    }
                }
            }

            if (isCraftingEnabled && before == context.getOperationsRemaining()) {
                attemptCrafting(context, cg, slotToExport, what);
            }
        }

        if (context.hasDoneWork()) {
            this.updateSchedulingMode(schedulingMode, x);
        }

        return context.hasDoneWork();
    }
}
