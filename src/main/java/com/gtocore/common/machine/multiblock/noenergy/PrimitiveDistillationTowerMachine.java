package com.gtocore.common.machine.multiblock.noenergy;

import com.gtocore.common.data.GTORecipeTypes;
import com.gtocore.common.machine.multiblock.part.SensorPartMachine;

import com.gtolib.GTOCore;
import com.gtolib.api.gui.MagicProgressBarProWidget;
import com.gtolib.api.machine.feature.DummyEnergyMachine;
import com.gtolib.api.machine.multiblock.NoEnergyMultiblockMachine;
import com.gtolib.api.recipe.IdleReason;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeBuilder;
import com.gtolib.api.recipe.RecipeRunner;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;
import com.gregtechceu.gtceu.api.machine.feature.IExplosionMachine;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.VoidFluidHandler;

import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.annotation.RequireRerender;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class PrimitiveDistillationTowerMachine extends NoEnergyMultiblockMachine implements IExplosionMachine, DummyEnergyMachine {

    private static final DummyContainer CONTAINER = new DummyContainer(120);
    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(PrimitiveDistillationTowerMachine.class, NoEnergyMultiblockMachine.MANAGED_FIELD_HOLDER);
    @NotNull
    private List<IFluidHandler> fluidOutputs = Collections.emptyList();

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public Widget createUIWidget() {
        var group = new WidgetGroup(0, 0, 190, 125);
        group.addWidget(new DraggableScrollableWidgetGroup(4, 4, 182, 106).setBackground(getScreenTexture()).addWidget(new LabelWidget(4, 5, self().getBlockState().getBlock().getDescriptionId())).addWidget(new ComponentPanelWidget(4, 17, this::addDisplayText).textSupplier(Objects.requireNonNull(getLevel()).isClientSide ? null : this::addDisplayText).setMaxWidthLimit(200).clickHandler(this::handleDisplayClick)));
        group.addWidget(progressBarPro);
        group.setBackground(GuiTextures.BACKGROUND_INVERSE);
        return group;
    }

    private static final ItemStack COAL = Items.COAL.getDefaultInstance();
    private static final ItemStack COAL_BLOCK = Items.COAL_BLOCK.getDefaultInstance();
    private static final ItemStack COAL_DUST = ChemicalHelper.get(TagPrefix.dust, GTMaterials.Coal);
    @Persisted
    @DescSynced
    @RequireRerender
    private int heat = 298;
    @DescSynced
    @RequireRerender
    private WaterState waterState = WaterState.NO_WATER;
    @DescSynced
    @RequireRerender
    private int waterLevel = 0; // Used for rendering water level in the machine
    @Persisted
    private int tier;
    @Persisted
    private long time;
    @Persisted
    private boolean isActive;
    private final ConditionalSubscriptionHandler tickSubs;
    private SensorPartMachine sensorMachine;

    public PrimitiveDistillationTowerMachine(MetaMachineBlockEntity holder) {
        super(holder);
        tickSubs = new ConditionalSubscriptionHandler(this, this::tickUpdate, this::shouldTick);
    }

    private boolean shouldTick() {
        return isFormed || heat > 298 || time > 0;
    }

    @Override
    public void clientTick() {
        super.clientTick();
        if (getOffsetTimer() % 10 == 0) {
            scheduleRenderUpdate();
        }
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
    private static final int EXPLOSION = 850;

    /**
     * 执行设备的定时更新操作。
     * 该方法负责处理以下逻辑：
     * <p>
     * *New* 更新水的状态以便渲染。
     * </p>
     * 1. 更新设备的工作状态。
     * 2. 处理设备的热量和水消耗。
     * 3. 检查燃料并进行补充。
     * 4. 根据当前状态调整设备热量。
     * 5. 调用后处理操作。
     */
    private void tickUpdate() {
        long offsetTimer = getOffsetTimer();
        if (offsetTimer % 20 == 0) {
            var water = (int) Math.min(MAX_WATER_USAGE, getFluidAmount(Fluids.WATER)[0]);
            updateWaterState(water);
            handleHeatAndWater(water);
        }
        if (time > 0) {
            activateMachine();
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
    private void handleHeatAndWater(int water) {
        if (time > 0) {
            if (heat > HEAT_THRESHOLD) {
                handleCooling(water > 0);
                adjustHeatWithWater(water);
            }
            heat += tier;
            if (getRecipeLogic().isWorking()) heat--;
        }
    }

    /**
     * 更新水的状态。用于在客户端渲染时选取不同水位模型。
     *
     * @param water 当前水量
     */
    private void updateWaterState(int water) {
        if (water > 0) {
            if (water < 100) {
                waterState = WaterState.HAS_LITTLE_WATER;
            } else {
                waterState = WaterState.HAS_ENOUGH_WATER;
            }
        } else {
            waterState = WaterState.NO_WATER;
        }
        waterLevel = water;
    }

    private void handleCooling(boolean isCooling) {
        waterState = isCooling ? WaterState.IS_COOLING : waterState;
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
            inputFluid(Fluids.WATER, water);
        }
    }

    /**
     * 如果配置允许机器播放声音且当前环境不为空时，播放冷却声音效果。
     * 该方法主要在机器执行某些操作时调用，以模拟冷却过程中的音效，使其更具沉浸感。
     */
    private void playCoolingSound() {
        if (shouldWorkingPlaySound() && getLevel() != null) {
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
            if (inputItem(COAL)) {
                tier = TIER_INCREASE;
                time += 1200;
            } else if (inputItem(COAL_BLOCK)) {
                tier = TIER_DECREASE;
                time += 21600;
            } else if (inputItem(COAL_DUST)) {
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
            if (heat > 400) getRecipeLogic().updateTickSubscription();
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
            if (heat > EXPLOSION) doExplosion(10);
            if (sensorMachine != null) {
                sensorMachine.update(heat);
            }
        }
    }

    @Nullable
    @Override
    protected Recipe getRealRecipe(Recipe recipe) {
        if (heat > 400) {
            recipe.duration = (int) (recipe.duration * getDurationMultiplier());
            return recipe;
        }
        setIdleReason(IdleReason.INSUFFICIENT_TEMPERATURE);
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
        return heat > 400 ? 800.0 / (heat * Math.min(2, Math.max(1, getRecipeLogic().getTotalContinuousRunningTime() / 1000))) : 1;
    }

    private final MagicProgressBarProWidget progressBarPro = new MagicProgressBarProWidget(4, 113, IDEAL_HEAT, 930).addStartColor(-16711936).addMilestone(HEAT_THRESHOLD, -16640, Component.translatable("gtocore.bar.distillation.1")).addMilestone(EXPLOSION, -65536, Component.translatable("gtocore.bar.exploration")).setLeftLabel(Component.translatable("gtocore.bar.heat"));

    @Override
    public void customText(List<Component> textList) {
        super.customText(textList);
        textList.add(Component.translatable("gtocore.machine.rest_burn_time", time));
        textList.add(Component.translatable("gtocore.machine.current_temperature", heat));
        textList.add(Component.translatable("gtocore.machine.total_time", getRecipeLogic().getTotalContinuousRunningTime()));
        textList.add(Component.translatable("gtocore.machine.duration_multiplier.tooltip", FormattingUtil.formatNumbers(getDurationMultiplier())));
        progressBarPro.setProgressSupplier(() -> heat);
    }

    @Override
    public void onPartScan(IMultiPart part) {
        super.onPartScan(part);
        if (sensorMachine == null && part instanceof SensorPartMachine sensorPartMachine) {
            sensorMachine = sensorPartMachine;
        }
    }

    @Override
    public Comparator<IMultiPart> getPartSorter() {
        return Comparator.comparingInt(p -> p.self().getPos().getY());
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        int startY = getPos().getY() + 1;
        List<IMultiPart> parts = getParts().stream().filter(part -> PartAbility.EXPORT_FLUIDS.isApplicable(part.self().getBlockState().getBlock())).filter(part -> part.self().getPos().getY() >= startY).toList();
        if (!parts.isEmpty()) {
            int maxY = parts.get(parts.size() - 1).self().getPos().getY();
            fluidOutputs = new ObjectArrayList<>(maxY - startY);
            int outputIndex = 0;
            for (int y = startY; y <= maxY; ++y) {
                if (parts.size() <= outputIndex) {
                    fluidOutputs.add(VoidFluidHandler.INSTANCE);
                    continue;
                }
                var part = parts.get(outputIndex);
                if (part.self().getPos().getY() == y) {
                    var handler = part.getRecipeHandlers().get(0).getCapability(FluidRecipeCapability.CAP).stream().filter(IFluidHandler.class::isInstance).findFirst().map(IFluidHandler.class::cast).orElse(VoidFluidHandler.INSTANCE);
                    addOutput(handler);
                    outputIndex++;
                } else if (part.self().getPos().getY() > y) {
                    fluidOutputs.add(VoidFluidHandler.INSTANCE);
                }
            }
        }
        tickSubs.initialize(getLevel());
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        sensorMachine = null;
        fluidOutputs = Collections.emptyList();
    }

    @Override
    public RecipeLogic createRecipeLogic(Object... args) {
        return new DistillationTowerLogic(this);
    }

    private void addOutput(IFluidHandler handler) {
        fluidOutputs.add(handler);
    }

    @Override
    @NotNull
    public IEnergyContainer gtolib$getEnergyContainer() {
        return CONTAINER;
    }

    @Override
    public boolean jade() {
        return false;
    }

    public WaterState getWaterState() {
        return waterState;
    }

    public static int getMaxHeat() {
        return EXPLOSION;
    }

    public static int getMaxWaterUsage() {
        return MAX_WATER_USAGE;
    }

    public int getHeat() {
        return heat;
    }

    public int getWaterLevel() {
        return waterLevel;
    }

    private static final class DistillationTowerLogic extends RecipeLogic {

        private static final ResourceLocation ID = RecipeBuilder.getTypeID(GTCEu.id("distill_water_large"), GTORecipeTypes.DISTILLATION_RECIPES);

        @Nullable
        @Persisted
        @DescSynced
        private GTRecipe workingRecipe = null;

        private DistillationTowerLogic(IRecipeLogicMachine machine) {
            super(machine);
        }

        @NotNull
        @Override
        public PrimitiveDistillationTowerMachine getMachine() {
            return (PrimitiveDistillationTowerMachine) super.getMachine();
        }

        @Override
        @Nullable
        public GTRecipe getLastRecipe() {
            return workingRecipe;
        }

        @Override
        protected boolean matchRecipe(GTRecipe recipe) {
            if (recipe.id.equals(ID)) return false;
            if (RecipeHelper.getRecipeEUtTier(recipe) > 2) {
                getMachine().setIdleReason(IdleReason.VOLTAGE_TIER_NOT_SATISFIES);
                return false;
            }
            return matchDTRecipe((Recipe) recipe);
        }

        @Override
        protected void handleSearchingRecipes(Iterator<GTRecipe> matches) {
            workingRecipe = null;
            super.handleSearchingRecipes(matches);
        }

        private boolean matchDTRecipe(Recipe recipe) {
            if (!RecipeRunner.matchRecipeInput(machine, recipe)) return false;
            var items = recipe.getOutputContents(ItemRecipeCapability.CAP);
            if ((!items.isEmpty() && !RecipeRunner.handleRecipe(machine, recipe, IO.OUT, Map.of(ItemRecipeCapability.CAP, items), Collections.emptyMap(), true)) || !applyFluidOutputs(recipe, IFluidHandler.FluidAction.SIMULATE)) {
                getMachine().setIdleReason(IdleReason.OUTPUT_FULL);
                return false;
            }
            return true;
        }

        private void updateWorkingRecipe(GTRecipe recipe) {
            this.workingRecipe = recipe.copy();
            var contents = recipe.getOutputContents(FluidRecipeCapability.CAP);
            var outputs = getMachine().getFluidOutputs();
            List<Content> trimmed = new ArrayList<>(12);
            for (int i = 0; i < Math.min(contents.size(), outputs.size()); ++i) {
                if (!(outputs.get(i) instanceof VoidFluidHandler)) trimmed.add(contents.get(i));
            }
            this.workingRecipe.outputs.put(FluidRecipeCapability.CAP, trimmed);
        }

        @Override
        protected boolean handleRecipeIO(GTRecipe recipe, IO io) {
            if (io != IO.OUT) {
                var handleIO = super.handleRecipeIO(recipe, io);
                if (handleIO) {
                    updateWorkingRecipe(recipe);
                } else {
                    this.workingRecipe = null;
                }
                return handleIO;
            }
            var items = recipe.getOutputContents(ItemRecipeCapability.CAP);
            if (!items.isEmpty()) {
                Map<RecipeCapability<?>, List<Content>> out = Map.of(ItemRecipeCapability.CAP, items);
                RecipeRunner.handleRecipe(this.machine, (Recipe) recipe, io, out, chanceCaches, false);
            }
            if (applyFluidOutputs(recipe, IFluidHandler.FluidAction.EXECUTE)) {
                workingRecipe = null;
                return true;
            }
            return false;
        }

        private boolean applyFluidOutputs(GTRecipe recipe, IFluidHandler.FluidAction action) {
            var fluids = recipe.getOutputContents(FluidRecipeCapability.CAP).stream().map(Content::getContent).map(FluidRecipeCapability.CAP::of).toList();
            boolean valid = true;
            var outputs = getMachine().getFluidOutputs();
            for (int i = 0; i < Math.min(fluids.size(), outputs.size()); ++i) {
                var handler = outputs.get(i);
                var fluid = fluids.get(i).getStacks()[0];
                int filled = (handler instanceof NotifiableFluidTank nft) ? nft.fillInternal(fluid, action) : handler.fill(fluid, action);
                if (filled != fluid.getAmount()) valid = false;
                if (action.simulate() && !valid) break;
            }
            return valid;
        }
    }

    @NotNull
    private List<IFluidHandler> getFluidOutputs() {
        return this.fluidOutputs;
    }

    public enum WaterState {

        NO_WATER(null),
        HAS_LITTLE_WATER("block/multiblock/primitive_distillation_tower/water"),
        HAS_ENOUGH_WATER("block/multiblock/primitive_distillation_tower/water"),
        IS_COOLING("block/multiblock/primitive_distillation_tower/steam");

        public final @Nullable ResourceLocation overlay;

        WaterState(@Nullable String overlay) {
            this.overlay = Optional.ofNullable(overlay)
                    .map(GTOCore::id)
                    .orElse(null);
        }
    }
}
