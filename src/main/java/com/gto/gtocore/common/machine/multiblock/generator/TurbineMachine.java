package com.gto.gtocore.common.machine.multiblock.generator;

import com.gto.gtocore.api.machine.part.ItemHatchPartMachine;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.ITieredMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMaintenanceMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.chance.logic.ChanceLogic;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.logic.OCParams;
import com.gregtechceu.gtceu.api.recipe.logic.OCResult;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import com.gregtechceu.gtceu.common.item.TurbineRotorBehaviour;
import com.gregtechceu.gtceu.common.machine.multiblock.part.RotorHolderPartMachine;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTUtil;

import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.misc.ItemStackTransfer;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import com.mojang.datafixers.util.Pair;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TurbineMachine extends WorkableElectricMultiblockMachine implements ITieredMachine {

    public static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            TurbineMachine.class, WorkableElectricMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    public static final int MIN_DURABILITY_TO_WARN = 10;

    private final int baseEUOutput;

    @Getter
    private final int tier;
    private final boolean mega;

    private long energyPerTick = 0;

    @Persisted
    private boolean highSpeedMode = false;

    private Set<RotorHolderPartMachine> rotorHolderMachines;

    private ItemHatchPartMachine rotorHatchPartMachine = null;

    private final ConditionalSubscriptionHandler rotorSubs;

    public TurbineMachine(IMachineBlockEntity holder, int tier, boolean special, boolean mega) {
        super(holder);
        this.mega = mega;
        this.tier = tier;
        this.baseEUOutput = (int) GTValues.V[tier] * (mega ? 4 : 1) * (special ? 3 : 2);
        this.rotorSubs = new ConditionalSubscriptionHandler(this, this::rotorUpdate, this::isFormed);
    }

    private void rotorUpdate() {
        if (rotorHatchPartMachine != null && getOffsetTimer() % 20 == 0 && !isActive()) {
            if (rotorHatchPartMachine.getInventory().isEmpty()) return;
            ItemStackTransfer storage = rotorHatchPartMachine.getInventory().storage;
            for (RotorHolderPartMachine part : rotorHolderMachines) {
                if (!part.hasRotor()) {
                    part.setRotorStack(storage.getStackInSlot(0));
                    storage.setStackInSlot(0, new ItemStack(Items.AIR));
                }
            }
        }
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        for (IMultiPart part : getParts()) {
            if (part instanceof RotorHolderPartMachine rotorHolderMachine) {
                rotorHolderMachines = Objects.requireNonNullElseGet(rotorHolderMachines, HashSet::new);
                rotorHolderMachines.add(rotorHolderMachine);
            }
            if (part instanceof ItemHatchPartMachine rotorHatchPart) {
                rotorHatchPartMachine = rotorHatchPart;
            }
        }
        if (mega) rotorSubs.initialize(getLevel());
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        rotorHolderMachines = null;
        rotorHatchPartMachine = null;
    }

    @Override
    public boolean onWorking() {
        if (highSpeedMode && getOffsetTimer() % 20 == 0) {
            for (RotorHolderPartMachine part : rotorHolderMachines) {
                part.damageRotor(11);
            }
        }
        return super.onWorking();
    }

    @Override
    public void afterWorking() {
        energyPerTick = 0;
        for (IMultiPart part : getParts()) {
            if (highSpeedMode && part instanceof IMaintenanceMachine maintenanceMachine) {
                maintenanceMachine.calculateMaintenance(maintenanceMachine, 12 * getRecipeLogic().getProgress());
                if (maintenanceMachine.hasMaintenanceProblems()) {
                    getRecipeLogic().markLastRecipeDirty();
                }
                continue;
            }
            part.afterWorking(this);
        }
    }

    @Nullable
    private RotorHolderPartMachine getRotorHolder() {
        if (rotorHolderMachines != null) {
            for (RotorHolderPartMachine part : rotorHolderMachines) {
                return part;
            }
        }
        return null;
    }

    private int getRotorSpeed() {
        if (mega) {
            Set<Material> material = new HashSet<>();
            int speed = 0;
            for (RotorHolderPartMachine part : rotorHolderMachines) {
                ItemStack stack = part.getRotorStack();
                TurbineRotorBehaviour rotorBehaviour = TurbineRotorBehaviour.getBehaviour(stack);
                if (rotorBehaviour == null) return -1;
                material.add(rotorBehaviour.getPartMaterial(stack));
                speed += part.getRotorSpeed();
            }
            return material.size() == 1 ? speed / 4 : -1;
        }
        RotorHolderPartMachine rotor = getRotorHolder();
        if (rotor != null) {
            return rotor.getRotorSpeed();
        }
        return 0;
    }

    @Override
    public long getOverclockVoltage() {
        var rotorHolder = getRotorHolder();
        if (rotorHolder != null && rotorHolder.hasRotor()) {
            return (long) (baseEUOutput * rotorHolder.getRotorPower() * Math.pow(2, rotorHolder.getTier() - 3) * (highSpeedMode ? 3 : 1) / 100);
        }
        return 0;
    }

    //////////////////////////////////////
    // ****** Recipe Logic *******//
    //////////////////////////////////////
    @Nullable
    public static GTRecipe recipeModifier(MetaMachine machine, @NotNull GTRecipe recipe, @NotNull OCParams params,
                                          @NotNull OCResult result) {
        if (machine instanceof TurbineMachine turbineMachine) {
            RotorHolderPartMachine rotorHolder = turbineMachine.getRotorHolder();
            long EUt = RecipeHelper.getOutputEUt(recipe);
            if (rotorHolder == null || EUt <= 0) return null;

            int rotorSpeed = turbineMachine.getRotorSpeed();
            if (rotorSpeed == -1) return null;
            int maxSpeed = rotorHolder.getMaxRotorHolderSpeed();

            long turbineMaxVoltage = (long) (turbineMachine.getOverclockVoltage() * Math.pow((double) Math.min(maxSpeed, rotorSpeed / 3) / maxSpeed, 2));

            Pair<GTRecipe, Integer> parallelResult = GTRecipeModifiers.fastParallel(turbineMachine, recipe, (int) (turbineMaxVoltage / EUt), false);

            long eut = Math.min(turbineMaxVoltage, parallelResult.getSecond() * EUt);
            turbineMachine.energyPerTick = eut;

            GTRecipe recipe1 = parallelResult.getFirst();
            recipe1.duration = recipe1.duration * rotorHolder.getTotalEfficiency() / 100;
            recipe1.tickOutputs.put(EURecipeCapability.CAP, List.of(new Content(eut, ChanceLogic.getMaxChancedValue(), ChanceLogic.getMaxChancedValue(), 0, null, null)));
            return recipe1;
        }
        return null;
    }

    @Override
    public boolean dampingWhenWaiting() {
        return false;
    }

    @Override
    public boolean canVoidRecipeOutputs(RecipeCapability<?> capability) {
        return capability != EURecipeCapability.CAP;
    }

    //////////////////////////////////////
    // ******* GUI ********//
    //////////////////////////////////////

    @Override
    public void handleDisplayClick(String componentData, ClickData clickData) {
        if (mega && !clickData.isRemote && !isActive() && getRotorHolder() != null && getRotorSpeed() < 1) {
            if (componentData.equals("highSpeedMode")) {
                this.highSpeedMode = !this.highSpeedMode;
            }
        }
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        if (isFormed()) {
            if (mega) textList.add(Component.translatable("gtocore.machine.mega_turbine.high_speed_mode").append(ComponentPanelWidget.withButton(Component.literal("[").append(this.highSpeedMode ? Component.translatable("gtocore.machine.on") : Component.translatable("gtocore.machine.off")).append(Component.literal("]")), "highSpeedMode")));
            var rotorHolder = getRotorHolder();

            if (rotorHolder != null && rotorHolder.getRotorEfficiency() > 0) {
                textList.add(Component.translatable("gtceu.multiblock.turbine.rotor_speed", FormattingUtil.formatNumbers(getRotorSpeed()), FormattingUtil.formatNumbers(rotorHolder.getMaxRotorHolderSpeed() * (highSpeedMode ? 3 : 1))));
                textList.add(Component.translatable("gtceu.multiblock.turbine.efficiency", rotorHolder.getTotalEfficiency()));

                if (isActive()) {
                    String voltageName = GTValues.VNF[GTUtil.getTierByVoltage(energyPerTick)];
                    textList.add(3, Component.translatable("gtceu.multiblock.turbine.energy_per_tick",
                            FormattingUtil.formatNumbers(energyPerTick), voltageName));
                }

                if (!mega) {
                    int rotorDurability = rotorHolder.getRotorDurabilityPercent();
                    if (rotorDurability > MIN_DURABILITY_TO_WARN) {
                        textList.add(Component.translatable("gtceu.multiblock.turbine.rotor_durability", rotorDurability));
                    } else {
                        textList.add(Component.translatable("gtceu.multiblock.turbine.rotor_durability", rotorDurability)
                                .setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
                    }
                }
            }
        }
    }
}
