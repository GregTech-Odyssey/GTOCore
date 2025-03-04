package com.gto.gtocore.common.machine.multiblock.generator;

import com.gto.gtocore.api.machine.multiblock.ElectricMultiblockMachine;
import com.gto.gtocore.api.machine.trait.CustomRecipeLogic;
import com.gto.gtocore.api.recipe.GTORecipeBuilder;
import com.gto.gtocore.api.recipe.RecipeRunner;
import com.gto.gtocore.utils.MachineUtils;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import earth.terrarium.adastra.api.planets.PlanetApi;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PhotovoltaicPowerStationMachine extends ElectricMultiblockMachine {

    private final int basic_rate;

    public PhotovoltaicPowerStationMachine(IMachineBlockEntity holder, int basicRate) {
        super(holder);
        basic_rate = basicRate;
    }

    private boolean canSeeSky(Level level) {
        BlockPos pos = MachineUtils.getOffsetPos(1, 5, getFrontFacing(), getPos());
        for (int i = -2; i < 3; i++) {
            for (int j = -2; j < 3; j++) {
                if (!level.canSeeSky(pos.offset(i, 0, j))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean keepSubscribing() {
        return true;
    }

    @Nullable
    private GTRecipe getRecipe() {
        if (hasProxies()) {
            Level level = getLevel();
            if (level != null && canSeeSky(level)) {
                int eut;
                int basic = (int) (basic_rate * PlanetApi.API.getSolarPower(level));
                if (PlanetApi.API.isSpace(level)) {
                    eut = MachineUtils.inputFluid(this, GTMaterials.DistilledWater.getFluid(basic / 4)) ? basic << 4 : 0;
                } else {
                    int minuteInTicks = 20 * 60;
                    int dayTime = (int) (level.getDayTime() % (minuteInTicks * 20));
                    if (dayTime <= minuteInTicks || dayTime > minuteInTicks * 9) return null;
                    float progress = (dayTime > minuteInTicks * 5) ? (10 * minuteInTicks - dayTime) : dayTime;
                    progress = (progress - minuteInTicks) / (4 * minuteInTicks);
                    double easing = (progress > 0.5) ? (1 - Math.pow(-2 * progress + 2, 2) / 2) : (2 * progress * progress);
                    if (level.isRaining()) easing -= level.isThundering() ? 0.7f : 0.3f;
                    eut = (int) (easing * basic);
                }
                GTRecipe recipe = GTORecipeBuilder.ofRaw().duration(20).EUt(-eut).buildRawRecipe();
                if (RecipeRunner.matchRecipeTickOutput(this, recipe)) return recipe;
            }
        }
        return null;
    }

    @Override
    protected @NotNull RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new CustomRecipeLogic(this, this::getRecipe);
    }
}
