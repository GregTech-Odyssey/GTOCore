package com.gtocore.common.machine.multiblock.generator;

import com.gtocore.api.gui.GTOGuiTextures;

import com.gtolib.GTOCore;
import com.gtolib.api.annotation.Scanned;
import com.gtolib.api.annotation.dynamic.DynamicInitialValue;
import com.gtolib.api.annotation.dynamic.DynamicInitialValueTypes;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.api.machine.feature.multiblock.ITierCasingMachine;
import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;
import com.gtolib.api.machine.part.ItemPartMachine;
import com.gtolib.api.machine.trait.CoilTrait;
import com.gtolib.api.machine.trait.TierCasingTrait;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.modifier.ParallelLogic;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.block.ICoilType;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfigurator;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfiguratorButton;
import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.ICoilMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMaintenanceMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IWorkableMultiPart;
import com.gregtechceu.gtceu.common.item.TurbineRotorBehaviour;
import com.gregtechceu.gtceu.common.machine.multiblock.part.RotorHolderPartMachine;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;

import com.fast.fastcollection.OpenCacheHashSet;
import com.hepdd.gtmthings.utils.FormatUtil;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.gtolib.api.GTOValues.GLASS_TIER;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@Scanned
public class TurbineMachine extends ElectricMultiblockMachine {

    @DynamicInitialValue(key = "gtocore.machine.mega_turbine.high_speed_mode_output_multiplier", typeKey = DynamicInitialValueTypes.KEY_MULTIPLY, easyValue = "4.0F", normalValue = "3.0F", expertValue = "2.5F", cn = "高速模式输出倍率 : %s 倍", en = "High Speed Mode Output Multiplier : %s Multiplier")
    private static float highSpeedModeOutputMultiplier = 3.0F;
    @DynamicInitialValue(key = "gtocore.machine.mega_turbine.high_speed_mode_rotor_damage_multiplier", typeKey = DynamicInitialValueTypes.KEY_MULTIPLY, easyValue = "4", normalValue = "10", expertValue = "12", cn = "高速模式转子损坏倍率 : %s 倍", en = "High Speed Mode Rotor Damage Multiplier : %s Multiplier")
    private static int highSpeedModeRotorDamageMultiplier = 10;
    @DynamicInitialValue(key = "gtocore.machine.mega_turbine.high_speed_mode_machine_fault", typeKey = DynamicInitialValueTypes.KEY_MULTIPLY, easyValue = "4F", normalValue = "8F", expertValue = "10F", cn = "高速模式机器故障倍率 : %s 倍", en = "High Speed Mode Machine Fault Multiplier : %s Multiplier")
    private static float highSpeedModeMachineFault = 8.0F;

    private final long baseEUOutput;
    private final int tier;
    private final boolean mega;
    private long energyPerTick;
    @Persisted
    private boolean highSpeedMode;
    @Persisted
    private float highSpeedFactor = 1.0f;
    final List<RotorHolderPartMachine> rotorHolderMachines = new ArrayList<>();
    private ItemPartMachine rotorHatchPartMachine;
    private final ConditionalSubscriptionHandler rotorSubs;

    private double extraOutput = 1;
    private double extraDamage = 1;
    private double extraEfficiency = 1;
    double damageBase = 2.2;
    private float accumulatedDamage = 0;

    public TurbineMachine(MetaMachineBlockEntity holder, int tier, boolean special, boolean mega) {
        super(holder);
        this.mega = mega;
        this.tier = tier;
        baseEUOutput = (long) (GTValues.V[tier] * (mega ? 4 : 1) * (special ? 2.5 : 2));
        rotorSubs = new ConditionalSubscriptionHandler(this, this::rotorUpdate, 20, () -> rotorHatchPartMachine != null);
    }

    private void rotorUpdate() {
        if (!isActive()) {
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
        } else if (rotorHatchPartMachine == null && part instanceof ItemPartMachine rotorHatchPart) {
            rotorHatchPartMachine = rotorHatchPart;
            traitSubscriptions.add(rotorHatchPartMachine.getInventory().addChangedListener(rotorSubs::updateSubscription));
        }
    }

