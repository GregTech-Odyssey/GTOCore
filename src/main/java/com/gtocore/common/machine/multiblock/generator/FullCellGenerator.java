package com.gtocore.common.machine.multiblock.generator;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOFluidStorageKey;
import com.gtocore.common.data.GTOMaterials;
import com.gtocore.common.data.GTORecipeTypes;

import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;
import com.gtolib.api.recipe.IdleReason;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.ingredient.FastFluidIngredient;
import com.gtolib.api.recipe.modifier.ParallelLogic;
import com.gtolib.utils.MachineUtils;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Fluid;

import com.google.common.collect.ImmutableMap;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Stream;

import static com.gregtechceu.gtceu.api.GTValues.*;

public class FullCellGenerator extends ElectricMultiblockMachine {

    @DescSynced
    private boolean isGenerator = false;
    @Persisted
    private double bonusEfficiency = 1.0f;
    private static final int MaxCanReleaseParallel = 50;

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            FullCellGenerator.class, ElectricMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Override
    public @NotNull ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    public FullCellGenerator(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        updateGeneratorState();
    }

    private void updateGeneratorState() {
        isGenerator = getRecipeTypes()[getActiveRecipeType()] == GTORecipeTypes.FUEL_CELL_ENERGY_RELEASE_RECIPES;
        requestSync();
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        bonusEfficiency = 1.0d;
    }

    @Override
    public boolean isGenerator() {
        return isGenerator;
    }

    @Override
    protected @Nullable Recipe getRealRecipe(Recipe recipe) {
        return isGenerator ? getReleaseRecipe(recipe) : getAbsorptionRecipe(recipe);
    }

    @Override
    public long getOverclockVoltage() {
        return super.getOverclockVoltage();
    }

    private Recipe getAbsorptionRecipe(Recipe recipe) {
        var fuelEnergyPerUnit = recipe.data.getLong("convertedEnergy");
        if (fuelEnergyPerUnit <= 0) return getElectrolyteTransferRecipe(recipe);

        // membrane bonus
        int membraneTier;
        for (membraneTier = Wrapper.MEMBRANE_MATS.size() - 1; membraneTier >= 0; membraneTier--) {
            if (MachineUtils.notConsumableItem(this, ChemicalHelper.get(GTOTagPrefix.MEMBRANE_ELECTRODE, Wrapper.MEMBRANE_MATS.get(membraneTier)))) {
                break;
            }
        }
        if (membraneTier < 0) {
            IdleReason.setIdleReason(this, IdleReason.INVALID_INPUT);
            return null;
        }
        bonusEfficiency = Math.pow(1.25d, membraneTier);
        fuelEnergyPerUnit = (long) (fuelEnergyPerUnit * bonusEfficiency);

        // find existing electrolytes
        Material electrolytesExisting = null;
        long amountExisting = 0;

        Material[] electrolyteMaterials = Wrapper.ELECTROLYTES_PER_MATERIAL_PER_MILLIBUCKET.keySet().toArray(new Material[0]);
        long[] cElectrolytesAmounts = getFluidAmount(
                Stream.of(electrolyteMaterials)
                        .map(m -> m.getFluid(GTOFluidStorageKey.ENERGY_RELEASE_CATHODE)).toArray(Fluid[]::new));
        long[] aElectrolytesAmounts = getFluidAmount(
                Stream.of(electrolyteMaterials)
                        .map(m -> m.getFluid(GTOFluidStorageKey.ENERGY_RELEASE_ANODE)).toArray(Fluid[]::new));
        for (int i = 0; i < cElectrolytesAmounts.length; i++) {
            if (cElectrolytesAmounts[i] > 0 && aElectrolytesAmounts[i] > 0) {
                electrolytesExisting = electrolyteMaterials[i];
                amountExisting = Math.min(cElectrolytesAmounts[i], aElectrolytesAmounts[i]);
                break;
            }
        }
        if (electrolytesExisting == null) return null;

        // parallel calculation
        long euPermB = Wrapper.ELECTROLYTES_PER_MATERIAL_PER_MILLIBUCKET.get(electrolytesExisting);
        long maxCanAbsorbParallel = amountExisting * euPermB / fuelEnergyPerUnit;
        Recipe result = ParallelLogic.accurateParallel(this, recipe, maxCanAbsorbParallel);
        if (result == null) return null;

        // electrolyte consumption adjustment
        long actuallyConsumedmB = result.parallels * fuelEnergyPerUnit / euPermB;
        var input = new ArrayList<>(result.inputs.get(FluidRecipeCapability.CAP));
        input.add(new Content(FastFluidIngredient.of(actuallyConsumedmB, electrolytesExisting.getFluid(GTOFluidStorageKey.ENERGY_RELEASE_ANODE)), 10000, 10000, 0));
        input.add(new Content(FastFluidIngredient.of(actuallyConsumedmB, electrolytesExisting.getFluid(GTOFluidStorageKey.ENERGY_RELEASE_CATHODE)), 10000, 10000, 0));
        var output = new ArrayList<Content>();
        output.add(new Content(FastFluidIngredient.of(actuallyConsumedmB, electrolytesExisting.getFluid(GTOFluidStorageKey.ENERGY_STORAGE_CATHODE)), 10000, 10000, 0));
        output.add(new Content(FastFluidIngredient.of(actuallyConsumedmB, electrolytesExisting.getFluid(GTOFluidStorageKey.ENERGY_STORAGE_ANODE)), 10000, 10000, 0));
        result.inputs.put(FluidRecipeCapability.CAP, input);
        result.outputs.put(FluidRecipeCapability.CAP, output);

        // content output check
        if (!this.canVoidRecipeOutputs(FluidRecipeCapability.CAP)) {
            var contents = result.getOutputContents(FluidRecipeCapability.CAP);
            List<FluidIngredient> copied = new ObjectArrayList<>(contents.size());
            for (var ing : contents) {
                copied.add(((FluidIngredient) (ing.content)).copy());
            }
            boolean success = false;
            for (var handler : getCapabilitiesFlat(IO.OUT, FluidRecipeCapability.CAP)) {
                // noinspection unchecked
                copied = (List<FluidIngredient>) handler.handleRecipe(IO.OUT, recipe, copied, true);
                if (copied == null || copied.isEmpty()) {
                    success = true;
                    break;
                }
            }
            if (!success) {
                IdleReason.setIdleReason(this, IdleReason.OUTPUT_FULL);
                return null;
            }
        }
        return result;
    }

