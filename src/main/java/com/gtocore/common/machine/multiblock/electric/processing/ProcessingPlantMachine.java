package com.gtocore.common.machine.multiblock.electric.processing;

import com.gtocore.common.data.GTORecipeTypes;

import com.gtolib.api.GTOValues;
import com.gtolib.api.gui.ParallelConfigurator;
import com.gtolib.api.machine.feature.multiblock.IParallelMachine;
import com.gtolib.api.machine.feature.multiblock.ITierCasingMachine;
import com.gtolib.api.machine.multiblock.StorageMultiblockMachine;
import com.gtolib.api.machine.trait.CustomParallelTrait;
import com.gtolib.api.machine.trait.TierCasingTrait;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeType;
import com.gtolib.api.recipe.modifier.RecipeModifierFunction;
import com.gtolib.utils.MachineUtils;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.item.MetaMachineItem;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.feature.ICleanroomProvider;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.common.machine.multiblock.electric.CleanroomMachine;

import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class ProcessingPlantMachine extends StorageMultiblockMachine implements IParallelMachine, ITierCasingMachine {

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
            GTRecipeTypes.PACKER_RECIPES,
            GTORecipeTypes.UNPACKER_RECIPES,
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
            GTRecipeTypes.ALLOY_SMELTER_RECIPES,
            GTRecipeTypes.ARC_FURNACE_RECIPES,
            GTORecipeTypes.ARC_GENERATOR_RECIPES,
            GTORecipeTypes.LOOM_RECIPES,
            GTORecipeTypes.LAMINATOR_RECIPES,
            GTORecipeTypes.LASER_WELDER_RECIPES);

    public static Component getComponent() {
        var c = Component.empty();
        boolean first = true;
        for (var r : RECIPE_TYPES) {
            if (!first) c.append(", ");
            first = false;
            c.append(Component.translatable("gtceu." + r.registryName.getPath()));
        }
        return c;
    }

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

    private final TierCasingTrait tierCasingTrait;

    public ProcessingPlantMachine(MetaMachineBlockEntity holder) {
        super(holder, 1, ProcessingPlantMachine::filter);
        customParallelTrait = new CustomParallelTrait(this, true, machine -> ((ProcessingPlantMachine) machine).getTier() > 0 ? (long) ((ProcessingPlantMachine) machine).getTier() * (((ProcessingPlantMachine) machine).getSubFormedAmount() > 0 ? 4 : 2) : 0);
        tierCasingTrait = new TierCasingTrait(this, GTOValues.INTEGRAL_FRAMEWORK_TIER);
    }

    private static boolean filter(ItemStack itemStack) {
        if (itemStack.getItem() instanceof MetaMachineItem metaMachineItem) {
            MachineDefinition definition = metaMachineItem.getDefinition();
            if (definition instanceof MultiblockMachineDefinition) {
                return false;
            }
            var types = definition.getRecipeTypes();
            if (types == null || types.length == 0) return false;
            GTRecipeType recipeType = types[0];
            return RECIPE_TYPES.contains(recipeType);
        }
        return false;
    }

    @Override
    protected boolean beforeWorking(@Nullable Recipe recipe) {
        if (mismatched || isEmpty()) return false;
        return super.beforeWorking(recipe);
    }

    @Nullable
    @Override
    protected Recipe getRealRecipe(@NotNull Recipe recipe) {
        if (!mismatched && !isEmpty()) {
            return RecipeModifierFunction.overclocking(this, recipe, false, 0.9, 0.8, 0.5);
        }
        return null;
    }

    @Override
    public GTRecipeType[] getRecipeTypes() {
        return recipeTypeCache;
    }

    @Override
    public RecipeType getRecipeType() {
        return (RecipeType) getRecipeTypes()[getActiveRecipeType()];
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
    public void onMachineChanged() {
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
    public long getMaxParallel() {
        return customParallelTrait.getMaxParallel();
    }

    @Override
    public long getParallelLong() {
        return customParallelTrait.getParallel();
    }

    @Override
    public void setParallel(long number) {
        customParallelTrait.setParallel(number);
    }

    @Override
    public void setCleanroom(@Nullable ICleanroomProvider provider) {
        if (provider instanceof CleanroomMachine) super.setCleanroom(provider);
    }

    @Override
    public int getTier() {
        if (!isFormed) return 0;
        return Math.min(getCasingTier(GTOValues.INTEGRAL_FRAMEWORK_TIER), tier);
    }

    @Override
    public Object2IntMap<String> getCasingTiers() {
        return tierCasingTrait.getCasingTiers();
    }
}
