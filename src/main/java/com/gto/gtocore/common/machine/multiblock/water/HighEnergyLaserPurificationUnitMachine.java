package com.gto.gtocore.common.machine.multiblock.water;

import com.gto.gtocore.api.machine.part.ItemHatchPartMachine;
import com.gto.gtocore.api.recipe.GTORecipeBuilder;
import com.gto.gtocore.common.machine.multiblock.part.IndicatorHatchPartMachine;
import com.gto.gtocore.utils.MachineUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.MarkerMaterials;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraftforge.fluids.FluidStack;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class HighEnergyLaserPurificationUnitMachine extends WaterPurificationUnitMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            HighEnergyLaserPurificationUnitMachine.class, WaterPurificationUnitMachine.MANAGED_FIELD_HOLDER);

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    private static final List<Item> LENS = List.of(
            ChemicalHelper.get(TagPrefix.lens, MarkerMaterials.Color.Red).getItem(),
            ChemicalHelper.get(TagPrefix.lens, MarkerMaterials.Color.Orange).getItem(),
            ChemicalHelper.get(TagPrefix.lens, MarkerMaterials.Color.Brown).getItem(),
            ChemicalHelper.get(TagPrefix.lens, MarkerMaterials.Color.Yellow).getItem(),
            ChemicalHelper.get(TagPrefix.lens, MarkerMaterials.Color.Green).getItem(),
            ChemicalHelper.get(TagPrefix.lens, MarkerMaterials.Color.Cyan).getItem(),
            ChemicalHelper.get(TagPrefix.lens, MarkerMaterials.Color.Blue).getItem(),
            ChemicalHelper.get(TagPrefix.lens, MarkerMaterials.Color.Purple).getItem(),
            ChemicalHelper.get(TagPrefix.lens, MarkerMaterials.Color.Magenta).getItem(),
            ChemicalHelper.get(TagPrefix.lens, MarkerMaterials.Color.Pink).getItem());

    @Persisted
    private int index;

    @Persisted
    private int time;

    @Persisted
    private int await;

    @Persisted
    private int working;

    @Persisted
    private long chance;

    @Persisted
    private int inputCount;

    private IndicatorHatchPartMachine indicatorHatchPartMachine;
    private ItemHatchPartMachine itemHatchPartMachine;

    public HighEnergyLaserPurificationUnitMachine(IMachineBlockEntity holder, Object... args) {
        super(holder);
    }

    @Override
    public void onPartScan(IMultiPart part) {
        super.onPartScan(part);
        if (indicatorHatchPartMachine == null && part instanceof IndicatorHatchPartMachine lensSensorPart) {
            indicatorHatchPartMachine = lensSensorPart;
        } else if (itemHatchPartMachine == null && part instanceof ItemHatchPartMachine itemHatchPart) {
            itemHatchPartMachine = itemHatchPart;
        }
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        indicatorHatchPartMachine = null;
        itemHatchPartMachine = null;
    }

    @Override
    public void customText(List<Component> textList) {
        super.customText(textList);
        if (getRecipeLogic().isWorking()) {
            textList.add(Component.translatable("gui.enderio.sag_mill_chance", chance));
            textList.add(Component.translatable("tooltip.avaritia.num_items", LENS.get(index).getDescription()));
        }
    }

    @Override
    public boolean onWorking() {
        if (!super.onWorking()) return false;
        if (getRecipeLogic().getProgress() > time) {
            time = GTValues.RNG.nextInt(120) + 120 + getRecipeLogic().getProgress();
            if (index < 9) {
                index++;
            } else {
                index = 0;
            }
            await = 80;
        }
        if (working > 0 && match()) {
            working++;
        }
        if (working > 80) {
            working = -1;
            chance += 10;
        }
        if (await > 0) {
            if (match()) {
                await = 0;
                working = 1;
            } else {
                indicatorHatchPartMachine.setRedstoneSignalOutput(15);
            }
            await--;
        } else {
            indicatorHatchPartMachine.setRedstoneSignalOutput(0);
        }
        return true;
    }

    private boolean match() {
        return itemHatchPartMachine.getInventory().storage.getStackInSlot(0).is(LENS.get(index));
    }

    @Override
    public void onRecipeFinish() {
        super.onRecipeFinish();
        if (GTValues.RNG.nextInt(100) <= chance) MachineUtils.outputFluid(this, WaterPurificationPlantMachine.GradePurifiedWater6, inputCount * 9 / 10);
    }

    @Override
    long before() {
        eut = 0;
        index = 0;
        chance = 0;
        time = GTValues.RNG.nextInt(4) + 4;
        inputCount = Math.min(getParallel(), MachineUtils.getFluidAmount(this, WaterPurificationPlantMachine.GradePurifiedWater5)[0]);
        recipe = GTORecipeBuilder.ofRaw().duration(WaterPurificationPlantMachine.DURATION).inputFluids(new FluidStack(WaterPurificationPlantMachine.GradePurifiedWater5, inputCount)).buildRawRecipe();
        if (recipe.matchRecipe(this).isSuccess()) {
            eut = (long) inputCount << 4;
        }
        return eut;
    }
}