    @Override
    public void onStructureFormed() {
        rotorHolderMachines.clear();
        super.onStructureFormed();
        if (mega) {
            rotorSubs.initialize(getLevel());
            if (GTOCore.isExpert() && this instanceof MegaTurbine) {
                damageBase = Math.max(2.2 - 0.08 * ((MegaTurbine) this).getCasingTier(GLASS_TIER), 1.2);
            }
        }
        if (formedAmount > 0) {
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
        damageBase = 2.0;
    }

    @Override
    public boolean onWorking() {
        if (highSpeedMode && getOffsetTimer() % 20 == 0) {
            accumulatedDamage += getHighSpeedModeDamageMultiplier();
            if (accumulatedDamage >= 1) {
                int damageToApply = (int) accumulatedDamage;
                accumulatedDamage -= damageToApply;
                for (RotorHolderPartMachine part : rotorHolderMachines) {
                    part.damageRotor(damageToApply);
                }
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
            if (part instanceof IWorkableMultiPart workableMultiPart) workableMultiPart.afterWorking(this);
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
            Set<Material> material = new OpenCacheHashSet<>(2);
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
            return (long) (baseEUOutput * rotorHolder.getTotalPower() * (highSpeedMode ? getHighSpeedModeOutputMultiplier() : 1L) / 100 * extraOutput);
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
        long turbineMaxVoltage = Math.min(getOverclockVoltage(), (long) (getVoltage() * Math.pow((double) Math.min(maxSpeed, rotorSpeed) / maxSpeed, 2)));
        recipe = ParallelLogic.accurateContentParallel(this, recipe, turbineMaxVoltage / EUt);
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

        if (mega && GTOCore.isExpert()) {
            configuratorPanel.attachConfigurators(new IFancyConfigurator() {

                @Override
                public Component getTitle() {
                    return Component.translatable(ADJUSTMENT1);
                }

                @Override
                public List<Component> getTooltips() {
                    return List.of(
                            Component.translatable(DESC1),
                            Component.translatable(DESC2),
                            Component.translatable(DESC3),
                            Component.translatable(DESC4),
                            Component.translatable(DESC5));
                }

                @Override
                public IGuiTexture getIcon() {
                    return GTOGuiTextures.PARALLEL_CONFIG;
                }

                @Override
                public Widget createConfigurator() {
                    return gtolib$configPanelWidget();
                }
            });
        }
    }

    private Widget gtolib$configPanelWidget() {
        WidgetGroup group = new WidgetGroup(0, 0, 100, 20);
        var panelWidget = new ComponentPanelWidget(0, 0, list -> {
            MutableComponent buttonText = Component.translatable(ADJUST);
            buttonText.append(" ");
            if (getRotorSpeed() == 0) {
                buttonText.append(ComponentPanelWidget.withButton(Component.literal("[-]").withStyle(ChatFormatting.RED),
                        "sub"));
                buttonText.append(" ");
                buttonText.append(ComponentPanelWidget.withButton(Component.literal("[+]").withStyle(ChatFormatting.GREEN),
                        "add"));
                buttonText.append(" ");
                buttonText.append(ComponentPanelWidget.withButton(Component.literal("[o]").withStyle(ChatFormatting.GREEN),
                        "reset"));
            } else {
                buttonText.append(Component.translatable("ars_nouveau.locked").withStyle(ChatFormatting.RED));
            }
            list.add(buttonText.setStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    Component.translatable(ADJUSTMENT2, String.format("%.2f", getHighSpeedModeOutputMultiplier()))
                            .append(" ").append(Component.translatable(ADJUSTMENT3, String.format("%.2f", getHighSpeedModeDamageMultiplier())))))));
        }).setMaxWidthLimit(150 - 8 - 8 - 4).clickHandler((componentData, clickData) -> {
            if (!clickData.isRemote) {
                if ("reset".equals(componentData)) {
                    highSpeedFactor = 1.0f;
                    return;
                }
                float multiplier = 0.01f;
                multiplier *= clickData.isShiftClick ? 10 : 1;
                multiplier *= clickData.isCtrlClick ? 100 : 1;
                if ("sub".equals(componentData)) {
                    onSub(multiplier);
                } else if ("add".equals(componentData)) {
                    onAdd(multiplier);
                }
            }
        });
        return group.addWidget(panelWidget);
    }

    private void onSub(float multiplier) {
        highSpeedFactor = Math.max(0.1f, highSpeedFactor - multiplier);
    }

    private void onAdd(float multiplier) {
        highSpeedFactor = Math.min(5f, highSpeedFactor + multiplier);
    }

