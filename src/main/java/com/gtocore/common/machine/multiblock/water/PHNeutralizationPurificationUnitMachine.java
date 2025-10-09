package com.gtocore.common.machine.multiblock.water;

import com.gtocore.common.machine.multiblock.part.SensorPartMachine;

import com.gtolib.api.recipe.RecipeRunner;
import com.gtolib.utils.MathUtil;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.List;

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
    private long inputCount;

    private final List<SensorPartMachine> sensorPartMachines = new ObjectArrayList<>(2);

    public PHNeutralizationPurificationUnitMachine(MetaMachineBlockEntity holder) {
        super(holder, 8);
    }

    @Override
    public void onPartScan(IMultiPart part) {
        super.onPartScan(part);
        if (part instanceof SensorPartMachine sensorPartMachine) {
            sensorPartMachines.add(sensorPartMachine);
        }
    }

    @Override
    public void onStructureFormed() {
        sensorPartMachines.clear();
        super.onStructureFormed();
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
            int sh = MathUtil.saturatedCast(getItemAmount(SodiumHydroxide.getItem())[0]);
            if (inputItem(SodiumHydroxide.getItem(), sh)) {
                ph = Math.min(14, ph + sh * 0.01F);
            }
            int hc = MathUtil.saturatedCast(getFluidAmount(HydrochloricAcid)[0]);
            if (inputFluid(HydrochloricAcid, hc)) {
                ph = Math.max(0.01F, ph - hc * 0.001F);
            }
            sensorPartMachines.forEach(s -> s.update(ph));
        }
        return true;
    }

    @Override
    public void onRecipeFinish() {
        super.onRecipeFinish();
        if (ph >= 6.95 && ph <= 7.05) outputFluid(WaterPurificationPlantMachine.GradePurifiedWater4, inputCount * 9 / 10);
    }

    @Override
    long before() {
        eut = 0;
        ph = ((float) Math.random() * 5) + 4.5F;
        inputCount = Math.min(parallel(), getFluidAmount(WaterPurificationPlantMachine.GradePurifiedWater3)[0]);
        if (inputCount > 0) {
            recipe = getRecipeBuilder().duration(WaterPurificationPlantMachine.DURATION).inputFluids(WaterPurificationPlantMachine.GradePurifiedWater3, inputCount).buildRawRecipe();
            if (RecipeRunner.matchRecipe(this, recipe)) {
                calculateVoltage(inputCount);
            }
        }
        return eut;
    }
}
