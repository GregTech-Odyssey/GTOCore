package com.gtocore.common.machine.multiblock.electric.space.spacestaion;

import com.gtocore.common.machine.multiblock.electric.space.SpaceElevatorMachine;

import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.api.capability.IIWirelessInteractor;
import com.gtolib.utils.MathUtil;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;

import com.fast.fastcollection.O2IOpenCacheHashMap;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.gregtechceu.gtceu.api.GTValues.UHV;
import static com.gregtechceu.gtceu.api.GTValues.VA;

@DataGeneratorScanned
public class SpaceElevatorConnectorModule extends Extension implements ISpaceServiceMachine, IIWirelessInteractor.IWirelessProvider {

    private final Map<SpaceElevatorMachine, Integer> elevatorTiers = new O2IOpenCacheHashMap<>();
    @DescSynced
    @Getter
    private int maxTier = 0;

    @Getter
    @DescSynced
    protected double high = 15;

    public SpaceElevatorConnectorModule(MetaMachineBlockEntity metaMachineBlockEntity) {
        super(metaMachineBlockEntity);
    }

    @Override
    public Set<BlockPos> getModulePositions() {
        var pos = getPos();
        var fFacing = getFrontFacing();
        var hallwayCenter = pos.relative(fFacing, 2).relative(Direction.DOWN, 15);
        return Set.of(hallwayCenter.relative(Direction.EAST, 2),
                hallwayCenter.relative(Direction.WEST, 2),
                hallwayCenter.relative(Direction.SOUTH, 2),
                hallwayCenter.relative(Direction.NORTH, 2));
    }

    public void registerElevator(SpaceElevatorMachine elevatorMachine, int tier) {
        elevatorTiers.put(elevatorMachine, tier);
        if (tier > maxTier) {
            maxTier = tier;
        }
        requestSync();
    }

    public void unregisterElevator(SpaceElevatorMachine elevatorMachine) {
        Integer removedTier = elevatorTiers.remove(elevatorMachine);
        if (removedTier != null && removedTier == maxTier) {
            // Recalculate maxTier
            maxTier = elevatorTiers.values().stream().max(Integer::compareTo).orElse(0);
        }
        requestSync();
    }

    public double getDurationMultiplier() {
        if (!isWorkspaceReady()) return 1.0;
        if (maxTier <= 0) {
            return 1.0;
        }
        return Math.pow(0.8f, maxTier);
    }

    @Override
    public void onUnload() {
        super.onUnload();
        IIWirelessInteractor.removeFromNet(this);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        IIWirelessInteractor.addToNet(this);
    }

    @Override
    public void onStructureInvalid() {
        IIWirelessInteractor.removeFromNet(this);
        elevatorTiers.clear();
        maxTier = 0;
        super.onStructureInvalid();
        IIWirelessInteractor.removeFromNet(this);
    }

    @Override
    public void customText(@NotNull List<Component> list) {
        super.customText(list);
        if (maxTier > 0) {
            list.add(Component.translatable(SPACE_ELEVATOR_CONNECTED_TEXT, maxTier));
            list.add(Component.translatable(SPACE_ELEVATOR_TIME_COST_MULTIPLIER_TEXT, FormattingUtil.formatNumber2Places(getDurationMultiplier())));
        } else {
            list.add(Component.translatable(SPACE_ELEVATOR_NOT_CONNECTED_TEXT));
        }
    }

    @Override
    public long getEUt() {
        return VA[UHV];
    }

    @Override
    public void clientTick() {
        super.clientTick();
        if (maxTier > 0) high = 480 + ((480) * MathUtil.sin((float) (getOffsetTimer() / 240.0F + Math.PI))) + 15;
    }

    @RegisterLanguage(cn = "已连接到太空电梯，动力模块最高等级: %s", en = "Connected to Space Elevator, Max Power Module Tier: %s")
    public static final String SPACE_ELEVATOR_CONNECTED_TEXT = "spacestation.space_elevator.connected";
    @RegisterLanguage(cn = "已连接到当前星球的空间站，可使用耗时倍率: x%s", en = "Connected to Space Station of Current Planet, Usable Time Cost Multiplier: x%s")
    public static final String SPACE_ELEVATOR_CONNECTED_CURRENT_PLANET_TEXT = "spacestation.space_elevator.connected_current_planet";
    @RegisterLanguage(cn = "可获得的耗时倍率增益: x%s", en = "Time Cost Multiplier Gain: x%s")
    public static final String SPACE_ELEVATOR_TIME_COST_MULTIPLIER_TEXT = "spacestation.space_elevator.time_cost_multiplier";
    @RegisterLanguage(cn = "未连接到太空电梯", en = "Not Connected to Space Elevator")
    public static final String SPACE_ELEVATOR_NOT_CONNECTED_TEXT = "spacestation.space_elevator.not_connected";
    @RegisterLanguage(cn = "未连接到当前星球的空间站", en = "Not Connected to Space Station of Current Planet")
    public static final String SPACE_ELEVATOR_NOT_CONNECTED_CURRENT_PLANET_TEXT = "spacestation.space_elevator.not_connected_current_planet";
}
