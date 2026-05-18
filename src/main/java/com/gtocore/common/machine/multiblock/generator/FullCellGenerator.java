package com.gtocore.common.machine.multiblock.generator;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOFluidStorageKey;
import com.gtocore.common.data.GTOMaterials;
import com.gtocore.common.data.GTORecipeDataKeys;
import com.gtocore.common.data.GTORecipeTypes;
import com.gtocore.common.machine.multiblock.part.SensorPartMachine;

import com.gtolib.GTOCore;
import com.gtolib.api.annotation.Scanned;
import com.gtolib.api.annotation.dynamic.DynamicInitialValue;
import com.gtolib.api.annotation.dynamic.DynamicInitialValueTypes;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;
import com.gtolib.api.recipe.IdleReason;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.modifier.ParallelLogic;
import com.gtolib.utils.MachineUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.recipe.ingredient.ItemIngredient;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Fluid;

import com.google.common.collect.ImmutableMap;
import com.gto.datasynclib.annotations.SyncToClient;
import com.gto.datasynclib.util.holder.BooleanHolder;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.gregtechceu.gtceu.api.GTValues.*;

@Scanned
public class FullCellGenerator extends ElectricMultiblockMachine {

    private static final int MaxCanReleaseParallel = 50;

    @DynamicInitialValue(key = "fuelcell.chance_consume", easyValue = "0.0d", normalValue = "0.0035d", expertValue = "0.007d", typeKey = DynamicInitialValueTypes.KEY_PROBABILITY, cn = "放电时膜损坏概率", cnComment = """
            放电时使用的膜材料的损坏概率。
            """, en = "Fuel Cell Membrane Damage Chance on Discharge", enComment = """
            The chance of the membrane material used being damaged upon discharging.
            """)
    public static double chanceConsumeMembraneOnDischarge = 0.0035d;

    @SyncToClient
    private boolean isGenerator = false;
    @Persisted
    private double bonusEfficiency = 1.0f;
    @Persisted
    private double accumulatedEfficiencyDecay = 1.0f;

    @Nullable
    private SensorPartMachine sensorPart;

    private TickableSubscription updateSubs;

    public FullCellGenerator(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        updateGeneratorState();
        subscribeServerTick(updateSubs, () -> {
            if ((getRecipeLogic().isIdle()) && inputFluid(GTMaterials.DistilledWater.getFluid(600))) {
                accumulatedEfficiencyDecay += (1.0d - accumulatedEfficiencyDecay) * 0.15d;
            }
        });
    }

