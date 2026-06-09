package com.gtocore.common.machine.multiblock.electric;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTORecipeDataKeys;
import com.gtocore.common.machine.multiblock.part.ThermalConductorHatchPartMachine;
import com.gtocore.common.machine.multiblock.part.research.ExResearchBasePartMachine;
import com.gtocore.common.machine.multiblock.part.research.ExResearchBridgePartMachine;
import com.gtocore.common.machine.multiblock.part.research.ExResearchComputationPartMachine;
import com.gtocore.common.machine.multiblock.part.research.ExResearchCoolerPartMachine;

import com.gtolib.api.item.IItem;
import com.gtolib.api.machine.multiblock.StorageMultiblockMachine;
import com.gtolib.api.recipe.RecipeBuilder;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.IOpticalComputationProvider;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;
import com.gregtechceu.gtceu.common.machine.multiblock.part.hpca.HPCABridgePartMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.hpca.HPCAComponentPartMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.hpca.HPCAComputationPartMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.hpca.HPCACoolerPartMachine;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTUtil;
import com.gregtechceu.gtceu.utils.TaskHandler;

import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import com.google.common.collect.ImmutableMap;
import com.gto.datasynclib.annotations.SaveToDisk;
import earth.terrarium.adastra.common.registry.ModItems;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    private static final Map<Item, Integer> MAINFRAME = Map.of(GTOItems.BIOWARE_MAINFRAME.asItem(), 2, GTOItems.SUPRACAUSAL_MAINFRAME.asItem(), 3);
    private static final Map<Integer, Integer> GLASS_MAP = Map.of(1, GTValues.IV, 2, GTValues.UHV, 3, GTValues.UIV);
    private static final Map<Item, Item> MFPCs;

    static {
        ImmutableMap.Builder<Item, Item> mfpcRecipe = ImmutableMap.builder();
        mfpcRecipe.put(ChemicalHelper.getItem(block, CascadeMFPC), ChemicalHelper.getItem(block, InvalidationCascadeMFPC));
        mfpcRecipe.put(ChemicalHelper.getItem(block, BasicMFPC), ChemicalHelper.getItem(block, InvalidationBasicMFPC));
        mfpcRecipe.put(ChemicalHelper.getItem(ingot, CascadeMFPC), ChemicalHelper.getItem(ingot, InvalidationCascadeMFPC));
        mfpcRecipe.put(ChemicalHelper.getItem(ingot, BasicMFPC), ChemicalHelper.getItem(ingot, InvalidationBasicMFPC));
        mfpcRecipe.put(ChemicalHelper.getItem(nugget, CascadeMFPC), ChemicalHelper.getItem(nugget, InvalidationCascadeMFPC));
        mfpcRecipe.put(ChemicalHelper.getItem(nugget, BasicMFPC), ChemicalHelper.getItem(nugget, InvalidationBasicMFPC));
        mfpcRecipe.put(ModItems.ICE_SHARD.get().asItem(), ChemicalHelper.getItem(dustTiny, Ice));
        MFPCs = mfpcRecipe.build();
    }

    private static final Map<Item, Integer> ITEM_INDEX_MAP = Map.of(ChemicalHelper.getItem(block, CascadeMFPC), 0, ChemicalHelper.getItem(block, BasicMFPC), 1, ChemicalHelper.getItem(ingot, CascadeMFPC), 2, ChemicalHelper.getItem(ingot, BasicMFPC), 3, ChemicalHelper.getItem(nugget, CascadeMFPC), 4, ChemicalHelper.getItem(nugget, BasicMFPC), 5, ModItems.ICE_SHARD.get().asItem(), 6);
    @Setter
    private ThermalConductorHatchPartMachine ThermalConductorHatchPart;
    private final ConditionalSubscriptionHandler maxCWUtModificationSubs;
    @SaveToDisk
    private int machineTier = 1;
    @SaveToDisk
    private int maxCWUtModification;
    private boolean incompatible;
    private boolean canBridge;
    private int maxCWUt;
    private int coolingAmountRequired;
    private int coolingAmountProvided;
    private int coolantAmount;
    private final Reference2IntOpenHashMap<IItem> componentsMap = new Reference2IntOpenHashMap<>();
    private int lastTimeStamp;;
    private long allocatedCWUt;
    private long cacheCWUt;
    private long maxEUt;
    private GTRecipeDefinition runRecipe;
    @Nullable
    private TickableSubscription tickSubs;

    public SupercomputingCenterMachine(MetaMachineBlockEntity holder) {
        super(holder, 1, stack -> MAINFRAME.containsKey(stack.getItem()));
        maxCWUtModificationSubs = new ConditionalSubscriptionHandler(this, this::maxCWUtModificationUpdate, 10, () -> isFormed);
    }

    private void clean(boolean scanOnly) {
        canBridge = false;
        incompatible = false;
        runRecipe = null;
        allocatedCWUt = scanOnly ? allocatedCWUt : 0;
        cacheCWUt = scanOnly ? cacheCWUt : 0;
        maxCWUt = 0;
        coolingAmountRequired = 0;
        coolingAmountProvided = 0;
        coolantAmount = 0;
        maxEUt = 0;
        componentsMap.clear();
    }

    private void changed(boolean scanOnly) {
        int maxCoolantAmount = 0;
        int hpcaPassiveCoolingAmount = 0;

        clean(scanOnly);
        if (!isFormed) return;
        Integer computerTier = getMultiblockState().getMatchContext().get(GTORecipeDataKeys.COMPUTER_CASING_TIER);
        if (computerTier == null || machineTier != computerTier) {
            incompatible = true;
            return;
        }
        Integer heatTier = getMultiblockState().getMatchContext().get(GTORecipeDataKeys.COMPUTER_HEAT_TIER);
        if (heatTier == null || machineTier != heatTier) {
            incompatible = true;
            return;
        }
        Integer glassTier = getMultiblockState().getMatchContext().get(GTORecipeDataKeys.GLASS_TIER);
        if (glassTier == null || glassTier < GLASS_MAP.get(machineTier)) {
            incompatible = true;
            return;
        }
        if (machineTier == 3) canBridge = true;

        for (IMultiPart part : getParts()) {
            if (incompatible) return;
            if (!(part instanceof HPCAComponentPartMachine componentPartMachine)) continue;

            maxEUt += componentPartMachine.getMaxEUt();
            addToComponentsMap(componentPartMachine);

            switch (componentPartMachine) {
                // NICH & GWCA
                case ExResearchBasePartMachine exPart -> {
                    if (exPart.getTier() - 1 != machineTier) {
                        incompatible = true;
                        return;
                    }

                    switch (exPart) {
                        case ExResearchBridgePartMachine b -> canBridge = true;
                        case ExResearchComputationPartMachine c when !c.isDamaged() -> {
                            maxCWUt += c.getCWUPerTick();
                            coolingAmountRequired += c.getCoolingPerTick();
                        }
                        case ExResearchCoolerPartMachine c -> {
                            coolingAmountProvided += c.getCoolingAmount();
                            maxCoolantAmount += c.getMaxCoolantPerTick();
                        }
                        default -> {}
                    }
                }

                // HPCA
                // must be handled after NICH & GWCA as they extend HPCAComponentPartMachine
                case HPCAComponentPartMachine hpcaPart -> {
                    if (machineTier > 1) {
                        incompatible = true;
                        return;
                    }

                    switch (hpcaPart) {
                        case HPCABridgePartMachine b -> canBridge = true;
                        case HPCAComputationPartMachine c when !c.isDamaged() -> {
                            maxCWUt += c.getCWUPerTick();
                            coolingAmountRequired += c.getCoolingPerTick();
                        }
                        case HPCACoolerPartMachine c -> {
                            coolingAmountProvided += c.getCoolingAmount();
                            if (!c.isActiveCooler()) hpcaPassiveCoolingAmount += c.getCoolingAmount();
                            maxCoolantAmount += c.getMaxCoolantPerTick();
                        }
                        default -> {}
                    }
                }
            }
        }

        if (coolingAmountProvided > 0 && coolingAmountProvided > hpcaPassiveCoolingAmount) {
            var coolantRatio = (double) maxCoolantAmount / (coolingAmountProvided - hpcaPassiveCoolingAmount);
            var activeCoolingNeeded = Math.max(0, coolingAmountRequired - hpcaPassiveCoolingAmount);
            coolantAmount = (int) Math.ceil(activeCoolingNeeded * coolantRatio);
        }

        if (maxEUt > 0) {
            if (machineTier == 1)
                runRecipe = RecipeBuilder.ofRaw().inputFluids(PCBCoolant.getFluid(LIQUID, coolantAmount)).EUt(maxEUt / 4).duration(20).build();
            else
                runRecipe = RecipeBuilder.ofRaw().inputFluids(Helium.getFluid(LIQUID, coolantAmount)).outputFluids(Helium.getFluid(GAS, coolantAmount)).EUt(maxEUt).duration(20).build();
        }
        maxCWUtModificationSubs.initialize(getLevel());

        updateTickSubscription();
    }

    private static int getIndexForItem(Item item) {
        return ITEM_INDEX_MAP.getOrDefault(item, -1);
    }

    private void addToComponentsMap(HPCAComponentPartMachine partMachine) {
        if (partMachine instanceof HPCAComponentPartMachine p && (p instanceof HPCAComputationPartMachine ||
                p instanceof HPCACoolerPartMachine ||
                p instanceof ExResearchComputationPartMachine ||
                p instanceof ExResearchCoolerPartMachine)) {
            if (partMachine.isDamaged()) {
                return;
            }
        } else {
            return;
        }

        componentsMap.addTo((IItem) partMachine.getDefinition().asItem(), 1);
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
        changed(false);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        changed(false);
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        ThermalConductorHatchPart = null;
        maxCWUtModification = 10000;
        clean(false);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (getLevel() instanceof ServerLevel serverLevel) {
            TaskHandler.enqueueTask(serverLevel, this::updateTickSubscription);
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        if (tickSubs != null) {
            tickSubs.unsubscribe();
            tickSubs = null;
        }
    }

    private void updateTickSubscription() {
        if (isFormed && !incompatible) {
            tickSubs = subscribeServerTick(tickSubs, this::tick);
        } else if (tickSubs != null) {
            tickSubs.unsubscribe();
            tickSubs = null;
        }
    }

    private void tick() {
        var timer = getOffsetTimer();
        if (lastTimeStamp != timer) {
            lastTimeStamp = timer;
            cacheCWUt = allocatedCWUt;
            allocatedCWUt = 0;
        }
    }

    @Override
    public void afterWorking() {
        boolean dirty = false;

        if (coolingAmountRequired > coolingAmountProvided) {
            int damaged = coolingAmountRequired - coolingAmountProvided;
            for (IMultiPart part : getParts()) {
                if (part instanceof HPCAComponentPartMachine componentPartMachine && componentPartMachine.canBeDamaged() && !componentPartMachine.isDamaged()) {
                    damaged -= GTValues.RNG.nextInt(256);
                    componentPartMachine.setDamaged(true);
                    dirty = true;
                }
                if (damaged <= 0) break;
            }
        }
        super.afterWorking();

        if (dirty) {
            changed(true);
        }
    }

    @Override
    public GTRecipe fullModifyRecipe(RecipeHandlerUnit unit, GTRecipe recipe) {
        // prevent any modification to mock the original behavior of setupRecipe
        return recipe;
    }

    private long requestCWUt(boolean simulate, long cwu) {
        var timer = getOffsetTimer();
        if (lastTimeStamp != timer) {
            lastTimeStamp = timer;
            cacheCWUt = allocatedCWUt;
            allocatedCWUt = 0;
        }
        long toAllocate = Math.min(cwu, getAdjustedMaxCWU() - allocatedCWUt);
        if (!simulate) {
            this.allocatedCWUt += toAllocate;
        }
        return toAllocate;
    }

    @Override
    public long requestCWU(long cwu, boolean simulate) {
        if (incompatible) return 0;
        if (runRecipe != null && isFormed) {
            if (simulate) return requestCWUt(true, cwu);
            if (getRecipeLogic().isWorking()) {
                return requestCWUt(false, cwu);
            } else if (!getRecipeLogic().isSuspend()) {
                for (var u : getInputUnits()) {
                    if (getRecipeLogic().checkMatchedRecipeAvailable(u, runRecipe) && getRecipeLogic().isWorking()) {
                        return requestCWUt(false, cwu);
                    }
                }
            }
        }
        return 0;
    }

    private long getAdjustedMaxCWU() {
        return (getMaxCWUt() * maxCWUtModification / 10000);
    }

    private void maxCWUtModificationUpdate() {
        if (isFormed) {
            if (machineTier > 1) {
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
                            int requestedConsumption = Math.min(count, (max - maxCWUtModification) / N_MFPCs[index] + 1);
                            ItemStack convertedStack = new ItemStack(valueItem);
                            int outputCapacity = getConversionOutputCapacity(stackTransfer, convertedStack);
                            if (outputCapacity < requestedConsumption && requestedConsumption == count) {
                                int sourceSlotCapacity = getEmptySlotOutputCapacity(stackTransfer, i, convertedStack);
                                if ((long) outputCapacity + sourceSlotCapacity >= count) {
                                    outputCapacity = count;
                                }
                            }
                            int consumption = Math.min(requestedConsumption, outputCapacity);
                            if (consumption <= 0) continue;
                            stackTransfer.setStackInSlot(i, consumption == count ? ItemStack.EMPTY : itemStack.copyWithCount(count - consumption));
                            maxCWUtModification += N_MFPCs[index] * consumption;
                            insertConversionOutput(stackTransfer, convertedStack, consumption);
                        }
                        if (maxCWUtModification >= max) break;
                    }
                }
                if (maxCWUtModification < 8000) maxCWUtModification = 8000;
            } else maxCWUtModification = 10000;
        }
        maxCWUtModificationSubs.updateSubscription();
    }

    private static int getConversionOutputCapacity(CustomItemStackHandler stackTransfer, ItemStack convertedStack) {
        int capacity = 0;
        for (int slot = 0; slot < stackTransfer.getSlots(); slot++) {
            ItemStack slotStack = stackTransfer.getStackInSlot(slot);
            if (slotStack.isEmpty()) {
                capacity = addCapacity(capacity, getEmptySlotOutputCapacity(stackTransfer, slot, convertedStack));
            } else if (ItemStack.isSameItemSameTags(slotStack, convertedStack)) {
                int stackLimit = getOutputStackLimit(stackTransfer, slot, slotStack);
                if (slotStack.getCount() < stackLimit) {
                    capacity = addCapacity(capacity, stackLimit - slotStack.getCount());
                }
            }
        }
        return capacity;
    }

    private static int getEmptySlotOutputCapacity(CustomItemStackHandler stackTransfer, int slot, ItemStack convertedStack) {
        return getOutputStackLimit(stackTransfer, slot, convertedStack);
    }

    private static int getOutputStackLimit(CustomItemStackHandler stackTransfer, int slot, ItemStack stack) {
        return Math.min(stackTransfer.getSlotLimit(slot), stack.getMaxStackSize());
    }

    private static int addCapacity(int capacity, int available) {
        if (available <= 0) return capacity;
        return capacity > Integer.MAX_VALUE - available ? Integer.MAX_VALUE : capacity + available;
    }

    private static void insertConversionOutput(CustomItemStackHandler stackTransfer, ItemStack convertedStack, int count) {
        int remaining = count;
        for (int slot = 0; slot < stackTransfer.getSlots(); slot++) {
            ItemStack slotStack = stackTransfer.getStackInSlot(slot);
            if (slotStack.isEmpty() || !ItemStack.isSameItemSameTags(slotStack, convertedStack)) continue;
            int stackLimit = getOutputStackLimit(stackTransfer, slot, slotStack);
            int toInsert = Math.min(remaining, stackLimit - slotStack.getCount());
            if (toInsert <= 0) continue;
            stackTransfer.setStackInSlot(slot, slotStack.copyWithCount(slotStack.getCount() + toInsert));
            remaining -= toInsert;
            if (remaining <= 0) return;
        }
        for (int slot = 0; slot < stackTransfer.getSlots(); slot++) {
            ItemStack slotStack = stackTransfer.getStackInSlot(slot);
            if (!slotStack.isEmpty()) continue;
            int toInsert = Math.min(remaining, getEmptySlotOutputCapacity(stackTransfer, slot, convertedStack));
            if (toInsert <= 0) continue;
            stackTransfer.setStackInSlot(slot, convertedStack.copyWithCount(toInsert));
            remaining -= toInsert;
            if (remaining <= 0) return;
        }
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
            textList.add(Component.translatable("ars_nouveau.tier", machineTier));
            textList.add(Component.translatable("gtceu.multiblock.invalid_structure").withStyle(ChatFormatting.RED));
        } else {
            super.addDisplayText(textList);
        }
    }

    @Override
    public void customText(List<Component> textList) {
        super.customText(textList);
        textList.add(Component.translatable("ars_nouveau.tier", machineTier));
        textList.add(Component.translatable(canBridge ? "gtceu.multiblock.hpca.info_bridging_enabled" : "gtceu.multiblock.hpca.info_bridging_disabled").withStyle(canBridge ? ChatFormatting.GREEN : ChatFormatting.RED));
        textList.add(Component.translatable("gtceu.multiblock.energy_consumption", FormattingUtil.formatNumbers(maxEUt), GTValues.VNF[GTUtil.getTierByVoltage(maxEUt)]).withStyle(ChatFormatting.YELLOW));
        textList.add(Component.translatable("gtceu.multiblock.hpca.computation", Component.literal(cacheCWUt + " / " + getAdjustedMaxCWU()).append(Component.literal(" CWU/t")).withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.GRAY));
        textList.add(Component.translatable("gtocore.machine.cwut_modification", ((double) maxCWUtModification / 10000)).withStyle(ChatFormatting.AQUA));
        textList.add(Component.translatable("gtceu.multiblock.hpca.info_max_coolant_required", Component.literal(coolingAmountRequired + " / " + coolingAmountProvided + "  " + coolantAmount).withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.GRAY));
        textList.add(Component.translatable("gtocore.machine.components_list").withStyle(ChatFormatting.YELLOW));
        for (var entries : componentsMap.reference2IntEntrySet()) {
            textList.add(Component.literal(" - ").append(entries.getKey().gtolib$getReadOnlyStack().getDisplayName()).append(Component.literal(" x" + entries.getIntValue())).withStyle(ChatFormatting.GRAY));
        }
    }
}
