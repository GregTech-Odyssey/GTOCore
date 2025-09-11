package com.gtocore.common.machine.multiblock.generator;

import com.gtolib.api.annotation.Scanned;
import com.gtolib.api.annotation.dynamic.DynamicInitialValue;
import com.gtolib.api.annotation.dynamic.DynamicInitialValueTypes;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.api.gui.GTOGuiTextures;
import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;
import com.gtolib.api.machine.part.ItemHatchPartMachine;
import com.gtolib.api.machine.trait.CoilTrait;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.modifier.ParallelLogic;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.block.ICoilType;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfiguratorButton;
import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.ICoilMachine;
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
public class TurbineMachine extends ElectricMultiblockMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(TurbineMachine.class, ElectricMultiblockMachine.MANAGED_FIELD_HOLDER);
    @DynamicInitialValue(key = "gtocore.machine.mega_turbine.high_speed_mode_output_multiplier", typeKey = DynamicInitialValueTypes.KEY_MULTIPLY, simpleValue = "4.0F", normalValue = "3.0F", expertValue = "2.5F", cn = "高速模式输出倍率 : %s 倍", en = "High Speed Mode Output Multiplier : %s Multiplier")
    private static float highSpeedModeOutputMultiplier = 3.0F;
    @DynamicInitialValue(key = "gtocore.machine.mega_turbine.high_speed_mode_rotor_damage_multiplier", typeKey = DynamicInitialValueTypes.KEY_MULTIPLY, simpleValue = "4", normalValue = "10", expertValue = "12", cn = "高速模式转子损坏倍率 : %s 倍", en = "High Speed Mode Rotor Damage Multiplier : %s Multiplier")
    private static int highSpeedModeRotorDamageMultiplier = 10;
    @DynamicInitialValue(key = "gtocore.machine.mega_turbine.high_speed_mode_machine_fault", typeKey = DynamicInitialValueTypes.KEY_MULTIPLY, simpleValue = "4F", normalValue = "8F", expertValue = "10F", cn = "高速模式机器故障倍率 : %s 倍", en = "High Speed Mode Machine Fault Multiplier : %s Multiplier")
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
    final List<RotorHolderPartMachine> rotorHolderMachines = new ObjectArrayList<>();
    private ItemHatchPartMachine rotorHatchPartMachine;
    private final ConditionalSubscriptionHandler rotorSubs;

    private double extraOutput = 1;
    private double extraDamage = 1;
    private double extraEfficiency = 1;

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
                    break;
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
            traitSubscriptions.add(rotorHolderMachine.inventory.addChangedListener(rotorSubs::updateSubscription));
        } else if (rotorHatchPartMachine == null && part instanceof ItemHatchPartMachine rotorHatchPart) {
            rotorHatchPartMachine = rotorHatchPart;
            traitSubscriptions.add(rotorHatchPartMachine.getInventory().addChangedListener(rotorSubs::updateSubscription));
        }
    }

    @Override
    public void onStructureFormed() {
        rotorHolderMachines.clear();
        super.onStructureFormed();
        if (mega) rotorSubs.initialize(getLevel());
        if (formedCount > 0) {
            if (mega) {
                extraOutput = 3;
                extraDamage = 3;
                extraEfficiency = 1.3;
            } else {
                extraOutput = 2;
                extraDamage = 2;
                extraEfficiency = 1.2;
            }
        }
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        rotorHolderMachines.clear();
        rotorHatchPartMachine = null;
        extraOutput = 1;
        extraDamage = 1;
        extraEfficiency = 1;
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
        var recipe = getRecipeLogic().getLastRecipe();
        for (IMultiPart part : getParts()) {
            if (highSpeedMode && recipe != null && part instanceof IMaintenanceMachine maintenanceMachine) {
                maintenanceMachine.calculateMaintenance(maintenanceMachine, (int) (highSpeedModeMachineFault * recipe.duration * extraDamage));
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
        long turbineMaxVoltage = (long) (getVoltage() * Math.pow((double) Math.min(maxSpeed, rotorSpeed) / maxSpeed, 2) * extraOutput);
        recipe = ParallelLogic.accurateParallel(this, recipe, (int) (turbineMaxVoltage / EUt));
        if (recipe == null) return null;
        long eut = Math.min(turbineMaxVoltage, recipe.parallels * EUt);
        energyPerTick = eut;
        recipe.duration = (int) (recipe.duration * rotorHolder.getTotalEfficiency() * extraEfficiency / 100);
        recipe.setOutputEUt(eut);
        return recipe;
    }

    @Override
    public boolean canVoidRecipeOutputs(RecipeCapability<?> capability) {
        return true;
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
            textList.add(Component.translatable("gtceu.multiblock.turbine.rotor_speed", FormattingUtil.formatNumbers(getRotorSpeed() * (highSpeedMode ? highSpeedModeOutputMultiplier : 1) * extraOutput), FormattingUtil.formatNumbers(rotorHolder.getMaxRotorHolderSpeed() * (highSpeedMode ? highSpeedModeOutputMultiplier : 1) * extraOutput)));
            textList.add(Component.translatable("gtceu.multiblock.turbine.efficiency", rotorHolder.getTotalEfficiency() * extraEfficiency));
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

    public static class MegaTurbine extends TurbineMachine implements ICoilMachine {

        private final CoilTrait coilTrait;
        private float workAccumulation = 0;

        public MegaTurbine(MetaMachineBlockEntity holder, int tier, boolean special) {
            super(holder, tier, special, true);
            coilTrait = new CoilTrait(this, false, false);
        }

        @Override
        public boolean onWorking() {
            this.workAccumulation += getCoilTier() / 5.0f;
            int addition = (int) Math.floor(this.workAccumulation);
            this.workAccumulation -= addition;
            for (var part : this.rotorHolderMachines) {
                part.setRotorSpeed(Math.min(part.getRotorSpeed() + addition, part.getMaxRotorHolderSpeed()));
            }
            return super.onWorking();
        }

        @Override
        public ICoilType getCoilType() {
            return coilTrait.getCoilType();
        }

        @Override
        public void customText(List<Component> textList) {
            super.customText(textList);
            textList.add(Component.translatable(COIL_BONUS, getCoilTier(), getCoilTier() * 20));
        }
    }

    @RegisterLanguage(cn = "线圈等级: %s，转子启动增速 %s%%", en = "Coil Tier: %s, Rotor Launch Speed Bonus %s%%")
    public static final String COIL_BONUS = "gtocore.machine.mega_turbine.coil_tier";
}
