package com.gtocore.common.machine.multiblock.electric.space.spacestaion;

import com.gtocore.api.machine.part.GTOPartAbility;
import com.gtocore.common.data.machines.SpaceMultiblock;

import com.gtolib.api.machine.trait.IEnhancedRecipeLogic;
import com.gtolib.api.recipe.IdleReason;
import com.gtolib.api.recipe.Recipe;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.ICleanroomProvider;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.Function;

public class RecipeExtension extends Extension {

    private boolean hasLaserInput = false;

    public RecipeExtension(MetaMachineBlockEntity metaMachineBlockEntity) {
        super(metaMachineBlockEntity);
    }

    public RecipeExtension(MetaMachineBlockEntity metaMachineBlockEntity, @Nullable Function<AbstractSpaceStation, Set<BlockPos>> positionFunction) {
        super(metaMachineBlockEntity, positionFunction);
    }

    @Override
    public @NotNull RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new RecipeLogic(this);
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
}
