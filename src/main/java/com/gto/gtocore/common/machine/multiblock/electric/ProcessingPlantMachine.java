package com.gto.gtocore.common.machine.multiblock.electric;

import com.gto.gtocore.api.machine.multiblock.IParallelMachine;
import com.gto.gtocore.api.machine.multiblock.StorageMachine;
import com.gto.gtocore.common.data.GTORecipeModifiers;
import com.gto.gtocore.common.data.GTORecipeTypes;

import com.gregtechceu.gtceu.api.item.MetaMachineItem;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.logic.OCParams;
import com.gregtechceu.gtceu.api.recipe.logic.OCResult;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ProcessingPlantMachine extends StorageMachine implements IParallelMachine {

    private static final Set<GTRecipeType> RECIPE_TYPES = Set.of(
            GTRecipeTypes.BENDER_RECIPES,
            GTRecipeTypes.COMPRESSOR_RECIPES,
            GTRecipeTypes.FORGE_HAMMER_RECIPES,
            GTRecipeTypes.CUTTER_RECIPES,
            GTRecipeTypes.LASER_ENGRAVER_RECIPES,
            GTRecipeTypes.EXTRUDER_RECIPES,
            GTRecipeTypes.LATHE_RECIPES,
            GTRecipeTypes.WIREMILL_RECIPES,
            GTRecipeTypes.FORMING_PRESS_RECIPES,
            GTRecipeTypes.DISTILLERY_RECIPES,
            GTRecipeTypes.POLARIZER_RECIPES,
            GTORecipeTypes.CLUSTER_RECIPES,
            GTORecipeTypes.ROLLING_RECIPES,
            GTRecipeTypes.ASSEMBLER_RECIPES,
            GTRecipeTypes.CIRCUIT_ASSEMBLER_RECIPES,
            GTRecipeTypes.CENTRIFUGE_RECIPES,
            GTRecipeTypes.THERMAL_CENTRIFUGE_RECIPES,
            GTRecipeTypes.ELECTROLYZER_RECIPES,
            GTRecipeTypes.SIFTER_RECIPES,
            GTRecipeTypes.MACERATOR_RECIPES,
            GTRecipeTypes.EXTRACTOR_RECIPES,
            GTORecipeTypes.DEHYDRATOR_RECIPES,
            GTRecipeTypes.MIXER_RECIPES,
            GTRecipeTypes.CHEMICAL_BATH_RECIPES,
            GTRecipeTypes.ORE_WASHER_RECIPES,
            GTRecipeTypes.CHEMICAL_RECIPES,
            GTRecipeTypes.FLUID_SOLIDFICATION_RECIPES,
            GTRecipeTypes.ARC_FURNACE_RECIPES,
            GTORecipeTypes.LIGHTNING_PROCESSOR_RECIPES,
            GTORecipeTypes.LOOM_RECIPES,
            GTORecipeTypes.LAMINATOR_RECIPES,
            GTORecipeTypes.LASER_WELDER_RECIPES);

    @Nullable
    private GTRecipeType[] recipeTypeCache = new GTRecipeType[] { GTRecipeTypes.DUMMY_RECIPES };

    private boolean mismatched = false;

    public ProcessingPlantMachine(IMachineBlockEntity holder) {
        super(holder, 1, ProcessingPlantMachine::filter);
    }

    private static boolean filter(ItemStack itemStack) {
        if (itemStack.getItem() instanceof MetaMachineItem metaMachineItem) {
            MachineDefinition definition = metaMachineItem.getDefinition();
            if (definition instanceof MultiblockMachineDefinition) {
                return false;
            }
            GTRecipeType recipeType = definition.getRecipeTypes()[0];
            return RECIPE_TYPES.contains(recipeType);
        }
        return false;
    }

    @Nullable
    public static GTRecipe processingPlantOverclock(MetaMachine machine, @NotNull GTRecipe recipe, @NotNull OCParams params,
                                                    @NotNull OCResult result) {
        if (machine instanceof ProcessingPlantMachine plantMachine) {
            GTRecipe recipe1 = GTORecipeModifiers.reduction(machine, recipe, 0.9, 0.6);
            if (recipe1 != null) {
                recipe1 = GTRecipeModifiers.accurateParallel(machine, recipe1, plantMachine.getParallel(), false).getFirst();
                if (recipe1 != null) return RecipeHelper.applyOverclock(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK, recipe1,
                        plantMachine.getOverclockVoltage(), params, result);
            }
        }
        return null;
    }

    @Override
    public GTRecipeType[] getRecipeTypes() {
        return recipeTypeCache;
    }

    @Override
    public GTRecipeType getRecipeType() {
        return getRecipeTypes()[getActiveRecipeType()];
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        update();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!isRemote()) {
            machineStorage.addChangedListener(this::onMachineChanged);
        }
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        if (!this.isFormed) return;
        textList.add(Component.translatable("gtceu.multiblock.parallel", Component.literal(FormattingUtil.formatNumbers(getParallel())).withStyle(ChatFormatting.DARK_PURPLE)).withStyle(ChatFormatting.GRAY));
        if (mismatched) textList.add(Component.translatable("gtocore.machine.processing_plant.mismatched").withStyle(ChatFormatting.RED));
    }

    private void update() {
        recipeTypeCache = new GTRecipeType[] { GTRecipeTypes.DUMMY_RECIPES };
        mismatched = false;
        if (machineStorage.storage.getStackInSlot(0).getItem() instanceof MetaMachineItem metaMachineItem) {
            MachineDefinition definition = metaMachineItem.getDefinition();
            if (tier != definition.getTier()) {
                tier = 0;
                mismatched = true;
            }
            recipeTypeCache = definition.getRecipeTypes();
        }
    }

    private void onMachineChanged() {
        if (isFormed) {
            if (getRecipeLogic().getLastRecipe() != null) {
                getRecipeLogic().markLastRecipeDirty();
            }
            getRecipeLogic().updateTickSubscription();
            tier = GTUtil.getFloorTierByVoltage(getMaxVoltage());
            update();
        }
    }

    @Override
    public int getParallel() {
        return getTier() > 0 ? 4 * (getTier() - 1) : 0;
    }
}
