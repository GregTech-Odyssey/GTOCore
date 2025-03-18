package com.gto.gtocore.api.recipe;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.machine.multiblock.CrossRecipeMultiblockMachine;

import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;

import net.minecraft.nbt.CompoundTag;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import lombok.Getter;
import lombok.Setter;

import java.util.Iterator;
import java.util.Set;

public final class AsyncCrossRecipeSearchTask extends AsyncRecipeSearchTask {

    private static final CompoundTag EMPTY_TAG = new CompoundTag();

    private final Set<GTRecipe> lastRecipes = new ObjectOpenHashSet<>();

    @Setter
    @Getter
    private boolean next;

    public AsyncCrossRecipeSearchTask(RecipeLogic logic) {
        super(logic);
    }

    public void clean() {
        super.clean();
        lastRecipes.clear();
        if (next) {
            AsyncRecipeSearchTask.addAsyncLogic(logic);
            hasTask = false;
        }
    }

    @Override
    Result searchRecipe() {
        return new Result(getRecipe((CrossRecipeMultiblockMachine) logic.machine), lastRecipes);
    }

    private GTRecipe getRecipe(CrossRecipeMultiblockMachine machine) {
        if (!machine.hasProxies()) return null;
        GTRecipe match = LookupRecipe(machine);
        if (match == null) return null;
        long totalEu = 0;
        lastRecipes.clear();
        for (int i = 0; i < machine.getThread(); i++) {
            totalEu += match.duration * match.data.getLong("eut");
            match.data = EMPTY_TAG;
            if (machine.isRepeatedRecipes()) match.id = GTOCore.id("thread_" + i);
            lastRecipes.add(match);
            match = LookupRecipe(machine);
            if (match == null) break;
        }
        long maxEUt = machine.getOverclockVoltage();
        double d = (double) totalEu / maxEUt;
        int limit = machine.gTOCore$getOCLimit();
        return GTORecipeBuilder.ofRaw().EUt(d >= limit ? maxEUt : (long) (maxEUt * d / limit)).duration((int) Math.max(d, limit)).buildRawRecipe();
    }

    private GTRecipe LookupRecipe(CrossRecipeMultiblockMachine machine) {
        boolean isLocked = machine.getRecipeLogic().gTOCore$isLockRecipe();
        if (isLocked && machine.getOriginRecipes().size() >= machine.getThread()) {
            for (GTRecipe recipe : machine.getOriginRecipes()) {
                recipe = modifyRecipe(machine, recipe.copy());
                if (recipe != null) return recipe;
            }
        } else {
            Iterator<GTRecipe> iterator = machine.getRecipeType().getLookup().getRecipeIterator(machine, recipe -> !recipe.isFuel && recipe.matchRecipe(machine).isSuccess() && recipe.matchTickRecipe(machine).isSuccess());
            while (iterator.hasNext()) {
                GTRecipe recipe = checkRecipe(isLocked, machine, iterator.next());
                if (recipe != null) {
                    return recipe;
                }
            }
            for (GTRecipeType.ICustomRecipeLogic customRecipeLogic : machine.getRecipeType().getCustomRecipeLogicRunners()) {
                GTRecipe recipe = checkRecipe(isLocked, machine, customRecipeLogic.createCustomRecipe(machine));
                if (recipe != null) {
                    return recipe;
                }
            }
        }
        return null;
    }

    private GTRecipe checkRecipe(boolean isLocked, CrossRecipeMultiblockMachine machine, GTRecipe recipe) {
        if (recipe != null) {
            if (machine.isRepeatedRecipes() || !lastRecipes.contains(recipe)) {
                GTRecipe modify = modifyRecipe(machine, recipe.copy());
                if (modify != null) {
                    if (isLocked) machine.getOriginRecipes().add(recipe);
                    return modify;
                }
            }
        }
        return null;
    }

    private static GTRecipe modifyRecipe(CrossRecipeMultiblockMachine machine, GTRecipe recipe) {
        int rt = RecipeHelper.getRecipeEUtTier(recipe);
        if (rt <= machine.getMaxOverclockTier() && recipe.checkConditions(machine.getRecipeLogic()).isSuccess()) {
            recipe.conditions.clear();
            for (IMultiPart part : machine.getParts()) {
                recipe = part.modifyRecipe(recipe);
                if (recipe == null) return null;
            }
            recipe = machine.modifyRecipe(recipe);
            if (RecipeRunner.matchRecipeInput(machine, recipe) && RecipeRunner.handleRecipeInput(machine, recipe)) {
                recipe.ocLevel = machine.getTier() - rt;
                recipe.inputs.clear();
                long eut = RecipeHelper.getInputEUt(recipe);
                recipe.data.putLong("eut", eut);
                recipe.tickInputs.clear();
                return recipe;
            }
        }
        return null;
    }

    public record Result(GTRecipe recipe, Set<GTRecipe> lastRecipes) implements IResult {

        @Override
        public GTRecipe modified() {
            return null;
        }
    }
}
