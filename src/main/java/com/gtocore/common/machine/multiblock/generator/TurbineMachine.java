package com.gtocore.common.machine.multiblock.generator;

import com.gtolib.api.annotation.Scanned;
import com.gtolib.api.annotation.dynamic.DynamicInitialValue;
import com.gtolib.api.annotation.dynamic.DynamicInitialValueTypes;
import com.gtolib.api.gui.GTOGuiTextures;
import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;
import com.gtolib.api.machine.part.ItemHatchPartMachine;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.modifier.ParallelLogic;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfiguratorButton;
import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMaintenanceMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
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
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@Scanned
public final class TurbineMachine extends ElectricMultiblockMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(TurbineMachine.class, ElectricMultiblockMachine.MANAGED_FIELD_HOLDER);
    @DynamicInitialValue(key = "gtocore.machine.mega_turbine.high_speed_mode_output_multiplier", typeKey = DynamicInitialValueTypes.KEY_MULTIPLY, simpleValue = "4.0F", normalValue = "3.0F", expertValue = "2.5F", cn = "高速模式输出倍率 : %s 倍", cnComment = """
            高速模式下，涡轮机的运行速度倍率，发电量也会随之提升
            或许你有时候会需要更高的发电量，亦可能你有无限的资源来维持涡轮机的高速运转？""", en = "High Speed Mode Output Multiplier : %s Multiplier", enComment = """
            In high speed mode, the turbine's running speed multiplier increases, and the power generation will also increase.
            Perhaps you sometimes need higher power generation, or you have unlimited resources to maintain the turbine's high-speed operation?""")
    private static float highSpeedModeOutputMultiplier = 3.0F;
    @DynamicInitialValue(key = "gtocore.machine.mega_turbine.high_speed_mode_rotor_damage_multiplier", typeKey = DynamicInitialValueTypes.KEY_MULTIPLY, simpleValue = "4", normalValue = "10", expertValue = "12", cn = "高速模式转子损坏倍率 : %s 倍", cnComment = """
            高速模式下，涡轮机转子损坏倍率提升
            这意味着在高速模式下，转子会更快地损坏，你需要更频繁地维护它们。""", en = "High Speed Mode Rotor Damage Multiplier : %s Multiplier", enComment = """
            In high speed mode, the turbine rotor damage multiplier increases.
            This means that in high speed mode, the rotor will be damaged faster, and you need to maintain them more frequently.""")
    private static int highSpeedModeRotorDamageMultiplier = 10;
    @DynamicInitialValue(key = "gtocore.machine.mega_turbine.high_speed_mode_machine_fault", typeKey = DynamicInitialValueTypes.KEY_MULTIPLY, simpleValue = "4F", normalValue = "8F", expertValue = "10F", cn = "高速模式机器故障倍率 : %s 倍", cnComment = """
            高速模式下，涡轮机机器故障倍率提升
            这意味着在高速模式下，机器更容易发生故障，你需要更频繁地检查它们。""", en = "High Speed Mode Machine Fault Multiplier : %s Multiplier", enComment = """
            In high speed mode, the turbine machine fault multiplier increases.
            This means that in high speed mode, the machine is more likely to malfunction, and you need to check them more frequently.""")
    private static float highSpeedModeMachineFault = 8.0F;

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    private final long baseEUOutput;
    private final int tier;
    private final boolean mega;
    private long energyPerTick;
    @Persisted
    private boolean highSpeedMode;
    private final List<RotorHolderPartMachine> rotorHolderMachines = new ObjectArrayList<>();
    private ItemHatchPartMachine rotorHatchPartMachine;
    private final ConditionalSubscriptionHandler rotorSubs;

    public TurbineMachine(MetaMachineBlockEntity holder, int tier, boolean special, boolean mega) {
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
    public boolean matchRecipe(Recipe recipe) {
        for (RotorHolderPartMachine part : rotorHolderMachines) {
            if (part.getRotorStack().isEmpty()) return false;
        }
        return super.matchRecipe(recipe);
    }

    @Override
    public void onPartScan(IMultiPart part) {
        super.onPartScan(part);
        if (part instanceof RotorHolderPartMachine rotorHolderMachine) {
            rotorHolderMachines.add(rotorHolderMachine);
            rotorHolderMachine.inventory.addChangedListener(rotorSubs::updateSubscription);
        } else if (rotorHatchPartMachine == null && part instanceof ItemHatchPartMachine rotorHatchPart) {
            rotorHatchPartMachine = rotorHatchPart;
            rotorHatchPartMachine.getInventory().addChangedListener(rotorSubs::updateSubscription);
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
                part.damageRotor(Math.max(highSpeedModeRotorDamageMultiplier - 1, 0));
            }
        }
        return super.onWorking();
    }

    @Override
    public void afterWorking() {
        energyPerTick = 0;
        for (IMultiPart part : getParts()) {
            if (highSpeedMode && part instanceof IMaintenanceMachine maintenanceMachine) {
                maintenanceMachine.calculateMaintenance(maintenanceMachine, (int) (highSpeedModeMachineFault * getRecipeLogic().getProgress()));
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
            return material.size() == 1 ? (speed / 12) : -1;
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
            return (long) (baseEUOutput * rotorHolder.getTotalPower() * (highSpeedMode ? highSpeedModeOutputMultiplier : 1L) / 100);
        }
        return 0;
    }

    //////////////////////////////////////
    // ****** Recipe Logic *******//
    //////////////////////////////////////
    @Nullable
    @Override
    protected Recipe getRealRecipe(Recipe recipe) {
        RotorHolderPartMachine rotorHolder = getRotorHolder();
        long EUt = recipe.getOutputEUt();
        if (rotorHolder == null || EUt <= 0) return null;
        int rotorSpeed = getRotorSpeed();
        if (rotorSpeed < 0) return null;
        int maxSpeed = rotorHolder.getMaxRotorHolderSpeed();
        long turbineMaxVoltage = (long) (getVoltage() * Math.pow((double) Math.min(maxSpeed, rotorSpeed) / maxSpeed, 2));
        recipe = ParallelLogic.accurateParallel(this, recipe, (int) (turbineMaxVoltage / EUt));
        if (recipe == null) return null;
        long eut = Math.min(turbineMaxVoltage, recipe.getParallels() * EUt);
        energyPerTick = eut;
        recipe.duration = recipe.duration * rotorHolder.getTotalEfficiency() / 100;
        recipe.setOutputEUt(eut);
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
        configuratorPanel.attachConfigurators(new IFancyConfiguratorButton.Toggle(GTOGuiTextures.HIGH_SPEED_MODE.getSubTexture(0, 0.5, 1, 0.5), GTOGuiTextures.HIGH_SPEED_MODE.getSubTexture(0, 0, 1, 0.5), () -> highSpeedMode, (clickData, pressed) -> {
            for (RotorHolderPartMachine part : rotorHolderMachines) {
                part.setRotorSpeed(0);
            }
            highSpeedMode = pressed;
        }).setTooltipsSupplier(pressed -> List.of(Component.translatable("gtocore.machine.mega_turbine.high_speed_mode").append("[").append(Component.translatable(pressed ? "gtocore.machine.on" : "gtocore.machine.off")).append("]"))));
    }

    @Override
    public void customText(List<Component> textList) {
        super.customText(textList);
        var rotorHolder = getRotorHolder();
        if (rotorHolder != null && rotorHolder.getRotorEfficiency() > 0) {
            textList.add(Component.translatable("gtceu.multiblock.turbine.rotor_speed", FormattingUtil.formatNumbers(getRotorSpeed() * (highSpeedMode ? highSpeedModeOutputMultiplier : 1)), FormattingUtil.formatNumbers(rotorHolder.getMaxRotorHolderSpeed() * (highSpeedMode ? highSpeedModeOutputMultiplier : 1))));
            textList.add(Component.translatable("gtceu.multiblock.turbine.efficiency", rotorHolder.getTotalEfficiency()));
            if (isActive()) {
                String voltageName = GTValues.VNF[GTUtil.getTierByVoltage(energyPerTick)];
                textList.add(3, Component.translatable("gtceu.multiblock.turbine.energy_per_tick", FormattingUtil.formatNumbers(energyPerTick), voltageName));
            }
            if (!mega) {
                int rotorDurability = rotorHolder.getRotorDurabilityPercent();
                if (rotorDurability > 10) {
                    textList.add(Component.translatable("gtceu.multiblock.turbine.rotor_durability", rotorDurability));
                } else {
                    textList.add(Component.translatable("gtceu.multiblock.turbine.rotor_durability", rotorDurability).setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
                }
            }
        }
    }

    @Override
    public int getTier() {
        return this.tier;
    }
}
