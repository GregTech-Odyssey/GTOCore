package com.gto.gtocore.common.machine.multiblock.electric;

import com.gto.gtocore.api.machine.multiblock.StorageMultiblockMachine;
import com.gto.gtocore.api.recipe.GTORecipeBuilder;
import com.gto.gtocore.api.recipe.RecipeRunner;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.machine.multiblock.part.research.ExResearchBasePartMachine;
import com.gto.gtocore.common.machine.multiblock.part.research.ExResearchBridgePartMachine;
import com.gto.gtocore.common.machine.multiblock.part.research.ExResearchComputationPartMachine;
import com.gto.gtocore.common.machine.multiblock.part.research.ExResearchCoolerPartMachine;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.IOpticalComputationProvider;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.machine.multiblock.part.hpca.HPCABridgePartMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.hpca.HPCAComponentPartMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.hpca.HPCAComputationPartMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.hpca.HPCACoolerPartMachine;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class SupercomputingCenterMachine extends StorageMultiblockMachine implements IOpticalComputationProvider {

    private static final Set<Item> MAINFRAME = Set.of(GTOItems.BIOWARE_MAINFRAME.asItem(), GTOItems.EXOTIC_MAINFRAME.asItem());

    private int machineTier;

    private boolean incompatible, canBridge;

    private int maxCWUt, coolingAmount, maxCoolingAmount, allocatedCWUt, cachedEUt;

    private long maxEUt;

    private GTRecipe runRecipe;

    public SupercomputingCenterMachine(IMachineBlockEntity holder) {
        super(holder, 1, stack -> MAINFRAME.contains(stack.getItem()));
    }

    private void clean() {
        machineTier = 1;
        canBridge = false;
        incompatible = false;
        runRecipe = null;
        allocatedCWUt = 0;
        maxCWUt = 0;
        coolingAmount = 0;
        maxCoolingAmount = 0;
        maxEUt = 0;
    }

    @Override
    protected void onMachineChanged() {
        clean();
        ItemStack stack1 = getStorageStack();
        Item Item1 = stack1.getItem();
        if (Item1.equals(GTOItems.BIOWARE_MAINFRAME.asItem())) {
            machineTier = 2;
        } else if (Item1.equals(GTOItems.EXOTIC_MAINFRAME.asItem())) {
            machineTier = 3;
        }
        for (IMultiPart part : getParts()) {
            if (incompatible) return;
            if (part instanceof HPCAComponentPartMachine componentPartMachine) {
                maxEUt += componentPartMachine.getMaxEUt();
                if (componentPartMachine instanceof ExResearchBasePartMachine basePartMachine) {
                    if (basePartMachine.getTier() - 1 != machineTier) {
                        incompatible = true;
                        return;
                    }
                    if (basePartMachine instanceof ExResearchBridgePartMachine) {
                        canBridge = true;
                    } else if (basePartMachine instanceof ExResearchComputationPartMachine computationPartMachine) {
                        maxCWUt += computationPartMachine.getCWUPerTick();
                        coolingAmount += computationPartMachine.getCoolingPerTick();
                    } else if (basePartMachine instanceof ExResearchCoolerPartMachine coolerPartMachine) {
                        maxCoolingAmount += coolerPartMachine.getMaxCoolantPerTick();
                    }
                } else {
                    if (machineTier > 1) {
                        incompatible = true;
                        return;
                    }
                }
                if (componentPartMachine instanceof HPCABridgePartMachine) {
                    canBridge = true;
                } else if (componentPartMachine instanceof HPCAComputationPartMachine computationPartMachine) {
                    maxCWUt += computationPartMachine.getCWUPerTick();
                    coolingAmount += computationPartMachine.getCoolingPerTick();
                } else if (componentPartMachine instanceof HPCACoolerPartMachine coolerPartMachine) {
                    maxCoolingAmount += coolerPartMachine.getMaxCoolantPerTick();
                }
            }
        }
        if (maxEUt > 0) runRecipe = GTORecipeBuilder.ofRaw()
                .inputFluids(GTMaterials.Helium.getFluid(coolingAmount))
                .EUt(maxEUt)
                .duration(20)
                .buildRawRecipe();
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        onMachineChanged();
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        clean();
    }

    @Override
    public boolean onWorking() {
        cachedEUt = allocatedCWUt;
        if (allocatedCWUt == 0) return false;
        allocatedCWUt = 0;
        return super.onWorking();
    }

    @Override
    public void afterWorking() {
        cachedEUt = 0;
        if (coolingAmount > maxCoolingAmount) {
            for (IMultiPart part : getParts()) {
                if (part instanceof HPCAComponentPartMachine componentPartMachine && componentPartMachine.canBeDamaged()) {
                    componentPartMachine.setDamaged(true);
                }
            }
        }
        super.afterWorking();
    }

    private int requestCWUt(boolean simulate, int cwut) {
        int maxCWUt = getMaxCWUt();
        int availableCWUt = maxCWUt - this.allocatedCWUt;
        int toAllocate = Math.min(cwut, availableCWUt);
        if (!simulate) {
            this.allocatedCWUt += toAllocate;
        }
        return toAllocate;
    }

    @Override
    public int requestCWUt(int cwut, boolean simulate, @NotNull Collection<IOpticalComputationProvider> seen) {
        seen.add(this);
        if (incompatible) return 0;
        if (runRecipe != null) {
            if (simulate) return requestCWUt(true, cwut);
            if (getRecipeLogic().isWorking()) {
                return requestCWUt(false, cwut);
            } else if (RecipeRunner.matchTickRecipe(this, runRecipe) && RecipeRunner.matchRecipe(this, runRecipe)) {
                getRecipeLogic().setupRecipe(runRecipe);
                if (getRecipeLogic().isWorking()) {
                    return requestCWUt(false, cwut);
                }
            }
        }
        return 0;
    }

    @Override
    public int getMaxCWUt(@NotNull Collection<IOpticalComputationProvider> seen) {
        seen.add(this);
        if (incompatible) return 0;
        return maxCWUt;
    }

    @Override
    public boolean canBridge(@NotNull Collection<IOpticalComputationProvider> seen) {
        seen.add(this);
        if (incompatible) return false;
        return canBridge;
    }

    @Override
    public void customText(List<Component> textList) {
        textList.add(Component.translatable("tooltip.avaritia.tier", machineTier));
        if (incompatible) {
            textList.add(Component.translatable("gtceu.multiblock.invalid_structure").withStyle(ChatFormatting.RED));
        } else {
            super.customText(textList);
            textList.add(Component.translatable("gtceu.multiblock.energy_consumption", maxEUt, GTValues.VNF[GTUtil.getTierByVoltage(maxEUt)]).withStyle(ChatFormatting.YELLOW));
            textList.add(Component.translatable("gtceu.multiblock.hpca.computation", Component.literal(cachedEUt + " / " + getMaxCWUt()).append(Component.literal(" CWU/t")).withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.GRAY));
            textList.add(Component.translatable("gtceu.multiblock.hpca.info_max_cooling_demand", Component.literal(coolingAmount + " / " + maxCoolingAmount).append(Component.literal(" mB/t")).withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.GRAY));
        }
    }
}
