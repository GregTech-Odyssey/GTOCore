package com.gto.gtocore.common.machine.multiblock.electric.space;

import com.gto.gtocore.api.machine.multiblock.ElectricMultiblockMachine;
import com.gto.gtocore.utils.MachineUtils;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import earth.terrarium.adastra.api.planets.PlanetApi;

public final class SpaceProbeSurfaceReceptionMachine extends ElectricMultiblockMachine {

    public SpaceProbeSurfaceReceptionMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Override
    public boolean onWorking() {
        if (super.onWorking()) {
            Level level = getLevel();
            if (level == null) return false;
            if (getOffsetTimer() % 20 == 0) {
                BlockPos pos = MachineUtils.getOffsetPos(8, 28, getFrontFacing(), getPos());
                for (int i = -4; i < 5; i++) {
                    for (int j = -4; j < 5; j++) {
                        if (!level.canSeeSky(pos.offset(i, 0, j))) {
                            return false;
                        }
                    }
                }
                return PlanetApi.API.isSpace(level);
            }
            return true;
        }
        return false;
    }
}
