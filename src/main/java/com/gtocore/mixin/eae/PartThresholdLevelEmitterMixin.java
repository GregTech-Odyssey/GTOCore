package com.gtocore.mixin.eae;

import appeng.api.config.FuzzyMode;
import appeng.api.config.Settings;
import appeng.api.networking.IGrid;
import appeng.api.parts.IPartItem;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import appeng.core.definitions.AEItems;
import appeng.parts.automation.AbstractLevelEmitterPart;
import com.glodblock.github.extendedae.common.parts.PartThresholdLevelEmitter;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = PartThresholdLevelEmitter.class, remap = false)
public abstract class PartThresholdLevelEmitterMixin extends AbstractLevelEmitterPart {

    protected PartThresholdLevelEmitterMixin(IPartItem<?> partItem) {
        super(partItem);
    }

    @Shadow
    @Nullable
    protected abstract AEKey getConfiguredKey();

    @Shadow
    public abstract long getUpperValue();

    /**
     * @author
     * @reason
     */
    @Overwrite
    private void updateReportingValue(IGrid grid) {
        KeyCounter stacks = grid.getStorageService().getCachedInventory();
        AEKey myStack = this.getConfiguredKey();
        if (myStack == null) {
            this.lastReportedValue = 0L;

            for (var st : stacks) {
                this.lastReportedValue += st.getLongValue();
                if (this.lastReportedValue > this.getUpperValue()) {
                    break;
                }
            }
        } else if (this.isUpgradedWith(AEItems.FUZZY_CARD)) {
            this.lastReportedValue = 0L;
            FuzzyMode fzMode = this.getConfigManager().getSetting(Settings.FUZZY_MODE);

            for (var st : stacks.findFuzzy(myStack, fzMode)) {
                this.lastReportedValue += st.getLongValue();
                if (this.lastReportedValue > this.getUpperValue()) {
                    break;
                }
            }
        } else {
            this.lastReportedValue = stacks.get(myStack);
        }

        this.updateState();
    }
}
