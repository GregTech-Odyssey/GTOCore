package com.gtocore.common.machine.multiblock.electric.space.spacestaion;

import com.gtocore.api.machine.ILargeSpaceStationMachine;
import com.gtocore.common.data.GTORecipeDataKeys;

import com.gtolib.api.capability.IIWirelessInteractor;
import com.gtolib.api.machine.feature.IWirelessDimensionProvider;
import com.gtolib.api.machine.trait.TierCasingTrait;
import com.gtolib.api.recipe.RecipeBuilder;
import com.gtolib.api.recipe.TierDataKey;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.TaskHandler;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import com.hepdd.gtmthings.api.misc.WirelessEnergyContainer;
import earth.terrarium.adastra.api.planets.PlanetApi;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.gregtechceu.gtceu.api.GTValues.IV;
import static com.gregtechceu.gtceu.api.GTValues.VA;
import static com.gregtechceu.gtceu.common.data.GTMaterials.DistilledWater;
import static com.gtocore.common.data.GTOMaterials.FlocculationWasteSolution;

public class Core extends AbstractSpaceStation implements ILargeSpaceStationMachine, IWirelessDimensionProvider {

    @Getter
    private final Map<Class<? extends ISpaceServiceMachine>, ISpaceServiceMachine> serviceMachineMap = new Reference2ObjectOpenHashMap<>();

    private final Set<ILargeSpaceStationMachine> subMachinesFlat;
    private WirelessEnergyContainer WirelessEnergyContainerCache;
    private final TierCasingTrait tierCasingTrait;

    @Getter
    private boolean dirty = false;

    @Override
    public void markDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public Core(MetaMachineBlockEntity metaMachineBlockEntity) {
        super(metaMachineBlockEntity);
        this.subMachinesFlat = new ObjectOpenHashSet<>();
        tierCasingTrait = new TierCasingTrait(this, GTORecipeDataKeys.INTEGRAL_FRAMEWORK_TIER);
    }

    @Override
    public Core getRoot() {
        return this;
    }

