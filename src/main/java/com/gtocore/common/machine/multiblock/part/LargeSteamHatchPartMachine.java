package com.gtocore.common.machine.multiblock.part;

import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.common.machine.multiblock.part.SteamHatchPartMachine;

import net.minecraftforge.fluids.FluidStack;

@DataGeneratorScanned
public final class LargeSteamHatchPartMachine extends SteamHatchPartMachine {

    @RegisterLanguage(cn = "§e接受流体：", en = "§eAccepted Fluid: ")
    public static final String ACCEPTED_FLUID = "gtocore.machine.accepted_fluid";
    @RegisterLanguage(cn = "转换比例: %s mb/EU", en = "Conversion Rate: %s mb/EU")
    public static final String CONVERSION_RATE = "gtocore.machine.conversion_rate";

    public final int o;
    public final int m;
    public final double c;
    public final FluidStack f;

    public LargeSteamHatchPartMachine(MetaMachineBlockEntity holder, int o, int m, double c, FluidStack f, Object... args) {
        super(holder, INITIAL_TANK_CAPACITY << m);
        this.o = o;
        this.m = m;
        this.c = c;
        this.f = f;
    }

    @Override
    protected NotifiableFluidTank createTank(int initialCapacity, int slots, Object... args) {
        return super.createTank(initialCapacity, slots, args)
                .setFilter(fluidStack -> fluidStack.getFluid() == f.getFluid());
    }
}
