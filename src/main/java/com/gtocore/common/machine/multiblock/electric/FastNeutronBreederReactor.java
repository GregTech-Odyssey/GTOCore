package com.gtocore.common.machine.multiblock.electric;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMachines;
import com.gtocore.common.data.GTOMaterials;
import com.gtocore.common.machine.multiblock.part.SensorPartMachine;

import com.gtolib.api.machine.feature.multiblock.IStorageMultiblock;
import com.gtolib.api.machine.multiblock.CustomParallelMultiblockMachine;
import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;
import com.gtolib.api.machine.trait.IEnhancedRecipeLogic;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.utils.GTOUtils;
import com.gtolib.utils.MachineUtils;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.feature.IExplosionMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

import com.google.common.collect.ImmutableMap;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class FastNeutronBreederReactor extends CustomParallelMultiblockMachine implements IStorageMultiblock, IExplosionMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(FastNeutronBreederReactor.class, ElectricMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Persisted
    private final NotifiableItemStackHandler machineStorage;
    @Persisted
    private float temperature = 298;
    @Persisted
    private double neutronFluxkeV = 0;
    @Persisted
    private double recipeHeat = 0;
    private SensorPartMachine sensorMachineTemp;
    private SensorPartMachine sensorNeutronFlux;

    private static final int MAX_TEMPERATURE = 2098;

    @Override
    public @NotNull ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    public FastNeutronBreederReactor(MetaMachineBlockEntity holder) {
        super(holder, true, h -> 2048);
        machineStorage = createMachineStorage(i -> i.getItem() == GTItems.NEUTRON_REFLECTOR.asItem());
    }

    @Override
    public NotifiableItemStackHandler getMachineStorage() {
        return machineStorage;
    }

    /**
     * 配方时间：
     * 实际并行越大，运行时间越短，运行时间由以下公式决定：
     * ![图片](https://docimg9.docs.qq.com/image/AgAABYHqnSOmBcyCRC5BK6fF-dDm6Ub4.png?w=207&h=64)
     * 其中T 为最终运行时间，t 为配方运行时间，p 为实际并行数量；
     */
    @Nullable
    @Override
    protected Recipe getRealRecipe(@NotNull Recipe recipe) {
        if (recipe.data.contains("neutron_flux")) {
            var neededNeutronFlux = recipe.data.getFloat("neutron_flux");
            if (neutronFluxkeV < neededNeutronFlux) {
                ((IEnhancedRecipeLogic) getRecipeLogic()).gtolib$setIdleReason(Component.translatable("gtocore.idle_reason.neutron_kinetic_energy_not_satisfies"));
                return null;
            }
            recipe.parallels = Math.min(recipe.parallels, 2048);
            recipe.duration = getRecipeDuration(recipe, neededNeutronFlux);
            return recipe;
        }
        return super.getRealRecipe(recipe);
    }

    @Override
    public boolean beforeWorking(@Nullable Recipe recipe) {
        if (recipe == null) return true;
        recipeHeat = getRecipeHeat(recipe);
        return super.beforeWorking(recipe);
    }

    @Override
    public @NotNull Widget createUIWidget() {
        return IStorageMultiblock.super.createUIWidget(super.createUIWidget());
    }

    @Override
    public boolean onWorking() {
        if (getRecipeLogic().getLastRecipe() != null && getOffsetTimer() % 20 == 0) {
            var recipe = getRecipeLogic().getLastRecipe();
            var change = recipe.data.getFloat("neutron_flux_change");
            neutronFluxkeV = Math.max(0, neutronFluxkeV + change);
            var neededNeutronFlux = recipe.data.getFloat("neutron_flux");
            if (neutronFluxkeV < neededNeutronFlux) {
                ((IEnhancedRecipeLogic) getRecipeLogic()).gtolib$setIdleReason(Component.translatable("gtocore.idle_reason.neutron_kinetic_energy_not_satisfies"));
                return false;
            }
            recipeHeat = getRecipeHeat((Recipe) recipe);
        }
        return super.onWorking();
    }

    @Override
    public void afterWorking() {
        super.afterWorking();
        recipeHeat = 0;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        tickSubscription = subscribeServerTick(this::tick);
    }

    @Nullable
    TickableSubscription tickSubscription;

    @Override
    public void onUnload() {
        super.onUnload();
        if (tickSubscription != null) {
            tickSubscription.unsubscribe();
            tickSubscription = null;
        }
    }

    @Override
    public void onPartScan(@NotNull IMultiPart part) {
        super.onPartScan(part);
        if (part instanceof SensorPartMachine sensorPartMachine) {
            if (sensorPartMachine.getHolder().getBlockState().is(GTOMachines.HEAT_SENSOR.getBlock()))
                sensorMachineTemp = sensorPartMachine;
            else if (sensorPartMachine.getHolder().getBlockState().is(GTOMachines.NEUTRON_SENSOR.getBlock()))
                sensorNeutronFlux = sensorPartMachine;
        }
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        sensorMachineTemp = null;
        sensorNeutronFlux = null;
        recipeHeat = 0;
    }

    @Override
    public void customText(@NotNull List<Component> textList) {
        super.customText(textList);
        textList.add(Component.translatable("gtocore.machine.current_temperature", FormattingUtil.formatNumber2Places(temperature)));
        textList.add(Component.translatable("gtocore.machine.neutron_flux", FormattingUtil.formatNumber2Places(neutronFluxkeV)));
        textList.add(Component.translatable("gtocore.machine.temp.per_second", FormattingUtil.formatNumber2Places(recipeHeat)));
    }

    private double getRecipeHeat(Recipe recipe) {
        if (recipe.data.contains("heat")) {
            return (recipe.data.getFloat("heat") * 1.27 * Math.pow(neutronFluxkeV / 100d, 1.88));
        }
        return 0;
    }

    private int getRecipeDuration(Recipe recipe, double neededNeutronFlux) {
        double k = Math.max(0.9 - (neutronFluxkeV - neededNeutronFlux) / 1e5d, 0.1);
        return Math.max((int) (recipe.duration * Math.pow(k, Math.sqrt(recipe.parallels))), 1);
    }

    /**
     * 一、消耗中子源提供初始中子通量，锑-铍粒子源提供10 keV，钚-铍粒子源提供100 keV，锎-252粒子源提供1 MeV
     * <p>
     * 二、中子通量每秒减少 10 keV，放入小撮石墨粉，小堆石墨粉，石墨粉立即降低 0.1 MeV，0.25 MeV，1 MeV；
     * <p>
     * 三、中子通量为E（keV）时，在主机内放入N个铱中子反射板后，中子通量每秒增加 (EN)^0.5 keV；
     */
    private void tick() {
        if (isFormed() && getOffsetTimer() % 20 == 0) {

            MachineUtils.forEachInputItems(this, (stack, amount) -> {
                if (Wrapper.NEUTRON_SOURCES.containsKey(stack.getItem())) {
                    neutronFluxkeV += (long) Wrapper.NEUTRON_SOURCES.get(stack.getItem()) * amount;
                    inputItem(stack.copyWithCount((int) amount));
                }
                return false;
            });
            neutronFluxkeV = Math.max(0, neutronFluxkeV - 10);

            int reflectors = machineStorage.getStackInSlot(0).getCount();
            if (reflectors > 0 && neutronFluxkeV > 0) {
                neutronFluxkeV += (long) Math.sqrt(neutronFluxkeV * reflectors);
            }
            temperature += (float) recipeHeat;
            MachineUtils.forEachInputFluids(this, (stack, amount) -> {
                if (Wrapper.COOLANTS.containsKey(stack.getFluid())) {
                    temperature -= Wrapper.COOLANTS.get(stack.getFluid()) * amount;
                    var copy = stack.copy();
                    copy.setAmount((int) amount);
                    inputFluid(copy);
                }
                return false;
            });
            temperature = Math.max(298, temperature);
            if (temperature > MAX_TEMPERATURE) {
                meltDown();
            }
            if (sensorMachineTemp != null) {
                sensorMachineTemp.update(temperature);
            }
            if (sensorNeutronFlux != null) {
                sensorNeutronFlux.update((float) (neutronFluxkeV / 1000f)); // MeV
            }

        }
    }

    private void meltDown() {
        MachineUtils.outputItem(this, GTOItems.NUCLEAR_WASTE.asStack(1 + (int) (Math.random() * 4)));
        var machine = self();
        var level = machine.getLevel();
        var pos = machine.getPos().relative(machine.getFrontFacing().getOpposite(), 6);
        if (level != null) {
            for (int x = -2; x <= 2; x++) {
                for (int y = 0; y <= 20; y++) {
                    for (int z = -2; z <= 2; z++) {
                        GTOUtils.fastRemoveBlock(level, pos.offset(x, y, z), false, false);
                    }
                }
            }
            GTOUtils.fastRemoveBlock(level, machine.getPos(), false, false);
            doExplosion(20);
        }
    }

    private static class Wrapper {

        public static final Map<Item, Integer> NEUTRON_SOURCES;
        public static final Map<Fluid, Integer> COOLANTS;
        static {
            ImmutableMap.Builder<Item, Integer> builder = ImmutableMap.builder();
            builder.put(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Graphite).getItem(), -1000);
            builder.put(ChemicalHelper.get(TagPrefix.dustSmall, GTMaterials.Graphite).getItem(), -250);
            builder.put(ChemicalHelper.get(TagPrefix.dustTiny, GTMaterials.Graphite).getItem(), -100);
            builder.put(ChemicalHelper.get(GTOTagPrefix.PARTICLE_SOURCE, GTOMaterials.AntinomyBerylliumSource).getItem(), 10);
            builder.put(ChemicalHelper.get(GTOTagPrefix.PARTICLE_SOURCE, GTOMaterials.PlutoniumBerylliumSource).getItem(), 100);
            builder.put(ChemicalHelper.get(GTOTagPrefix.PARTICLE_SOURCE, GTOMaterials.Californium252Source).getItem(), 1000);
            NEUTRON_SOURCES = builder.build();
            ImmutableMap.Builder<Fluid, Integer> builder1 = ImmutableMap.builder();
            builder1.put(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID), 80);
            builder1.put(GTOMaterials.LiquidNitrogen.getFluid(), 4);
            builder1.put(GTMaterials.DistilledWater.getFluid(), 1);
            COOLANTS = builder1.build();
        }
    }
}
