package com.gtocore.common.machine.multiblock.generator;

import com.gtocore.common.data.GTORecipeTypes;

import com.gtolib.api.annotation.Scanned;
import com.gtolib.api.annotation.dynamic.DynamicInitialValue;
import com.gtolib.api.annotation.dynamic.DynamicInitialValueTypes;
import com.gtolib.api.capability.IExtendWirelessEnergyContainerHolder;
import com.gtolib.api.machine.feature.multiblock.IArrayMachine;
import com.gtolib.api.machine.multiblock.StorageMultiblockMachine;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeRunner;
import com.gtolib.api.recipe.RecipeType;
import com.gtolib.api.recipe.modifier.ParallelLogic;
import com.gtolib.utils.GTOUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.item.MetaMachineItem;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import com.hepdd.gtmthings.api.misc.WirelessEnergyContainer;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.annotation.ParametersAreNonnullByDefault;

@Scanned
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class GeneratorArrayMachine extends StorageMultiblockMachine implements IArrayMachine, IExtendWirelessEnergyContainerHolder {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(GeneratorArrayMachine.class, StorageMultiblockMachine.MANAGED_FIELD_HOLDER);
    @DynamicInitialValue(key = "generator_array.multiply", simpleValue = "2", normalValue = "1.3", expertValue = "1.3", typeKey = DynamicInitialValueTypes.KEY_MULTIPLY, cn = "发电阵列乘数", cnComment = """
            发电阵列的功率奖励乘数，影响每个发电机的输出功率。
            数值越大，发电机的输出功率越高。
            此值仅与难度挂钩，代表不同难度下的发电机效率。""", en = "Generator Array Multiply", enComment = """
            The power multiplier bonus of the generator array, which affects the output power of each generator.
            The larger the value, the higher the output power of the generator.
            This value is only related to difficulty, representing the efficiency of generators at different difficulty levels.""")
    private static double multiply;

    @DynamicInitialValue(key = "generator_array.loss", typeKey = DynamicInitialValueTypes.KEY_MULTIPLY, simpleValue = "4", normalValue = "5", expertValue = "8", cn = "发电阵列无线模式损耗 : 0.0%s", cnComment = """
            发电阵列在无线模式下的损耗，影响传输到无线网络的能量损失。
            数值越大，连接无线网络的损耗越大。""", en = "Generator Array Wireless Loss : 0.0%s", enComment = """
            The loss of the generator array in wireless mode, which affects the loss of energy transferred to the wireless network.
            The larger the value, the greater the connection loss to the wireless network.""")
    private static int f_loss;
    @DynamicInitialValue(key = "generator_array.limit", simpleValue = "16", normalValue = "4", expertValue = "4", cn = "发电阵列内部发电机限制", cnComment = """
            发电阵列发电量和消耗量取决于内部发电机种类和个数
            内部发电机个数越多，其发电量和消耗量越高。
            例如：放4个蒸汽发电机，发电量为(4*发电阵列乘数*蒸汽发电机的发电量)，""", en = "Generator Array Internal Generator Limit", enComment = """
            The power generation and consumption of the generator array depend on the types and number of internal generators.
            The more internal generators, the higher the power generation and consumption.
            For example: placing 4 steam generators will result in a power generation of (4 * generator array multiplier * steam generator's power generation).""")
    private static int generatorLimit = 16;
    private WirelessEnergyContainer WirelessEnergyContainerCache;
    private MachineDefinition machineDefinitionCache;
    private GTRecipeType[] RecipeTypeCache;
    @Persisted
    private boolean isw;
    @Persisted
    private long eut;

    private static boolean isEligibleRecipeType(GTRecipeType type) {
        return Wrapper.ELIGIBLE_RECIPE_TYPES.contains(type);
    }

    public GeneratorArrayMachine(MetaMachineBlockEntity holder) {
        super(holder, generatorLimit, GeneratorArrayMachine::filter);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    private static boolean filter(ItemStack itemStack) {
        if (itemStack.getItem() instanceof MetaMachineItem metaMachineItem) {
            MachineDefinition definition = metaMachineItem.getDefinition();
            if (definition instanceof MultiblockMachineDefinition) {
                return false;
            }
            var recipeTypes = definition.getRecipeTypes();
            if (recipeTypes == null) {
                return false;
            }
            for (GTRecipeType type : recipeTypes) {
                if (isEligibleRecipeType(type)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public GTRecipeType[] getRecipeTypes() {
        return recipeTypes();
    }

    @NotNull
    @Override
    public RecipeType getRecipeType() {
        return (RecipeType) recipeTypes()[getActiveRecipeType()];
    }

    @Override
    public void onMachineChanged() {
        onStorageChanged();
    }

    @Override
    public int getTier() {
        MachineDefinition definition = getMachineDefinition();
        int definitionTier = definition == null ? 0 : definition.getTier();
        if (isw) {
            return definitionTier;
        }
        return Math.min(definitionTier, tier);
    }

    @Override
    public boolean onWorking() {
        if (!super.onWorking()) return false;
        if (isw) {
            if (eut > 0) {
                var container = getWirelessEnergyContainer();
                if (container != null) {
                    int loss = container.getLoss();
                    container.setLoss(loss + f_loss * 10);
                    container.addEnergy(eut, this);
                    container.setLoss(loss);
                }
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean canVoidRecipeOutputs(RecipeCapability<?> capability) {
        return true;
    }

    @Override
    protected boolean beforeWorking(@Nullable Recipe recipe) {
        if (isEmpty()) return false;
        return super.beforeWorking(recipe);
    }

    @Nullable
    @Override
    protected Recipe getRealRecipe(Recipe recipe) {
        int a = machineStorage.storage.getStackInSlot(0).getCount();
        if (a > 0) {
            long EUt = recipe.getOutputEUt();
            if (EUt > 0) {
                recipe.outputs.clear();
                long paralle = ParallelLogic.getInputFluidParallel(this, getCurrentHandlerList(), recipe.getInputContents(FluidRecipeCapability.CAP), (int) (multiply * GTValues.V[getOverclockTier()] * a * GTOUtils.getGeneratorAmperage(getTier()) / EUt));
                if (paralle == 0) return null;
                recipe.modifier(ContentModifier.multiplier(paralle), true);
                recipe.duration = recipe.duration * GTOUtils.getGeneratorEfficiency(getRecipeType(), getTier()) / 100;
                if (isw) {
                    recipe.setOutputEUt(0);
                    eut = EUt * paralle;
                }
                return recipe;
            }
        }
        return null;
    }

    @Override
    public void customText(List<Component> textList) {
        super.customText(textList);
        textList.add(Component.translatable("gtocore.machine.generator_array.wireless").append(ComponentPanelWidget.withButton(Component.literal("[").append(isw ? Component.translatable("gtocore.machine.on") : Component.translatable("gtocore.machine.off")).append(Component.literal("]")), "wireless_switch")));
        if (isActive() && isw) {
            GTRecipe r = getRecipeLogic().getLastRecipe();
            if (r != null) {
                textList.add(Component.translatable("gtceu.multiblock.max_energy_per_tick", FormattingUtil.formatNumbers(eut), Component.literal(GTValues.VNF[GTUtil.getFloorTierByVoltage(eut)])));
            }
        }
    }

    @Override
    public void handleDisplayClick(String componentData, ClickData clickData) {
        if (!clickData.isRemote) {
            if ("wireless_switch".equals(componentData)) {
                isw = !isw;
                eut = 0;
                getRecipeLogic().markLastRecipeDirty();
            } else super.handleDisplayClick(componentData, clickData);
        }
    }

    @Override
    public Item getStorageItem() {
        return getStorageStack().getItem();
    }

    @Override
    @Nullable
    public UUID getUUID() {
        return getOwnerUUID();
    }

    @Override
    public boolean matchRecipe(Recipe recipe) {
        return RecipeRunner.matchRecipeInput(this, recipe);
    }

    @Override
    public boolean matchTickRecipe(Recipe recipe) {
        return isw || super.matchTickRecipe(recipe);
    }

    private static class Wrapper {

        private static final Set<GTRecipeType> ELIGIBLE_RECIPE_TYPES = Set.of(GTRecipeTypes.STEAM_TURBINE_FUELS, GTRecipeTypes.GAS_TURBINE_FUELS, GTRecipeTypes.COMBUSTION_GENERATOR_FUELS, GTORecipeTypes.SEMI_FLUID_GENERATOR_FUELS, GTORecipeTypes.ROCKET_ENGINE_FUELS, GTORecipeTypes.NAQUADAH_REACTOR);
    }

    public static double getMultiply() {
        return GeneratorArrayMachine.multiply;
    }

    @Override
    public void setWirelessEnergyContainerCache(final WirelessEnergyContainer WirelessEnergyContainerCache) {
        this.WirelessEnergyContainerCache = WirelessEnergyContainerCache;
    }

    @Override
    public WirelessEnergyContainer getWirelessEnergyContainerCache() {
        return this.WirelessEnergyContainerCache;
    }

    @Override
    public void setMachineDefinitionCache(final MachineDefinition machineDefinitionCache) {
        this.machineDefinitionCache = machineDefinitionCache;
    }

    @Override
    public MachineDefinition getMachineDefinitionCache() {
        return this.machineDefinitionCache;
    }

    @Override
    public void setRecipeTypeCache(final GTRecipeType[] RecipeTypeCache) {
        this.RecipeTypeCache = RecipeTypeCache;
    }

    @Override
    public GTRecipeType[] getRecipeTypeCache() {
        return this.RecipeTypeCache;
    }
}
