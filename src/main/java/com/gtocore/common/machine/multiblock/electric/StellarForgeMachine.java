package com.gtocore.common.machine.multiblock.electric;

import com.gtocore.common.data.GTOBlocks;

import com.gtolib.api.annotation.Scanned;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.api.machine.multiblock.TierCasingMultiblockMachine;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.modifier.RecipeModifierFunction;
import com.gtolib.utils.SphereExplosion;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IExplosionMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static com.gtolib.api.GTOValues.STELLAR_CONTAINMENT_TIER;

@Scanned
public final class StellarForgeMachine extends TierCasingMultiblockMachine implements IExplosionMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            StellarForgeMachine.class, TierCasingMultiblockMachine.MANAGED_FIELD_HOLDER);

    @RegisterLanguage(cn = "内部压力：", en = "Internal Pressure: ")
    private static final String PRESSURE = "gtocore.machine.stellar_forge.pressure";

    @Persisted
    private int pressure;

    private int consecutiveRecipes;

    public StellarForgeMachine(MetaMachineBlockEntity holder) {
        super(holder, STELLAR_CONTAINMENT_TIER);
    }

    @Override
    public @NotNull ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        tier = switch (getCasingTier(STELLAR_CONTAINMENT_TIER)) {
            case 3 -> GTValues.MAX;
            case 2 -> GTValues.OpV;
            default -> GTValues.UXV;
        };
    }

    @Nullable
    @Override
    protected Recipe getRealRecipe(@NotNull Recipe recipe) {
        consecutiveRecipes++;
        recipe = RecipeModifierFunction.laserLossOverclocking(this, recipe);
        if (recipe != null && consecutiveRecipes > 1) {
            recipe.duration = Math.max(recipe.duration / 2, 1);
        }
        return recipe;
    }

    @Override
    public void regressRecipe(RecipeLogic recipeLogic) {
        recipeLogic.interruptRecipe();
        doExplosion(1);
    }

    @Override
    public void doExplosion(BlockPos pos, float explosionPower) {
        var machine = self();
        var level = machine.getLevel();
        if (level != null) {
            level.removeBlock(pos, false);
            SphereExplosion.explosion(pos, level, 100, true, true);
        }
    }

    private static final class Wrapper {

        private static final Map<Item, Integer> BOMB = Map.of(GTOBlocks.NUKE_BOMB.asItem(), 1,
                GTOBlocks.NAQUADRIA_CHARGE.asItem(), 4,
                GTOBlocks.LEPTONIC_CHARGE.asItem(), 16,
                GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asItem(), 64);
    }
}