    @Override
    public void onPartScan(@NotNull IMultiPart part) {
        super.onPartScan(part);
        if (sensorPart == null && part instanceof SensorPartMachine sensor) {
            this.sensorPart = sensor;
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        if (updateSubs != null) {
            updateSubs.unsubscribe();
            updateSubs = null;
        }
    }

    private void updateGeneratorState() {
        isGenerator = getRecipeTypes()[getActiveRecipeType()] == GTORecipeTypes.FUEL_CELL_ENERGY_RELEASE_RECIPES;
        requestSync();
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        bonusEfficiency = 1.0d;
        sensorPart = null;
    }

    @Override
    public boolean isGenerator() {
        return isGenerator;
    }

    @Override
    protected @Nullable Recipe getRealRecipe(Recipe recipe) {
        var activeType = getRecipeTypes()[getActiveRecipeType()];
        if (activeType == GTORecipeTypes.FUEL_CELL_ENERGY_RELEASE_RECIPES) {
            return getReleaseRecipe(recipe);
        } else if (activeType == GTORecipeTypes.FUEL_CELL_ENERGY_ABSORPTION_RECIPES) {
            return getAbsorptionRecipe(recipe);
        } else if (activeType == GTORecipeTypes.FUEL_CELL_ENERGY_TRANSFER_RECIPES) {
            return getElectrolyteTransferRecipe(recipe);
        }
        return null;
    }

    @Override
    public long getOverclockVoltage() {
        return super.getOverclockVoltage();
    }

    private Recipe getAbsorptionRecipe(Recipe recipe) {
        var fuelEnergyPerUnit = recipe.data.getLong(GTORecipeDataKeys.CONVERTED_ENERGY);

        // membrane bonus
        MembraneBonusInfo membraneInfo = null;
        for (int membraneTier = Wrapper.MEMBRANE_MATS.length - 1; membraneTier >= 0; membraneTier--) {
            if (MachineUtils.notConsumableItem(this, ChemicalHelper.get(GTOTagPrefix.MEMBRANE_ELECTRODE, Wrapper.MEMBRANE_MATS[membraneTier].membrane))) {
                membraneInfo = Wrapper.MEMBRANE_MATS[membraneTier];
                break;
            }
        }
        if (membraneInfo == null) {
            IdleReason.setIdleReason(this, IdleReason.INVALID_INPUT);
            return null;
        }
        if (GTOCore.isEasy()) {
            bonusEfficiency = membraneInfo.efficiencyBonus;
        } else {
            var efficiencyBonusDecayFactor = GTOCore.isExpert() ? membraneInfo.efficiencyBonusDecayFactorExpertMode : membraneInfo.efficiencyBonusDecayFactor;
            var efficiencyBonus = GTOCore.isExpert() ? membraneInfo.efficiencyBonusExpertMode : membraneInfo.efficiencyBonus;
            bonusEfficiency = efficiencyBonus * accumulatedEfficiencyDecay;
            accumulatedEfficiencyDecay *= efficiencyBonusDecayFactor;
        }
        fuelEnergyPerUnit = (long) (fuelEnergyPerUnit * bonusEfficiency);
        if (sensorPart != null) {
            sensorPart.update((float) bonusEfficiency * 4.0f);
        }

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
        input.add(new Content(FluidIngredient.of(electrolytesExisting.getFluid(GTOFluidStorageKey.ENERGY_RELEASE_ANODE), actuallyConsumedmB), 10000, 0));
        input.add(new Content(FluidIngredient.of(electrolytesExisting.getFluid(GTOFluidStorageKey.ENERGY_RELEASE_CATHODE), actuallyConsumedmB), 10000, 0));
        var output = new ArrayList<Content>();
        output.add(new Content(FluidIngredient.of(electrolytesExisting.getFluid(GTOFluidStorageKey.ENERGY_STORAGE_CATHODE), actuallyConsumedmB), 10000, 0));
        output.add(new Content(FluidIngredient.of(electrolytesExisting.getFluid(GTOFluidStorageKey.ENERGY_STORAGE_ANODE), actuallyConsumedmB), 10000, 0));
        result.inputs.put(FluidRecipeCapability.CAP, input);
        result.outputs.put(FluidRecipeCapability.CAP, output);

        // content output check
        if (!this.canVoidRecipeOutputs(FluidRecipeCapability.CAP)) {
            var contents = result.getOutputContents(FluidRecipeCapability.CAP);
            List<FluidIngredient> copied = new ArrayList<>(contents.size());
            for (var ing : contents) {
                copied.add(((FluidIngredient) (ing.inner)).copy());
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
        if (recipe.data.getFloat(GTORecipeDataKeys.EFFICIENCY) <= 0) {
            return null;
        }
        bonusEfficiency = recipe.data.getFloat(GTORecipeDataKeys.EFFICIENCY) * 0.25d;
        if (sensorPart != null) {
            sensorPart.update((float) bonusEfficiency);
        }
        return ParallelLogic.accurateParallel(this, recipe, Long.MAX_VALUE);
    }

    @Override
    public void customText(@NotNull List<Component> textList) {
        super.customText(textList);
        if (!isGenerator) {
            textList.add(
                    Component.translatable(FUEL_EFFICIENCY, FormattingUtil.formatNumber2Places(bonusEfficiency * 400) + "%"));
            textList.add(
                    Component.translatable(EFFICIENCY_DECAY, DECIMAL_FORMAT_4F.format((1 - accumulatedEfficiencyDecay) * 100) + "%"));
        }
    }

    @Override
    public void afterWorking() {
        super.afterWorking();
        bonusEfficiency = 1.0d;
    }

    private Recipe getReleaseRecipe(Recipe recipe) {
        var input = new ArrayList<>(recipe.inputs.get(ItemRecipeCapability.CAP));
        var ingredient = (ItemIngredient) input.getFirst().inner;
        var item = ingredient.getInnerItemStack().getItem();
        BooleanHolder hasMembrane = new BooleanHolder(false);
        fastForEachInputItems((i, a) -> {
            if (i.getItem() == item) {
                hasMembrane.value = true;
            }
        });
        if (!hasMembrane.value) {
            IdleReason.setIdleReason(this, IdleReason.INVALID_INPUT);
            return null;
        }
        if (GTValues.RNG.nextFloat() < chanceConsumeMembraneOnDischarge) {
            inputItem(ingredient.getInnerItemStack().getItem(), ingredient.amount);
        }
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
        public static final MembraneBonusInfo[] MEMBRANE_MATS = new MembraneBonusInfo[] {
                new MembraneBonusInfo(
                        0, GTMaterials.Polytetrafluoroethylene,
                        GTOMaterials.IronChromiumRedoxFlowBatteryElectrolyte,
                        1.05d, 0.997d, 1.6d, 0.992d),
                new MembraneBonusInfo(
                        1, GTMaterials.Graphene,
                        GTOMaterials.VanadiumRedoxFlowBatteryElectrolyte,
                        1.52d, 0.999d, 2.09d, 0.993d),
                new MembraneBonusInfo(
                        2, GTOMaterials.PolousPolyolefinSulfonate,
                        GTOMaterials.ZincIodideFlowBatteryElectrolyte,
                        2.09d, 0.9995d, 2.44d, 0.995d),
                new MembraneBonusInfo(
                        3, GTOMaterials.PerfluorosulfonicAcidPolytetrafluoroethyleneCopolymer,
                        GTOMaterials.OrganicMoleculeRedoxFlowBatteryElectrolyte,
                        2.44d, 0.9998d, 2.93d, 0.997d),
                new MembraneBonusInfo(
                        4, GTOMaterials.CeOxPolyDopamineReinforcedPolytetrafluoroethylene,
                        GTOMaterials.SuperconductingIonRedoxFlowBatteryElectrolyte,
                        3.05d, 0.9999d, 3.31d, 0.9991d),
                new MembraneBonusInfo(
                        5, GTOMaterials.NanocrackRegulatedSelfHumidifyingCompositeMaterial,
                        GTOMaterials.AntimatterRedoxFlowBatteryElectrolyte,
                        3.51d, 1.0d, 3.57d, 0.9995d)
        };
        public static final ImmutableMap<Material, MembraneBonusInfo> MEMBRANE_MAT_TO_BONUS = Arrays.stream(MEMBRANE_MATS).collect(ImmutableMap.toImmutableMap(info -> info.membrane, info -> info));
    }

    public record MembraneBonusInfo(
                                    int tier,
                                    Material membrane,
                                    Material electrolyte,
                                    double efficiencyBonus,
                                    double efficiencyBonusDecayFactor,
                                    double efficiencyBonusExpertMode,
                                    double efficiencyBonusDecayFactorExpertMode) {

        public void getInfoComponents(List<Component> components) {
            components.add(Component.translatable(MEMBRANE_TIER, Component.literal(String.valueOf(tier)).withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.GRAY));
            components.add(Component.translatable(DISCHARGE_ELECTROLYTE, electrolyte.getLocalizedName().withStyle(ChatFormatting.YELLOW)).withStyle(ChatFormatting.GRAY));
            components.add(Component.translatable(ABSORPTION_EFFICIENCY, Component.literal(FormattingUtil.formatNumber2Places(efficiencyBonus * 400) + "%").withStyle(ChatFormatting.GREEN)).withStyle(ChatFormatting.GRAY));
            if (!GTOCore.isEasy()) {
                if (efficiencyBonusDecayFactor == 1.0d) {
                    components.add(Component.translatable(ABSORPTION_EFFICIENCY_NO_DECAY).withStyle(ChatFormatting.GRAY));
                } else {
                    components.add(Component.translatable(ABSORPTION_EFFICIENCY_DECAY, Component.literal("x" + DECIMAL_FORMAT_4F.format(efficiencyBonusDecayFactor)).withStyle(ChatFormatting.RED)).withStyle(ChatFormatting.GRAY));
                }
            }
        }
    }

    private static final DecimalFormat DECIMAL_FORMAT_4F = new DecimalFormat("#,##0.####");

    @RegisterLanguage(cn = "燃料效率乘数：%s", en = "Fuel Efficiency Multiplier: %s")
    private static final String FUEL_EFFICIENCY = "gtocore.machine.fuelcell_efficiency";
    @RegisterLanguage(cn = "连续运行效率衰减：%s", en = "Continuous Operation Efficiency Decay: %s")
    private static final String EFFICIENCY_DECAY = "gtocore.machine.fuelcell_efficiency_decay";
    @RegisterLanguage(cn = "膜等级: %s", en = "Membrane Tier: %s")
    private static final String MEMBRANE_TIER = "gtocore.machine.fuelcell_membrane_tier";
    @RegisterLanguage(cn = "放电模式适用电解质: %s", en = "Discharge Mode Applicable Electrolyte: %s")
    private static final String DISCHARGE_ELECTROLYTE = "gtocore.machine.fuelcell_discharge_electrolyte";
    @RegisterLanguage(cn = "吸收模式效率乘数: %s", en = "Absorption Mode Efficiency Multiplier: %s")
    private static final String ABSORPTION_EFFICIENCY = "gtocore.machine.fuelcell_absorption_efficiency";
    @RegisterLanguage(cn = "吸收模式效率衰减: %s/运行次", en = "Absorption Mode Efficiency Decay: %s/op")
    private static final String ABSORPTION_EFFICIENCY_DECAY = "gtocore.machine.fuelcell_absorption_efficiency_decay";
    @RegisterLanguage(cn = "吸收模式效率衰减: §a无衰减§r", en = "Absorption Mode Efficiency Decay: §aNo Decay§r")
    private static final String ABSORPTION_EFFICIENCY_NO_DECAY = "gtocore.machine.fuelcell_absorption_efficiency_decay.no_decay";
}
