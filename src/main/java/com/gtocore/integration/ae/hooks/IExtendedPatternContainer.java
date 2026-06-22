package com.gtocore.integration.ae.hooks;

import com.gtocore.common.data.GTORecipeTypes;
import com.gtocore.integration.ae.PatternContainerGroupHelper;

import com.gtolib.api.blockentity.IDirectionCacheBlockEntity;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Nameable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import appeng.api.implementations.blockentities.PatternContainerGroup;
import appeng.blockentity.crafting.MolecularAssemblerBlockEntity;
import appeng.helpers.patternprovider.PatternContainer;

import com.glodblock.github.extendedae.common.tileentities.TileExMolecularAssembler;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public interface IExtendedPatternContainer extends PatternContainer {

    @Nullable
    default GTRecipeType gto$getRecipeType() {
        return null;
    }

    @Nullable
    default Collection<GTRecipeType> gto$getRecipeTypes() {
        return null;
    }

    default boolean gto$supportsRecipeType(GTRecipeType targetRecipeType) {
        var recipeType = gto$getRecipeType();
        if (recipeType == null ||
                recipeType == GTORecipeTypes.DUMMY_RECIPES ||
                recipeType == GTORecipeTypes.HATCH_COMBINED) {
            var recipeTypes = gto$getRecipeTypes();
            if (recipeTypes == null) {
                return false;
            }
            for (GTRecipeType type : recipeTypes) {
                if (matchesRecipeType(type, targetRecipeType)) {
                    return true;
                }
            }
            return false;
        }
        return matchesRecipeType(recipeType, targetRecipeType);
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

    default Component gto$getTerminalGroupSearchName() {
        if (this instanceof Nameable nameable && nameable.hasCustomName()) {
            String customName = nameable.getCustomName().getString();
            if (!customName.startsWith("+")) {
                return nameable.getCustomName();
            }
        }
        if (this instanceof IPPPC self) {
            var level = self.gto$getLevel();
            var pos = self.gto$getBlockPos();
            var extraSuffix = IExtendedPatternContainer.gto$getExtraSuffix(this);
            for (var direction : self.gto$getPushDirection()) {
                var adjacentPos = pos.relative(direction);
                var searchName = PatternContainerGroupHelper.getSearchName(level, adjacentPos, extraSuffix);
                if (searchName != null) {
                    return searchName;
                }
                var fallbackGroup = PatternContainerGroup.fromMachine(level, adjacentPos, direction.getOpposite());
                if (fallbackGroup != null) {
                    return extraSuffix.isEmpty() ?
                            fallbackGroup.name() :
                            fallbackGroup.name().copy().append(" ").append(extraSuffix);
                }
            }
        }
        return getTerminalGroup().name();
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

    static String gto$getExtraSuffix(PatternContainer container) {
        if (container instanceof Nameable nameable && nameable.hasCustomName()) {
            String customName = nameable.getCustomName().getString();
            if (customName.startsWith("+")) {
                return customName.substring(1).strip();
            }
        }
        return "";
    }

    static boolean matchesRecipeType(@Nullable GTRecipeType recipeType, GTRecipeType targetRecipeType) {
        if (recipeType == null) {
            return false;
        }
        return recipeType == targetRecipeType || recipeType.getSmallRecipeMap() == targetRecipeType;
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
        if (!(adjBe instanceof MetaMachineBlockEntity mmbe)) {
            return null;
        }
        MetaMachine mm = mmbe.getMetaMachine();
        if (mm instanceof IMultiPart partMachine) {
            return partMachine.getController() instanceof IRecipeLogicMachine rlm ? Arrays.asList(rlm.getAvailableRecipeTypes()) : null;
        }

        if (mm instanceof IRecipeLogicMachine rlm) {
            return Arrays.asList(rlm.getAvailableRecipeTypes());
        }

        return null;
    }
}
