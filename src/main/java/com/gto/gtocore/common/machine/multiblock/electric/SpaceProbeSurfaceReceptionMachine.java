package com.gto.gtocore.common.machine.multiblock.electric;

import com.gto.gtocore.utils.MachineUtil;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import earth.terrarium.adastra.api.planets.PlanetApi;
import org.jetbrains.annotations.Nullable;

public class SpaceProbeSurfaceReceptionMachine extends WorkableElectricMultiblockMachine {

    public SpaceProbeSurfaceReceptionMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Override
    public boolean beforeWorking(@Nullable GTRecipe recipe) {
        Level level = getLevel();
        if (level == null) return false;
        BlockPos pos = MachineUtil.getOffsetPos(8, 28, getFrontFacing(), getPos());
        for (int i = -4; i < 5; i++) {
            for (int j = -4; j < 5; j++) {
                if (!level.canSeeSky(pos.offset(i, 0, j))) {
                    return false;
                }
            }
        }
        return PlanetApi.API.isSpace(level) && super.beforeWorking(recipe);
    }
}
