package com.gto.gtocore.api.recipe;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.capability.recipe.ManaRecipeCapability;
import com.gto.gtocore.api.data.chemical.GTOChemicalHelper;
import com.gto.gtocore.api.data.tag.ITagPrefix;
import com.gto.gtocore.api.item.NBTItem;
import com.gto.gtocore.common.recipe.condition.GravityCondition;
import com.gto.gtocore.common.recipe.condition.HeatCondition;
import com.gto.gtocore.common.recipe.condition.RunLimitCondition;
import com.gto.gtocore.common.recipe.condition.VacuumCondition;
import com.gto.gtocore.utils.RegistriesUtils;

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
import com.gregtechceu.gtceu.api.registry.GTRegistries;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.crafting.StrictNBTIngredient;
import net.minecraftforge.fluids.FluidStack;

import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import javax.annotation.ParametersAreNonnullByDefault;

import static it.unimi.dsi.fastutil.Hash.DEFAULT_INITIAL_SIZE;
import static it.unimi.dsi.fastutil.Hash.VERY_FAST_LOAD_FACTOR;

@SuppressWarnings({ "unchecked", "UnusedReturnValue" })
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class GTORecipeBuilder extends GTRecipeBuilder {

    public static Map<ResourceLocation, GTRecipe> RECIPE_MAP;

    private static Map<UnificationEntry, Ingredient> MATERIAL_INGREDIENT_MAP;
    private static Map<NBTItem, Ingredient> ITEM_INGREDIENT_MAP;
    private static Map<TagKey<Item>, Ingredient> TAG_INGREDIENT_MAP;

    public static void initialization() {
        RECIPE_MAP = new Object2ObjectOpenHashMap<>(4096);
        MATERIAL_INGREDIENT_MAP = new Object2ObjectOpenHashMap<>(1024, VERY_FAST_LOAD_FACTOR);
        ITEM_INGREDIENT_MAP = new Object2ObjectOpenHashMap<>(1024, VERY_FAST_LOAD_FACTOR);
        TAG_INGREDIENT_MAP = new Object2ObjectOpenHashMap<>(1024, VERY_FAST_LOAD_FACTOR);
        GTRegistries.RECIPE_TYPES.forEach(t -> ((GTORecipeType) t).filter = new Object2BooleanOpenHashMap<>(DEFAULT_INITIAL_SIZE, VERY_FAST_LOAD_FACTOR));
    }

    public static void clean() {
        MATERIAL_INGREDIENT_MAP = null;
        ITEM_INGREDIENT_MAP = null;
        TAG_INGREDIENT_MAP = null;
        GTRegistries.RECIPE_TYPES.forEach(t -> {
            ((GTORecipeType) t).filter.forEach((k, v) -> {
                if (!v) GTOCore.LOGGER.error("Recipe filter [{}] not in use", k);
            });
            ((GTORecipeType) t).filter = null;
        });
    }

    private static Ingredient getNonRepetitionIngredient(Item item) {
        if (ITEM_INGREDIENT_MAP == null) return Ingredient.of(item);
        NBTItem nbtItem = new NBTItem(item, null);
        Ingredient ingredient = ITEM_INGREDIENT_MAP.get(nbtItem);
        if (ingredient == null) {
            ingredient = Ingredient.of(item);
            ITEM_INGREDIENT_MAP.put(nbtItem, ingredient);
        }
        return ingredient;
    }

    private static SizedIngredient createSizedIngredient(Item item, int amount) {
        return SizedIngredient.create(getNonRepetitionIngredient(item), amount);
    }

    private static SizedIngredient createSizedIngredient(UnificationEntry unificationEntry, int amount) {
        if (MATERIAL_INGREDIENT_MAP == null) return SizedIngredient.create(ChemicalHelper.get(unificationEntry, amount));
        Ingredient ingredient = MATERIAL_INGREDIENT_MAP.get(unificationEntry);
        if (ingredient == null) {
            Item item = GTOChemicalHelper.getItem(unificationEntry);
            if (item == Items.AIR) {
                GTOCore.LOGGER.error("Tried to set output item stack that doesn't exist, TagPrefix: {}, Material: {}", unificationEntry.tagPrefix, unificationEntry.material);
            }
            ingredient = getNonRepetitionIngredient(item);
            MATERIAL_INGREDIENT_MAP.put(unificationEntry, ingredient);
        }
        return SizedIngredient.create(ingredient, amount);
    }

    private static SizedIngredient createSizedIngredient(ItemStack stack) {
        if (ITEM_INGREDIENT_MAP == null) return SizedIngredient.create(stack);
        NBTItem nbtItem = NBTItem.of(stack);
        Ingredient ingredient = ITEM_INGREDIENT_MAP.get(nbtItem);
        if (ingredient == null) {
            ingredient = nbtItem.nbt() == null ? Ingredient.of(stack) : StrictNBTIngredient.of(stack);
            ITEM_INGREDIENT_MAP.put(nbtItem, ingredient);
        }
        return SizedIngredient.create(ingredient, stack.getCount());
    }

    private static SizedIngredient createSizedIngredient(TagKey<Item> tag, int amount) {
        if (TAG_INGREDIENT_MAP == null) return SizedIngredient.create(tag, amount);
        Ingredient ingredient = TAG_INGREDIENT_MAP.get(tag);
        if (ingredient == null) {
            ingredient = Ingredient.of(tag);
            TAG_INGREDIENT_MAP.put(tag, ingredient);
        }
        return SizedIngredient.create(ingredient, amount);
    }

    boolean deleted;

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
        if (deleted) return this;
        var t = (perTick ? tickInput : input);
        t.computeIfAbsent(capability, c -> new ArrayList<>()).add(new Content(capability.of(obj), chance, maxChance, tierChanceBoost, slotName, uiName));
        return this;
    }

    @Override
    @SafeVarargs
    public final <T> GTORecipeBuilder input(RecipeCapability<T> capability, T... obj) {
        if (deleted) return this;
        var t = (perTick ? tickInput : input);
        t.computeIfAbsent(capability, c -> new ArrayList<>()).addAll(Arrays.stream(obj).map(capability::of).map(o -> new Content(o, chance, maxChance, tierChanceBoost, slotName, uiName)).toList());
        return this;
    }

    @Override
    public <T> GTORecipeBuilder output(RecipeCapability<T> capability, T obj) {
        if (deleted) return this;
        var t = (perTick ? tickOutput : output);
        t.computeIfAbsent(capability, c -> new ArrayList<>()).add(new Content(capability.of(obj), chance, maxChance, tierChanceBoost, slotName, uiName));
        return this;
    }

    @SafeVarargs
    @Override
    public final <T> GTORecipeBuilder output(RecipeCapability<T> capability, T... obj) {
        if (deleted) return this;
        var t = (perTick ? tickOutput : output);
        t.computeIfAbsent(capability, c -> new ArrayList<>()).addAll(Arrays.stream(obj).map(capability::of).map(o -> new Content(o, chance, maxChance, tierChanceBoost, slotName, uiName)).toList());
        return this;
    }

    @Override
    public <T> GTORecipeBuilder inputs(RecipeCapability<T> capability, Object obj) {
        if (deleted) return this;
        var t = (perTick ? tickInput : input);
        t.computeIfAbsent(capability, c -> new ArrayList<>()).add(new Content(capability.of(obj), chance, maxChance, tierChanceBoost, slotName, uiName));
        return this;
    }

    @Override
    public <T> GTORecipeBuilder inputs(RecipeCapability<T> capability, Object... obj) {
        if (deleted) return this;
        var t = (perTick ? tickInput : input);
        t.computeIfAbsent(capability, c -> new ArrayList<>()).addAll(Arrays.stream(obj).map(capability::of).map(o -> new Content(o, chance, maxChance, tierChanceBoost, slotName, uiName)).toList());
        return this;
    }

    @Override
    public <T> GTORecipeBuilder outputs(RecipeCapability<T> capability, Object obj) {
        if (deleted) return this;
        var t = (perTick ? tickOutput : output);
        t.computeIfAbsent(capability, c -> new ArrayList<>()).add(new Content(capability.of(obj), chance, maxChance, tierChanceBoost, slotName, uiName));
        return this;
    }

    @Override
    public <T> GTORecipeBuilder outputs(RecipeCapability<T> capability, Object... obj) {
        if (deleted) return this;
        var t = (perTick ? tickOutput : output);
        t.computeIfAbsent(capability, c -> new ArrayList<>()).addAll(Arrays.stream(obj).map(capability::of).map(o -> new Content(o, chance, maxChance, tierChanceBoost, slotName, uiName)).toList());
        return this;
    }

    @Override
    public GTORecipeBuilder addCondition(RecipeCondition condition) {
        if (deleted) return this;
        conditions.add(condition);
        return this;
    }

    @Override
    public GTORecipeBuilder inputEU(long eu) {
        return input(EURecipeCapability.CAP, eu);
    }

    @Override
    public GTORecipeBuilder EUt(long eu) {
        if (deleted) return this;
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
        if (deleted) return this;
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
        if (deleted) return this;
        if (input instanceof String string) {
            return inputItems(string);
        } else if (input instanceof Item item) {
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
        if (deleted) return this;
        if (input instanceof String string) {
            return inputItems(string, count);
        } else if (input instanceof Item item) {
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
        if (deleted) return this;
        if (input.isEmpty()) {
            GTOCore.LOGGER.error("Input items is empty, id: {}", id);
        }
        return input(ItemRecipeCapability.CAP, createSizedIngredient(input));
    }

    @Override
    public GTORecipeBuilder inputItems(ItemStack... inputs) {
        if (deleted) return this;
        for (ItemStack itemStack : inputs) {
            if (itemStack.isEmpty()) {
                GTOCore.LOGGER.error("Input item is empty, id: {}", id);
            }
        }
        return input(ItemRecipeCapability.CAP, Arrays.stream(inputs).map(GTORecipeBuilder::createSizedIngredient).toArray(Ingredient[]::new));
    }

    @Override
    public GTORecipeBuilder inputItems(TagKey<Item> tag, int amount) {
        if (deleted) return this;
        if (amount == 0) {
            GTOCore.LOGGER.error("Item Count is 0, id: {}", id);
        }
        return inputItems(createSizedIngredient(tag, amount));
    }

    @Override
    public GTORecipeBuilder inputItems(TagKey<Item> tag) {
        return inputItems(tag, 1);
    }

    @Override
    public GTORecipeBuilder inputItems(Item input, int amount) {
        if (deleted) return this;
        return inputItems(createSizedIngredient(input, amount));
    }

    @Override
    public GTORecipeBuilder inputItems(Item input) {
        if (deleted) return this;
        return inputItems(input, 1);
    }

    @Override
    public GTORecipeBuilder inputItems(Supplier<? extends Item> input) {
        if (deleted) return this;
        return inputItems(input.get());
    }

    @Override
    public GTORecipeBuilder inputItems(Supplier<? extends Item> input, int amount) {
        if (deleted) return this;
        return inputItems(input.get(), amount);
    }

    @Override
    public GTORecipeBuilder inputItems(TagPrefix orePrefix, Material material) {
        return inputItems(orePrefix, material, 1);
    }

    @Override
    public GTORecipeBuilder inputItems(UnificationEntry input) {
        return inputItems(input, 1);
    }

    @Override
    public GTORecipeBuilder inputItems(UnificationEntry input, int count) {
        if (deleted) return this;
        if (input.material == null) {
            GTOCore.LOGGER.error("Unification Entry material is null, id: {}, TagPrefix: {}", id, input.tagPrefix);
            return this;
        }
        if (((ITagPrefix) input.tagPrefix).gtocore$isTagInput()) {
            TagKey<Item> tag = ChemicalHelper.getTag(input.tagPrefix, input.material);
            if (tag != null) {
                return inputItems(tag, count);
            }
        }
        return input(ItemRecipeCapability.CAP, createSizedIngredient(input, count));
    }

    @Override
    public GTORecipeBuilder inputItems(TagPrefix orePrefix, Material material, int count) {
        if (deleted) return this;
        return inputItems(new UnificationEntry(orePrefix, material), count);
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
        if (deleted) return this;
        if (input instanceof String string) {
            return outputItems(string);
        } else if (input instanceof Item item) {
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
        if (deleted) return this;
        if (input instanceof String string) {
            return outputItems(string, count);
        } else if (input instanceof Item item) {
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
        if (deleted) return this;
        if (output.isEmpty()) {
            GTOCore.LOGGER.error("Output items is empty, id: {}", id);
        }
        return output(ItemRecipeCapability.CAP, createSizedIngredient(output));
    }

    @Override
    public GTORecipeBuilder outputItems(ItemStack... outputs) {
        if (deleted) return this;
        for (ItemStack itemStack : outputs) {
            if (itemStack.isEmpty()) {
                GTOCore.LOGGER.error("Output items is empty, id: {}", id);
            }
        }
        return output(ItemRecipeCapability.CAP, Arrays.stream(outputs).map(GTORecipeBuilder::createSizedIngredient).toArray(Ingredient[]::new));
    }

    @Override
    public GTORecipeBuilder outputItems(Item output, int amount) {
        if (deleted) return this;
        return output(ItemRecipeCapability.CAP, createSizedIngredient(output, amount));
    }

    @Override
    public GTORecipeBuilder outputItems(Item output) {
        if (deleted) return this;
        return outputItems(output, 1);
    }

    @Override
    public GTORecipeBuilder outputItems(Supplier<? extends ItemLike> input) {
        if (deleted) return this;
        return outputItems(input.get().asItem());
    }

    @Override
    public GTORecipeBuilder outputItems(Supplier<? extends ItemLike> input, int amount) {
        if (deleted) return this;
        return outputItems(input.get().asItem(), amount);
    }

    @Override
    public GTORecipeBuilder outputItems(TagPrefix orePrefix, Material material) {
        return outputItems(orePrefix, material, 1);
    }

    @Override
    public GTORecipeBuilder outputItems(TagPrefix orePrefix, Material material, int count) {
        if (deleted) return this;
        return outputItems(new UnificationEntry(orePrefix, material), count);
    }

    @Override
    public GTORecipeBuilder outputItems(UnificationEntry entry) {
        return outputItems(entry, 1);
    }

    @Override
    public GTORecipeBuilder outputItems(UnificationEntry output, int count) {
        if (deleted) return this;
        if (output.material == null) {
            GTOCore.LOGGER.error("Unification Entry material is null, id: {}, TagPrefix: {}", id, output.tagPrefix);
            return this;
        }
        return output(ItemRecipeCapability.CAP, createSizedIngredient(output, count));
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
        if (deleted) return this;
        return output(ItemRecipeCapability.CAP, IntProviderIngredient.create(createSizedIngredient(output), intProvider));
    }

    @Override
    public GTORecipeBuilder outputItemsRanged(Item output, IntProvider intProvider) {
        if (deleted) return this;
        return output(ItemRecipeCapability.CAP, IntProviderIngredient.create(createSizedIngredient(output, 1), intProvider));
    }

    @Override
    public GTORecipeBuilder outputItemsRanged(Supplier<? extends ItemLike> output, IntProvider intProvider) {
        if (deleted) return this;
        return outputItemsRanged(output.get().asItem(), intProvider);
    }

    @Override
    public GTORecipeBuilder outputItemsRanged(TagPrefix orePrefix, Material material, IntProvider intProvider) {
        if (deleted) return this;
        var item = ChemicalHelper.get(orePrefix, material, 1);
        if (item.isEmpty()) {
            GTOCore.LOGGER.error("Tried to set output ranged item stack that doesn't exist, TagPrefix: {}, Material: {}", orePrefix, material);
        }
        return outputItemsRanged(item, intProvider);
    }

    @Override
    public GTORecipeBuilder outputItemsRanged(MachineDefinition machine, IntProvider intProvider) {
        if (deleted) return this;
        return outputItemsRanged(machine.asStack(), intProvider);
    }

    @Override
    public GTORecipeBuilder notConsumable(ItemStack itemStack) {
        if (deleted) return this;
        int lastChance = this.chance;
        this.chance = 0;
        inputItems(itemStack);
        this.chance = lastChance;
        return this;
    }

    @Override
    public GTORecipeBuilder notConsumable(Ingredient ingredient) {
        if (deleted) return this;
        int lastChance = this.chance;
        this.chance = 0;
        inputItems(ingredient);
        this.chance = lastChance;
        return this;
    }

    @Override
    public GTORecipeBuilder notConsumable(Item item) {
        if (deleted) return this;
        int lastChance = this.chance;
        this.chance = 0;
        inputItems(item);
        this.chance = lastChance;
        return this;
    }

    @Override
    public GTORecipeBuilder notConsumable(Supplier<? extends Item> item) {
        if (deleted) return this;
        int lastChance = this.chance;
        this.chance = 0;
        inputItems(item);
        this.chance = lastChance;
        return this;
    }

    @Override
    public GTORecipeBuilder notConsumable(TagPrefix orePrefix, Material material) {
        if (deleted) return this;
        int lastChance = this.chance;
        this.chance = 0;
        inputItems(orePrefix, material);
        this.chance = lastChance;
        return this;
    }

    @Override
    public GTORecipeBuilder notConsumable(TagPrefix orePrefix, Material material, int count) {
        if (deleted) return this;
        int lastChance = this.chance;
        this.chance = 0;
        inputItems(orePrefix, material, count);
        this.chance = lastChance;
        return this;
    }

    @Override
    public GTORecipeBuilder notConsumableFluid(FluidStack fluid) {
        if (deleted) return this;
        return notConsumableFluid(FluidIngredient.of(fluid));
    }

    @Override
    public GTORecipeBuilder notConsumableFluid(FluidIngredient ingredient) {
        if (deleted) return this;
        int lastChance = this.chance;
        this.chance = 0;
        inputFluids(ingredient);
        this.chance = lastChance;
        return this;
    }

    @Override
    public GTORecipeBuilder circuitMeta(int configuration) {
        if (deleted) return this;
        if (configuration < 0 || configuration > IntCircuitBehaviour.CIRCUIT_MAX) {
            GTOCore.LOGGER.error("Circuit configuration must be in the bounds 0 - 32");
        }
        return notConsumable(IntCircuitIngredient.circuitInput(configuration));
    }

    @Override
    public GTORecipeBuilder chancedInput(ItemStack stack, int chance, int tierChanceBoost) {
        if (deleted) return this;
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
        if (deleted) return this;
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
        if (deleted) return this;
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
        if (deleted) return this;
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
        if (deleted) return this;
        return chancedOutput(ChemicalHelper.get(tag, mat), chance, tierChanceBoost);
    }

    @Override
    public GTORecipeBuilder chancedOutput(TagPrefix tag, Material mat, int count, int chance, int tierChanceBoost) {
        if (deleted) return this;
        return chancedOutput(ChemicalHelper.get(tag, mat, count), chance, tierChanceBoost);
    }

    @Override
    public GTORecipeBuilder chancedOutput(ItemStack stack, String fraction, int tierChanceBoost) {
        if (deleted) return this;
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
        if (deleted) return this;
        return chancedOutput(ChemicalHelper.get(prefix, material, count), fraction, tierChanceBoost);
    }

    @Override
    public GTORecipeBuilder chancedOutput(TagPrefix prefix, Material material, String fraction, int tierChanceBoost) {
        if (deleted) return this;
        return chancedOutput(prefix, material, 1, fraction, tierChanceBoost);
    }

    @Override
    public GTORecipeBuilder chancedOutput(Item item, int count, String fraction, int tierChanceBoost) {
        if (deleted) return this;
        return chancedOutput(new ItemStack(item, count), fraction, tierChanceBoost);
    }

    @Override
    public GTORecipeBuilder chancedOutput(Item item, String fraction, int tierChanceBoost) {
        if (deleted) return this;
        return chancedOutput(item, 1, fraction, tierChanceBoost);
    }

    @Override
    public GTORecipeBuilder chancedFluidOutput(FluidStack stack, String fraction, int tierChanceBoost) {
        if (deleted) return this;
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

    @Override
    public GTORecipeBuilder inputFluids(FluidStack input) {
        if (deleted) return this;
        return input(FluidRecipeCapability.CAP, FluidIngredient.of(input));
    }

    @Override
    public GTORecipeBuilder inputFluids(FluidStack... inputs) {
        if (deleted) return this;
        return input(FluidRecipeCapability.CAP, Arrays.stream(inputs).map(FluidIngredient::of).toArray(FluidIngredient[]::new));
    }

    @Override
    public GTORecipeBuilder inputFluids(FluidIngredient... inputs) {
        if (deleted) return this;
        return input(FluidRecipeCapability.CAP, inputs);
    }

    @Override
    public GTORecipeBuilder outputFluids(FluidStack output) {
        if (deleted) return this;
        return output(FluidRecipeCapability.CAP, FluidIngredient.of(output));
    }

    @Override
    public GTORecipeBuilder outputFluids(FluidStack... outputs) {
        if (deleted) return this;
        return output(FluidRecipeCapability.CAP, Arrays.stream(outputs).map(FluidIngredient::of).toArray(FluidIngredient[]::new));
    }

    @Override
    public GTORecipeBuilder outputFluids(FluidIngredient... outputs) {
        if (deleted) return this;
        return output(FluidRecipeCapability.CAP, outputs);
    }

    //////////////////////////////////////
    // ********** DATA ***********//
    //////////////////////////////////////
    @Override
    public GTORecipeBuilder addData(String key, Tag data) {
        if (deleted) return this;
        this.data.put(key, data);
        return this;
    }

    @Override
    public GTORecipeBuilder addData(String key, int data) {
        if (deleted) return this;
        this.data.putInt(key, data);
        return this;
    }

    @Override
    public GTORecipeBuilder addData(String key, long data) {
        if (deleted) return this;
        this.data.putLong(key, data);
        return this;
    }

    @Override
    public GTORecipeBuilder addData(String key, String data) {
        if (deleted) return this;
        this.data.putString(key, data);
        return this;
    }

    @Override
    public GTORecipeBuilder addData(String key, Float data) {
        if (deleted) return this;
        this.data.putFloat(key, data);
        return this;
    }

    @Override
    public GTORecipeBuilder addData(String key, boolean data) {
        if (deleted) return this;
        this.data.putBoolean(key, data);
        return this;
    }

    @Override
    public GTORecipeBuilder blastFurnaceTemp(int blastTemp) {
        if (deleted) return this;
        return addData("ebf_temp", blastTemp);
    }

    @Override
    public GTORecipeBuilder explosivesAmount(int explosivesAmount) {
        if (deleted) return this;
        return inputItems(new ItemStack(Blocks.TNT, explosivesAmount));
    }

    @Override
    public GTORecipeBuilder explosivesType(ItemStack explosivesType) {
        if (deleted) return this;
        return inputItems(explosivesType);
    }

    @Override
    public GTORecipeBuilder solderMultiplier(int multiplier) {
        if (deleted) return this;
        return addData("solder_multiplier", multiplier);
    }

    @Override
    public GTORecipeBuilder disableDistilleryRecipes(boolean flag) {
        if (deleted) return this;
        return addData("disable_distillery", flag);
    }

    @Override
    public GTORecipeBuilder fusionStartEU(long eu) {
        if (deleted) return this;
        return addData("eu_to_start", eu);
    }

    @Override
    public GTORecipeBuilder researchScan(boolean isScan) {
        if (deleted) return this;
        return addData("scan_for_research", isScan);
    }

    @Override
    public GTORecipeBuilder durationIsTotalCWU(boolean durationIsTotalCWU) {
        if (deleted) return this;
        return addData("duration_is_total_cwu", durationIsTotalCWU);
    }

    @Override
    public GTORecipeBuilder hideDuration(boolean hideDuration) {
        if (deleted) return this;
        return addData("hide_duration", hideDuration);
    }

    //////////////////////////////////////
    // ******* CONDITIONS ********//
    //////////////////////////////////////
    @Override
    public GTORecipeBuilder cleanroom(CleanroomType cleanroomType) {
        if (deleted) return this;
        return addCondition(new CleanroomCondition(cleanroomType));
    }

    @Override
    public GTORecipeBuilder dimension(ResourceLocation dimension, boolean reverse) {
        if (deleted) return this;
        return addCondition(new DimensionCondition(dimension).setReverse(reverse));
    }

    @Override
    public GTORecipeBuilder dimension(ResourceLocation dimension) {
        if (deleted) return this;
        return dimension(dimension, false);
    }

    @Override
    public GTORecipeBuilder biome(ResourceLocation biome, boolean reverse) {
        if (deleted) return this;
        return addCondition(new BiomeCondition(biome).setReverse(reverse));
    }

    @Override
    public GTORecipeBuilder biome(ResourceLocation biome) {
        if (deleted) return this;
        return biome(biome, false);
    }

    @Override
    public GTORecipeBuilder rain(float level, boolean reverse) {
        if (deleted) return this;
        return addCondition(new RainingCondition(level).setReverse(reverse));
    }

    @Override
    public GTORecipeBuilder rain(float level) {
        if (deleted) return this;
        return rain(level, false);
    }

    @Override
    public GTORecipeBuilder thunder(float level, boolean reverse) {
        if (deleted) return this;
        return addCondition(new ThunderCondition(level).setReverse(reverse));
    }

    @Override
    public GTORecipeBuilder thunder(float level) {
        if (deleted) return this;
        return thunder(level, false);
    }

    @Override
    public GTORecipeBuilder posY(int min, int max, boolean reverse) {
        if (deleted) return this;
        return addCondition(new PositionYCondition(min, max).setReverse(reverse));
    }

    @Override
    public GTORecipeBuilder posY(int min, int max) {
        if (deleted) return this;
        return posY(min, max, false);
    }

    @Override
    public GTORecipeBuilder daytime(boolean isNight) {
        if (deleted) return this;
        return addCondition(new DaytimeCondition().setReverse(isNight));
    }

    @Override
    public GTORecipeBuilder daytime() {
        if (deleted) return this;
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
        if (deleted) return this;
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
        if (deleted) return this;
        return (GTORecipeBuilder) super.researchWithoutRecipe(researchId, dataStack);
    }

    /**
     * Generates a research recipe for the Scanner.
     */
    @Override
    public GTORecipeBuilder scannerResearch(UnaryOperator<ResearchRecipeBuilder.ScannerRecipeBuilder> research) {
        if (deleted) return this;
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
        if (deleted) return this;
        return scannerResearch(b -> b.researchStack(researchStack));
    }

    /**
     * Generates a research recipe for the Research Station.
     */
    @Override
    public GTORecipeBuilder stationResearch(UnaryOperator<ResearchRecipeBuilder.StationRecipeBuilder> research) {
        if (deleted) return this;
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

    public void save() {
        if (deleted) return;
        ResourceLocation typeid = getTypeID(id, recipeType);
        if (!id.getNamespace().equals(GTCEu.MOD_ID) && RECIPE_MAP.containsKey(typeid)) throw new IllegalStateException("Recipe with id " + typeid + " already exists!");
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
        RECIPE_MAP.put(typeid, new GTRecipe(this.recipeType, typeid, this.input, this.output, this.tickInput, this.tickOutput, Map.of(), Map.of(), Map.of(), Map.of(), this.conditions, List.of(), this.data, this.duration, false, this.recipeCategory));
    }

    public static ResourceLocation getTypeID(ResourceLocation id, GTRecipeType recipeType) {
        return new ResourceLocation(id.getNamespace(), recipeType.registryName.getPath() + "/" + id.getPath());
    }

    public GTORecipeBuilder notConsumable(TagKey<Item> tag) {
        int lastChance = this.chance;
        this.chance = 0;
        inputItems(tag);
        this.chance = lastChance;
        return this;
    }

    public GTORecipeBuilder notConsumable(String id) {
        return notConsumable(RegistriesUtils.getItem(id));
    }

    public GTORecipeBuilder notConsumable(String id, int count) {
        return notConsumable(RegistriesUtils.getItemStack(id, count));
    }

    public GTORecipeBuilder inputItems(String id) {
        return inputItems(RegistriesUtils.getItem(id));
    }

    public GTORecipeBuilder inputItems(String id, int count) {
        return inputItems(RegistriesUtils.getItem(id), count);
    }

    public GTORecipeBuilder outputItems(String id) {
        return outputItems(RegistriesUtils.getItem(id));
    }

    public GTORecipeBuilder outputItems(String id, int count) {
        return outputItems(RegistriesUtils.getItem(id), count);
    }

    public GTORecipeBuilder vacuum(int tier) {
        return addCondition(new VacuumCondition(tier));
    }

    public GTORecipeBuilder gravity(boolean noGravity) {
        return addCondition(new GravityCondition(noGravity));
    }

    public GTORecipeBuilder heat(int temperature) {
        return addCondition(new HeatCondition(temperature));
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

    public GTORecipeBuilder temperature(int temperature) {
        return addData("temperature", temperature);
    }

    public GTORecipeBuilder runLimit(int count) {
        return addCondition(new RunLimitCondition(count));
    }
}
