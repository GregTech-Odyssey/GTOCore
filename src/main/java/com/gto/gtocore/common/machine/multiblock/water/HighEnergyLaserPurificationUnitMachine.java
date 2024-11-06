package com.gto.gtocore.common.machine.multiblock.water;

import com.gto.gtocore.api.machine.part.ItemHatchPartMachine;
import com.gto.gtocore.common.machine.multiblock.part.IndicatorHatchPartMachine;
import com.gto.gtocore.utils.MachineUtil;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.MarkerMaterials;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;

import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class HighEnergyLaserPurificationUnitMachine extends WaterPurificationUnitMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
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
        super(holder, args);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        for (IMultiPart part : getParts()) {
            if (part instanceof IndicatorHatchPartMachine lensSensorPart) {
                indicatorHatchPartMachine = lensSensorPart;
            } else if (part instanceof ItemHatchPartMachine itemHatchPart) {
                itemHatchPartMachine = itemHatchPart;
            }
        }
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        indicatorHatchPartMachine = null;
        itemHatchPartMachine = null;
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        if (getRecipeLogic().isWorking()) {
            textList.add(Component.translatable("gtceu.gui.content.chance_1", chance));
            textList.add(Component.translatable("tooltip.avaritia.num_items", LENS.get(index).getDescription()));
        }
    }

    @Override
    public boolean onWorking() {
        boolean result = super.onWorking();
        if (getRecipeLogic().getProgress() > time) {
            time = (int) (Math.random() * 120) + 120 + getRecipeLogic().getProgress();
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
        return result;
    }

    private boolean match() {
        return itemHatchPartMachine.getInventory().storage.getStackInSlot(0).is(LENS.get(index));
    }

    @Override
    public void afterWorking() {
        super.afterWorking();
        if (Math.random() * 100 <= chance) MachineUtil.outputFluid(this, FluidStack.create(WaterPurificationPlantMachine.GradePurifiedWater6, inputCount * 9L / 10));
    }

    @Override
    long before() {
        eut = 0;
        index = 0;
        chance = 0;
        time = (int) (Math.random() * 4) + 4;
        inputCount = (int) Math.min(Integer.MAX_VALUE, MachineUtil.getFluidAmount(this, WaterPurificationPlantMachine.GradePurifiedWater5)[0]);
        recipe = GTRecipeBuilder.ofRaw().duration(WaterPurificationPlantMachine.DURATION).inputFluids(FluidStack.create(WaterPurificationPlantMachine.GradePurifiedWater5, inputCount)).buildRawRecipe();
        if (recipe.matchRecipe(this).isSuccess()) {
            eut = inputCount * 6L;
        }
        return eut;
    }
}
