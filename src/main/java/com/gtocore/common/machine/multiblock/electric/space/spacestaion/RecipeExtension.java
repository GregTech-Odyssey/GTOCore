package com.gtocore.common.machine.multiblock.electric.space.spacestaion;

import com.gtocore.api.machine.part.GTOPartAbility;
import com.gtocore.common.data.machines.SpaceMultiblock;

import com.gtolib.api.machine.feature.multiblock.ICrossRecipeMachine;
import com.gtolib.api.machine.trait.CrossRecipeTrait;
import com.gtolib.api.machine.trait.IEnhancedRecipeLogic;
import com.gtolib.api.recipe.IdleReason;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.utils.MachineUtils;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.machine.feature.ICleanroomProvider;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import earth.terrarium.adastra.api.planets.PlanetApi;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.Function;
import java.util.function.ToLongFunction;

public class RecipeExtension extends Extension implements ICrossRecipeMachine {

    private boolean hasLaserInput = false;
    private final Set<Recipe> lastRecipes = new ReferenceOpenHashSet<>();
    @Persisted
    private double totalEu;
    @Persisted
    protected final CrossRecipeTrait crossRecipeTrait;

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
    protected boolean beforeWorking(@Nullable Recipe recipe) {
        if (!isWorkspaceReady()) {
            setIdleReason(IdleReason.CANNOT_WORK_IN_SPACE);
            return false;
        }
        if (hasLaserInput && !core.canUseLaser()) {
            ((IEnhancedRecipeLogic) getRecipeLogic())
                    .gtolib$setIdleReason(Component.translatable("gtocore.machine.spacestation.require_module", Component.translatable(SpaceMultiblock.SPACE_STATION_ENERGY_CONVERSION_MODULE.getDescriptionId())));
            return false;
        }

        return super.beforeWorking(recipe);
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
    public Recipe getRecipe() {
        if (!PlanetApi.API.isSpace(getLevel()))
            return null;
        if (getRoot() == null || !getRoot().isWorkspaceReady())
            return null;

        return crossRecipeTrait.getRecipe();
    }

    @Override
    public Recipe getRealRecipe(@NotNull Recipe recipe) {
        return ICrossRecipeMachine.super.getRealRecipe(recipe);
    }

    @Override
    public CrossRecipeTrait getCrossRecipeTrait() {
        return crossRecipeTrait;
    }

    @Override
    public Set<Recipe> getLastRecipes() {
        return lastRecipes;
    }

    @Override
    public double getTotalEu() {
        return totalEu;
    }
}
