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
        return new Result(getRecipe(), lastRecipes);
    }

    private CrossRecipeMultiblockMachine getMachine() {
        return (CrossRecipeMultiblockMachine) logic.machine;
    }

    private GTRecipe getRecipe() {
        if (!getMachine().hasProxies()) return null;
        GTRecipe match = LookupRecipe();
        if (match == null) return null;
        long totalEu = 0;
        lastRecipes.clear();
        for (int i = 0; i < getMachine().getThread(); i++) {
            totalEu += match.duration * RecipeHelper.getInputEUt(match);
            match.tickInputs.clear();
            match.data = EMPTY_TAG;
            if (getMachine().isRepeatedRecipes()) match.id = GTOCore.id("thread_" + i);
            lastRecipes.add(match);
            match = LookupRecipe();
            if (match == null) break;
        }
        long maxEUt = getMachine().getOverclockVoltage();
        double d = (double) totalEu / maxEUt;
        int limit = getMachine().gTOCore$getOCLimit();
        return GTORecipeBuilder.ofRaw().EUt(d >= limit ? maxEUt : (long) (maxEUt * d / limit)).duration((int) Math.max(d, limit)).buildRawRecipe();
    }

    private GTRecipe LookupRecipe() {
        if (getMachine().getRecipeLogic().gTOCore$isLockRecipe() && getMachine().getOriginRecipes().size() >= getMachine().getThread()) {
            for (GTRecipe recipe : getMachine().getOriginRecipes()) {
                recipe = modifyRecipe(recipe.copy());
                if (recipe != null) return recipe;
            }
        } else {
            Iterator<GTRecipe> iterator = getMachine().getRecipeType().getLookup().getRecipeIterator(getMachine(), recipe -> !recipe.isFuel && RecipeRunner.matchRecipe(getMachine(), recipe) && RecipeRunner.matchTickRecipe(getMachine(), recipe));
            while (iterator.hasNext()) {
                GTRecipe recipe = checkRecipe(iterator.next());
                if (recipe != null) {
                    return recipe;
                }
            }
            for (GTRecipeType.ICustomRecipeLogic customRecipeLogic : getMachine().getRecipeType().getCustomRecipeLogicRunners()) {
                GTRecipe recipe = checkRecipe(customRecipeLogic.createCustomRecipe(getMachine()));
                if (recipe != null) {
                    return recipe;
                }
            }
        }
        return null;
    }

    private GTRecipe checkRecipe(GTRecipe recipe) {
        if (recipe != null) {
            if (getMachine().isRepeatedRecipes() || !lastRecipes.contains(recipe)) {
                GTRecipe modify = modifyRecipe(recipe.copy());
                if (modify != null) {
                    if (getMachine().getRecipeLogic().gTOCore$isLockRecipe()) getMachine().getOriginRecipes().add(recipe);
                    return modify;
                }
            }
        }
        return null;
    }

    private GTRecipe modifyRecipe(GTRecipe recipe) {
        int rt = RecipeHelper.getRecipeEUtTier(recipe);
        if (rt <= getMachine().getMaxOverclockTier() && RecipeRunner.checkConditions(getMachine(), recipe)) {
            recipe.conditions.clear();
            for (IMultiPart part : getMachine().getParts()) {
                recipe = part.modifyRecipe(recipe);
                if (recipe == null) return null;
            }
            recipe = getMachine().modifyRecipe(recipe);
            if (RecipeRunner.matchRecipeInput(getMachine(), recipe) && RecipeRunner.handleRecipeInput(getMachine(), recipe)) {
                recipe.ocLevel = getMachine().getTier() - rt;
                recipe.inputs.clear();
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
