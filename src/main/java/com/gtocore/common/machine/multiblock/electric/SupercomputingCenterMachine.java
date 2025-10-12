package com.gtocore.common.machine.multiblock.electric;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.machine.multiblock.part.ThermalConductorHatchPartMachine;
import com.gtocore.common.machine.multiblock.part.research.ExResearchBasePartMachine;
import com.gtocore.common.machine.multiblock.part.research.ExResearchBridgePartMachine;
import com.gtocore.common.machine.multiblock.part.research.ExResearchComputationPartMachine;
import com.gtocore.common.machine.multiblock.part.research.ExResearchCoolerPartMachine;

import com.gtolib.api.GTOValues;
import com.gtolib.api.data.chemical.GTOChemicalHelper;
import com.gtolib.api.machine.multiblock.StorageMultiblockMachine;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeBuilder;
import com.gtolib.api.recipe.RecipeRunner;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.IOpticalComputationProvider;
import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;
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

import com.google.common.collect.ImmutableMap;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import earth.terrarium.adastra.common.registry.ModItems;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys.GAS;
import static com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys.LIQUID;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.common.data.GTOMaterials.*;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class SupercomputingCenterMachine extends StorageMultiblockMachine implements IOpticalComputationProvider {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(SupercomputingCenterMachine.class, StorageMultiblockMachine.MANAGED_FIELD_HOLDER);
    private static final Map<Item, Integer> MAINFRAME = Map.of(GTOItems.BIOWARE_MAINFRAME.asItem(), 2, GTOItems.SUPRACAUSAL_MAINFRAME.asItem(), 3);
    private static final Map<Integer, Integer> GLASS_MAP = Map.of(1, GTValues.IV, 2, GTValues.UHV, 3, GTValues.UIV);
    private static final Map<Item, Item> MFPCs;

    static {
        ImmutableMap.Builder<Item, Item> mfpcRecipe = ImmutableMap.builder();
        mfpcRecipe.put(GTOChemicalHelper.getItem(block, CascadeMFPC), GTOChemicalHelper.getItem(block, InvalidationCascadeMFPC));
        mfpcRecipe.put(GTOChemicalHelper.getItem(block, BasicMFPC), GTOChemicalHelper.getItem(block, InvalidationBasicMFPC));
        mfpcRecipe.put(GTOChemicalHelper.getItem(ingot, CascadeMFPC), GTOChemicalHelper.getItem(ingot, InvalidationCascadeMFPC));
        mfpcRecipe.put(GTOChemicalHelper.getItem(ingot, BasicMFPC), GTOChemicalHelper.getItem(ingot, InvalidationBasicMFPC));
        mfpcRecipe.put(GTOChemicalHelper.getItem(nugget, CascadeMFPC), GTOChemicalHelper.getItem(nugget, InvalidationCascadeMFPC));
        mfpcRecipe.put(GTOChemicalHelper.getItem(nugget, BasicMFPC), GTOChemicalHelper.getItem(nugget, InvalidationBasicMFPC));
        mfpcRecipe.put(ModItems.ICE_SHARD.get().asItem(), GTOChemicalHelper.getItem(dustTiny, Ice));
        MFPCs = mfpcRecipe.build();
    }

    private static final Map<Item, Integer> ITEM_INDEX_MAP = Map.of(GTOChemicalHelper.getItem(block, CascadeMFPC), 0, GTOChemicalHelper.getItem(block, BasicMFPC), 1, GTOChemicalHelper.getItem(ingot, CascadeMFPC), 2, GTOChemicalHelper.getItem(ingot, BasicMFPC), 3, GTOChemicalHelper.getItem(nugget, CascadeMFPC), 4, GTOChemicalHelper.getItem(nugget, BasicMFPC), 5, ModItems.ICE_SHARD.get().asItem(), 6);
    private ThermalConductorHatchPartMachine ThermalConductorHatchPart;
    private final ConditionalSubscriptionHandler maxCWUtModificationSubs;
    @Persisted
    private int machineTier = 1;
    @Persisted
    private int maxCWUtModification;
    private boolean incompatible;
    private boolean canBridge;
    private int maxCWUt;
    private int coolingAmount;
    private int maxCoolingAmount;
    private long allocatedCWUt;
    private long cacheCWUt;
    private long maxEUt;
    private Recipe runRecipe;

    public SupercomputingCenterMachine(MetaMachineBlockEntity holder) {
        super(holder, 1, stack -> MAINFRAME.containsKey(stack.getItem()));
        maxCWUtModificationSubs = new ConditionalSubscriptionHandler(this, this::maxCWUtModificationUpdate, () -> isFormed);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    private void clean() {
        canBridge = false;
        incompatible = false;
        runRecipe = null;
        allocatedCWUt = 0;
        cacheCWUt = 0;
        maxCWUt = 0;
        coolingAmount = 0;
        maxCoolingAmount = 0;
        maxEUt = 0;
    }

    private void changed() {
        clean();
        if (!isFormed) return;
        Integer computerTier = getMultiblockState().getMatchContext().get(GTOValues.COMPUTER_CASING_TIER);
        if (computerTier == null || machineTier != computerTier) {
            incompatible = true;
            return;
        }
        Integer heatTier = getMultiblockState().getMatchContext().get(GTOValues.COMPUTER_HEAT_TIER);
        if (heatTier == null || machineTier != heatTier) {
            incompatible = true;
            return;
        }
        Integer glassTier = getMultiblockState().getMatchContext().get(GTOValues.GLASS_TIER);
        if (glassTier == null || glassTier < GLASS_MAP.get(machineTier)) {
            incompatible = true;
            return;
        }
        if (machineTier == 3) canBridge = true;
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
        if (maxEUt > 0) {
            if (machineTier == 1)
                runRecipe = RecipeBuilder.ofRaw().inputFluids(PCBCoolant.getFluid(LIQUID, coolingAmount)).EUt(maxEUt / 4).duration(20).buildRawRecipe();
            else
                runRecipe = RecipeBuilder.ofRaw().inputFluids(Helium.getFluid(LIQUID, coolingAmount)).outputFluids(Helium.getFluid(GAS, coolingAmount)).EUt(maxEUt).duration(20).buildRawRecipe();
        }
        maxCWUtModificationSubs.initialize(getLevel());
    }

    private static int getIndexForItem(Item item) {
        return ITEM_INDEX_MAP.getOrDefault(item, -1);
    }

    private final int[] N_MFPCs = { 5400, 1800, 600, 200, 66, 22, 1 };

    @Override
    public void onPartScan(IMultiPart part) {
        super.onPartScan(part);
        if (ThermalConductorHatchPart == null && part instanceof ThermalConductorHatchPartMachine thermalConductorHatchPart) {
            ThermalConductorHatchPart = thermalConductorHatchPart;
        }
    }

    @Override
    public void onMachineChanged() {
        machineTier = 1;
        Integer integer = MAINFRAME.get(getStorageStack().getItem());
        if (integer != null) {
            machineTier = integer;
        }
        changed();
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        changed();
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        ThermalConductorHatchPart = null;
        maxCWUtModification = 10000;
        clean();
    }

    @Override
    public boolean onWorking() {
        if (allocatedCWUt == 0) return false;
        cacheCWUt = allocatedCWUt;
        allocatedCWUt = 0;
        return super.onWorking();
    }

    @Override
    public void afterWorking() {
        cacheCWUt = allocatedCWUt;
        allocatedCWUt = 0;
        if (coolingAmount > maxCoolingAmount) {
            int damaged = coolingAmount - maxCoolingAmount;
            for (IMultiPart part : getParts()) {
                if (part instanceof HPCAComponentPartMachine componentPartMachine && componentPartMachine.canBeDamaged()) {
                    damaged -= GTValues.RNG.nextInt(256);
                    componentPartMachine.setDamaged(true);
                }
                if (damaged <= 0) break;
            }
        }
        super.afterWorking();
    }

    private long requestCWUt(boolean simulate, long cwu) {
        long toAllocate = Math.min(cwu, getMaxCWU());
        if (!simulate) {
            this.allocatedCWUt += toAllocate;
        }
        return toAllocate;
    }

    @Override
    public long requestCWU(long cwu, boolean simulate) {
        if (incompatible) return 0;
        if (runRecipe != null) {
            if (simulate) return requestCWUt(true, cwu);
            if (getRecipeLogic().isWorking()) {
                return requestCWUt(false, cwu);
            } else if (RecipeRunner.matchTickRecipe(this, runRecipe) && RecipeRunner.matchRecipe(this, runRecipe)) {
                getRecipeLogic().setupRecipe(runRecipe);
                if (getRecipeLogic().isWorking()) {
                    return requestCWUt(false, cwu);
                }
            }
        }
        return 0;
    }

    @Override
    public long getMaxCWU() {
        return (getMaxCWUt() * maxCWUtModification / 10000) - cacheCWUt;
    }

    private void maxCWUtModificationUpdate() {
        if (isFormed) {
            if (machineTier > 1) {
                if (getOffsetTimer() % 10 == 0) {
                    int max = (machineTier == 2) ? 40000 : 160000;
                    maxCWUtModification -= (int) ((Math.pow(maxCWUtModification - 4000, 2) / 500000) * (0.8 / (Math.log(maxCWUtModification + 600000) - Math.log(10000))));
                    if ((maxCWUtModification <= max) && (ThermalConductorHatchPart != null)) {
                        CustomItemStackHandler stackTransfer = ThermalConductorHatchPart.getInventory().storage;
                        for (int i = 0; i < stackTransfer.getSlots(); i++) {
                            ItemStack itemStack = stackTransfer.getStackInSlot(i);
                            Item valueItem = MFPCs.get(itemStack.getItem());
                            if (valueItem != null) {
                                int count = itemStack.getCount();
                                int index = getIndexForItem(itemStack.getItem());
                                int consumption = Math.min(count, (max - maxCWUtModification) / N_MFPCs[index] + 1);
                                stackTransfer.setStackInSlot(i, itemStack.copyWithCount(count - consumption));
                                maxCWUtModification += N_MFPCs[index] * consumption;
                                for (int j = 0; j < stackTransfer.getSlots(); j++) {
                                    if (stackTransfer.getStackInSlot(j).getItem() == valueItem) {
                                        int count2 = stackTransfer.getStackInSlot(j).getCount();
                                        if (count2 + consumption <= 64) {
                                            stackTransfer.setStackInSlot(j, new ItemStack(valueItem, count2 + consumption));
                                            break;
                                        }
                                    }
                                    if (stackTransfer.getStackInSlot(j).isEmpty()) {
                                        ItemStack convertedStack = new ItemStack(valueItem, consumption);
                                        stackTransfer.setStackInSlot(j, convertedStack);
                                        break;
                                    }
                                }
                            }
                            if (maxCWUtModification >= max) break;
                        }
                    }
                    if (maxCWUtModification < 8000) maxCWUtModification = 8000;
                }
            } else maxCWUtModification = 10000;
        }
        maxCWUtModificationSubs.updateSubscription();
    }

    public long getMaxCWUt() {
        if (incompatible) return 0;
        return maxCWUt;
    }

    @Override
    public boolean canBridge() {
        if (incompatible) return false;
        return canBridge;
    }

    @Override
    public void addDisplayText(@NotNull List<Component> textList) {
        if (incompatible) {
            textList.add(Component.translatable("tooltip.avaritia.tier", machineTier));
            textList.add(Component.translatable("gtceu.multiblock.invalid_structure").withStyle(ChatFormatting.RED));
        } else {
            super.addDisplayText(textList);
        }
    }

    @Override
    public void customText(List<Component> textList) {
        super.customText(textList);
        textList.add(Component.translatable("tooltip.avaritia.tier", machineTier));
        textList.add(Component.translatable("gtceu.multiblock.energy_consumption", maxEUt, GTValues.VNF[GTUtil.getTierByVoltage(maxEUt)]).withStyle(ChatFormatting.YELLOW));
        textList.add(Component.translatable("gtceu.multiblock.hpca.computation", Component.literal(cacheCWUt + " / " + getMaxCWUt()).append(Component.literal(" CWU/t")).withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.GRAY));
        textList.add(Component.translatable("gtocore.machine.cwut_modification", ((double) maxCWUtModification / 10000)).withStyle(ChatFormatting.AQUA));
        textList.add(Component.translatable("gtceu.multiblock.hpca.info_max_coolant_required", Component.literal(coolingAmount + " / " + maxCoolingAmount).withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.GRAY));
    }

    public void setThermalConductorHatchPart(final ThermalConductorHatchPartMachine ThermalConductorHatchPart) {
        this.ThermalConductorHatchPart = ThermalConductorHatchPart;
    }
}