    @Override
    public void customText(List<Component> textList) {
        super.customText(textList);
        var v = getVoltage();
        textList.add(Component.translatable(ESTIMATED_MAX_OUTPUT, FormattingUtil.formatNumbers(v))
                .setStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        Component.empty()
                                .append(Component.literal(FormatUtil.voltageAmperage(BigDecimal.valueOf(v)).toEngineeringString()).append("A "))
                                .append(FormatUtil.voltageName(BigDecimal.valueOf(v)))))));
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

    private float getHighSpeedModeOutputMultiplier() {
        if (!GTOCore.isExpert()) {
            return highSpeedModeOutputMultiplier;
        }
        return highSpeedModeOutputMultiplier * highSpeedFactor;
    }

    private float getHighSpeedModeDamageMultiplier() {
        if (!GTOCore.isExpert()) {
            return highSpeedModeRotorDamageMultiplier;
        }
        return Math.max(1f, (float) (highSpeedModeRotorDamageMultiplier * Math.pow(damageBase, highSpeedFactor - 1)));
    }

    public static class MegaTurbine extends TurbineMachine implements ICoilMachine, ITierCasingMachine {

        private final CoilTrait coilTrait;
        private final TierCasingTrait tierCasingTrait;
        private float workAccumulation = 0;

        public MegaTurbine(MetaMachineBlockEntity holder, int tier, boolean special) {
            super(holder, tier, special, true);
            coilTrait = new CoilTrait(this, false, false);
            this.tierCasingTrait = new TierCasingTrait(this, GLASS_TIER);
        }

        @Override
        public boolean onWorking() {
            if (getCoilTier() > 0) {
                this.workAccumulation += getCoilTier() * 1.25f + 4;
                int addition = (int) Math.floor(this.workAccumulation);
                this.workAccumulation -= addition;
                for (var part : this.rotorHolderMachines) {
                    part.setRotorSpeed(Math.min(part.getRotorSpeed() + addition, part.getMaxRotorHolderSpeed()));
                }
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
            if (GTOCore.isExpert())
                textList.add(Component.translatable(GLASS_BONUS, getCasingTier(GLASS_TIER), FormattingUtil.formatNumber2Places(damageBase)));
        }

        @Override
        public Object2IntMap<String> getCasingTiers() {
            return tierCasingTrait.getCasingTiers();
        }
    }

    @RegisterLanguage(cn = "线圈等级: %s，转子启动增速 %s%%", en = "Coil Tier: %s, Rotor Launch Speed Bonus %s%%")
    public static final String COIL_BONUS = "gtocore.machine.mega_turbine.coil_tier";
    @RegisterLanguage(cn = "玻璃等级: %s，转子损坏倍率乘数：%s", en = "Glass Tier: %s, Rotor Damage Multiplier Bonus: %s")
    public static final String GLASS_BONUS = "gtocore.machine.mega_turbine.glass_tier";
    @RegisterLanguage(cn = "高速模式倍率调节：", en = "High Speed Mode Multiplier Adjustment:")
    public static final String ADJUSTMENT1 = "gtocore.machine.mega_turbine.expert.adjustment.1";
    @RegisterLanguage(cn = "输出：%sx", en = "Output EU: %sx")
    public static final String ADJUSTMENT2 = "gtocore.machine.mega_turbine.expert.adjustment.2";
    @RegisterLanguage(cn = "损坏：%sx", en = "Damage: %sx")
    public static final String ADJUSTMENT3 = "gtocore.machine.mega_turbine.expert.adjustment.3";
    @RegisterLanguage(cn = "预计最大输出：%s EU/t", en = "Estimated Max Output: %s EU/t")
    public static final String ESTIMATED_MAX_OUTPUT = "gtocore.machine.mega_turbine.expert.estimated_max_output";
    @RegisterLanguage(cn = "专家模式下，允许调节高速模式下的输出倍率。", en = "In Expert Mode, allows adjustment of the output multiplier in High Speed Mode.")
    public static final String DESC1 = "gtocore.machine.mega_turbine.expert.desc.1";
    @RegisterLanguage(cn = "不过，调节输出倍率会同时大幅牺牲转子寿命。", en = "However, adjusting the output multiplier will also significantly sacrifice rotor durability.")
    public static final String DESC2 = "gtocore.machine.mega_turbine.expert.desc.2";
    @RegisterLanguage(cn = "调节范围：0.1 倍 - 5 倍。", en = "Adjustment Range: 0.1x - 5x.")
    public static final String DESC3 = "gtocore.machine.mega_turbine.expert.desc.3";
    @RegisterLanguage(cn = "公式：输出倍率 = 基础倍率 x 调节倍率", en = "Formula: Output Multiplier = Base Multiplier x Adjustment Multiplier")
    public static final String DESC4 = "gtocore.machine.mega_turbine.expert.desc.4";
    @RegisterLanguage(cn = "转子损坏倍率 = 基础倍率 x max(2.2 - 0.08 * 玻璃等级, 1.2) ^ (调节倍率 - 1)", en = "Rotor Damage Multiplier = Base Multiplier x max(2.2 - 0.08 * Coil Tier, 1.2) ^ (Adjustment Multiplier - 1)")
    public static final String DESC5 = "gtocore.machine.mega_turbine.expert.desc.5";
    @RegisterLanguage(cn = "调节：", en = "Adjustment: ")
    public static final String ADJUST = "gtocore.machine.mega_turbine.expert.adjust";
}
