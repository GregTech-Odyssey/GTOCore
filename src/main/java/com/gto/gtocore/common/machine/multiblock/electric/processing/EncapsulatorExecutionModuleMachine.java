package com.gto.gtocore.common.machine.multiblock.electric.processing;

import com.gto.gtocore.api.GTOValues;
import com.gto.gtocore.api.gui.ParallelConfigurator;
import com.gto.gtocore.api.item.NBTItem;
import com.gto.gtocore.api.machine.feature.IOverclockConfigMachine;
import com.gto.gtocore.api.machine.feature.IRecipeSearchMachine;
import com.gto.gtocore.api.machine.feature.multiblock.IParallelMachine;
import com.gto.gtocore.api.machine.feature.multiblock.ITierCasingMachine;
import com.gto.gtocore.api.machine.multiblock.StorageMultiblockMachine;
import com.gto.gtocore.api.machine.trait.CustomParallelTrait;
import com.gto.gtocore.api.machine.trait.CustomRecipeLogic;
import com.gto.gtocore.api.machine.trait.TierCasingTrait;
import com.gto.gtocore.api.recipe.GTORecipeBuilder;
import com.gto.gtocore.api.recipe.RecipeRunnerHelper;
import com.gto.gtocore.utils.GTOUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.item.MetaMachineItem;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.chance.logic.ChanceLogic;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import appeng.api.crafting.PatternDetailsHelper;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.GenericStack;
import appeng.core.definitions.AEItems;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class EncapsulatorExecutionModuleMachine extends StorageMultiblockMachine implements ITierCasingMachine, IParallelMachine, IRecipeSearchMachine, IOverclockConfigMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            EncapsulatorExecutionModuleMachine.class, StorageMultiblockMachine.MANAGED_FIELD_HOLDER);

    private GTRecipeType recipeTypeCache = GTRecipeTypes.DUMMY_RECIPES;

    @Persisted
    private GTRecipe finalRecipe;

    @Persisted
    private final List<GTRecipe> packageRecipe = new ArrayList<>();

    private final List<GTRecipe> invalidRecipe = new ArrayList<>();

    private final Object2IntOpenHashMap<NBTItem> inputItemStackMap = new Object2IntOpenHashMap<>();
    private final Object2IntOpenHashMap<FluidStack> inputFluidStackMap = new Object2IntOpenHashMap<>();
    private final Object2IntOpenHashMap<NBTItem> outputItemStackMap = new Object2IntOpenHashMap<>();
    private final Object2IntOpenHashMap<FluidStack> outputFluidStackMap = new Object2IntOpenHashMap<>();

    @Persisted
    private final CustomParallelTrait customParallelTrait;

    private final TierCasingTrait tierCasingTrait;

    public EncapsulatorExecutionModuleMachine(IMachineBlockEntity holder) {
        super(holder, 1, i -> true);
        customParallelTrait = new CustomParallelTrait(this, true, machine -> Integer.MAX_VALUE);
        tierCasingTrait = new TierCasingTrait(this, GTOValues.INTEGRAL_FRAMEWORK_TIER);
        customParallelTrait.setDefaultMax(false);
    }

    private void update() {
        recipeTypeCache = GTRecipeTypes.DUMMY_RECIPES;
        if (getStorageStack().getItem() instanceof MetaMachineItem metaMachineItem) {
            MachineDefinition definition = metaMachineItem.getDefinition();
            recipeTypeCache = definition.getRecipeTypes()[0];
        }
    }

    @Override
    public void saveCustomPersistedData(@NotNull CompoundTag tag, boolean forDrop) {
        super.saveCustomPersistedData(tag, forDrop);
        if (!inputItemStackMap.isEmpty()) {
            var map = new ListTag();
            for (var entry : inputItemStackMap.object2IntEntrySet()) {
                var pairs = new CompoundTag();
                pairs.put("K", entry.getKey().serializeNBT());
                pairs.putInt("V", entry.getIntValue());
                map.add(pairs);
            }
            tag.put("inputItemStackMap", map);
        }
        if (!inputFluidStackMap.isEmpty()) {
            var map = new ListTag();
            for (var entry : inputFluidStackMap.object2IntEntrySet()) {
                var pairs = new CompoundTag();
                pairs.put("K", entry.getKey().writeToNBT(new CompoundTag()));
                pairs.putInt("V", entry.getIntValue());
                map.add(pairs);
            }
            tag.put("inputFluidStackMap", map);
        }
        if (!outputItemStackMap.isEmpty()) {
            var map = new ListTag();
            for (var entry : outputItemStackMap.object2IntEntrySet()) {
                var pairs = new CompoundTag();
                pairs.put("K", entry.getKey().serializeNBT());
                pairs.putInt("V", entry.getIntValue());
                map.add(pairs);
            }
            tag.put("outputItemStackMap", map);
        }
        if (!outputFluidStackMap.isEmpty()) {
            var map = new ListTag();
            for (var entry : outputFluidStackMap.object2IntEntrySet()) {
                var pairs = new CompoundTag();
                pairs.put("K", entry.getKey().writeToNBT(new CompoundTag()));
                pairs.putInt("V", entry.getIntValue());
                map.add(pairs);
            }
            tag.put("outputFluidStackMap", map);
        }
    }

    @Override
    public void loadCustomPersistedData(@NotNull CompoundTag tag) {
        super.loadCustomPersistedData(tag);
        if (tag.contains("inputItemStackMap")) {
            inputItemStackMap.clear();
            var map = tag.getList("inputItemStackMap", Tag.TAG_COMPOUND);
            for (var c : map) {
                CompoundTag pairs = (CompoundTag) c;
                var key = NBTItem.of(pairs.getCompound("K"));
                var value = pairs.getInt("V");
                inputItemStackMap.put(key, value);
            }
        }
        if (tag.contains("inputFluidStackMap")) {
            inputFluidStackMap.clear();
            var map = tag.getList("inputFluidStackMap", Tag.TAG_COMPOUND);
            for (var c : map) {
                CompoundTag pairs = (CompoundTag) c;
                var key = FluidStack.loadFluidStackFromNBT(pairs.getCompound("K"));
                var value = pairs.getInt("V");
                inputFluidStackMap.put(key, value);
            }
        }
        if (tag.contains("outputItemStackMap")) {
            outputItemStackMap.clear();
            var map = tag.getList("outputItemStackMap", Tag.TAG_COMPOUND);
            for (var c : map) {
                CompoundTag pairs = (CompoundTag) c;
                var key = NBTItem.of(pairs.getCompound("K"));
                var value = pairs.getInt("V");
                outputItemStackMap.put(key, value);
            }
        }
        if (tag.contains("outputFluidStackMap")) {
            outputFluidStackMap.clear();
            var map = tag.getList("outputFluidStackMap", Tag.TAG_COMPOUND);
            for (var c : map) {
                CompoundTag pairs = (CompoundTag) c;
                var key = FluidStack.loadFluidStackFromNBT(pairs.getCompound("K"));
                var value = pairs.getInt("V");
                outputFluidStackMap.put(key, value);
            }
        }
    }

    @Override
    public void onMachineChanged() {
        if (isFormed) {
            if (getRecipeLogic().getLastRecipe() != null) {
                getRecipeLogic().markLastRecipeDirty();
            }
            getRecipeLogic().updateTickSubscription();
            update();
        }
    }

    @Override
    public boolean storageFilter(ItemStack stack) {
        return ProcessingArrayMachine.storageFilter(stack, getCasingTier(GTOValues.INTEGRAL_FRAMEWORK_TIER));
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public boolean matchRecipe(GTRecipe recipe) {
        if (getStorageStack().getItem() instanceof MetaMachineItem metaMachineItem) {
            MachineDefinition definition = metaMachineItem.getDefinition();
            if (definition.getTier() >= RecipeHelper.getRecipeEUtTier(recipe)) {
                return getOverclockTier() >= RecipeHelper.getRecipeEUtTier(recipe) && RecipeRunnerHelper.matchRecipeInput(this, recipe);
            }
        }
        return false;
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        update();
        for (var recipe : packageRecipe) {
            boolean invalid = true;
            var mapReicpe = GTORecipeBuilder.RECIPE_MAP.get(recipe.id);
            if (mapReicpe != null) {
                var inputItem = GTOUtils.getInputItems(recipe);
                var mapInputItem = GTOUtils.getInputItems(mapReicpe);
                if (inputItem.size() == mapInputItem.size()) {
                    boolean itemMatch = true;
                    for (int j = 0; j < inputItem.size(); j++) {
                        if (!ItemStack.isSameItemSameTags(inputItem.get(j), (mapInputItem.get(j)))) {
                            itemMatch = false;
                            break;
                        }
                    }
                    if (itemMatch) {
                        var inputFluid = GTOUtils.getInputFluids(recipe);
                        var mapInputFluid = GTOUtils.getInputFluids(mapReicpe);
                        if (inputFluid.size() == mapInputFluid.size()) {
                            boolean fluidMatch = true;
                            for (int j = 0; j < inputFluid.size(); j++) {
                                if (!inputFluid.get(j).equals(mapInputFluid.get(j))) {
                                    fluidMatch = false;
                                    break;
                                }
                            }
                            if (fluidMatch) {
                                invalid = false;
                            }
                        }
                    }
                }
            }
            if (invalid) invalidRecipe.add(recipe);
        }
        if (invalidRecipe.isEmpty()) return;
        clean();
    }

    private void clean() {
        finalRecipe = null;
        packageRecipe.clear();
        inputItemStackMap.clear();
        inputFluidStackMap.clear();
        outputItemStackMap.clear();
        outputFluidStackMap.clear();
        getRecipeLogic().markLastRecipeDirty();
    }

    @Override
    public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
        super.attachConfigurators(configuratorPanel);
        configuratorPanel.attachConfigurators(new ParallelConfigurator(this));
    }

    @Override
    public void customText(List<Component> textList) {
        super.customText(textList);
        textList.add(ComponentPanelWidget.withButton(Component.literal("[").append(Component.translatable("gui.ae2.Clean")).append(Component.literal("]")), "clean"));
        textList.add(ComponentPanelWidget.withButton(Component.literal("[").append(Component.translatable("gui.jade.search")).append(Component.literal("]")), "search"));
        textList.add(ComponentPanelWidget.withButton(Component.literal("[").append(Component.translatable("gtocore.build")).append(Component.literal("]")), "build"));
        textList.add(ComponentPanelWidget.withButton(Component.literal("[").append(Component.translatable("gui.ae2.Patterns")).append(Component.literal("]")), "pattern"));
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        if (isRemote()) return;
        if (finalRecipe != null) {
            long eut = RecipeHelper.getInputEUt(finalRecipe);
            textList.add(Component.literal(FormattingUtil.formatNumbers(eut)).append(" EU/t (").append(GTValues.VNF[GTUtil.getFloorTierByVoltage(eut)]).append(") "));
            textList.add(Component.literal("Duration: ").append(String.valueOf(finalRecipe.duration)));
        } else if (!invalidRecipe.isEmpty()) {
            textList.add(Component.translatable("attributeslib.value.boolean.invalid"));
            for (var recipe : invalidRecipe) {
                textList.add(Component.literal("Recipe: ").append(Component.translatable(recipe.id.toString())));
            }
        }
        textList.add(Component.translatable("gtceu.io.import"));
        for (var inputItem : inputItemStackMap.object2IntEntrySet()) {
            if (inputItem.getIntValue() > 0) textList.add(inputItem.getKey().toStack().getDisplayName().copy().append(" x").append(String.valueOf(inputItem.getIntValue())));
        }
        for (var inputFluid : inputFluidStackMap.object2IntEntrySet()) {
            if (inputFluid.getIntValue() > 0) textList.add(inputFluid.getKey().getDisplayName().copy().append(" x").append(String.valueOf(inputFluid.getIntValue())));
        }
        textList.add(Component.translatable("gtceu.io.export"));
        for (var outputItem : outputItemStackMap.object2IntEntrySet()) {
            if (outputItem.getIntValue() > 0) textList.add(outputItem.getKey().toStack().getDisplayName().copy().append(" x").append(String.valueOf(outputItem.getIntValue())));
        }
        for (var outputFluid : outputFluidStackMap.object2IntEntrySet()) {
            if (outputFluid.getIntValue() > 0) textList.add(outputFluid.getKey().getDisplayName().copy().append(" x").append(String.valueOf(outputFluid.getIntValue())));
        }
    }

    @Override
    public void handleDisplayClick(String componentData, ClickData clickData) {
        if (!clickData.isRemote) {
            switch (componentData) {
                case "clean" -> {
                    clean();
                    invalidRecipe.clear();
                }
                case "pattern" -> {
                    if (finalRecipe != null && inputItem(AEItems.BLANK_PATTERN.stack())) {
                        List<GenericStack> input = new ArrayList<>();
                        List<GenericStack> output = new ArrayList<>();
                        for (var inputItem : inputItemStackMap.object2IntEntrySet()) {
                            if (inputItem.getIntValue() > 0) input.add(new GenericStack(AEItemKey.of(inputItem.getKey().toStack()), inputItem.getIntValue()));
                        }
                        for (var inputFluid : inputFluidStackMap.object2IntEntrySet()) {
                            if (inputFluid.getIntValue() > 0) input.add(new GenericStack(AEFluidKey.of(inputFluid.getKey()), inputFluid.getIntValue()));
                        }
                        for (var outputItem : outputItemStackMap.object2IntEntrySet()) {
                            if (outputItem.getIntValue() > 0) output.add(new GenericStack(AEItemKey.of(outputItem.getKey().toStack()), outputItem.getIntValue()));
                        }
                        for (var outputFluid : outputFluidStackMap.object2IntEntrySet()) {
                            if (outputFluid.getIntValue() > 0) output.add(new GenericStack(AEFluidKey.of(outputFluid.getKey()), outputFluid.getIntValue()));
                        }
                        outputItem(PatternDetailsHelper.encodeProcessingPattern(input.toArray(new GenericStack[0]), output.toArray(new GenericStack[0])));
                    }
                }
                case "build" -> {
                    if (finalRecipe != null || packageRecipe.isEmpty()) return;
                    var recipeBuilder = GTORecipeBuilder.ofRaw();
                    long totalEU = 0;
                    for (var recipe : packageRecipe) {
                        totalEU += (RecipeHelper.getInputEUt(recipe) * recipe.duration * recipe.parallels);
                    }
                    long maxEUt = getOverclockVoltage();
                    double d = (double) totalEU / maxEUt;
                    int limit = gTOCore$getOCLimit();
                    recipeBuilder.EUt(d >= limit ? maxEUt : Math.max(1, (long) (maxEUt * d / limit))).duration((int) Math.max(Math.max(1, d), limit));
                    for (var it2 = outputItemStackMap.object2IntEntrySet().iterator(); it2.hasNext();) {
                        var entry = it2.next();
                        var stack = entry.getKey();
                        var count = entry.getIntValue();
                        if (inputItemStackMap.containsKey(stack)) {
                            int inputCount = inputItemStackMap.getInt(stack);
                            if (inputCount >= count) {
                                inputCount -= count;
                                if (inputCount > 0) {
                                    inputItemStackMap.put(stack, inputCount);
                                } else {
                                    inputItemStackMap.removeInt(stack);
                                }
                                it2.remove();
                            } else {
                                count -= inputCount;
                                entry.setValue(count);
                                inputItemStackMap.removeInt(stack);
                            }
                        }
                    }
                    for (var it2 = outputFluidStackMap.object2IntEntrySet().iterator(); it2.hasNext();) {
                        var entry = it2.next();
                        var stack = entry.getKey();
                        var count = entry.getIntValue();
                        if (inputFluidStackMap.containsKey(stack)) {
                            int inputCount = inputFluidStackMap.getInt(stack);
                            if (inputCount >= count) {
                                inputCount -= count;
                                if (inputCount > 0) {
                                    inputFluidStackMap.put(stack, inputCount);
                                } else {
                                    inputFluidStackMap.removeInt(stack);
                                }
                                it2.remove();
                            } else {
                                count -= inputCount;
                                entry.setValue(count);
                                inputFluidStackMap.removeInt(stack);
                            }
                        }
                    }
                    for (var inputItem : inputItemStackMap.object2IntEntrySet()) {
                        if (inputItem.getIntValue() > 0) recipeBuilder.inputItems(inputItem.getKey().toStack(inputItem.getIntValue()));
                    }
                    for (var inputFluid : inputFluidStackMap.object2IntEntrySet()) {
                        if (inputFluid.getIntValue() > 0) {
                            var fluid = inputFluid.getKey();
                            fluid.setAmount(inputFluid.getIntValue());
                            recipeBuilder.inputFluids(fluid);
                        }
                    }
                    for (var outputItem : outputItemStackMap.object2IntEntrySet()) {
                        if (outputItem.getIntValue() > 0) recipeBuilder.outputItems(outputItem.getKey().toStack(outputItem.getIntValue()));
                    }
                    for (var outputFluid : outputFluidStackMap.object2IntEntrySet()) {
                        if (outputFluid.getIntValue() > 0) {
                            var fluid = outputFluid.getKey();
                            fluid.setAmount(outputFluid.getIntValue());
                            recipeBuilder.outputFluids(fluid);
                        }
                    }
                    finalRecipe = recipeBuilder.buildRawRecipe();
                }
                case "search" -> {
                    if (recipeTypeCache == GTRecipeTypes.DUMMY_RECIPES || finalRecipe != null) return;
                    Iterator<GTRecipe> iterator = recipeTypeCache.searchRecipe(this, this::matchRecipe);
                    while (iterator.hasNext()) {
                        GTRecipe recipe = iterator.next();
                        if (recipe == null) continue;
                        if (RecipeRunnerHelper.checkConditions(this, recipe)) {
                            int parallel = getParallel();
                            recipe.parallels = parallel;
                            packageRecipe.add(recipe);
                            var inputItem = recipe.inputs.get(ItemRecipeCapability.CAP);
                            if (inputItem != null) {
                                for (var content : inputItem) {
                                    if (content.chance > 0) {
                                        var ingredient = ItemRecipeCapability.CAP.of(content.content);
                                        if (ingredient.getItems().length > 0 && !ingredient.getItems()[0].isEmpty())
                                            forEachInputItems(stack -> {
                                                if (ingredient.test(stack)) {
                                                    inputItemStackMap.addTo(NBTItem.of(stack), parallel * ingredient.getItems()[0].getCount());
                                                    return true;
                                                }
                                                return false;
                                            });
                                    }
                                }
                            }
                            var inputFluid = recipe.inputs.get(FluidRecipeCapability.CAP);
                            if (inputFluid != null) {
                                for (var content : inputFluid) {
                                    if (content.chance > 0) {
                                        var ingredient = FluidRecipeCapability.CAP.of(content.content);
                                        if (ingredient.getStacks().length > 0 && !ingredient.getStacks()[0].isEmpty())
                                            forEachInputFluids(stack -> {
                                                if (ingredient.test(stack)) {
                                                    inputFluidStackMap.addTo(stack, parallel * ingredient.getStacks()[0].getAmount());
                                                    return true;
                                                }
                                                return false;
                                            });
                                    }
                                }
                            }
                            var outputItem = recipe.outputs.get(ItemRecipeCapability.CAP);
                            if (outputItem != null) {
                                for (var content : outputItem) {
                                    if (content.chance == ChanceLogic.getMaxChancedValue()) {
                                        var ingredient = ItemRecipeCapability.CAP.of(content.content);
                                        if (ingredient.getItems().length > 0 && !ingredient.getItems()[0].isEmpty()) {
                                            outputItemStackMap.addTo(NBTItem.of(ingredient.getItems()[0]), parallel * ingredient.getItems()[0].getCount());
                                        }
                                    }
                                }
                            }
                            var outputFluid = recipe.outputs.get(FluidRecipeCapability.CAP);
                            if (outputFluid != null) {
                                for (var content : outputFluid) {
                                    if (content.chance == ChanceLogic.getMaxChancedValue()) {
                                        var ingredient = FluidRecipeCapability.CAP.of(content.content);
                                        if (ingredient.getStacks().length > 0 && !ingredient.getStacks()[0].isEmpty()) {
                                            outputFluidStackMap.addTo(ingredient.getStacks()[0], parallel * ingredient.getStacks()[0].getAmount());
                                        }
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    private @Nullable GTRecipe getRecipe() {
        if (finalRecipe != null) {
            var recipe = fullModifyRecipe(finalRecipe);
            if (RecipeRunnerHelper.check(this, recipe)) {
                return recipe;
            }
        }
        return null;
    }

    @Override
    protected @NotNull RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new CustomRecipeLogic(this, this::getRecipe);
    }

    @Override
    public int getMaxParallel() {
        return customParallelTrait.getMaxParallel();
    }

    @Override
    public int getParallel() {
        return customParallelTrait.getParallel();
    }

    @Override
    public void setParallel(int number) {
        customParallelTrait.setParallel(number);
    }

    @Override
    public Object2IntMap<String> getCasingTiers() {
        return tierCasingTrait.getCasingTiers();
    }
}
