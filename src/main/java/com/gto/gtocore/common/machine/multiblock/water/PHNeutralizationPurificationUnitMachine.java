package com.gto.gtocore.common.machine.multiblock.water;

import com.gto.gtocore.common.machine.multiblock.part.SensorPartMachine;
import com.gto.gtocore.utils.MachineUtil;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;

import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PHNeutralizationPurificationUnitMachine extends WaterPurificationUnitMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            PHNeutralizationPurificationUnitMachine.class, WaterPurificationUnitMachine.MANAGED_FIELD_HOLDER);

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    private static final ItemStack SodiumHydroxide = ChemicalHelper.get(TagPrefix.dust, GTMaterials.SodiumHydroxide);
    private static final Fluid HydrochloricAcid = GTMaterials.HydrochloricAcid.getFluid();

    @Persisted
    private float ph = 7;

    @Persisted
    private int inputCount;

    private final Set<SensorPartMachine> sensorPartMachines = new HashSet<>();

    public PHNeutralizationPurificationUnitMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        for (IMultiPart part : getParts()) {
            if (part instanceof SensorPartMachine sensorPartMachine) {
                sensorPartMachines.add(sensorPartMachine);
            }
        }
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        sensorPartMachines.clear();
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        if (getRecipeLogic().isWorking()) {
            textList.add(Component.literal("pH: " + ph));
        }
    }

    @Override
    public boolean onWorking() {
        boolean result = super.onWorking();
        if (getOffsetTimer() % 20 == 0) {
            long sh = MachineUtil.getItemAmount(this, SodiumHydroxide.getItem())[0];
            if (MachineUtil.inputItem(this, SodiumHydroxide.copyWithCount((int) sh))) {
                ph = Math.min(14, ph + sh * 0.01F);
            }
            long hc = MachineUtil.getFluidAmount(this, HydrochloricAcid)[0];
            if (MachineUtil.inputFluid(this, FluidStack.create(HydrochloricAcid, hc))) {
                ph = Math.max(0.01F, ph - hc * 0.001F);
            }
            sensorPartMachines.forEach(s -> s.update(ph));
        }
        return result;
    }

    @Override
    public void afterWorking() {
        super.afterWorking();
        if (ph >= 6.95 && ph <= 7.05) MachineUtil.outputFluid(this, FluidStack.create(WaterPurificationPlantMachine.GradePurifiedWater4, inputCount * 9L / 10));
    }

    @Override
    long before() {
        eut = 0;
        ph = ((float) Math.random() * 5) + 4.5F;
        inputCount = (int) Math.min(Integer.MAX_VALUE, MachineUtil.getFluidAmount(this, WaterPurificationPlantMachine.GradePurifiedWater3)[0]);
        recipe = GTRecipeBuilder.ofRaw().duration(WaterPurificationPlantMachine.DURATION).inputFluids(FluidStack.create(WaterPurificationPlantMachine.GradePurifiedWater3, inputCount)).buildRawRecipe();
        if (recipe.matchRecipe(this).isSuccess()) {
            eut = inputCount * 4L;
        }
        return eut;
    }
}
