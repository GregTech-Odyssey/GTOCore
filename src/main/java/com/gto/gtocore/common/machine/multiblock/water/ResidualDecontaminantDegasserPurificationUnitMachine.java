package com.gto.gtocore.common.machine.multiblock.water;

import com.gto.gtocore.api.machine.multiblock.WorkableNoEnergyMultiblockMachine;
import com.gto.gtocore.common.machine.multiblock.part.IndicatorHatchPartMachine;
import com.gto.gtocore.utils.MachineUtil;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;

import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ResidualDecontaminantDegasserPurificationUnitMachine extends WaterPurificationUnitMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            ResidualDecontaminantDegasserPurificationUnitMachine.class, WorkableNoEnergyMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    private static final List<FluidStack> FLUIDS = List.of(
            GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 10000),
            GTMaterials.Helium.getFluid(10000),
            GTMaterials.SamariumIronArsenicOxide.getFluid(1000),
            GTMaterials.Neon.getFluid(8000),
            GTMaterials.IndiumTinBariumTitaniumCuprate.getFluid(1000),
            GTMaterials.Argon.getFluid(6000),
            GTMaterials.UraniumRhodiumDinaquadide.getFluid(1000),
            GTMaterials.Krypton.getFluid(4000),
            GTMaterials.EnrichedNaquadahTriniumEuropiumDuranide.getFluid(1000),
            GTMaterials.Xenon.getFluid(2000),
            GTMaterials.RutheniumTriniumAmericiumNeutronate.getFluid(1000),
            GTMaterials.Neutronium.getFluid(2000));

    @Persisted
    private int inputCount;

    @Persisted
    private boolean successful;

    @Persisted
    private boolean failed;

    @Persisted
    @DescSynced
    private FluidStack fluidStack;

    private IndicatorHatchPartMachine indicatorHatchPartMachine;

    public ResidualDecontaminantDegasserPurificationUnitMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        if (getRecipeLogic().isWorking()) {
            textList.add(Component.translatable("gtocore.machine.residual_decontaminant_degasser_purification_unit.fluids", fluidStack.getDisplayName()));
            textList.add(Component.translatable("gtceu.gui.content.chance_1", (successful && !failed) ? 100 : 0));
        }
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        for (IMultiPart part : getParts()) {
            if (part instanceof IndicatorHatchPartMachine indicatorHatch) {
                indicatorHatchPartMachine = indicatorHatch;
            }
        }
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        indicatorHatchPartMachine = null;
    }

    @Override
    public boolean onWorking() {
        boolean result = super.onWorking();
        if (getOffsetTimer() % 20 == 0) {
            for (IRecipeHandler<?> handler : Objects.requireNonNullElseGet(getCapabilitiesProxy().get(IO.IN, FluidRecipeCapability.CAP), Collections::<IRecipeHandler<?>>emptyList)) {
                for (Object contents : handler.getContents()) {
                    if (contents instanceof FluidStack stack && stack.getAmount() > 0) {
                        if (!fluidStack.isEmpty() && fluidStack.getFluid() == stack.getFluid() && fluidStack.getAmount() <= stack.getAmount()) {
                            successful = true;
                        } else {
                            failed = true;
                        }
                        MachineUtil.inputFluid(this, stack);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public void afterWorking() {
        super.afterWorking();
        indicatorHatchPartMachine.setRedstoneSignalOutput(0);
        if (successful && !failed) MachineUtil.outputFluid(this, FluidStack.create(WaterPurificationPlantMachine.GradePurifiedWater7, inputCount * 9L / 10));
    }

    @Override
    long before() {
        eut = 0;
        successful = false;
        failed = false;
        inputCount = (int) Math.min(Integer.MAX_VALUE, MachineUtil.getFluidAmount(this, WaterPurificationPlantMachine.GradePurifiedWater6)[0]);
        recipe = GTRecipeBuilder.ofRaw().duration(WaterPurificationPlantMachine.DURATION).inputFluids(FluidStack.create(WaterPurificationPlantMachine.GradePurifiedWater6, inputCount)).buildRawRecipe();
        if (recipe.matchRecipe(this).isSuccess()) {
            indicatorHatchPartMachine.setRedstoneSignalOutput((int) (Math.random() * 15));
            if (indicatorHatchPartMachine.getRedstoneSignalOutput() == 13 || indicatorHatchPartMachine.getRedstoneSignalOutput() == 15) {
                fluidStack = FLUIDS.get(11);
            } else if (indicatorHatchPartMachine.getRedstoneSignalOutput() == 12 || indicatorHatchPartMachine.getRedstoneSignalOutput() == 14) {
                fluidStack = FluidStack.empty();
            } else {
                fluidStack = FLUIDS.get(indicatorHatchPartMachine.getRedstoneSignalOutput());
            }
            eut = inputCount * 7L;
        }
        return eut;
    }
}