    private Recipe getElectrolyteTransferRecipe(Recipe recipe) {
        if (recipe.data.getFloat("efficiency") <= 0) {
            return null;
        }
        bonusEfficiency = recipe.data.getFloat("efficiency") * 0.25d;
        return ParallelLogic.accurateParallel(this, recipe, Long.MAX_VALUE);
    }

    @Override
    public void customText(@NotNull List<Component> textList) {
        super.customText(textList);
        if (!isGenerator) {
            textList.add(
                    Component.translatable("tooltip.enderio.capacitor.fuel_efficiency", FormattingUtil.formatNumber2Places(bonusEfficiency * 400) + "%"));
        }
    }

    @Override
    public void afterWorking() {
        super.afterWorking();
        bonusEfficiency = 1.0d;
    }

    private Recipe getReleaseRecipe(Recipe recipe) {
        return ParallelLogic.accurateParallel(this, recipe, MaxCanReleaseParallel);
    }

    @Override
    public void setActiveRecipeType(int activeRecipeType) {
        super.setActiveRecipeType(activeRecipeType);
        updateGeneratorState();
    }

    public static class Wrapper {

        public static final ImmutableMap<Material, Long> ELECTROLYTES_PER_MATERIAL_PER_MILLIBUCKET = ImmutableMap.<Material, Long>builder()
                .put(GTOMaterials.IronChromiumRedoxFlowBatteryElectrolyte, V[UEV] * 2 / 1000)
                .put(GTOMaterials.VanadiumRedoxFlowBatteryElectrolyte, V[UXV] / 1000)
                .put(GTOMaterials.ZincIodideFlowBatteryElectrolyte, V[OpV] * 2 / 1000)
                .put(GTOMaterials.OrganicMoleculeRedoxFlowBatteryElectrolyte, V[MAX] * 4 / 1000)
                .put(GTOMaterials.SuperconductingIonRedoxFlowBatteryElectrolyte, V[MAX] * 32 / 1000)
                .put(GTOMaterials.AntimatterRedoxFlowBatteryElectrolyte, V[MAX] * 256 / 1000)
                .build();
        public static final ObjectList<Material> MEMBRANE_MATS = new ObjectImmutableList<>(
                new Material[] {
                        GTMaterials.Polytetrafluoroethylene,
                        GTMaterials.Graphene,
                        GTOMaterials.PolousPolyolefinSulfonate,
                        GTOMaterials.PerfluorosulfonicAcidPolytetrafluoroethyleneCopolymer,
                        GTOMaterials.CeOxPolyDopamineReinforcedPolytetrafluoroethylene,
                        GTOMaterials.NanocrackRegulatedSelfHumidifyingCompositeMaterial
                });
    }
}
