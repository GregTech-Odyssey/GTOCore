package com.gtocore.common.machine.multiblock.electric.space.spacestaion;

import com.gtocore.api.machine.part.GTOPartAbility;
import com.gtocore.common.data.machines.SpaceMultiblock;

import com.gtolib.api.machine.feature.multiblock.ICrossRecipeElectricMachine;
import com.gtolib.api.machine.trait.CrossRecipeTrait;
import com.gtolib.api.recipe.IdleReason;
import com.gtolib.utils.MachineUtils;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.machine.feature.ICleanroomProvider;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;
import com.gregtechceu.gtceu.api.recipe.modifier.RecipeModifier;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import com.gto.datasynclib.annotations.SaveToDisk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.Function;
import java.util.function.ToLongFunction;

public class RecipeExtension extends Extension implements ICrossRecipeElectricMachine {

    private boolean hasLaserInput = false;
    @SaveToDisk
    private final CrossRecipeTrait crossRecipeTrait;

    @NotNull
    private ToLongFunction<RecipeExtension> parallel = MachineUtils::getHatchParallel;

    public RecipeExtension(MetaMachineBlockEntity metaMachineBlockEntity) {
        super(metaMachineBlockEntity);
        crossRecipeTrait = new CrossRecipeTrait(this, false, true, machine -> parallel.applyAsLong((RecipeExtension) machine));
    }

    public RecipeExtension(MetaMachineBlockEntity metaMachineBlockEntity, @Nullable Function<AbstractSpaceStation, Set<BlockPos>> positionFunction) {
        super(metaMachineBlockEntity, positionFunction);
        crossRecipeTrait = new CrossRecipeTrait(this, false, true, machine -> parallel.applyAsLong((RecipeExtension) machine));
    }

    @Override
    public void onPartScan(@NotNull IMultiPart iMultiPart) {
        super.onPartScan(iMultiPart);
        if (hasLaserInput) return;
        for (var partAbility : new PartAbility[] {
                PartAbility.INPUT_LASER, GTOPartAbility.OVERCLOCK_HATCH, GTOPartAbility.THREAD_HATCH }) {
            if (partAbility.isApplicable(iMultiPart.self().getBlockState().getBlock()))
                hasLaserInput = true;
        }
    }

    @Override
    public void onStructureFormed() {
        hasLaserInput = false;
        super.onStructureFormed();
    }

    @Override
    public @Nullable ICleanroomProvider getCleanroom() {
        return this;
    }

    public void setParallel(@NotNull ToLongFunction<RecipeExtension> parallel) {
        this.parallel = parallel;
    }

    @Override
    public void attachConfigurators(@NotNull ConfiguratorPanel configuratorPanel) {
        super.attachConfigurators(configuratorPanel);
        crossRecipeTrait.attachConfigurators(configuratorPanel);
    }

    @Override
    public @Nullable GTRecipeDefinition createCustomRecipe(RecipeHandlerUnit unit) {
        return null;
    }

    @Override
    public boolean searchRecipe() {
        return true;
    }

    @Override
    public GTRecipe getRealRecipe(@NotNull RecipeHandlerUnit unit, @NotNull GTRecipe recipe) {
        if (!isWorkspaceReady()) {
            setIdleReason(IdleReason.CANNOT_WORK_IN_SPACE);
            return null;
        }
        if (hasLaserInput && !core.canUseLaser()) {
            setIdleReason(Component.translatable("gtocore.machine.spacestation.require_module", Component.translatable(SpaceMultiblock.SPACE_STATION_ENERGY_CONVERSION_MODULE.getDescriptionId())));
            return null;
        }

        return ICrossRecipeElectricMachine.super.getRealRecipe(unit, RecipeModifier.multiplier(recipe, 1, core.getDurationMultiplierFromSpaceElevator()));
    }

    @Override
    public CrossRecipeTrait getCrossRecipeTrait() {
        return crossRecipeTrait;
    }
}
