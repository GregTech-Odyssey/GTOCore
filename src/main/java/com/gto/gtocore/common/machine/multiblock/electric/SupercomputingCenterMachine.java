package com.gto.gtocore.common.machine.multiblock.electric;

import com.gto.gtocore.api.data.chemical.GTOChemicalHelper;
import com.gto.gtocore.api.machine.feature.multiblock.IMultiStructureMachine;
import com.gto.gtocore.api.machine.feature.multiblock.IParallelMachine;
import com.gto.gtocore.api.machine.multiblock.StorageMultiblockMachine;
import com.gto.gtocore.api.recipe.GTORecipeBuilder;
import com.gto.gtocore.api.recipe.RecipeRunner;
import com.gto.gtocore.common.data.GTOBlocks;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMachines;
import com.gto.gtocore.common.data.machines.ExResearchMachines;
import com.gto.gtocore.common.machine.multiblock.part.ThermalConductorHatchPartMachine;
import com.gto.gtocore.common.machine.multiblock.part.research.ExResearchBasePartMachine;
import com.gto.gtocore.common.machine.multiblock.part.research.ExResearchBridgePartMachine;
import com.gto.gtocore.common.machine.multiblock.part.research.ExResearchComputationPartMachine;
import com.gto.gtocore.common.machine.multiblock.part.research.ExResearchCoolerPartMachine;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.IOpticalComputationProvider;
import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.pattern.BlockPattern;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;
import com.gregtechceu.gtceu.common.data.GTBlocks;
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
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys.GAS;
import static com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys.LIQUID;
import static com.gregtechceu.gtceu.api.machine.multiblock.PartAbility.*;
import static com.gregtechceu.gtceu.api.machine.multiblock.PartAbility.MAINTENANCE;
import static com.gregtechceu.gtceu.api.pattern.Predicates.*;
import static com.gregtechceu.gtceu.api.pattern.Predicates.abilities;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gto.gtocore.common.data.GTOMaterials.*;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class SupercomputingCenterMachine extends StorageMultiblockMachine
        implements IOpticalComputationProvider, IParallelMachine, IMultiStructureMachine {

    private static final Set<Item> MAINFRAME = Set.of(GTOItems.BIOWARE_MAINFRAME.asItem(), GTOItems.EXOTIC_MAINFRAME.asItem());

    @Setter
    private ThermalConductorHatchPartMachine thermalConductorHatchPartMachine;

    public static final Map<Item, Item> MFPCs;

    private final ConditionalSubscriptionHandler maxCWUtModificationSubs;

    private int machineTier;

    private boolean incompatible, canBridge;

    private int maxCWUt, coolingAmount, maxCoolingAmount, allocatedCWUt, maxCWUtModification;

    private long maxEUt;

    private GTRecipe runRecipe;

    public SupercomputingCenterMachine(IMachineBlockEntity holder) {
        super(holder, 1, stack -> MAINFRAME.contains(stack.getItem()));
        maxCWUtModificationSubs = new ConditionalSubscriptionHandler(this, this::maxCWUtModificationUpdate, () -> isFormed || maxCWUtModification > 0);
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
        maxCWUtModification = 10000;
    }

    static {
        ImmutableMap.Builder<Item, Item> mfpcRecipe = ImmutableMap.builder();
        mfpcRecipe.put(GTOChemicalHelper.getItem(block, CascadeMFPC), GTOChemicalHelper.getItem(block, InvalidationCascadeMFPC));
        mfpcRecipe.put(GTOChemicalHelper.getItem(block, BasicMFPC), GTOChemicalHelper.getItem(block, InvalidationBasicMFPC));
        mfpcRecipe.put(GTOChemicalHelper.getItem(ingot, CascadeMFPC), GTOChemicalHelper.getItem(ingot, InvalidationCascadeMFPC));
        mfpcRecipe.put(GTOChemicalHelper.getItem(ingot, BasicMFPC), GTOChemicalHelper.getItem(ingot, InvalidationBasicMFPC));
        mfpcRecipe.put(GTOChemicalHelper.getItem(nugget, CascadeMFPC), GTOChemicalHelper.getItem(nugget, InvalidationCascadeMFPC));
        mfpcRecipe.put(GTOChemicalHelper.getItem(nugget, BasicMFPC), GTOChemicalHelper.getItem(nugget, InvalidationBasicMFPC));
        MFPCs = mfpcRecipe.build();
    }

    private int getIndexForItem(Item item) {
        if (item.equals(GTOChemicalHelper.getItem(block, CascadeMFPC))) return 0;
        if (item.equals(GTOChemicalHelper.getItem(block, BasicMFPC))) return 1;
        if (item.equals(GTOChemicalHelper.getItem(ingot, CascadeMFPC))) return 2;
        if (item.equals(GTOChemicalHelper.getItem(ingot, BasicMFPC))) return 3;
        if (item.equals(GTOChemicalHelper.getItem(nugget, CascadeMFPC))) return 4;
        if (item.equals(GTOChemicalHelper.getItem(nugget, BasicMFPC))) return 5;
        return -1;
    }

    int[] N_MFPCs = { 5400, 1800, 600, 200, 66, 22 };

    @Override
    public void onPartScan(IMultiPart part) {
        super.onPartScan(part);
        if (thermalConductorHatchPartMachine == null && part instanceof ThermalConductorHatchPartMachine thermalConductorHatchPart) {
            thermalConductorHatchPartMachine = thermalConductorHatchPart;
        }
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
                .inputFluids(Helium.getFluid(LIQUID, coolingAmount))
                .outputFluids(Helium.getFluid(GAS, coolingAmount))
                .EUt(maxEUt)
                .duration(20)
                .buildRawRecipe();
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        maxCWUtModificationSubs.initialize(getLevel());
        onMachineChanged();
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        thermalConductorHatchPartMachine = null;
        clean();
    }

    @Override
    public boolean onWorking() {
        if (allocatedCWUt == 0) return false;
        allocatedCWUt = 0;
        return super.onWorking();
    }

    @Override
    public void afterWorking() {
        allocatedCWUt = 0;
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
        int maxCWUt = getMaxCWUt() * maxCWUtModification / 10000;
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

    void maxCWUtModificationUpdate() {
        if (isFormed) {
            if (machineTier > 1) {
                if (getOffsetTimer() % 10 == 0) {
                    int max = (machineTier == 2) ? 40000 : 160000;
                    maxCWUtModification -= (int) (Math.pow(maxCWUtModification - 4000, 2) / 500000);
                    if ((maxCWUtModification <= max) && (thermalConductorHatchPartMachine != null)) {
                        CustomItemStackHandler stackTransfer = thermalConductorHatchPartMachine.getInventory().storage;
                        for (int i = 0; i < stackTransfer.getSlots(); i++) {
                            ItemStack itemStack = stackTransfer.getStackInSlot(i);
                            if (MFPCs.containsKey(itemStack.getItem())) {
                                int count = itemStack.getCount();
                                int index = getIndexForItem(itemStack.getItem());
                                int Consumption = Math.min(count, (max - maxCWUtModification) / N_MFPCs[index] + 1);
                                stackTransfer.setStackInSlot(i, new ItemStack(itemStack.getItem(), count - Consumption));
                                maxCWUtModification += N_MFPCs[index] * Consumption;
                                for (int j = 0; j < stackTransfer.getSlots(); j++) {
                                    if (stackTransfer.getStackInSlot(j).getItem() == MFPCs.get(itemStack.getItem())) {
                                        int count2 = stackTransfer.getStackInSlot(j).getCount();
                                        if (count2 + Consumption <= 64) {
                                            stackTransfer.setStackInSlot(j, new ItemStack(MFPCs.get(itemStack.getItem()), count2 + Consumption));
                                            break;
                                        }
                                    }
                                    if (stackTransfer.getStackInSlot(j).isEmpty()) {
                                        ItemStack convertedStack = new ItemStack(MFPCs.get(itemStack.getItem()), Consumption);
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
            textList.add(Component.translatable("gtceu.multiblock.hpca.computation", Component.literal(allocatedCWUt + " / " + getMaxCWUt()).append(Component.literal(" CWU/t")).withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.GRAY));
            textList.add(Component.translatable("gtocore.machine.cwut_modification", ((double) maxCWUtModification / 10000)).withStyle(ChatFormatting.GRAY));
            textList.add(Component.translatable("gtceu.multiblock.hpca.info_max_coolant_required", Component.literal(coolingAmount + " / " + maxCoolingAmount).append(Component.literal(" mB/t")).withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.GRAY));
        }
    }

    private static final Map<Integer, BlockPattern> PATTERNS = new Int2ObjectOpenHashMap<>(4, 0.9F);



    public static BlockPattern getBlockPattern(int tier, MachineDefinition definition) {
        FactoryBlockPattern builder = FactoryBlockPattern.start()
                .aisle("  AAAAAAAAAAA  ", " AA         AA ", "AA           AA", "A             A", "A             A", "A             A", "A             A", "AA           AA", " AA         AA ", "  AAAAAAAAAAA  ")
                .aisle(" AAABBBBBBBAAA ", "AACCCCCCCCCCCAA", "ACCKKKKKKKKKCCA", " CKKKKKKKKKKKC ", " CKKKKKKKKKKKC ", " CKKKKKKKKKKKC ", " CKKKKKKKKKKKC ", "ACCKKKKKKKKKCCA", "AACCCCCCCCCCCAA", " AA         AA ")
                .aisle("AAABBBBBBBBBAAA", "ACC         CCA", " C           C ", " K           K ", " K           K ", " K           K ", " K           K ", " C           C ", "ACCKKKKKKKKKCCA", "AA           AA")
                .aisle("AABBBBBBBBBBBAA", " C  CC   CC  C ", " K  CC   CC  K ", " K  CC   CC  K ", " K  CC   CC  K ", " K  CC   CC  K ", " K  CC   CC  K ", " K  CC   CC  K ", " CKKKKKKKKKKKC ", "A             A")
                .aisle("ABBBBBBBBBBBBBA", " C  CC   CC  C ", " K  DE   ED  K ", " K  DE   ED  K ", " K  DE   ED  K ", " K  DE   ED  K ", " K  DE   ED  K ", " K  CC   CC  K ", " CKKKKKKKKKKKC ", "A             A")
                .aisle("ABBBBBBBBBBBBBA", " C  CC   CC  C ", " K  DE   ED  K ", " K  DE   ED  K ", " K  DE   ED  K ", " K  DE   ED  K ", " K  DE   ED  K ", " K  CC   CC  K ", " CKKKKKKKKKKKC ", "A             A")
                .aisle("ABBBBBBBBBBBBBA", " C  CC   CC  C ", " K  DE   ED  K ", " K  DE   ED  K ", " K  DE   ED  K ", " K  DE   ED  K ", " K  DE   ED  K ", " K  CC   CC  K ", " CKKKKKKKKKKKC ", "A             A")
                .aisle("ABBBBBBBBBBBBBA", " C  CC   CC  C ", " K  DE   ED  K ", " K  DE   ED  K ", " K  DE   ED  K ", " K  DE   ED  K ", " K  DE   ED  K ", " K  CC   CC  K ", " CKKKKKKKKKKKC ", "A             A")
                .aisle("ABBBBBBBBBBBBBA", " C  CC   CC  C ", " K  DE   ED  K ", " K  DE   ED  K ", " K  DE   ED  K ", " K  DE   ED  K ", " K  DE   ED  K ", " K  CC   CC  K ", " CKKKKKKKKKKKC ", "A             A")
                .aisle("AABBBBBBBBBBBAA", " C  CC   CC  C ", " K  CC   CC  K ", " K  CC   CC  K ", " K  CC   CC  K ", " K  CC   CC  K ", " K  CC   CC  K ", " K  CC   CC  K ", " CKKKKKKKKKKKC ", "A             A")
                .aisle("AAABBBBBBBBBAAA", "ACC         CCA", " C           C ", " K           K ", " K           K ", " K           K ", " K           K ", " C           C ", "ACCKKKKKKKKKCCA", "AA           AA")
                .aisle(" AAABBBBBBBAAA ", "AACCCCCCCCCCCAA", "ACCKKKKKKKKKCCA", " CKKKVVVVVKKKC ", " CKKVVV~VVVKKC ", " CKKKVVVVVKKKC ", " CKKKKKKKKKKKC ", "ACCKKKKKKKKKCCA", "AACCCCCCCCCCCAA", " AA         AA ")
                .aisle("  AAAAAAAAAAA  ", " AA         AA ", "AA           AA", "A             A", "A             A", "A             A", "A             A", "AA           AA", " AA         AA ", "  AAAAAAAAAAA  ")
                .where('A', blocks(GTBlocks.ADVANCED_COMPUTER_CASING.get()))
                .where('B', blocks(GTBlocks.HIGH_POWER_CASING.get()))
                .where('~', controller(blocks(definition.get())))
                .where(' ', any())
                .where('V', blocks(GTBlocks.COMPUTER_CASING.get())
                        .or(blocks(GTOMachines.THERMAL_CONDUCTOR_HATCH.get()).setMaxGlobalLimited(1))
                        .or(abilities(IMPORT_ITEMS))
                        .or(abilities(IMPORT_FLUIDS))
                        .or(abilities(EXPORT_ITEMS))
                        .or(abilities(EXPORT_FLUIDS))
                        .or(abilities(INPUT_ENERGY).setMaxGlobalLimited(2))
                        .or(abilities(COMPUTATION_DATA_TRANSMISSION).setMaxGlobalLimited(1))
                        .or(abilities(MAINTENANCE).setExactLimit(1)));
        return PATTERNS.computeIfAbsent(tier, t -> switch (t) {
            case 2 -> builder
                    .where('C', blocks(GTOBlocks.BIOCOMPUTER_SHELL.get()))
                    .where('K', blocks(GTOBlocks.AMPROSIUM_BOROSILICATE_GLASS.get()))
                    .where('E', blocks(ExResearchMachines.NICH_EMPTY_COMPONENT.get())
                            .or(blocks(ExResearchMachines.NICH_COOLING_COMPONENTS.get()))
                            .or(blocks(ExResearchMachines.NICH_COMPUTING_COMPONENTS.get())))
                    .where('D', blocks(GTOBlocks.PHASE_CHANGE_BIOCOMPUTER_COOLING_VENTS.get()))
                    .build();
            case 3 -> builder
                    .where('C', blocks(GTOBlocks.GRAVITON_COMPUTER_SHELL.get()))
                    .where('K', blocks(GTOBlocks.TARANIUM_BOROSILICATE_GLASS.get()))
                    .where('E', blocks(ExResearchMachines.GWCA_EMPTY_COMPONENT.get())
                            .or(blocks(ExResearchMachines.GWCA_COMPUTING_COMPONENTS.get()))
                            .or(blocks(ExResearchMachines.GWCA_COOLING_COMPONENTS.get())))
                    .where('D', blocks(GTOBlocks.ANTI_ENTROPY_COMPUTER_CONDENSATION_MATRIX.get()))
                    .build();
            default -> builder
                    .where('C', blocks(GTBlocks.COMPUTER_CASING.get()))
                    .where('K', blocks(GTBlocks.CASING_LAMINATED_GLASS.get()))
                    .where('E', abilities(HPCA_COMPONENT))
                    .where('D', blocks(GTBlocks.COMPUTER_HEAT_VENT.get()))
                    .build();
        });
    }

    @Override
    public BlockPattern getPattern() {
        return getBlockPattern(machineTier, getDefinition());
    }

    @Override
    public int getMaxParallel() {
        return machineTier > 0 ? getStorageStack().getCount() : 0;
    }

    @Override
    public List<BlockPattern> getMultiPattern() {
        return List.of(getBlockPattern(1, getDefinition()), getBlockPattern(2, getDefinition()), getBlockPattern(3, getDefinition()));
    }
}
