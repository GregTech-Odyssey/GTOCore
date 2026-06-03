package com.gtocore.common.machine.multiblock.part;

import com.gtocore.common.data.GTOItems;

import com.gtolib.api.machine.part.WorkableItemPartMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IInteractedMachine;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IWorkableMultiController;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;
import com.gregtechceu.gtceu.common.data.GTDamageTypes;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import com.google.common.collect.ImmutableMap;
import com.gto.datasynclib.annotations.SaveToDisk;
import com.gto.datasynclib.annotations.SyncToClient;
import lombok.Getter;

import java.util.Map;

import javax.annotation.ParametersAreNonnullByDefault;

@Getter
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class BallHatchPartMachine extends WorkableItemPartMachine implements IInteractedMachine {

    public static final Map<Item, Integer> GRINDBALL;

    static {
        ImmutableMap.Builder<Item, Integer> grindball = ImmutableMap.builder();
        grindball.put(GTOItems.GRINDBALL_SOAPSTONE.get(), 1);
        grindball.put(GTOItems.GRINDBALL_ALUMINIUM.get(), 2);
        GRINDBALL = grindball.build();
    }

    @SaveToDisk
    @SyncToClient(notifyUpdate = true)
    private boolean isWorking;

    public BallHatchPartMachine(MetaMachineBlockEntity holder) {
        super(holder, 1, i -> GRINDBALL.containsKey(i.getItem()));
    }

    @Override
    protected void onMachineChanged() {
        for (var controller : getControllers()) {
            if (controller instanceof IRecipeLogicMachine recipeLogicMachine) {
                recipeLogicMachine.getRecipeLogic().updateTickSubscription();
            }
        }
    }

    @Override
    public void beforeWorking(IWorkableMultiController controller, RecipeHandlerUnit unit, GTRecipe recipe) {
        isWorking = true;
    }

    @Override
    public void afterWorking(IWorkableMultiController controller) {
        isWorking = false;
    }

    @Override
    public InteractionResult onUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!isRemote() && isWorking && !player.isCreative()) {
            player.hurt(GTDamageTypes.TURBINE.source(level), 40);
            return InteractionResult.FAIL;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onMachineRemoved() {
        if (!isWorking) {
            super.onMachineRemoved();
        }
    }
}
