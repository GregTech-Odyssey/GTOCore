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
    private static final int TEMPERATURE_FACTOR = 5;
    @Unique
    private static final int WATER_DIVISOR = 16000;
    @Unique
    private static final int STEAM_MULTIPLIER = 100;

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
            long baseAmount = (long) currentTemperature * throttle * TEMPERATURE_FACTOR;
            int water = (int) (baseAmount / WATER_DIVISOR);

            if (water > 0) {
                boolean gtolib$hasNoWater;
                if (inputFluid(Fluids.WATER, water)) {
                    gtolib$hasNoWater = false; // 成功供水则清除无水状态
                    int steamGenerated = (int) (baseAmount / STEAM_MULTIPLIER);
                    if (steamGenerated > 0) {
                        outputFluid(gtolib$STEAM, steamGenerated);
                    }
                } else {
                    gtolib$hasNoWater = true;
                }

                // 爆炸判断应在供水失败后立即执行
                if (gtolib$hasNoWater) {
                    doExplosion(2.0F);
                }
            }
        }
    }
}
