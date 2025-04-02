package com.gto.gtocore.common.cover;

import com.gto.gtocore.common.data.GTORecipeModifiers;

import com.gregtechceu.gtceu.api.capability.ICoverable;
import com.gregtechceu.gtceu.api.cover.CoverBehavior;
import com.gregtechceu.gtceu.api.cover.CoverDefinition;
import com.gregtechceu.gtceu.api.machine.*;
import com.gregtechceu.gtceu.api.machine.feature.IOverclockMachine;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.recipe.modifier.RecipeModifier;
import com.gregtechceu.gtceu.api.recipe.modifier.RecipeModifierList;

import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public final class PowerAmplifierCover extends CoverBehavior {

    private final double durationReduction;

    public PowerAmplifierCover(CoverDefinition definition, ICoverable coverHolder, Direction attachedSide, int tier) {
        super(definition, coverHolder, attachedSide);
        this.durationReduction = getReduction(tier);
    }

    public static double getReduction(int tier) {
        return 1D / (1 + tier * 0.25);
    }

    private MetaMachine machine;

    private RecipeModifier recipeModifier;

    @Override
    public boolean canAttach() {
        if (super.canAttach()) {
            var machine = getMachine();
            if (machine instanceof IRecipeLogicMachine && machine instanceof IOverclockMachine) {
                if (machine instanceof SimpleGeneratorMachine) return false;
                if (!(machine.getDefinition() instanceof MultiblockMachineDefinition)) {
                    for (CoverBehavior cover : machine.getCoverContainer().getCovers()) {
                        if (cover instanceof PowerAmplifierCover) {
                            return false;
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onAttached(@NotNull ItemStack itemStack, @NotNull ServerPlayer player) {
        super.onAttached(itemStack, player);
        updateCoverSub();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        updateCoverSub();
    }

    @Override
    public void onRemoved() {
        super.onRemoved();
        MetaMachine machine = getMachine();
        if (machine instanceof IRecipeLogicMachine recipeLogicMachine && recipeModifier != null) {
            machine.getDefinition().setRecipeModifier(recipeModifier);
            recipeLogicMachine.getRecipeLogic().markLastRecipeDirty();
        }
        this.machine = null;
        recipeModifier = null;
    }

    private void updateCoverSub() {
        MetaMachine machine = getMachine();
        if (recipeModifier == null && machine instanceof IRecipeLogicMachine recipeLogicMachine) {
            recipeLogicMachine.getRecipeLogic().markLastRecipeDirty();
            recipeModifier = machine.getDefinition().getRecipeModifier();
            machine.getDefinition().setRecipeModifier(new RecipeModifierList(recipeModifier, GTORecipeModifiers.recipeReduction(1, durationReduction)));
        }
    }

    @Nullable
    private MetaMachine getMachine() {
        if (machine == null) machine = MetaMachine.getMachine(coverHolder.getLevel(), coverHolder.getPos());
        return machine;
    }
}
