package com.gtocore.common.machine.multiblock.water;

import com.gtocore.common.machine.multiblock.part.IndicatorHatchPartMachine;

import com.gtolib.api.data.chemical.GTOChemicalHelper;
import com.gtolib.api.machine.part.ItemHatchPartMachine;
import com.gtolib.api.recipe.RecipeRunner;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.data.chemical.material.MarkerMaterials;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class HighEnergyLaserPurificationUnitMachine extends WaterPurificationUnitMachine {

    private static final List<Item> LENS = List.of(
            GTOChemicalHelper.getItem(TagPrefix.lens, MarkerMaterials.Color.Red),
            GTOChemicalHelper.getItem(TagPrefix.lens, MarkerMaterials.Color.Orange),
            GTOChemicalHelper.getItem(TagPrefix.lens, MarkerMaterials.Color.Brown),
            GTOChemicalHelper.getItem(TagPrefix.lens, MarkerMaterials.Color.Yellow),
            GTOChemicalHelper.getItem(TagPrefix.lens, MarkerMaterials.Color.Green),
            GTOChemicalHelper.getItem(TagPrefix.lens, MarkerMaterials.Color.Cyan),
            GTOChemicalHelper.getItem(TagPrefix.lens, MarkerMaterials.Color.Blue),
            GTOChemicalHelper.getItem(TagPrefix.lens, MarkerMaterials.Color.Purple),
            GTOChemicalHelper.getItem(TagPrefix.lens, MarkerMaterials.Color.Magenta),
            GTOChemicalHelper.getItem(TagPrefix.lens, MarkerMaterials.Color.Pink));

    @Persisted
    private int index;

    @Persisted
    private int time;

    @Persisted
    private int await;

    @Persisted
    private int working;

    @Persisted
    private int chance;

    @Persisted
    private long inputCount;

    private IndicatorHatchPartMachine indicatorHatchPartMachine;
    private ItemHatchPartMachine itemHatchPartMachine;

    public HighEnergyLaserPurificationUnitMachine(MetaMachineBlockEntity holder) {
        super(holder, 32);
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
        if (GTValues.RNG.nextInt(100) <= chance) outputFluid(WaterPurificationPlantMachine.GradePurifiedWater6, inputCount * 9 / 10);
    }

    @Override
    long before() {
        eut = 0;
        chance = 0;
        time = GTValues.RNG.nextInt(120) + 120;
        inputCount = Math.min(parallel(), getFluidAmount(WaterPurificationPlantMachine.GradePurifiedWater5)[0]);
        if (inputCount > 0) {
            recipe = getRecipeBuilder().duration(WaterPurificationPlantMachine.DURATION).inputFluids(WaterPurificationPlantMachine.GradePurifiedWater5, inputCount).buildRawRecipe();
            if (RecipeRunner.matchRecipe(this, recipe)) {
                calculateVoltage(inputCount);
            }
        }
        return eut;
    }
}
