package com.gto.gtocore.mixin.gtm.machine;

import com.gto.gtocore.utils.MachineUtils;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IExplosionMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.machine.multiblock.steam.LargeBoilerMachine;

import net.minecraft.util.Mth;
import net.minecraft.world.level.material.Fluids;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LargeBoilerMachine.class)
public abstract class LargeBoilerMachineMixin extends WorkableMultiblockMachine implements IExplosionMachine {

    @Shadow(remap = false)
    private int currentTemperature;

    @Shadow(remap = false)
    @Final
    public int maxTemperature;

    @Shadow(remap = false)
    @Final
    public int heatSpeed;

    @Shadow(remap = false)
    private int throttle;

    @Shadow(remap = false)
    private boolean hasNoWater;

    protected LargeBoilerMachineMixin(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    protected void updateCurrentTemperature() {
        if (recipeLogic.isWorking()) {
            if (getOffsetTimer() % 10 == 0) {
                if (currentTemperature < maxTemperature) {
                    currentTemperature = Mth.clamp(currentTemperature + heatSpeed * 10, 0, maxTemperature);
                }
            }
        } else if (currentTemperature > 0) {
            currentTemperature -= 1;
        }
        if (isFormed() && getOffsetTimer() % 5 == 0 && currentTemperature > 100) {
            int setam = currentTemperature * throttle * 5 / 100;
            if (setam > 0) {
                MachineUtils.outputFluid(this, GTMaterials.Steam.getFluid(setam));
            }
            int water = currentTemperature * throttle * 5 / 16000;
            if (water > 0) {
                if (MachineUtils.inputFluid(this, Fluids.WATER, water)) {
                    if (hasNoWater) {
                        doExplosion(2.0F);
                    }
                } else {
                    hasNoWater = true;
                }
            }
        }
    }
}
