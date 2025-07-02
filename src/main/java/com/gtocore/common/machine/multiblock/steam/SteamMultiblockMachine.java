package com.gtocore.common.machine.multiblock.steam;

import com.gtolib.api.annotation.Scanned;
import com.gtolib.api.annotation.dynamic.DynamicInitialValue;
import com.gtolib.api.annotation.dynamic.DynamicInitialValueTypes;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

@Scanned
public final class SteamMultiblockMachine extends BaseSteamMultiblockMachine {

    @DynamicInitialValue(
                         key = "gtocore.multiblock.steam.duration_multiplier",
                         typeKey = DynamicInitialValueTypes.KEY_MULTIPLY,
                         simpleValue = "1.2D",
                         normalValue = "1.5D",
                         expertValue = "1.6D",
                         cn = "蒸汽耗时为普通的 : %s 倍",
                         cnComment = "蒸汽时代，多方块并行机器的配方处理受技术限制，时间被迫延长",
                         en = "Recipe using time: Duration Multiplier x %s",
                         enComment = "In the steam era, the processing time of parallel machines is limited by technology, and the time is forced to be extended")
    public static double STEAM_MULTIBLOCK_DURATION_MULTIPLIER = 1.2D;
    @DynamicInitialValue(
                         key = "gtocore.machine.multiblock.steam.max_parallels",
                         typeKey = DynamicInitialValueTypes.KEY_MAX_PARALLEL,
                         en = "Max Parallels",
                         enComment = "The maximum number of parallel machines that can be used in this multiblock",
                         cn = "最大并行数",
                         cnComment = "此多方块机器可以使用的最大并行数",
                         simpleValue = "16",
                         normalValue = "8",
                         expertValue = "8")
    public static int STEAM_MULTIBLOCK_MAX_PARALLELS = 8;

    public SteamMultiblockMachine(IMachineBlockEntity holder) {
        super(holder, STEAM_MULTIBLOCK_MAX_PARALLELS, STEAM_MULTIBLOCK_DURATION_MULTIPLIER);
    }

    public SteamMultiblockMachine(IMachineBlockEntity holder, int eut) {
        super(holder, STEAM_MULTIBLOCK_MAX_PARALLELS, eut, STEAM_MULTIBLOCK_DURATION_MULTIPLIER);
    }
}
