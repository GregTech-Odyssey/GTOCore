package com.gto.gtocore.common.machine.multiblock.noenergy;

import com.gto.gtocore.api.machine.feature.IInfinityEnergyMachine;
import com.gto.gtocore.api.machine.multiblock.NoEnergyMultiblockMachine;
import com.gto.gtocore.common.machine.multiblock.part.SensorPartMachine;
import com.gto.gtocore.utils.MachineUtils;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IExplosionMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class PrimitiveDistillationTowerMachine extends NoEnergyMultiblockMachine implements IExplosionMachine, IInfinityEnergyMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            PrimitiveDistillationTowerMachine.class, NoEnergyMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    private static final ItemStack COAL = Items.COAL.getDefaultInstance();
    private static final ItemStack COAL_BLOCK = Items.COAL_BLOCK.getDefaultInstance();
    private static final ItemStack COAL_DUST = ChemicalHelper.get(TagPrefix.dust, GTMaterials.Coal);

    @Persisted
    private int heat = 298;

    @Persisted
    private int tier;

    @Persisted
    private long time;

    @Persisted
    private boolean isActive;

    private final ConditionalSubscriptionHandler tickSubs;

    private SensorPartMachine sensorMachine;

    public PrimitiveDistillationTowerMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
        tickSubs = new ConditionalSubscriptionHandler(this, this::tickUpdate, () -> isFormed || heat > 298 || time > 0);
    }

    /**
     * 执行设备的更新逻辑。此方法会在每个周期被调用，用于调整设备的状态和处理输入/输出。
     * 方法内部处理以下几个主要逻辑：
     * 1. 根据当前时间 (time) 判断设备是否应该处于活动状态，并在必要时更新状态。
     * 2. 如果时间可以被20整除，进行以下操作：
     * - 检查设备的温度 (heat)，如果温度超过373时尝试使用水进行冷却。
     * - 根据配方逻辑 (recipeLogic) 调整温度。
     * 3. 根据当前时间递减。
     * 4. 当时间耗尽时，处理以下几项内容：
     * - 停止设备并且更新相关方块状态。
     * - 检查是否有燃料物品的输入，并且根据燃料类型调整tier和时间。
     * - 调整温度以接近理想值 (298)。
     * 5. 每20个周期执行以下操作：
     * - 检查温度是否超过850，如果是则触发爆炸 (doExplosion)。
     * - 更新传感器设备 (sensorMachine) 的温度。
     */
    private static final int HEAT_THRESHOLD = 373;
    private static final int MAX_WATER_USAGE = 9000;
    private static final int TIER_DECREASE = 1;
    private static final int TIER_INCREASE = 4;
    private static final int IDEAL_HEAT = 298;

    /**
     * 执行设备的定时更新操作。
     * 该方法负责处理以下逻辑：
     * 1. 更新设备的工作状态。
     * 2. 处理设备的热量和水消耗。
     * 3. 检查燃料并进行补充。
     * 4. 根据当前状态调整设备热量。
     * 5. 调用后处理操作。
     */
    private void tickUpdate() {
        long offsetTimer = getOffsetTimer();
        if (time > 0) {
            activateMachine();
            handleHeatAndWater();
            time--;
        } else {
            deactivateMachine();
            checkAndRefuel(offsetTimer);
            adjustHeat(offsetTimer);
        }
        postTickActions(offsetTimer);
        tickSubs.updateSubscription();
    }

    /**
     * 激活机器的方法。
     * 如果机器当前未激活，将其状态设为激活并更新激活的区块。
     */
    private void activateMachine() {
        if (!isActive) {
            isActive = true;
            updateActiveBlocks(true);
        }
    }

    /**
     * 处理设备的热量和水消耗。每20个时间单位执行一次检查。
     * 如果设备的热量高于设定的阈值，则消耗一定量的水来调节热量。
     * 每次调用此方法，设备的热量会根据设备的级别增加。如果设备正在工作，热量会减少。
     */
    private void handleHeatAndWater() {
        if (time % 20 == 0) {
            if (heat > HEAT_THRESHOLD) {
                int water = Math.min(MAX_WATER_USAGE, MachineUtils.getFluidAmount(this, Fluids.WATER)[0]);
                adjustHeatWithWater(water);
            }
            heat += tier;
            if (getRecipeLogic().isWorking()) heat--;
        }
    }

    /**
     * 根据输入的水量调整设备的热度。
     *
     * @param water 输送到设备中的水量，水量大于0才能影响热度。水量大于100时减少热度，小于等于100时增加热度并减少时间。
     */
    private void adjustHeatWithWater(int water) {
        if (water > 0) {
            if (water > 100) {
                heat -= water / 100;
            } else {
                heat += water / 20;
                time -= water;
            }
            playCoolingSound();
            MachineUtils.inputFluid(this, Fluids.WATER, water);
        }
    }

    /**
     * 如果配置允许机器播放声音且当前环境不为空时，播放冷却声音效果。
     * 该方法主要在机器执行某些操作时调用，以模拟冷却过程中的音效，使其更具沉浸感。
     */
    private void playCoolingSound() {
        if (ConfigHolder.INSTANCE.machines.machineSounds && getLevel() != null) {
            getLevel().playSound(null, getPos(), SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    /**
     * 停用当前的蒸馏塔机器。
     * 如果机器当前处于激活状态，则将其状态设为非激活，并更新相关的活动块标志。
     */
    private void deactivateMachine() {
        if (isActive) {
            isActive = false;
            updateActiveBlocks(false);
        }
    }

    /**
     * 检查设备的燃料状况并根据不同类型的燃料进行补充。
     *
     * @param offsetTimer 当前的时间偏移量，用于决定何时需要检查和补充燃料。
     */
    private void checkAndRefuel(long offsetTimer) {
        if (isWorkingEnabled() && offsetTimer % 10 == 0) {
            if (MachineUtils.inputItem(this, COAL)) {
                tier = TIER_INCREASE;
                time += 1200;
            } else if (MachineUtils.inputItem(this, COAL_BLOCK)) {
                tier = TIER_DECREASE;
                time += 21600;
            } else if (MachineUtils.inputItem(this, COAL_DUST)) {
                tier = 4;
                time += 500;
            }
        }
    }

    /**
     * 调整设备的热度，根据传入的计时器偏移量每20次减少一次热度，当热度超过理想热度时生效。
     *
     * @param offsetTimer 计时器的偏移量，用于控制热度调整频率，每20次减少一次热度
     */
    private void adjustHeat(long offsetTimer) {
        if (heat > IDEAL_HEAT && offsetTimer % 20 == 0) {
            heat--;
        }
    }

    /**
     * 根据给定的时间偏移量执行周期性的后置操作。
     * 如果时间偏移量是20的倍数，检查当前热度并可能触发爆炸，同时更新传感器状态。
     *
     * @param offsetTimer 时间偏移量，通常基于周期性计时器的当前值。
     */
    private void postTickActions(long offsetTimer) {
        if (offsetTimer % 20 == 0) {
            if (heat > 850) doExplosion(10);
            if (sensorMachine != null) {
                sensorMachine.update(heat);
            }
        }
    }

    @Nullable
    @Override
    protected GTRecipe getRealRecipe(GTRecipe recipe) {
        if (heat > 400) {
            recipe.duration = (int) (recipe.duration * getDurationMultiplier());
            return recipe;
        }
        return null;
    }

    /**
     * 根据设备的当前热度和连续运行时间计算持续时间的倍数。
     * 当热度高于400时，持续时间的倍数如下计算：
     * 800 除以（热度乘以min(2, max(1, 连续运行时间 / 1000))）。
     * 当热度不高于400时，持续时间的倍数为1。
     *
     * @return 当前设备持续时间的倍数
     */
    private double getDurationMultiplier() {
        return heat > 400 ? 800.0D / (heat * Math.min(2, Math.max(1, getRecipeLogic().getTotalContinuousRunningTime() / 1000))) : 1;
    }

    @Override
    public void customText(List<Component> textList) {
        super.customText(textList);
        textList.add(Component.translatable("gtocore.machine.rest_burn_time", time));
        textList.add(Component.translatable("gtocore.machine.advanced_infinite_driller.current_heat", heat));
        textList.add(Component.translatable("gtocore.machine.total_time", getRecipeLogic().getTotalContinuousRunningTime()));
        textList.add(Component.translatable("gtocore.machine.duration_multiplier.tooltip", FormattingUtil.formatNumbers(getDurationMultiplier())));
    }

    @Override
    public void onPartScan(IMultiPart part) {
        super.onPartScan(part);
        if (sensorMachine == null && part instanceof SensorPartMachine sensorPartMachine) {
            sensorMachine = sensorPartMachine;
        }
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        IInfinityEnergyMachine.addProxy(getCapabilitiesProxy(), 120);
        tickSubs.initialize(getLevel());
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        sensorMachine = null;
    }
}
