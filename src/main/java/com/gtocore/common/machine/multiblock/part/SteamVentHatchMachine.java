package com.gtocore.common.machine.multiblock.part;

import com.gtolib.api.machine.trait.IEnhancedRecipeLogic;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IExhaustVentMachine;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IWorkableMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SteamVentHatchMachine extends MultiblockPartMachine implements IExhaustVentMachine {

    @Persisted
    private boolean needsVenting;

    public SteamVentHatchMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public boolean isVentingBlocked() {
        Level level = getLevel();
        Direction ventingSide = this.getVentingDirection();
        BlockPos ventingBlockPos = getPos().relative(ventingSide);
        if (level != null) {
            BlockState state = level.getBlockState(ventingBlockPos);
            if (state.getBlock() instanceof CarpetBlock) return false; // 地毯不挡排气
        }
        return IExhaustVentMachine.super.isVentingBlocked();
    }

    @Override
    public boolean afterWorking(IWorkableMultiController controller) {
        this.needsVenting = true;
        checkVenting();
        return super.afterWorking(controller);
    }

    @Override
    @NotNull
    public Direction getVentingDirection() {
        return this.getFrontFacing();
    }

    @Override
    public boolean isNeedsVenting() {
        return this.needsVenting;
    }

    @Override
    public void markVentingComplete() {
        this.needsVenting = false;
    }

    @Override
    @Nullable
    public GTRecipe modifyRecipe(GTRecipe recipe) {
        if (needsVenting && isVentingBlocked()) {
            for (var controller : getControllers()) {
                ((IEnhancedRecipeLogic) ((IRecipeLogicMachine) controller).getRecipeLogic()).gtolib$setIdleReason(Component.translatable("gtceu.recipe_logic.condition_fails").append(": ").append(Component.translatable("recipe.condition.steam_vent.tooltip")));
            }
            return null;
        }
        return super.modifyRecipe(recipe);
    }

    @Override
    public float getVentingDamage() {
        return 24.0F;
    }

    @Override
    public boolean canShared() {
        return false;
    }

    @Override
    public boolean shouldOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
        return false;
    }

    @Override
    public void setNeedsVenting(final boolean needsVenting) {
        this.needsVenting = needsVenting;
    }
}
