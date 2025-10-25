package com.gtocore.common.machine.multiblock.noenergy;

import com.gtolib.api.annotation.Scanned;
import com.gtolib.api.annotation.dynamic.DynamicInitialValue;
import com.gtolib.api.annotation.dynamic.DynamicInitialValueTypes;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.fluids.PropertyFluidFilter;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.machine.multiblock.electric.MultiblockTankMachine;

import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

@Scanned
public class LargeSteamStorageTank extends MultiblockTankMachine {

    @DynamicInitialValue(
                         key = "gtocore.machine.multiblock.noenergy.large_steam_storage_tank.capacity",
                         typeKey = DynamicInitialValueTypes.KEY_CAPACITY,
                         en = "Capacity",
                         enComment = "The capacity of the large steam storage tank in mB",
                         cn = "容量",
                         cnComment = "大型蒸汽储存罐的容量，单位为mB",
                         easyValue = "1296000000", // 360分钟青铜锅炉x2x3
                         normalValue = "216000000", // 60分钟青铜锅炉x2x3
                         expertValue = "162000000") // 45分钟青铜锅炉x2x3
    private static int capacity = 120000000;

    public LargeSteamStorageTank(MetaMachineBlockEntity holder, Object... args) {
        super(holder, capacity, new MyPropertyFluidFilter(), args);
    }

    private static final class MyPropertyFluidFilter extends PropertyFluidFilter {

        private MyPropertyFluidFilter() {
            super(true, false);
        }

        @Override
        public boolean test(@NotNull FluidStack stack) {
            return stack.getFluid() == GTMaterials.Steam.getFluid();
        }
    }
}
