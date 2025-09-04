package com.gtocore.mixin.gtm.machine;

import com.gtolib.api.machine.feature.multiblock.IEnhancedMultiblockMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IExplosionMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.machine.multiblock.steam.LargeBoilerMachine;

import net.minecraft.util.Mth;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import org.spongepowered.asm.mixin.*;

@Mixin(LargeBoilerMachine.class)
public abstract class LargeBoilerMachineMixin extends WorkableMultiblockMachine implements IExplosionMachine, IEnhancedMultiblockMachine {

    @Unique
    private static final Fluid gtolib$STEAM = GTMaterials.Steam.getFluid();

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

    @Unique
    private boolean gtolib$hasNoWater;

    @Shadow(remap = false)
    private int steamGenerated;

    protected LargeBoilerMachineMixin(MetaMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Override
    @SuppressWarnings("all")
    public boolean alwaysSearchRecipe() {
        return true;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    protected void updateCurrentTemperature() {
        if (recipeLogic.isWorking()) {
            if (getOffsetTimer() % 5 == 0) {
                if (currentTemperature < maxTemperature) {
                    currentTemperature = Mth.clamp(currentTemperature + heatSpeed, 0, maxTemperature);
                }
            }
        } else if (currentTemperature > 0) {
            currentTemperature -= 1;
        }
        if (currentTemperature > 100 && isFormed() && getOffsetTimer() % 5 == 0) {
            int water = currentTemperature * throttle * 5 / 16000;
            if (water > 0) {
                if (inputFluid(Fluids.WATER, water)) {
                    steamGenerated = currentTemperature * throttle * 5 / 100;
                    if (steamGenerated > 0) {
                        outputFluid(gtolib$STEAM, steamGenerated);
                    }
                    if (gtolib$hasNoWater) {
                        doExplosion(2.0F);
                    }
                } else {
                    gtolib$hasNoWater = true;
                }
            }
        }
    }
}
