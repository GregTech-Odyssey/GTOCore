package com.gtocore.integration.ae;

import com.gtocore.common.data.GTORecipeDataKeys;
import com.gtocore.common.data.GTORecipeTypes;
import com.gtocore.common.machine.multiblock.electric.processing.ProcessingPlantMachine;
import com.gtocore.common.machine.multiblock.part.ProgrammableHatchPartMachine;
import com.gtocore.config.GTOConfig;

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

    private static final char MACHINE_PLACEHOLDER = 'm';
    private static final char TIER_PLACEHOLDER = 't';
    private static final char SUFFIX_PLACEHOLDER = 's';
    private static final char RECIPE_TYPE_MULTI_PLACEHOLDER = 'r';
    private static final char RECIPE_TYPE_ALWAYS_PLACEHOLDER = 'R';
    private static final String SEARCH_NAME_FORMAT = "%m %t %s %R";

    private PatternContainerGroupHelper() {}

    public static @Nullable PatternContainerGroup fromMachine(Level level, BlockPos pos, String extraSuffix) {
        MachineNameContext context = getMachineNameContext(level, pos);
        if (context == null) {
            return null;
        }

        return createGroup(context.displayMachine(), extraSuffix, context.selectedRecipeType(),
                getAvailableRecipeTypes(context.recipeMachine()), context.showAllRecipeTypes(), context.tooltip());
    }

    public static PatternContainerGroup forPatternBuffer(MetaMachine displayMachine, MetaMachine actualMachine,
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
        NameParts parts = getNameParts(displayMachine, extraSuffix, selectedRecipeType, availableRecipeTypes,
                showAllRecipeTypes);
        MutableComponent name = formatName(parts, GTOConfig.INSTANCE.misc.patternContainerNameFormat);
        return new PatternContainerGroup(AEItemKey.of(displayMachine.getDefinition().asStack()), name, tooltip);
    }

    public static Component getSearchName(MetaMachine displayMachine, String extraSuffix,
                                          @Nullable GTRecipeType selectedRecipeType,
                                          Collection<GTRecipeType> availableRecipeTypes) {
        return formatName(getNameParts(displayMachine, extraSuffix, selectedRecipeType, availableRecipeTypes,
                selectedRecipeType == null || selectedRecipeType == GTORecipeTypes.HATCH_COMBINED), SEARCH_NAME_FORMAT);
    }

    public static @Nullable Component getSearchName(Level level, BlockPos pos, String extraSuffix) {
        MachineNameContext context = getMachineNameContext(level, pos);
        if (context == null) {
            return null;
        }

        return formatName(getNameParts(context.displayMachine(), extraSuffix, context.selectedRecipeType(),
                getAvailableRecipeTypes(context.recipeMachine()), context.showAllRecipeTypes()), SEARCH_NAME_FORMAT);
    }

    private static @Nullable MachineNameContext getMachineNameContext(Level level, BlockPos pos) {
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

        return new MachineNameContext(displayMachine, recipeMachine, selectedRecipeType, showAllRecipeTypes, tooltip);
    }

    private static Collection<GTRecipeType> getAvailableRecipeTypes(@Nullable IRecipeLogicMachine recipeMachine) {
        return recipeMachine == null ? List.of() : Arrays.asList(recipeMachine.getAvailableRecipeTypes());
    }

    private static NameParts getNameParts(MetaMachine displayMachine, String extraSuffix,
                                          @Nullable GTRecipeType selectedRecipeType,
                                          Collection<GTRecipeType> availableRecipeTypes,
                                          boolean showAllRecipeTypes) {
        return new NameParts(
                getMachineName(displayMachine),
                getMachineTier(displayMachine),
                extraSuffix.isBlank() ? null : Component.literal(extraSuffix.strip()),
                getRecipeTypeName(selectedRecipeType, availableRecipeTypes, showAllRecipeTypes),
                hasMultipleDisplayableRecipeTypes(availableRecipeTypes));
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
        List<GTRecipeType> displayableRecipeTypes = availableRecipeTypes.stream()
                .filter(PatternContainerGroupHelper::isDisplayableRecipeType)
                .toList();
        if (displayableRecipeTypes.isEmpty()) {
            return null;
        }
        if (displayableRecipeTypes.size() == 1) {
            return getRecipeTypeDisplayName(displayableRecipeTypes.get(0));
        }

        if (!showAllRecipeTypes) {
            return isDisplayableRecipeType(selectedRecipeType) ?
                    getRecipeTypeDisplayName(selectedRecipeType) : null;
        }

        MutableComponent result = Component.empty();
        for (GTRecipeType recipeType : displayableRecipeTypes) {
            if (!result.getString().isEmpty()) {
                result.append("/");
            }
            result.append(getRecipeTypeDisplayName(recipeType));
        }
        return result.getString().isEmpty() ? null : result;
    }

    private static boolean hasMultipleDisplayableRecipeTypes(Collection<GTRecipeType> availableRecipeTypes) {
        return availableRecipeTypes.stream()
                .filter(PatternContainerGroupHelper::isDisplayableRecipeType)
                .limit(2)
                .count() > 1;
    }

    private static Component getRecipeTypeDisplayName(GTRecipeType recipeType) {
        return Component.translatable(recipeType.registryName.toLanguageKey());
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

    private static MutableComponent formatName(NameParts parts, String format) {
        if (format == null || format.isBlank()) {
            format = "%m";
        }

        MutableComponent result = Component.empty();
        StringBuilder literal = new StringBuilder();
        boolean appended = false;
        boolean hasMachinePlaceholder = format.indexOf("%" + MACHINE_PLACEHOLDER) >= 0;
        for (int index = 0; index < format.length(); index++) {
            char current = format.charAt(index);
            if (current != '%' || index + 1 >= format.length()) {
                literal.append(current);
                continue;
            }

            char placeholder = format.charAt(index + 1);
            if (!isPlaceholder(placeholder)) {
                literal.append(current).append(placeholder);
                index++;
                continue;
            }

            Component component = parts.get(placeholder);
            if (component == null &&
                    placeholder == RECIPE_TYPE_ALWAYS_PLACEHOLDER &&
                    !hasMachinePlaceholder) {
                component = parts.machine();
            }
            if (component != null && !component.getString().isBlank()) {
                appendLiteral(result, literal.toString(), appended);
                literal.setLength(0);
                result.append(component);
                appended = true;
            }
            index++;
        }

        if (appended) {
            appendTrailingLiteral(result, literal.toString());
            return result;
        }
        if (!literal.toString().isBlank()) {
            return Component.literal(literal.toString().strip());
        }
        return parts.machine().copy();
    }

    private static boolean isPlaceholder(char placeholder) {
        return placeholder == MACHINE_PLACEHOLDER ||
                placeholder == TIER_PLACEHOLDER ||
                placeholder == SUFFIX_PLACEHOLDER ||
                placeholder == RECIPE_TYPE_MULTI_PLACEHOLDER ||
                placeholder == RECIPE_TYPE_ALWAYS_PLACEHOLDER;
    }

    private static void appendLiteral(MutableComponent result, String literal, boolean hasPreviousField) {
        if (literal.isEmpty()) {
            return;
        }
        if (literal.isBlank()) {
            if (hasPreviousField && !result.getString().endsWith(" ")) {
                result.append(" ");
            }
            return;
        }
        result.append(hasPreviousField ? literal : literal.stripLeading());
    }

    private static void appendTrailingLiteral(MutableComponent result, String literal) {
        if (!literal.isBlank()) {
            result.append(literal.stripTrailing());
        }
    }

    private record MachineNameContext(MetaMachine displayMachine,
                                      @Nullable IRecipeLogicMachine recipeMachine,
                                      @Nullable GTRecipeType selectedRecipeType,
                                      boolean showAllRecipeTypes,
                                      List<Component> tooltip) {}

    private record NameParts(Component machine,
                             @Nullable Component tier,
                             @Nullable Component suffix,
                             @Nullable Component recipeType,
                             boolean multipleRecipeTypes) {

        private @Nullable Component get(char placeholder) {
            return switch (placeholder) {
                case MACHINE_PLACEHOLDER -> machine;
                case TIER_PLACEHOLDER -> tier;
                case SUFFIX_PLACEHOLDER -> suffix;
                case RECIPE_TYPE_MULTI_PLACEHOLDER -> multipleRecipeTypes ? recipeType : null;
                case RECIPE_TYPE_ALWAYS_PLACEHOLDER -> recipeType;
                default -> null;
            };
        }
    }
}
