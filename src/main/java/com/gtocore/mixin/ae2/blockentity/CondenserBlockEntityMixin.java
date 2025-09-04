package com.gtocore.mixin.ae2.blockentity;

import appeng.api.config.Settings;
import appeng.blockentity.misc.CondenserBlockEntity;
import appeng.util.ConfigManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CondenserBlockEntity.class)
public abstract class CondenserBlockEntityMixin {

    @Shadow(remap = false)
    protected abstract void setStoredPower(double storedPower);

    @Shadow(remap = false)
    public abstract double getStoredPower();

    @Shadow(remap = false)
    public abstract double getStorage();

    @Shadow(remap = false)
    protected abstract void fillOutput();

    @Shadow(remap = false)
    @Final
    private ConfigManager cm;

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    boolean canAddOutput() {
        return true;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void addPower(double rawPower) {
        if (this.cm.getSetting(Settings.CONDENSER_OUTPUT).ordinal() == 0) return;
        this.setStoredPower(Math.max(0.0, Math.min(this.getStorage(), this.getStoredPower() + rawPower)));
        fillOutput();
    }
}
