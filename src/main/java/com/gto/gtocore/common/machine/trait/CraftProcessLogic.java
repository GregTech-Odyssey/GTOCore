package com.gto.gtocore.common.machine.trait;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.recipe.GTORecipeBuilder;
import com.gto.gtocore.common.data.GTORecipeTypes;
import com.gto.gtocore.common.machine.multiblock.electric.CraftProcessMachine;
import com.gto.gtocore.common.machine.multiblock.part.ae.MECraftPatternPartMachine;

import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import net.minecraft.world.item.Item;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;

import appeng.api.crafting.IPatternDetails;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.GenericStack;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class CraftProcessLogic extends RecipeLogic {

    private final Map<Integer, List<GTRecipe>> recipes = new HashMap<>();

    private final Set<GTRecipe> currentRecipes = new ObjectOpenHashSet<>();

    public CraftProcessLogic(CraftProcessMachine machine) {
        super(machine);
    }

    @Override
    public CraftProcessMachine getMachine() {
        return (CraftProcessMachine) machine;
    }

    @Override
    public @NotNull Iterator<GTRecipe> searchRecipe() {
        List<GTRecipe> doRun = new ArrayList<>();
        currentRecipes.forEach(recipes -> {
            if (this.matchRecipe(recipes).isSuccess()) {
                doRun.add(recipes);
            }
        });
        return doRun.iterator();
    }

    public void generateRecipes(int index) {
        var craftPatternPartMachines = this.getMachine().getCraftPatternPartMachines();
        if (craftPatternPartMachines.isEmpty()) return;
        if (index > 0) {
            var machine = craftPatternPartMachines.get(index);
            updateCache(machine, index);
        } else {
            int partIndex = 0;
            for (MECraftPatternPartMachine partMachine : craftPatternPartMachines) {
                updateCache(partMachine, partIndex++);
            }
        }
        currentRecipes.clear();
        currentRecipes.addAll(recipes.values().stream().flatMap(Collection::stream).collect(Collectors.toSet()));
    }

    private void updateCache(MECraftPatternPartMachine partMachine, int index) {
        List<IPatternDetails> patterns = partMachine.getAvailablePatterns();
        if (!patterns.isEmpty()) {
            List<GTRecipe> recipeList = recipes.get(index);
            if (recipeList != null) {
                recipeList.clear();
            } else {
                recipeList = new ArrayList<>();
            }
            for (int i = 0; i < patterns.size(); i++) {
                var pattern = patterns.get(i);
                recipeList.add(buildRecipe(i, pattern));
            }
            recipes.put(index, recipeList);
        }
    }

    private GTRecipe buildRecipe(int index, IPatternDetails pattern) {
        var builder = GTORecipeBuilder.of(GTOCore.id("craft_process_" + index), GTORecipeTypes.CRAFT_PROCESS_RECIPE);
        for (var input : pattern.getInputs()) {
            var inputs = input.getPossibleInputs();
            var generic = inputs[0];
            int amount = (int) (generic.amount() * input.getMultiplier());
            if (inputs.length == 1) {
                if (generic.what() instanceof AEItemKey aeItemKey) {
                    builder.inputItems(aeItemKey.getItem(), amount);
                } else if (generic.what() instanceof AEFluidKey fluidKey) {
                    builder.inputFluids(new FluidStack(fluidKey.getFluid(), amount));
                }
            } else {
                if (generic.what() instanceof AEItemKey itemKey) {
                    ITagManager<Item> tags = ForgeRegistries.ITEMS.tags();
                    if (tags == null) {
                        continue;
                    }
                    var tag = tags.getReverseTag(itemKey.getItem()).get().getTagKeys().findFirst().orElse(null);
                    if (tag == null) continue;
                    builder.inputItems(tag, amount);
                } else if (generic.what() instanceof AEFluidKey fluidKey) {
                    builder.inputFluids(new FluidStack(fluidKey.getFluid(), amount));
                }
            }
        }

        for (GenericStack output : pattern.getOutputs()) {
            var aeKey = output.what();
            if (aeKey instanceof AEItemKey itemKey) {
                builder.outputItems(itemKey.getItem(), (int) output.amount());
            } else if (aeKey instanceof AEFluidKey fluidKey) {
                builder.outputFluids(new FluidStack(fluidKey.getFluid(), (int) output.amount()));
            }
        }
        return builder.EUt(8).duration(20).buildRawRecipe();
    }
}
