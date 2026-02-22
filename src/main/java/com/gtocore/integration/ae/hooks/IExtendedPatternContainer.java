package com.gtocore.integration.ae.hooks;

import com.gtocore.common.data.GTORecipeTypes;

import com.gtolib.api.blockentity.IDirectionCacheBlockEntity;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import appeng.blockentity.crafting.MolecularAssemblerBlockEntity;
import appeng.helpers.patternprovider.PatternContainer;
import com.glodblock.github.extendedae.common.tileentities.TileExMolecularAssembler;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface IExtendedPatternContainer extends PatternContainer {

    @Nullable
    default GTRecipeType gto$getRecipeType() {
        return null;
    }

    @Nullable
    default Collection<GTRecipeType> gto$getRecipeTypes() {
        return null;
    }

    default Set<GTRecipeType> getSupportedRecipeTypes() {
        var recipeType = gto$getRecipeType();
        if (recipeType == null ||
                recipeType == GTORecipeTypes.DUMMY_RECIPES ||
                recipeType == GTORecipeTypes.HATCH_COMBINED) {
            var recipeTypes = gto$getRecipeTypes();
            return recipeTypes != null ? recipeTypes.stream()
                    .flatMap(rt -> rt.getSmallRecipeMap() != null ? Stream.of(rt, rt.getSmallRecipeMap()) : Stream.of(rt))
                    .collect(Collectors.toSet()) : Collections.emptySet();
        } else if (recipeType.getSmallRecipeMap() != null) {
            return Set.of(recipeType, recipeType.getSmallRecipeMap());
        }
        return Collections.singleton(recipeType);
    }

    default boolean gto$isCraftingContainer() {
        return false;
    }

    default boolean hasEmptyPatternSlot() {
        var inv = getTerminalPatternInventory();
        for (int slot = 0; slot < inv.size(); slot++) {
            if (inv.getStackInSlot(slot).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    default boolean isOutOfService() {
        return getGrid() == null;
    }

    interface IPPPC extends IExtendedPatternContainer {

        Level gto$getLevel();

        BlockPos gto$getBlockPos();

        BlockEntity gto$getBlockEntity();

        EnumSet<Direction> gto$getPushDirection();
    }

    static BlockEntity getPushBlockEntity(IPPPC be) {
        var cache = IDirectionCacheBlockEntity.getBlockEntityDirectionCache(be.gto$getBlockEntity());
        var pos = be.gto$getBlockPos();

        for (var direction : be.gto$getPushDirection()) {
            var adjBe = cache.getAdjacentBlockEntity(be.gto$getLevel(), pos, direction);
            if (adjBe != null) {
                return adjBe;
            }
        }
        return null;
    }

    static boolean gto$isCraftingContainer(IPPPC self) {
        var adjBe = IExtendedPatternContainer.getPushBlockEntity(self);
        return adjBe instanceof MolecularAssemblerBlockEntity || adjBe instanceof TileExMolecularAssembler;
    }

    static GTRecipeType gto$getRecipeType(IPPPC self) {
        var adjBe = IExtendedPatternContainer.getPushBlockEntity(self);
        if (!(adjBe instanceof MetaMachineBlockEntity mmbe)) {
            return null;
        }
        MetaMachine mm = mmbe.getMetaMachine();
        if (mm instanceof IMultiPart partMachine) {
            return partMachine.getController() instanceof IRecipeLogicMachine rlm ? rlm.getRecipeType() : null;
        }

        if (mm instanceof IRecipeLogicMachine rlm) {
            return rlm.getRecipeType();
        }

        return null;
    }

    @Nullable
    static List<GTRecipeType> gto$getRecipeTypes(IPPPC self) {
        var adjBe = IExtendedPatternContainer.getPushBlockEntity(self);
        if (adjBe instanceof IMultiPart partMachine) {
            return partMachine.getController() instanceof IRecipeLogicMachine rlm ? Arrays.asList(rlm.getRecipeTypes()) : null;
        }

        if (adjBe instanceof IRecipeLogicMachine rlm) {
            return Arrays.asList(rlm.getRecipeTypes());
        }

        return null;
    }
}