    @Override
    public void setRoot(Core root) {}

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        onFormed();
        IIWirelessInteractor.addToNet(this);
        markDirty(true);
        loadContainer();
    }

    @Override
    public void onUnload() {
        unloadContainer();
        IIWirelessInteractor.removeFromNet(this);
        super.onUnload();
    }

    @Override
    public boolean isWorkingEnabled() {
        return true;
    }

    @Override
    public void setWorkingEnabled(boolean ignored) {}

    private void delayedUnload() {
        if (!isRemote()) {
            TaskHandler.enqueueTask(Objects.requireNonNull(getLevel()), () -> {
                if (getHolder().hasLevel() && !isFormed()) unloadContainer();
            }, 200);
        }
    }

    @Override
    public void onStructureInvalid() {
        delayedUnload();
        super.onStructureInvalid();
        IIWirelessInteractor.removeFromNet(this);
        onInvalid();
    }

    @Override
    public void onMachineRemoved() {
        super.onMachineRemoved();
        removeAllSubMachines();
    }

    @Override
    public void customText(@NotNull List<Component> list) {
        super.customText(list);
        list.add(Component.translatable("gui.ae2.PowerUsageRate", "%s EU/t".formatted(FormattingUtil.formatNumbers(getEUt()))).withStyle(ChatFormatting.YELLOW));
        list.add(Component.translatable("gtocore.machine.spacestation.energy_consumption.total", FormattingUtil.formatNumbers(Optional.ofNullable(getRecipeLogic().getLastRecipe()).map(GTRecipe::getInputEUt).orElse(0L))).withStyle(ChatFormatting.GOLD));
        list.add(Component.translatable("gtocore.machine.spacestation.module_count", subMachinesFlat.size()));
    }

    private void removeAllSubMachines() {
        for (ILargeSpaceStationMachine m : subMachinesFlat) {
            if (m != this && m.getRoot() == this) {
                m.setRoot(null);
            }
        }
        subMachinesFlat.clear();
    }

    /// 很吃性能的操作，使用dirty标记需要更新
    private void refreshModules() {
        removeAllSubMachines();
        // provider = null;
        // laserProvider = null;
        serviceMachineMap.clear();
        Set<ILargeSpaceStationMachine> its = new ReferenceOpenHashSet<>(getConnectedModules());
        while (!its.isEmpty()) {
            var it = its.iterator();
            ILargeSpaceStationMachine m = it.next();
            it.remove();
            if (m.getRoot() != null) continue;
            m.setRoot(this);
            if (m instanceof ISpaceServiceMachine serviceMachine) {
                serviceMachineMap.putIfAbsent(serviceMachine.getClass(), serviceMachine);
            }
            if (subMachinesFlat.add(m)) {
                its.addAll(m.getConnectedModules());
            }
        }
    }

    @Override
    public Set<BlockPos> getModulePositions() {
        var pos = getPos();
        var fFacing = getFrontFacing();
        var uFacing = RelativeDirection.UP.getRelative(fFacing, getUpwardsFacing(), false);
        var thirdAxis = RelativeDirection.RIGHT.getRelative(fFacing, getUpwardsFacing(), false);
        return Set.of(pos.relative(fFacing, 38).relative(uFacing, 6).relative(thirdAxis, 2),
                pos.relative(fFacing, 38).relative(uFacing, 6).relative(thirdAxis.getOpposite(), 2),
                pos.relative(fFacing, 38).relative(uFacing, 4),
                pos.relative(fFacing, 38).relative(uFacing, 8));
    }

    @Override
    public ConnectType getConnectType() {
        return ConnectType.CORE;
    }

    @Override
    public GTRecipeDefinition createCustomRecipe(RecipeHandlerUnit unit) {
        if (!PlanetApi.API.isSpace(getLevel()))
            return null;
        if (dirty) {
            refreshModules();
            dirty = false;
        }
        long EUt = getEUt();
        for (ILargeSpaceStationMachine machine : subMachinesFlat) {
            EUt += machine.getEUt();
            if (machine instanceof IRecipeLogicMachine r) r.getRecipeLogic().updateTickSubscription();
        }
        return inputFluids(getRecipeBuilder().duration(20).EUt(EUt), subMachinesFlat.size() + 1)
                .tier(1)
                .outputFluids(FlocculationWasteSolution.getFluid(30 * (subMachinesFlat.size() + 1)))
                .build();
    }

    @Override
    public boolean alwaysSearchRecipe() {
        return true;
    }

    private static RecipeBuilder inputFluids(RecipeBuilder builder, int mul) {
        builder.inputFluids(DistilledWater, 15 * mul);
        builder.inputFluids(GTMaterials.RocketFuel, 10 * mul);
        builder.inputFluids(GTMaterials.Air, 100 * mul);
        return builder;
    }

    @Override
    public void onWorking() {
        if (firstLoad() || getOffsetTimer() % 400 == 0) provideOxygen();
        super.onWorking();
    }

    @Override
    public long getEUt() {
        return VA[IV];
    }

    @Override
    public Reference2IntMap<TierDataKey> getCasingTiers() {
        return tierCasingTrait.getCasingTiers();
    }

    @Override
    @Nullable
    public UUID getUUID() {
        return getOwnerUUID();
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
    public Set<CleanroomType> getTypes() {
        CleanroomProvider provider = (CleanroomProvider) serviceMachineMap.get(CleanroomProvider.class);
        if (provider == null) {
            return Collections.emptySet();
        }
        return provider.getTypes();
    }

    public boolean canUseLaser() {
        return serviceMachineMap.get(SpaceStationEnergyConversionModule.class) != null;
    }

    public double getDurationMultiplierFromSpaceElevator() {
        SpaceElevatorConnectorModule provider = (SpaceElevatorConnectorModule) serviceMachineMap.get(SpaceElevatorConnectorModule.class);
        if (provider == null) {
            return 1.0;
        }
        return provider.getDurationMultiplier();
    }
}
