package com.gto.gtocore.common.machine.multiblock.electric;

import com.gto.gtocore.api.gui.ParallelConfigurator;
import com.gto.gtocore.api.machine.feature.multiblock.IParallelMachine;
import com.gto.gtocore.api.machine.multiblock.StorageMultiblockMachine;
import com.gto.gtocore.api.machine.trait.CustomParallelTrait;
import com.gto.gtocore.common.data.GTORecipeModifiers;
import com.gto.gtocore.common.data.GTORecipeTypes;
import com.gto.gtocore.utils.MachineUtils;

import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.item.MetaMachineItem;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class ProcessingPlantMachine extends StorageMultiblockMachine implements IParallelMachine {

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
            GTORecipeTypes.ARC_GENERATOR_RECIPES,
            GTORecipeTypes.LOOM_RECIPES,
            GTORecipeTypes.LAMINATOR_RECIPES,
            GTORecipeTypes.LASER_WELDER_RECIPES);

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            ProcessingPlantMachine.class, StorageMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Nullable
    private GTRecipeType[] recipeTypeCache = { GTRecipeTypes.DUMMY_RECIPES };

    private boolean mismatched;

    @Persisted
    private final CustomParallelTrait customParallelTrait;

    public ProcessingPlantMachine(IMachineBlockEntity holder) {
        super(holder, 1, ProcessingPlantMachine::filter);
        customParallelTrait = new CustomParallelTrait(this, true, machine -> ((ProcessingPlantMachine) machine).getTier() > 0 ? 4 * (((ProcessingPlantMachine) machine).getTier() - 1) : 0);
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
    @Override
    protected GTRecipe getRealRecipe(@NotNull GTRecipe recipe) {
        if (!mismatched) {
            return GTORecipeModifiers.overclocking(this, recipe, false, false, 0.9, 0.8);
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
    public void onStructureInvalid() {
        super.onStructureInvalid();
        customParallelTrait.onStructureInvalid();
    }

    @Override
    public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
        super.attachConfigurators(configuratorPanel);
        configuratorPanel.attachConfigurators(new ParallelConfigurator(this));
    }

    @Override
    public void customText(List<Component> textList) {
        super.customText(textList);
        MachineUtils.addRecipeTypeText(textList, this);
        if (mismatched) textList.add(Component.translatable("gtocore.machine.processing_plant.mismatched").withStyle(ChatFormatting.RED));
    }

    private void update() {
        recipeTypeCache = new GTRecipeType[] { GTRecipeTypes.DUMMY_RECIPES };
        mismatched = false;
        if (machineStorage.storage.getStackInSlot(0).getItem() instanceof MetaMachineItem metaMachineItem) {
            MachineDefinition definition = metaMachineItem.getDefinition();
            if (tier != definition.getTier()) {
                mismatched = true;
            }
            recipeTypeCache = definition.getRecipeTypes();
        }
    }

    @Override
    protected void onMachineChanged() {
        customParallelTrait.onStructureInvalid();
        if (isFormed) {
            if (getRecipeLogic().getLastRecipe() != null) {
                getRecipeLogic().markLastRecipeDirty();
            }
            getRecipeLogic().updateTickSubscription();
            update();
        }
    }

    @Override
    public int getMaxParallel() {
        return customParallelTrait.getMaxParallel();
    }

    @Override
    public int getParallel() {
        return customParallelTrait.getParallel();
    }

    @Override
    public void setParallel(int number) {
        customParallelTrait.setParallel(number);
    }
}
