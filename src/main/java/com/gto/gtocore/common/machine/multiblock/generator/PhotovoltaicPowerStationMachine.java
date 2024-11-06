package com.gto.gtocore.common.machine.multiblock.generator;

import com.gto.gtocore.utils.MachineUtil;

import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.chance.logic.ChanceLogic;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.logic.OCParams;
import com.gregtechceu.gtceu.api.recipe.logic.OCResult;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import earth.terrarium.adastra.api.planets.PlanetApi;

import java.util.List;

public class PhotovoltaicPowerStationMachine extends WorkableElectricMultiblockMachine {

    private final int basic_rate;

    public PhotovoltaicPowerStationMachine(IMachineBlockEntity holder, int basicRate) {
        super(holder);
        basic_rate = basicRate;
    }

    public static GTRecipe recipeModifier(MetaMachine machine, GTRecipe recipe, OCParams params, OCResult result) {
        if (machine instanceof PhotovoltaicPowerStationMachine powerStationMachine) {
            long eut = powerStationMachine.getGenerationRate();
            if (eut > 0) {
                GTRecipe recipe1 = recipe.copy();
                recipe1.tickOutputs.put(EURecipeCapability.CAP, List.of(new Content(eut, ChanceLogic.getMaxChancedValue(), ChanceLogic.getMaxChancedValue(), 0, null, null)));
                return recipe1;
            }
        }
        return recipe;
    }

    private long getGenerationRate() {
        Level level = getLevel();
        if (level == null || !canSeeSky(level)) return 0;
        long basic = basic_rate * PlanetApi.API.getSolarPower(level);
        if (PlanetApi.API.isSpace(level)) return MachineUtil.inputFluid(this, GTMaterials.DistilledWater.getFluid(basic / 4)) ? basic * 16 : 0;
        int minuteInTicks = 20 * 60;
        int dayTime = (int) (level.getDayTime() % (minuteInTicks * 20));
        if (dayTime <= minuteInTicks || dayTime > minuteInTicks * 9) return 0;
        float progress = (dayTime > minuteInTicks * 5) ? (10 * minuteInTicks - dayTime) : dayTime;
        progress = (progress - minuteInTicks) / (4 * minuteInTicks);
        double easing = (progress > 0.5) ? (1 - Math.pow(-2 * progress + 2, 2) / 2) : (2 * progress * progress);
        if (level.isRaining()) easing -= level.isThundering() ? 0.7f : 0.3f;
        return (long) (easing * basic);
    }

    private boolean canSeeSky(Level level) {
        BlockPos pos = MachineUtil.getOffsetPos(3, 1, getFrontFacing(), getPos());
        for (int i = -2; i < 3; i++) {
            for (int j = -2; j < 3; j++) {
                if (!level.canSeeSky(pos.offset(i, 0, j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
