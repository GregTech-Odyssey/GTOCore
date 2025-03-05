package com.gto.gtocore.api.recipe;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.capability.recipe.ManaRecipeCapability;
import com.gto.gtocore.api.data.tag.ITagPrefix;
import com.gto.gtocore.common.data.GTORecipes;
import com.gto.gtocore.common.recipe.condition.GravityCondition;
import com.gto.gtocore.common.recipe.condition.VacuumCondition;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.capability.recipe.*;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.UnificationEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.item.component.IDataItem;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.api.recipe.*;
import com.gregtechceu.gtceu.api.recipe.category.GTRecipeCategory;
import com.gregtechceu.gtceu.api.recipe.chance.logic.ChanceLogic;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.recipe.ingredient.IntCircuitIngredient;
import com.gregtechceu.gtceu.api.recipe.ingredient.IntProviderIngredient;
import com.gregtechceu.gtceu.api.recipe.ingredient.SizedIngredient;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;
import com.gregtechceu.gtceu.common.recipe.condition.*;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.gregtechceu.gtceu.utils.ResearchManager;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings({ "unchecked", "UnusedReturnValue" })
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class GTORecipeBuilder extends GTRecipeBuilder {

    GTORecipeBuilder(ResourceLocation id, GTRecipeType recipeType) {
        super(id, recipeType);
    }

    public static GTORecipeBuilder ofRaw() {
        return new GTORecipeBuilder(GTCEu.id("raw"), GTRecipeTypes.DUMMY_RECIPES);
    }

    @Override
    public GTORecipeBuilder copy(String id) {
        return copy(GTCEu.id(id));
    }

    @Override
    public GTORecipeBuilder copy(ResourceLocation id) {
        GTORecipeBuilder copy = new GTORecipeBuilder(id, this.recipeType);
        this.input.forEach((k, v) -> copy.input.put(k, new ArrayList<>(v)));
        this.output.forEach((k, v) -> copy.output.put(k, new ArrayList<>(v)));
        this.tickInput.forEach((k, v) -> copy.tickInput.put(k, new ArrayList<>(v)));
        this.tickOutput.forEach((k, v) -> copy.tickOutput.put(k, new ArrayList<>(v)));
        copy.conditions.addAll(this.conditions);
        copy.data = this.data.copy();
        copy.duration = this.duration;
        copy.chance = this.chance;
        copy.perTick = this.perTick;
        copy.recipeCategory = this.recipeCategory;
        copy.uiName = this.uiName;
        copy.slotName = this.slotName;
        copy.onSave = this.onSave;
        return copy;
    }

    @Override
    public GTORecipeBuilder copyFrom(GTRecipeBuilder builder) {
        return ((GTORecipeBuilder) builder).copy(builder.id).onSave(null).recipeType(recipeType).category(recipeCategory);
    }

    @Override
    public <T> GTORecipeBuilder input(RecipeCapability<T> capability, T obj) {
        var t = (perTick ? tickInput : input);
        t.computeIfAbsent(capability, c -> new ArrayList<>()).add(new Content(capability.of(obj), chance, maxChance, tierChanceBoost, slotName, uiName));
        return this;
    }

    @Override
    @SafeVarargs
    public final <T> GTORecipeBuilder input(RecipeCapability<T> capability, T... obj) {
        var t = (perTick ? tickInput : input);
        t.computeIfAbsent(capability, c -> new ArrayList<>()).addAll(Arrays.stream(obj).map(capability::of).map(o -> new Content(o, chance, maxChance, tierChanceBoost, slotName, uiName)).toList());
        return this;
    }

    @Override
    public <T> GTORecipeBuilder output(RecipeCapability<T> capability, T obj) {
        var t = (perTick ? tickOutput : output);
        t.computeIfAbsent(capability, c -> new ArrayList<>()).add(new Content(capability.of(obj), chance, maxChance, tierChanceBoost, slotName, uiName));
        return this;
    }

    @SafeVarargs
    @Override
    public final <T> GTORecipeBuilder output(RecipeCapability<T> capability, T... obj) {
        var t = (perTick ? tickOutput : output);
        t.computeIfAbsent(capability, c -> new ArrayList<>()).addAll(Arrays.stream(obj).map(capability::of).map(o -> new Content(o, chance, maxChance, tierChanceBoost, slotName, uiName)).toList());
        return this;
    }

    @Override
    public <T> GTORecipeBuilder inputs(RecipeCapability<T> capability, Object obj) {
        var t = (perTick ? tickInput : input);
        t.computeIfAbsent(capability, c -> new ArrayList<>()).add(new Content(capability.of(obj), chance, maxChance, tierChanceBoost, slotName, uiName));
        return this;
    }

    @Override
    public <T> GTORecipeBuilder inputs(RecipeCapability<T> capability, Object... obj) {
        var t = (perTick ? tickInput : input);
        t.computeIfAbsent(capability, c -> new ArrayList<>()).addAll(Arrays.stream(obj).map(capability::of).map(o -> new Content(o, chance, maxChance, tierChanceBoost, slotName, uiName)).toList());
        return this;
    }

    @Override
    public <T> GTORecipeBuilder outputs(RecipeCapability<T> capability, Object obj) {
        var t = (perTick ? tickOutput : output);
        t.computeIfAbsent(capability, c -> new ArrayList<>()).add(new Content(capability.of(obj), chance, maxChance, tierChanceBoost, slotName, uiName));
        return this;
    }

    @Override
    public <T> GTORecipeBuilder outputs(RecipeCapability<T> capability, Object... obj) {
        var t = (perTick ? tickOutput : output);
        t.computeIfAbsent(capability, c -> new ArrayList<>()).addAll(Arrays.stream(obj).map(capability::of).map(o -> new Content(o, chance, maxChance, tierChanceBoost, slotName, uiName)).toList());
        return this;
    }

    @Override
    public GTORecipeBuilder addCondition(RecipeCondition condition) {
        conditions.add(condition);
        return this;
    }

    @Override
    public GTORecipeBuilder inputEU(long eu) {
        return input(EURecipeCapability.CAP, eu);
    }

    @Override
    public GTORecipeBuilder EUt(long eu) {
        if (eu == 0) {
            GTOCore.LOGGER.error("EUt can't be explicitly set to 0, id: {}", id);
        }
        var lastPerTick = perTick;
        perTick = true;
        if (eu > 0) {
            tickInput.remove(EURecipeCapability.CAP);
            inputEU(eu);
        } else if (eu < 0) {
            tickOutput.remove(EURecipeCapability.CAP);
            outputEU(-eu);
        }
        perTick = lastPerTick;
        return this;
    }

    @Override
    public GTORecipeBuilder outputEU(long eu) {
        return output(EURecipeCapability.CAP, eu);
    }

    @Override
    public GTORecipeBuilder inputCWU(int cwu) {
        return input(CWURecipeCapability.CAP, cwu);
    }

    @Override
    public GTORecipeBuilder CWUt(int cwu) {
        if (cwu == 0) {
            GTOCore.LOGGER.error("CWUt can't be explicitly set to 0, id: {}", id);
        }
        var lastPerTick = perTick;
        perTick = true;
        if (cwu > 0) {
            tickInput.remove(CWURecipeCapability.CAP);
            inputCWU(cwu);
        } else if (cwu < 0) {
            tickOutput.remove(CWURecipeCapability.CAP);
            outputCWU(cwu);
        }
        perTick = lastPerTick;
        return this;
    }

    @Override
    public GTORecipeBuilder totalCWU(int cwu) {
        this.durationIsTotalCWU(true);
        this.hideDuration(true);
        this.duration(cwu);
        return this;
    }

    @Override
    public GTORecipeBuilder outputCWU(int cwu) {
        return output(CWURecipeCapability.CAP, cwu);
    }

    @Override
    public GTORecipeBuilder inputItems(Object input) {
        if (input instanceof Item item) {
            return inputItems(item);
        } else if (input instanceof Supplier<?> supplier && supplier.get() instanceof ItemLike item) {
            return inputItems(item.asItem());
        } else if (input instanceof ItemStack stack) {
            return inputItems(stack);
        } else if (input instanceof Ingredient ingredient) {
            return inputItems(ingredient);
        } else if (input instanceof UnificationEntry entry) {
            return inputItems(entry);
        } else if (input instanceof TagKey<?> tag) {
            return inputItems((TagKey<Item>) tag);
        } else if (input instanceof MachineDefinition machine) {
            return inputItems(machine);
        } else {
            GTOCore.LOGGER.error("Input item is not one of: Item, Supplier<Item>, ItemStack, Ingredient, UnificationEntry, TagKey<Item>, MachineDefinition, id: {}", id);
            return this;
        }
    }

    @Override
    public GTORecipeBuilder inputItems(Object input, int count) {
        if (input instanceof Item item) {
            return inputItems(item, count);
        } else if (input instanceof Supplier<?> supplier && supplier.get() instanceof ItemLike item) {
            return inputItems(item.asItem(), count);
        } else if (input instanceof ItemStack stack) {
            return inputItems(stack.copyWithCount(count));
        } else if (input instanceof Ingredient ingredient) {
            return inputItems(ingredient, count);
        } else if (input instanceof UnificationEntry entry) {
            return inputItems(entry, count);
        } else if (input instanceof TagKey<?> tag) {
            return inputItems((TagKey<Item>) tag, count);
        } else if (input instanceof MachineDefinition machine) {
            return inputItems(machine, count);
        } else {
            GTOCore.LOGGER.error("Input item is not one of: Item, Supplier<Item>, ItemStack, Ingredient, UnificationEntry, TagKey<Item>, MachineDefinition, id: {}", id);
            return this;
        }
    }

    @Override
    public GTORecipeBuilder inputItems(Ingredient inputs) {
        return input(ItemRecipeCapability.CAP, inputs);
    }

    @Override
    public GTORecipeBuilder inputItems(Ingredient... inputs) {
        return input(ItemRecipeCapability.CAP, inputs);
    }

    @Override
    public GTORecipeBuilder inputItems(ItemStack input) {
        if (input.isEmpty()) {
            GTOCore.LOGGER.error("Input items is empty, id: {}", id);
        }
        return input(ItemRecipeCapability.CAP, SizedIngredient.create(input));
    }

    @Override
    public GTORecipeBuilder inputItems(ItemStack... inputs) {
        for (ItemStack itemStack : inputs) {
            if (itemStack.isEmpty()) {
                GTOCore.LOGGER.error("Input item is empty, id: {}", id);
            }
        }
        return input(ItemRecipeCapability.CAP, Arrays.stream(inputs).map(SizedIngredient::create).toArray(Ingredient[]::new));
    }

    @Override
    public GTORecipeBuilder inputItems(TagKey<Item> tag, int amount) {
        if (amount == 0) {
            GTOCore.LOGGER.error("Item Count is 0, id: {}", id);
        }
        return inputItems(SizedIngredient.create(tag, amount));
    }

    @Override
    public GTORecipeBuilder inputItems(TagKey<Item> tag) {
        return inputItems(tag, 1);
    }

    @Override
    public GTORecipeBuilder inputItems(Item input, int amount) {
        return inputItems(new ItemStack(input, amount));
    }

    @Override
    public GTORecipeBuilder inputItems(Item input) {
        return inputItems(SizedIngredient.create(new ItemStack(input)));
    }

    @Override
    public GTORecipeBuilder inputItems(Supplier<? extends Item> input) {
        return inputItems(input.get());
    }

    @Override
    public GTORecipeBuilder inputItems(Supplier<? extends Item> input, int amount) {
        return inputItems(new ItemStack(input.get(), amount));
    }

    @Override
    public GTORecipeBuilder inputItems(TagPrefix orePrefix, Material material) {
        return inputItems(orePrefix, material, 1);
    }

    @Override
    public GTORecipeBuilder inputItems(UnificationEntry input) {
        if (input.material == null) {
            GTOCore.LOGGER.error("Unification Entry material is null, id: {}, TagPrefix: {}", id, input.tagPrefix);
        }
        return inputItems(input.tagPrefix, input.material, 1);
    }

    @Override
    public GTORecipeBuilder inputItems(UnificationEntry input, int count) {
        if (input.material == null) {
            GTOCore.LOGGER.error("Unification Entry material is null, id: {}, TagPrefix: {}", id, input.tagPrefix);
        }
        return inputItems(input.tagPrefix, input.material, count);
    }

    @Override
    public GTORecipeBuilder inputItems(TagPrefix orePrefix, Material material, int count) {
        if (((ITagPrefix) orePrefix).gtocore$isTagInput()) {
            TagKey<Item> tag = ChemicalHelper.getTag(orePrefix, material);
            if (tag != null) {
                return inputItems(tag, count);
            }
        }
        return inputItems(ChemicalHelper.get(orePrefix, material, count));
    }

    @Override
    public GTORecipeBuilder inputItems(MachineDefinition machine) {
        return inputItems(machine, 1);
    }

    @Override
    public GTORecipeBuilder inputItems(MachineDefinition machine, int count) {
        return inputItems(machine.asStack(count));
    }

    @Override
    public GTORecipeBuilder outputItems(Object input) {
        if (input instanceof Item item) {
            return outputItems(item);
        } else if (input instanceof Supplier<?> supplier && supplier.get() instanceof ItemLike item) {
            return outputItems(item.asItem());
        } else if (input instanceof ItemStack stack) {
            return outputItems(stack);
        } else if (input instanceof UnificationEntry entry) {
            return outputItems(entry);
        } else if (input instanceof MachineDefinition machine) {
            return outputItems(machine);
        } else {
            GTOCore.LOGGER.error("Output item is not one of: Item, Supplier<Item>, ItemStack, Ingredient, UnificationEntry, TagKey<Item>, MachineDefinition, id: {}", id);
            return this;
        }
    }

    @Override
    public GTORecipeBuilder outputItems(Object input, int count) {
        if (input instanceof Item item) {
            return outputItems(item, count);
        } else if (input instanceof Supplier<?> supplier && supplier.get() instanceof ItemLike item) {
            return outputItems(item.asItem(), count);
        } else if (input instanceof ItemStack stack) {
            return outputItems(stack.copyWithCount(count));
        } else if (input instanceof UnificationEntry entry) {
            return outputItems(entry, count);
        } else if (input instanceof MachineDefinition machine) {
            return outputItems(machine, count);
        } else {
            GTOCore.LOGGER.error("Output item is not one of: Item, Supplier<Item>, ItemStack, Ingredient, UnificationEntry, TagKey<Item>, MachineDefinition, id: {}", id);
            return this;
        }
    }

    @Override
    public GTORecipeBuilder outputItems(Ingredient... inputs) {
        return output(ItemRecipeCapability.CAP, inputs);
    }

    @Override
    public GTORecipeBuilder outputItems(ItemStack output) {
        if (output.isEmpty()) {
            GTOCore.LOGGER.error("Output items is empty, id: {}", id);
        }
        return output(ItemRecipeCapability.CAP, SizedIngredient.create(output));
    }

    @Override
    public GTORecipeBuilder outputItems(ItemStack... outputs) {
        for (ItemStack itemStack : outputs) {
            if (itemStack.isEmpty()) {
                GTOCore.LOGGER.error("Output items is empty, id: {}", id);
            }
        }
        return output(ItemRecipeCapability.CAP, Arrays.stream(outputs).map(SizedIngredient::create).toArray(Ingredient[]::new));
    }

    @Override
    public GTORecipeBuilder outputItems(Item output, int amount) {
        return outputItems(new ItemStack(output, amount));
    }

    @Override
    public GTORecipeBuilder outputItems(Item output) {
        return outputItems(new ItemStack(output));
    }

    @Override
    public GTORecipeBuilder outputItems(Supplier<? extends ItemLike> input) {
        return outputItems(new ItemStack(input.get().asItem()));
    }

    @Override
    public GTORecipeBuilder outputItems(Supplier<? extends ItemLike> input, int amount) {
        return outputItems(new ItemStack(input.get().asItem(), amount));
    }

    @Override
    public GTORecipeBuilder outputItems(TagPrefix orePrefix, Material material) {
        return outputItems(orePrefix, material, 1);
    }

    @Override
    public GTORecipeBuilder outputItems(TagPrefix orePrefix, Material material, int count) {
        var item = ChemicalHelper.get(orePrefix, material, count);
        if (item.isEmpty()) {
            GTOCore.LOGGER.error("Tried to set output item stack that doesn't exist, TagPrefix: {}, Material: {}", orePrefix, material);
        }
        return outputItems(item);
    }

    @Override
    public GTORecipeBuilder outputItems(UnificationEntry entry) {
        if (entry.material == null) {
            GTOCore.LOGGER.error("Unification Entry material is null, id: {}, TagPrefix: {}", id, entry.tagPrefix);
        }
        return outputItems(entry.tagPrefix, entry.material);
    }

    @Override
    public GTORecipeBuilder outputItems(UnificationEntry entry, int count) {
        if (entry.material == null) {
            GTOCore.LOGGER.error("Unification Entry material is null, id: {}, TagPrefix: {}", id, entry.tagPrefix);
        }
        return outputItems(entry.tagPrefix, entry.material, count);
    }

    @Override
    public GTORecipeBuilder outputItems(MachineDefinition machine) {
        return outputItems(machine, 1);
    }

    @Override
    public GTORecipeBuilder outputItems(MachineDefinition machine, int count) {
        return outputItems(machine.asStack(count));
    }

    @Override
    public GTORecipeBuilder outputItemsRanged(ItemStack output, IntProvider intProvider) {
        return outputItems(IntProviderIngredient.create(SizedIngredient.create(output), intProvider));
    }

    @Override
    public GTORecipeBuilder outputItemsRanged(Item input, IntProvider intProvider) {
        return outputItemsRanged(new ItemStack(input), intProvider);
    }

    @Override
    public GTORecipeBuilder outputItemsRanged(Supplier<? extends ItemLike> output, IntProvider intProvider) {
        return outputItemsRanged(new ItemStack(output.get().asItem()), intProvider);
    }

    @Override
    public GTORecipeBuilder outputItemsRanged(TagPrefix orePrefix, Material material, IntProvider intProvider) {
        var item = ChemicalHelper.get(orePrefix, material, 1);
        if (item.isEmpty()) {
            GTOCore.LOGGER.error("Tried to set output ranged item stack that doesn't exist, TagPrefix: {}, Material: {}", orePrefix, material);
        }
        return outputItemsRanged(item, intProvider);
    }

    @Override
    public GTORecipeBuilder outputItemsRanged(MachineDefinition machine, IntProvider intProvider) {
        return outputItemsRanged(machine.asStack(), intProvider);
    }

    @Override
    public GTORecipeBuilder notConsumable(ItemStack itemStack) {
        int lastChance = this.chance;
        this.chance = 0;
        inputItems(itemStack);
        this.chance = lastChance;
        return this;
    }

    @Override
    public GTORecipeBuilder notConsumable(Ingredient ingredient) {
        int lastChance = this.chance;
        this.chance = 0;
        inputItems(ingredient);
        this.chance = lastChance;
        return this;
    }

    @Override
    public GTORecipeBuilder notConsumable(Item item) {
        int lastChance = this.chance;
        this.chance = 0;
        inputItems(item);
        this.chance = lastChance;
        return this;
    }

    @Override
    public GTORecipeBuilder notConsumable(Supplier<? extends Item> item) {
        int lastChance = this.chance;
        this.chance = 0;
        inputItems(item);
        this.chance = lastChance;
        return this;
    }

    @Override
    public GTORecipeBuilder notConsumable(TagPrefix orePrefix, Material material) {
        int lastChance = this.chance;
        this.chance = 0;
        inputItems(orePrefix, material);
        this.chance = lastChance;
        return this;
    }

    @Override
    public GTORecipeBuilder notConsumable(TagPrefix orePrefix, Material material, int count) {
        int lastChance = this.chance;
        this.chance = 0;
        inputItems(orePrefix, material, count);
        this.chance = lastChance;
        return this;
    }

    @Override
    public GTORecipeBuilder notConsumableFluid(FluidStack fluid) {
        return notConsumableFluid(FluidIngredient.of(fluid));
    }

    @Override
    public GTORecipeBuilder notConsumableFluid(FluidIngredient ingredient) {
        int lastChance = this.chance;
        this.chance = 0;
        inputFluids(ingredient);
        this.chance = lastChance;
        return this;
    }

    @Override
    public GTORecipeBuilder circuitMeta(int configuration) {
        if (configuration < 0 || configuration > IntCircuitBehaviour.CIRCUIT_MAX) {
            GTOCore.LOGGER.error("Circuit configuration must be in the bounds 0 - 32");
        }
        return notConsumable(IntCircuitIngredient.circuitInput(configuration));
    }

    @Override
    public GTORecipeBuilder chancedInput(ItemStack stack, int chance, int tierChanceBoost) {
        if (0 >= chance || chance > ChanceLogic.getMaxChancedValue()) {
            GTOCore.LOGGER.error("Chance cannot be less or equal to 0 or more than {}. Actual: {}.", ChanceLogic.getMaxChancedValue(), chance, new Throwable());
            return this;
        }
        int lastChance = this.chance;
        int lastTierChanceBoost = this.tierChanceBoost;
        this.chance = chance;
        this.tierChanceBoost = tierChanceBoost;
        inputItems(stack);
        this.chance = lastChance;
        this.tierChanceBoost = lastTierChanceBoost;
        return this;
    }

    @Override
    public GTORecipeBuilder chancedInput(FluidStack stack, int chance, int tierChanceBoost) {
        if (0 >= chance || chance > ChanceLogic.getMaxChancedValue()) {
            GTOCore.LOGGER.error("Chance cannot be less or equal to 0 or more than {}. Actual: {}.", ChanceLogic.getMaxChancedValue(), chance, new Throwable());
            return this;
        }
        int lastChance = this.chance;
        int lastTierChanceBoost = this.tierChanceBoost;
        this.chance = chance;
        this.tierChanceBoost = tierChanceBoost;
        inputFluids(stack);
        this.chance = lastChance;
        this.tierChanceBoost = lastTierChanceBoost;
        return this;
    }

    @Override
    public GTORecipeBuilder chancedOutput(ItemStack stack, int chance, int tierChanceBoost) {
        if (0 >= chance || chance > ChanceLogic.getMaxChancedValue()) {
            GTOCore.LOGGER.error("Chance cannot be less or equal to 0 or more than {}. Actual: {}.", ChanceLogic.getMaxChancedValue(), chance, new Throwable());
            return this;
        }
        int lastChance = this.chance;
        int lastTierChanceBoost = this.tierChanceBoost;
        this.chance = chance;
        this.tierChanceBoost = tierChanceBoost;
        outputItems(stack);
        this.chance = lastChance;
        this.tierChanceBoost = lastTierChanceBoost;
        return this;
    }

    @Override
    public GTORecipeBuilder chancedOutput(FluidStack stack, int chance, int tierChanceBoost) {
        if (0 >= chance || chance > ChanceLogic.getMaxChancedValue()) {
            GTOCore.LOGGER.error("Chance cannot be less or equal to 0 or more than {}. Actual: {}.", ChanceLogic.getMaxChancedValue(), chance, new Throwable());
            return this;
        }
        int lastChance = this.chance;
        int lastTierChanceBoost = this.tierChanceBoost;
        this.chance = chance;
        this.tierChanceBoost = tierChanceBoost;
        outputFluids(stack);
        this.chance = lastChance;
        this.tierChanceBoost = lastTierChanceBoost;
        return this;
    }

    @Override
    public GTORecipeBuilder chancedOutput(TagPrefix tag, Material mat, int chance, int tierChanceBoost) {
        return chancedOutput(ChemicalHelper.get(tag, mat), chance, tierChanceBoost);
    }

    @Override
    public GTORecipeBuilder chancedOutput(TagPrefix tag, Material mat, int count, int chance, int tierChanceBoost) {
        return chancedOutput(ChemicalHelper.get(tag, mat, count), chance, tierChanceBoost);
    }

    @Override
    public GTORecipeBuilder chancedOutput(ItemStack stack, String fraction, int tierChanceBoost) {
        if (stack.isEmpty()) {
            return this;
        }
        String[] split = fraction.split("/");
        if (split.length != 2) {
            GTOCore.LOGGER.error("Fraction was not parsed correctly! Expected format is \"1/3\". Actual: \"{}\".", fraction, new Throwable());
            return this;
        }
        int chance;
        int maxChance;
        try {
            chance = Integer.parseInt(split[0]);
            maxChance = Integer.parseInt(split[1]);
        } catch (NumberFormatException e) {
            GTOCore.LOGGER.error("Fraction was not parsed correctly! Expected format is \"1/3\". Actual: \"{}\".", fraction, new Throwable());
            return this;
        }
        if (0 >= chance || chance > ChanceLogic.getMaxChancedValue()) {
            GTOCore.LOGGER.error("Chance cannot be less or equal to 0 or more than {}. Actual: {}.", ChanceLogic.getMaxChancedValue(), chance, new Throwable());
            return this;
        }
        if (chance >= maxChance || maxChance > ChanceLogic.getMaxChancedValue()) {
            GTOCore.LOGGER.error("Max Chance cannot be less or equal to Chance or more than {}. Actual: {}.", ChanceLogic.getMaxChancedValue(), maxChance, new Throwable());
            return this;
        }
        int scalar = Math.floorDiv(ChanceLogic.getMaxChancedValue(), maxChance);
        chance *= scalar;
        maxChance *= scalar;
        int lastChance = this.chance;
        int lastMaxChance = this.maxChance;
        int lastTierChanceBoost = this.tierChanceBoost;
        this.chance = chance;
        this.maxChance = maxChance;
        this.tierChanceBoost = tierChanceBoost;
        outputItems(stack);
        this.chance = lastChance;
        this.maxChance = lastMaxChance;
        this.tierChanceBoost = lastTierChanceBoost;
        return this;
    }

    @Override
    public GTORecipeBuilder chancedOutput(TagPrefix prefix, Material material, int count, String fraction, int tierChanceBoost) {
        return chancedOutput(ChemicalHelper.get(prefix, material, count), fraction, tierChanceBoost);
    }

    @Override
    public GTORecipeBuilder chancedOutput(TagPrefix prefix, Material material, String fraction, int tierChanceBoost) {
        return chancedOutput(prefix, material, 1, fraction, tierChanceBoost);
    }

    @Override
    public GTORecipeBuilder chancedOutput(Item item, int count, String fraction, int tierChanceBoost) {
        return chancedOutput(new ItemStack(item, count), fraction, tierChanceBoost);
    }

    @Override
    public GTORecipeBuilder chancedOutput(Item item, String fraction, int tierChanceBoost) {
        return chancedOutput(item, 1, fraction, tierChanceBoost);
    }

    @Override
    public GTORecipeBuilder chancedFluidOutput(FluidStack stack, String fraction, int tierChanceBoost) {
        if (stack.isEmpty()) {
            return this;
        }
        String[] split = fraction.split("/");
        if (split.length != 2) {
            GTOCore.LOGGER.error("Fraction was not parsed correctly! Expected format is \"1/3\". Actual: \"{}\".", fraction, new Throwable());
            return this;
        }
        int chance;
        int maxChance;
        try {
            chance = Integer.parseInt(split[0]);
            maxChance = Integer.parseInt(split[1]);
        } catch (NumberFormatException e) {
            GTOCore.LOGGER.error("Fraction was not parsed correctly! Expected format is \"1/3\". Actual: \"{}\".", fraction, new Throwable());
            return this;
        }
        if (0 >= chance || chance > ChanceLogic.getMaxChancedValue()) {
            GTOCore.LOGGER.error("Chance cannot be less or equal to 0 or more than {}. Actual: {}.", ChanceLogic.getMaxChancedValue(), chance, new Throwable());
            return this;
        }
        if (chance >= maxChance || maxChance > ChanceLogic.getMaxChancedValue()) {
            GTOCore.LOGGER.error("Max Chance cannot be less or equal to Chance or more than {}. Actual: {}.", ChanceLogic.getMaxChancedValue(), maxChance, new Throwable());
            return this;
        }
        int scalar = Math.floorDiv(ChanceLogic.getMaxChancedValue(), maxChance);
        chance *= scalar;
        maxChance *= scalar;
        int lastChance = this.chance;
        int lastMaxChance = this.maxChance;
        int lastTierChanceBoost = this.tierChanceBoost;
        this.chance = chance;
        this.maxChance = maxChance;
        this.tierChanceBoost = tierChanceBoost;
        outputFluids(stack);
        this.chance = lastChance;
        this.maxChance = lastMaxChance;
        this.tierChanceBoost = lastTierChanceBoost;
        return this;
    }

    /**
     * Set a chanced output logic for a specific capability.
     * all capabilities default to OR logic if not set.
     *
     * @param cap   the {@link RecipeCapability} to set the logic for
     * @param logic the {@link ChanceLogic} to use
     * @return this builder
     */
    @Override
    public GTORecipeBuilder chancedOutputLogic(RecipeCapability<?> cap, ChanceLogic logic) {
        this.outputChanceLogic.put(cap, logic);
        return this;
    }

    @Override
    public GTORecipeBuilder chancedItemOutputLogic(ChanceLogic logic) {
        return chancedOutputLogic(ItemRecipeCapability.CAP, logic);
    }

    @Override
    public GTORecipeBuilder chancedFluidOutputLogic(ChanceLogic logic) {
        return chancedOutputLogic(FluidRecipeCapability.CAP, logic);
    }

    @Override
    public GTORecipeBuilder chancedInputLogic(RecipeCapability<?> cap, ChanceLogic logic) {
        this.inputChanceLogic.put(cap, logic);
        return this;
    }

    @Override
    public GTORecipeBuilder chancedItemInputLogic(ChanceLogic logic) {
        return chancedInputLogic(ItemRecipeCapability.CAP, logic);
    }

    @Override
    public GTORecipeBuilder chancedFluidInputLogic(ChanceLogic logic) {
        return chancedInputLogic(FluidRecipeCapability.CAP, logic);
    }

    @Override
    public GTORecipeBuilder chancedTickOutputLogic(RecipeCapability<?> cap, ChanceLogic logic) {
        this.tickOutputChanceLogic.put(cap, logic);
        return this;
    }

    @Override
    public GTORecipeBuilder chancedTickInputLogic(RecipeCapability<?> cap, ChanceLogic logic) {
        this.tickInputChanceLogic.put(cap, logic);
        return this;
    }

    @Override
    public GTORecipeBuilder inputFluids(FluidStack input) {
        return input(FluidRecipeCapability.CAP, FluidIngredient.of(input));
    }

    @Override
    public GTORecipeBuilder inputFluids(FluidStack... inputs) {
        return input(FluidRecipeCapability.CAP, Arrays.stream(inputs).map(FluidIngredient::of).toArray(FluidIngredient[]::new));
    }

    @Override
    public GTORecipeBuilder inputFluids(FluidIngredient... inputs) {
        return input(FluidRecipeCapability.CAP, inputs);
    }

    @Override
    public GTORecipeBuilder outputFluids(FluidStack output) {
        return output(FluidRecipeCapability.CAP, FluidIngredient.of(output));
    }

    @Override
    public GTORecipeBuilder outputFluids(FluidStack... outputs) {
        return output(FluidRecipeCapability.CAP, Arrays.stream(outputs).map(FluidIngredient::of).toArray(FluidIngredient[]::new));
    }

    @Override
    public GTORecipeBuilder outputFluids(FluidIngredient... outputs) {
        return output(FluidRecipeCapability.CAP, outputs);
    }

    //////////////////////////////////////
    // ********** DATA ***********//
    //////////////////////////////////////
    @Override
    public GTORecipeBuilder addData(String key, Tag data) {
        this.data.put(key, data);
        return this;
    }

    @Override
    public GTORecipeBuilder addData(String key, int data) {
        this.data.putInt(key, data);
        return this;
    }

    @Override
    public GTORecipeBuilder addData(String key, long data) {
        this.data.putLong(key, data);
        return this;
    }

    @Override
    public GTORecipeBuilder addData(String key, String data) {
        this.data.putString(key, data);
        return this;
    }

    @Override
    public GTORecipeBuilder addData(String key, Float data) {
        this.data.putFloat(key, data);
        return this;
    }

    @Override
    public GTORecipeBuilder addData(String key, boolean data) {
        this.data.putBoolean(key, data);
        return this;
    }

    @Override
    public GTORecipeBuilder blastFurnaceTemp(int blastTemp) {
        return addData("ebf_temp", blastTemp);
    }

    @Override
    public GTORecipeBuilder explosivesAmount(int explosivesAmount) {
        return inputItems(new ItemStack(Blocks.TNT, explosivesAmount));
    }

    @Override
    public GTORecipeBuilder explosivesType(ItemStack explosivesType) {
        return inputItems(explosivesType);
    }

    @Override
    public GTORecipeBuilder solderMultiplier(int multiplier) {
        return addData("solder_multiplier", multiplier);
    }

    @Override
    public GTORecipeBuilder disableDistilleryRecipes(boolean flag) {
        return addData("disable_distillery", flag);
    }

    @Override
    public GTORecipeBuilder fusionStartEU(long eu) {
        return addData("eu_to_start", eu);
    }

    @Override
    public GTORecipeBuilder researchScan(boolean isScan) {
        return addData("scan_for_research", isScan);
    }

    @Override
    public GTORecipeBuilder durationIsTotalCWU(boolean durationIsTotalCWU) {
        return addData("duration_is_total_cwu", durationIsTotalCWU);
    }

    @Override
    public GTORecipeBuilder hideDuration(boolean hideDuration) {
        return addData("hide_duration", hideDuration);
    }

    //////////////////////////////////////
    // ******* CONDITIONS ********//
    //////////////////////////////////////
    @Override
    public GTORecipeBuilder cleanroom(CleanroomType cleanroomType) {
        return addCondition(new CleanroomCondition(cleanroomType));
    }

    @Override
    public GTORecipeBuilder dimension(ResourceLocation dimension, boolean reverse) {
        return addCondition(new DimensionCondition(dimension).setReverse(reverse));
    }

    @Override
    public GTORecipeBuilder dimension(ResourceLocation dimension) {
        return dimension(dimension, false);
    }

    @Override
    public GTORecipeBuilder biome(ResourceLocation biome, boolean reverse) {
        return addCondition(new BiomeCondition(biome).setReverse(reverse));
    }

    @Override
    public GTORecipeBuilder biome(ResourceLocation biome) {
        return biome(biome, false);
    }

    @Override
    public GTORecipeBuilder rain(float level, boolean reverse) {
        return addCondition(new RainingCondition(level).setReverse(reverse));
    }

    @Override
    public GTORecipeBuilder rain(float level) {
        return rain(level, false);
    }

    @Override
    public GTORecipeBuilder thunder(float level, boolean reverse) {
        return addCondition(new ThunderCondition(level).setReverse(reverse));
    }

    @Override
    public GTORecipeBuilder thunder(float level) {
        return thunder(level, false);
    }

    @Override
    public GTORecipeBuilder posY(int min, int max, boolean reverse) {
        return addCondition(new PositionYCondition(min, max).setReverse(reverse));
    }

    @Override
    public GTORecipeBuilder posY(int min, int max) {
        return posY(min, max, false);
    }

    @Override
    public GTORecipeBuilder daytime(boolean isNight) {
        return addCondition(new DaytimeCondition().setReverse(isNight));
    }

    @Override
    public GTORecipeBuilder daytime() {
        return daytime(false);
    }

    /**
     * Does not generate a research recipe.
     *
     * @param researchId the researchId for the recipe
     * @return this
     */
    @Override
    public GTORecipeBuilder researchWithoutRecipe(@NotNull String researchId) {
        return researchWithoutRecipe(researchId, ResearchManager.getDefaultScannerItem());
    }

    /**
     * Does not generate a research recipe.
     *
     * @param researchId the researchId for the recipe
     * @param dataStack  the stack to hold the data. Must have the {@link IDataItem} behavior.
     * @return this
     */
    @Override
    public GTORecipeBuilder researchWithoutRecipe(@NotNull String researchId, @NotNull ItemStack dataStack) {
        return (GTORecipeBuilder) super.researchWithoutRecipe(researchId, dataStack);
    }

    /**
     * Generates a research recipe for the Scanner.
     */
    @Override
    public GTORecipeBuilder scannerResearch(UnaryOperator<ResearchRecipeBuilder.ScannerRecipeBuilder> research) {
        return (GTORecipeBuilder) super.scannerResearch(research);
    }

    /**
     * Generates a research recipe for the Scanner. All values are defaults other than the research stack.
     *
     * @param researchStack the stack to use for research
     * @return this
     */
    @Override
    public GTORecipeBuilder scannerResearch(@NotNull ItemStack researchStack) {
        return scannerResearch(b -> b.researchStack(researchStack));
    }

    /**
     * Generates a research recipe for the Research Station.
     */
    @Override
    public GTORecipeBuilder stationResearch(UnaryOperator<ResearchRecipeBuilder.StationRecipeBuilder> research) {
        return (GTORecipeBuilder) super.stationResearch(research);
    }

    @Override
    public GTORecipeBuilder category(@NotNull GTRecipeCategory category) {
        this.recipeCategory = category;
        return this;
    }

    @Override
    public void save(Consumer<FinishedRecipe> consumer) {
        save();
    }

    public void save() {
        ResourceLocation typeid = getTypeID(this.id, this.recipeType);
        if (GTORecipes.GT_FILTER_RECIPES != null && GTORecipes.GT_FILTER_RECIPES.contains(typeid)) return;
        if (onSave != null) {
            onSave.accept(this, a -> {});
        }
        if (recipeType.isHasResearchSlot()) {
            ResearchCondition condition = this.conditions.stream().filter(ResearchCondition.class::isInstance).findAny().map(ResearchCondition.class::cast).orElse(null);
            if (condition != null) {
                for (ResearchData.ResearchEntry entry : condition.data) {
                    this.recipeType.addDataStickEntry(entry.getResearchId(), buildRawRecipe());
                }
            }
        }
        GTORecipes.GT_RECIPE_MAP.put(typeid, new GTRecipe(this.recipeType, typeid, this.input, this.output, this.tickInput, this.tickOutput, Map.of(), Map.of(), Map.of(), Map.of(), this.conditions, List.of(), this.data, this.duration, false, this.recipeCategory));
    }

    public static ResourceLocation getTypeID(ResourceLocation id, GTRecipeType recipeType) {
        return new ResourceLocation(id.getNamespace(), recipeType.registryName.getPath() + "/" + id.getPath());
    }

    @Override
    public GTRecipe buildRawRecipe() {
        return new GTRecipe(recipeType, id.withPrefix(recipeType.registryName.getPath() + "/"), input, output, tickInput, tickOutput, Map.of(), Map.of(), Map.of(), Map.of(), conditions, List.of(), data, duration, false, recipeCategory);
    }

    @Override
    public GTORecipeBuilder id(final ResourceLocation id) {
        this.id = id;
        return this;
    }

    @Override
    public GTORecipeBuilder recipeType(final GTRecipeType recipeType) {
        this.recipeType = recipeType;
        return this;
    }

    @Override
    public GTORecipeBuilder duration(final int duration) {
        this.duration = duration;
        return this;
    }

    @Override
    public GTORecipeBuilder perTick(final boolean perTick) {
        this.perTick = perTick;
        return this;
    }

    @Override
    public GTORecipeBuilder slotName(final String slotName) {
        this.slotName = slotName;
        return this;
    }

    @Override
    public GTORecipeBuilder uiName(final String uiName) {
        this.uiName = uiName;
        return this;
    }

    @Override
    public GTORecipeBuilder chance(final int chance) {
        this.chance = chance;
        return this;
    }

    @Override
    public GTORecipeBuilder maxChance(final int maxChance) {
        this.maxChance = maxChance;
        return this;
    }

    @Override
    public GTORecipeBuilder tierChanceBoost(final int tierChanceBoost) {
        this.tierChanceBoost = tierChanceBoost;
        return this;
    }

    @Override
    public GTORecipeBuilder onSave(@Nullable BiConsumer<GTRecipeBuilder, Consumer<FinishedRecipe>> onSave) {
        this.onSave = onSave;
        return this;
    }

    public GTORecipeBuilder vacuum(int tier) {
        return addCondition(new VacuumCondition(tier));
    }

    public GTORecipeBuilder gravity(boolean noGravity) {
        return addCondition(new GravityCondition(noGravity));
    }

    public GTORecipeBuilder MANAt(int mana) {
        var lastPerTick = perTick;
        perTick = true;
        if (mana > 0) {
            input(ManaRecipeCapability.CAP, mana);
        } else {
            output(ManaRecipeCapability.CAP, -mana);
        }
        perTick = lastPerTick;
        return this;
    }
}
