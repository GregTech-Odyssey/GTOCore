package com.gtocore.integration.ae;

import com.gtocore.common.data.GTORecipeDataKeys;
import com.gtocore.common.data.GTORecipeTypes;
import com.gtocore.common.machine.multiblock.electric.processing.ProcessingPlantMachine;
import com.gtocore.common.machine.multiblock.part.ProgrammableHatchPartMachine;

import com.gtolib.api.machine.feature.multiblock.ITierCasingMachine;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.item.MetaMachineItem;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IOverclockMachine;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.feature.ITieredMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import appeng.api.implementations.blockentities.PatternContainerGroup;
import appeng.api.stacks.AEItemKey;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public final class PatternContainerGroupHelper {

    private PatternContainerGroupHelper() {}

    public static @Nullable PatternContainerGroup fromMachine(Level level, BlockPos pos, String extraSuffix) {
        if (!(level.getBlockEntity(pos) instanceof MetaMachineBlockEntity blockEntity)) {
            return null;
        }

        MetaMachine machine = blockEntity.getMetaMachine();
        if (machine == null) {
            return null;
        }

        MetaMachine displayMachine = machine;
        IRecipeLogicMachine recipeMachine = machine instanceof IRecipeLogicMachine logicMachine ? logicMachine : null;
        GTRecipeType selectedRecipeType = getCurrentRecipeType(recipeMachine);
        boolean showAllRecipeTypes = false;
        List<Component> tooltip = List.of();

        if (machine instanceof IMultiPart partMachine) {
            IMultiController controller = partMachine.getController();
            if (controller != null) {
                displayMachine = controller.self();
                recipeMachine = controller instanceof IRecipeLogicMachine logicMachine ? logicMachine : null;
                tooltip = List.of(Component.translatable(machine.getDefinition().getDescriptionId()));

                if (machine instanceof ProgrammableHatchPartMachine programmableHatch) {
                    selectedRecipeType = programmableHatch.getRecipeType();
                    showAllRecipeTypes = selectedRecipeType == null ||
                            selectedRecipeType == GTORecipeTypes.HATCH_COMBINED;
                } else {
                    selectedRecipeType = getCurrentRecipeType(recipeMachine);
                }
            }
        }

        Collection<GTRecipeType> availableRecipeTypes = recipeMachine == null ?
                List.of() : Arrays.asList(recipeMachine.getAvailableRecipeTypes());
        return createGroup(displayMachine, extraSuffix, selectedRecipeType, availableRecipeTypes,
                showAllRecipeTypes, tooltip);
    }

    public static PatternContainerGroup forPatternAssembly(MetaMachine displayMachine, MetaMachine actualMachine,
                                                           String customName,
                                                           @Nullable GTRecipeType selectedRecipeType,
                                                           Collection<GTRecipeType> availableRecipeTypes) {
        var icon = AEItemKey.of(displayMachine.getDefinition().asStack());
        List<Component> tooltip = List.of(
                Component.translatable(actualMachine.getDefinition().getDescriptionId()));
        if (!customName.isEmpty() && !customName.startsWith("+")) {
            return new PatternContainerGroup(icon, Component.literal(customName), tooltip);
        }

        String extraSuffix = customName.startsWith("+") ? customName.substring(1).strip() : "";
        boolean showAllRecipeTypes = selectedRecipeType == null ||
                selectedRecipeType == GTORecipeTypes.HATCH_COMBINED;
        return createGroup(displayMachine, extraSuffix, selectedRecipeType, availableRecipeTypes,
                showAllRecipeTypes, tooltip);
    }

    private static PatternContainerGroup createGroup(MetaMachine displayMachine, String extraSuffix,
                                                     @Nullable GTRecipeType selectedRecipeType,
                                                     Collection<GTRecipeType> availableRecipeTypes,
                                                     boolean showAllRecipeTypes,
                                                     List<Component> tooltip) {
        MutableComponent name = getMachineName(displayMachine);
        appendField(name, getMachineTier(displayMachine));
        appendField(name, extraSuffix.isBlank() ? null : Component.literal(extraSuffix.strip()));
        appendField(name, getRecipeTypeName(selectedRecipeType, availableRecipeTypes, showAllRecipeTypes));
        return new PatternContainerGroup(AEItemKey.of(displayMachine.getDefinition().asStack()), name, tooltip);
    }

    private static MutableComponent getMachineName(MetaMachine machine) {
        var title = Component.translatable(machine.getDefinition().getDescriptionId());
        if (machine instanceof ProcessingPlantMachine processingPlantMachine) {
            ItemStack stack = processingPlantMachine.getMachineStorage().getStackInSlot(0);
            if (stack.getItem() instanceof MetaMachineItem metaMachineItem) {
                return title.copy()
                        .append(" - ")
                        .append(Component.translatable(metaMachineItem.getDefinition().getDescriptionId()));
            }
        }
        return title;
    }

    private static @Nullable Component getMachineTier(MetaMachine machine) {
        Integer tier = getMachineRecipeTier(machine);
        if (tier == null) {
            return null;
        }
        if (tier >= 0 && tier < GTValues.TIER_COUNT) {
            return Component.literal(GTValues.VNF[tier])
                    .withStyle(style -> style.withColor(GTValues.VC[tier]));
        }
        return Component.literal("MAX");
    }

    private static @Nullable Integer getMachineRecipeTier(MetaMachine machine) {
        if (machine instanceof ITieredMachine tieredMachine && tieredMachine.getTier() >= GTValues.ULV) {
            return tieredMachine.getTier();
        }
        if (machine instanceof IOverclockMachine overclockMachine &&
                overclockMachine.getMaxOverclockTier() >= GTValues.ULV) {
            return overclockMachine.getMaxOverclockTier();
        }
        if (machine instanceof IOverclockMachine overclockMachine) {
            long voltage = overclockMachine.getOverclockVoltage();
            if (voltage > 0) {
                return (int) GTUtil.getFloorTierByVoltage(voltage);
            }
        }
        if (machine instanceof ProcessingPlantMachine || !(machine instanceof ITierCasingMachine tierCasingMachine)) {
            return null;
        }
        if (!tierCasingMachine.getCasingTiers().containsKey(GTORecipeDataKeys.INTEGRAL_FRAMEWORK_TIER)) {
            return null;
        }
        return tierCasingMachine.getCasingTier(GTORecipeDataKeys.INTEGRAL_FRAMEWORK_TIER);
    }

    private static @Nullable Component getRecipeTypeName(@Nullable GTRecipeType selectedRecipeType,
                                                         Collection<GTRecipeType> availableRecipeTypes,
                                                         boolean showAllRecipeTypes) {
        long displayableRecipeTypeCount = availableRecipeTypes.stream()
                .filter(PatternContainerGroupHelper::isDisplayableRecipeType)
                .count();
        if (displayableRecipeTypeCount <= 1) {
            return null;
        }

        if (!showAllRecipeTypes) {
            return isDisplayableRecipeType(selectedRecipeType) ?
                    Component.translatable(selectedRecipeType.registryName.toLanguageKey()) : null;
        }

        MutableComponent result = Component.empty();
        for (GTRecipeType recipeType : availableRecipeTypes) {
            if (!isDisplayableRecipeType(recipeType)) {
                continue;
            }
            if (!result.getString().isEmpty()) {
                result.append("/");
            }
            result.append(Component.translatable(recipeType.registryName.toLanguageKey()));
        }
        return result.getString().isEmpty() ? null : result;
    }

    private static boolean isDisplayableRecipeType(@Nullable GTRecipeType recipeType) {
        return recipeType != null &&
                recipeType != GTORecipeTypes.DUMMY_RECIPES &&
                recipeType != GTORecipeTypes.HATCH_COMBINED;
    }

    private static @Nullable GTRecipeType getCurrentRecipeType(@Nullable IRecipeLogicMachine recipeMachine) {
        if (recipeMachine == null || recipeMachine.getAvailableRecipeTypes().length == 0) {
            return null;
        }
        return recipeMachine.getRecipeType();
    }

    private static void appendField(MutableComponent target, @Nullable Component field) {
        if (field != null && !field.getString().isBlank()) {
            target.append(" ").append(field);
        }
    }
}
