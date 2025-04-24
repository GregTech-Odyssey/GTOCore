package com.gto.gtocore.common.machine.multiblock.generator;

import com.gto.gtocore.api.gui.GTOGuiTextures;
import com.gto.gtocore.api.machine.multiblock.ElectricMultiblockMachine;
import com.gto.gtocore.api.machine.part.ItemHatchPartMachine;
import com.gto.gtocore.api.recipe.ContentBuilder;
import com.gto.gtocore.common.data.GTORecipeModifiers;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfiguratorButton;
import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMaintenanceMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.common.item.TurbineRotorBehaviour;
import com.gregtechceu.gtceu.common.machine.multiblock.part.RotorHolderPartMachine;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class TurbineMachine extends ElectricMultiblockMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            TurbineMachine.class, ElectricMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    private final long baseEUOutput;

    @Getter
    private final int tier;
    private final boolean mega;

    private long energyPerTick;

    @Persisted
    private boolean highSpeedMode;

    private final List<RotorHolderPartMachine> rotorHolderMachines = new ObjectArrayList<>();

    private ItemHatchPartMachine rotorHatchPartMachine;

    private final ConditionalSubscriptionHandler rotorSubs;

    public TurbineMachine(IMachineBlockEntity holder, int tier, boolean special, boolean mega) {
        super(holder);
        this.mega = mega;
        this.tier = tier;
        baseEUOutput = (long) (GTValues.V[tier] * (mega ? 4 : 1) * (special ? 2.5 : 2));
        rotorSubs = new ConditionalSubscriptionHandler(this, this::rotorUpdate, () -> rotorHatchPartMachine != null);
    }

    private void rotorUpdate() {
        if (getOffsetTimer() % 20 == 0 && !isActive()) {
            rotorSubs.updateSubscription();
            if (rotorHatchPartMachine == null || rotorHatchPartMachine.getInventory().isEmpty()) return;
            boolean full = true;
            for (RotorHolderPartMachine part : rotorHolderMachines) {
                if (part.getRotorStack().isEmpty()) {
                    full = false;
                    part.setRotorStack(rotorHatchPartMachine.getInventory().getStackInSlot(0));
                    rotorHatchPartMachine.getInventory().setStackInSlot(0, ItemStack.EMPTY);
                }
            }
            if (full) {
                rotorSubs.unsubscribe();
            }
        }
    }

    @Override
    public void onPartScan(IMultiPart part) {
        super.onPartScan(part);
        if (part instanceof RotorHolderPartMachine rotorHolderMachine) {
            rotorHolderMachines.add(rotorHolderMachine);
            rotorHolderMachine.inventory.addChangedListener(rotorSubs::updateSubscription);
        } else if (rotorHatchPartMachine == null && part instanceof ItemHatchPartMachine rotorHatchPart) {
            rotorHatchPartMachine = rotorHatchPart;
        }
    }

    @Override
    public void onStructureFormed() {
        rotorHolderMachines.clear();
        super.onStructureFormed();
        if (mega) rotorSubs.initialize(getLevel());
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        rotorHolderMachines.clear();
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
        for (RotorHolderPartMachine part : rotorHolderMachines) {
            return part;
        }
        return null;
    }

    private int getRotorSpeed() {
        if (mega) {
            Set<Material> material = new ObjectOpenHashSet<>(2, 0.9F);
            int speed = 0;
            for (RotorHolderPartMachine part : rotorHolderMachines) {
                ItemStack stack = part.getRotorStack();
                TurbineRotorBehaviour rotorBehaviour = TurbineRotorBehaviour.getBehaviour(stack);
                if (rotorBehaviour == null) return -1;
                material.add(rotorBehaviour.getPartMaterial(stack));
                speed += part.getRotorSpeed();
            }
            return material.size() == 1 ? speed / 12 : -1;
        }
        RotorHolderPartMachine rotor = getRotorHolder();
        if (rotor != null) {
            return rotor.getRotorSpeed();
        }
        return 0;
    }

    private long getVoltage() {
        var rotorHolder = getRotorHolder();
        if (rotorHolder != null && rotorHolder.hasRotor()) {
            return baseEUOutput * rotorHolder.getTotalPower() * (highSpeedMode ? 3 : 1L) / 100;
        }
        return 0;
    }

    //////////////////////////////////////
    // ****** Recipe Logic *******//
    //////////////////////////////////////
    @Nullable
    @Override
    protected GTRecipe getRealRecipe(GTRecipe recipe) {
        RotorHolderPartMachine rotorHolder = getRotorHolder();
        long EUt = RecipeHelper.getOutputEUt(recipe);
        if (rotorHolder == null || EUt <= 0) return null;
        int rotorSpeed = getRotorSpeed();
        if (rotorSpeed < 0) return null;
        int maxSpeed = rotorHolder.getMaxRotorHolderSpeed();
        long turbineMaxVoltage = (long) (getVoltage() * Math.pow((double) Math.min(maxSpeed, rotorSpeed) / maxSpeed, 2));
        recipe = GTORecipeModifiers.accurateParallel(this, recipe, (int) (turbineMaxVoltage / EUt));
        long eut = Math.min(turbineMaxVoltage, recipe.parallels * EUt);
        energyPerTick = eut;
        recipe.duration = recipe.duration * rotorHolder.getTotalEfficiency() / 100;
        recipe.tickOutputs.put(EURecipeCapability.CAP, List.of(ContentBuilder.builderLong(eut)));
        return recipe;
    }

    @Override
    public boolean canVoidRecipeOutputs(RecipeCapability<?> capability) {
        return capability != EURecipeCapability.CAP;
    }

    //////////////////////////////////////
    // ******* GUI ********//
    //////////////////////////////////////

    @Override
    public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
        super.attachConfigurators(configuratorPanel);
        configuratorPanel.attachConfigurators(new IFancyConfiguratorButton.Toggle(
                GTOGuiTextures.HIGH_SPEED_MODE.getSubTexture(0, 0.5, 1, 0.5),
                GTOGuiTextures.HIGH_SPEED_MODE.getSubTexture(0, 0, 1, 0.5),
                () -> highSpeedMode, (clickData, pressed) -> {
                    for (RotorHolderPartMachine part : rotorHolderMachines) {
                        part.setRotorSpeed(0);
                    }
                    highSpeedMode = pressed;
                })
                .setTooltipsSupplier(pressed -> List.of(Component.translatable("gtocore.machine.mega_turbine.high_speed_mode").append("[").append(Component.translatable(pressed ? "gtocore.machine.on" : "gtocore.machine.off")).append("]"))));
    }

    @Override
    public void customText(List<Component> textList) {
        super.customText(textList);
        var rotorHolder = getRotorHolder();
        if (rotorHolder != null && rotorHolder.getRotorEfficiency() > 0) {
            textList.add(Component.translatable("gtceu.multiblock.turbine.rotor_speed", FormattingUtil.formatNumbers(getRotorSpeed() * (highSpeedMode ? 3 : 1)), FormattingUtil.formatNumbers(rotorHolder.getMaxRotorHolderSpeed() * (highSpeedMode ? 3 : 1))));
            textList.add(Component.translatable("gtceu.multiblock.turbine.efficiency", rotorHolder.getTotalEfficiency()));
            if (isActive()) {
                String voltageName = GTValues.VNF[GTUtil.getTierByVoltage(energyPerTick)];
                textList.add(3, Component.translatable("gtceu.multiblock.turbine.energy_per_tick",
                        FormattingUtil.formatNumbers(energyPerTick), voltageName));
            }
            if (!mega) {
                int rotorDurability = rotorHolder.getRotorDurabilityPercent();
                if (rotorDurability > 10) {
                    textList.add(Component.translatable("gtceu.multiblock.turbine.rotor_durability", rotorDurability));
                } else {
                    textList.add(Component.translatable("gtceu.multiblock.turbine.rotor_durability", rotorDurability)
                            .setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
                }
            }
        }
    }
}
