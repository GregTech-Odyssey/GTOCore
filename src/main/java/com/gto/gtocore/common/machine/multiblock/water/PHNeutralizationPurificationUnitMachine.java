package com.gto.gtocore.common.machine.multiblock.water;

import com.gto.gtocore.api.recipe.GTORecipeBuilder;
import com.gto.gtocore.api.recipe.RecipeRunner;
import com.gto.gtocore.common.machine.multiblock.part.SensorPartMachine;
import com.gto.gtocore.utils.MachineUtils;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.List;
import java.util.Set;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class PHNeutralizationPurificationUnitMachine extends WaterPurificationUnitMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
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

    private final Set<SensorPartMachine> sensorPartMachines = new ObjectOpenHashSet<>(3);

    public PHNeutralizationPurificationUnitMachine(IMachineBlockEntity holder, Object... args) {
        super(holder);
    }

    @Override
    public void onPartScan(IMultiPart part) {
        super.onPartScan(part);
        if (part instanceof SensorPartMachine sensorPartMachine) {
            sensorPartMachines.add(sensorPartMachine);
        }
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        sensorPartMachines.clear();
    }

    @Override
    public void customText(List<Component> textList) {
        super.customText(textList);
        if (getRecipeLogic().isWorking()) {
            textList.add(Component.literal("pH: " + ph));
        }
    }

    @Override
    public boolean onWorking() {
        if (!super.onWorking()) return false;
        if (getOffsetTimer() % 20 == 0) {
            int sh = MachineUtils.getItemAmount(this, SodiumHydroxide.getItem())[0];
            if (MachineUtils.inputItem(this, SodiumHydroxide.copyWithCount(sh))) {
                ph = Math.min(14, ph + sh * 0.01F);
            }
            int hc = MachineUtils.getFluidAmount(this, HydrochloricAcid)[0];
            if (MachineUtils.inputFluid(this, HydrochloricAcid, hc)) {
                ph = Math.max(0.01F, ph - hc * 0.001F);
            }
            sensorPartMachines.forEach(s -> s.update(ph));
        }
        return true;
    }

    @Override
    public void onRecipeFinish() {
        super.onRecipeFinish();
        if (ph >= 6.95 && ph <= 7.05) MachineUtils.outputFluid(this, WaterPurificationPlantMachine.GradePurifiedWater4, inputCount * 9 / 10);
    }

    @Override
    long before() {
        eut = 0;
        ph = ((float) Math.random() * 5) + 4.5F;
        inputCount = Math.min(getParallel(), MachineUtils.getFluidAmount(this, WaterPurificationPlantMachine.GradePurifiedWater3)[0]);
        recipe = GTORecipeBuilder.ofRaw().duration(WaterPurificationPlantMachine.DURATION).inputFluids(new FluidStack(WaterPurificationPlantMachine.GradePurifiedWater3, inputCount)).buildRawRecipe();
        if (RecipeRunner.matchRecipe(this, recipe)) {
            eut = (long) inputCount << 2;
        }
        return eut;
    }
}
