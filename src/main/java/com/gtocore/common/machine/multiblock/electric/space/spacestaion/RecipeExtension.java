package com.gtocore.common.machine.multiblock.electric.space.spacestaion;

import com.gtolib.api.recipe.IdleReason;
import com.gtolib.api.recipe.Recipe;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.ICleanroomProvider;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;

import net.minecraft.core.BlockPos;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.Function;

public class RecipeExtension extends Extension {

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
        return super.beforeWorking(recipe);
    }

    @Override
    public @Nullable ICleanroomProvider getCleanroom() {
        return this;
    }
}
