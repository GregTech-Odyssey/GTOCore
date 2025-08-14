package com.gtocore.common.machine.multiblock.steam;

import com.gtocore.common.machine.multiblock.part.LargeSteamHatchPartMachine;

import com.gtolib.api.annotation.Scanned;
import com.gtolib.api.annotation.dynamic.DynamicInitialValue;
import com.gtolib.api.annotation.dynamic.DynamicInitialValueTypes;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

@Scanned
public final class LargeSteamMultiblockMachine extends BaseSteamMultiblockMachine {

    @DynamicInitialValue(
                         key = "gtocore.multiblock.large_steam.duration_multiplier",
                         typeKey = DynamicInitialValueTypes.KEY_MULTIPLY,
                         simpleValue = "1.0D",
                         normalValue = "1.2D",
                         expertValue = "1.5D",
                         cn = "蒸汽耗时为普通的 : %s 倍",
                         cnComment = "蒸汽时代，多方块并行机器的配方处理受技术限制，时间被迫延长",
                         en = "Recipe using time: Duration Multiplier x %s",
                         enComment = "In the steam era, the processing time of parallel machines is limited by technology, and the time is forced to be extended")
    public static double STEAM_MULTIBLOCK_DURATION_MULTIPLIER = 1.2D;
    @DynamicInitialValue(
                         key = "gtocore.machine.multiblock.large_steam.max_parallels",
                         typeKey = DynamicInitialValueTypes.KEY_MAX_PARALLEL,
                         en = "Max Parallels",
                         enComment = "The maximum number of parallel machines that can be used in this multiblock",
                         cn = "最大并行数",
                         cnComment = "此多方块机器可以使用的最大并行数",
                         simpleValue = "64",
                         normalValue = "32",
                         expertValue = "32")
    public static int STEAM_MULTIBLOCK_MAX_PARALLELS = 32;

    public LargeSteamMultiblockMachine(MetaMachineBlockEntity holder) {
        super(holder, STEAM_MULTIBLOCK_MAX_PARALLELS, STEAM_MULTIBLOCK_DURATION_MULTIPLIER);
    }

    public LargeSteamMultiblockMachine(MetaMachineBlockEntity holder, int eut) {
        super(holder, STEAM_MULTIBLOCK_MAX_PARALLELS, eut, STEAM_MULTIBLOCK_DURATION_MULTIPLIER);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        isOC = getParts().stream().anyMatch(LargeSteamHatchPartMachine.class::isInstance);
    }
}
