package com.gtocore.common.data.translation

import com.gtocore.api.misc.AutoInitialize
import com.gtocore.common.machine.electric.ElectricHeaterMachine
import com.gtocore.common.machine.multiblock.storage.MultiblockCrateMachine
import com.gtocore.common.machine.noenergy.BoilWaterMachine
import com.gtocore.common.machine.noenergy.HeaterMachine
import com.gtocore.utils.ComponentListSupplier
import com.gtocore.utils.toLiteralSupplier
import com.gtocore.utils.translatedTo

object GTOMachineTranslation : AutoInitialize<GTOMachineTranslation>() {
    val BoilWaterMachineTooltips = ComponentListSupplier {
        add("需要外部热源工作" translatedTo "Requires external heat source to operate") { aqua() }
        add(("当蒸汽溢出后继续工作会" translatedTo "When steam overflows, continuing to work will ") + ("爆炸" translatedTo "explode").red().bold()) { aqua() }
        add(("可能发生爆炸的临界温度为" translatedTo "The critical temperature for explosion is ") + (BoilWaterMachine.DrawWaterExplosionLine.toLiteralSupplier()).red().bold()) { aqua() }
    }
    val PerformanceMonitorMachineTooltips = ComponentListSupplier {
        add("能监测全部机器2秒内的平均延迟" translatedTo "Can monitor all machines' average delay within 2 seconds and support highlighting") { aqua() }
        add("右键点击机器以打开性能监测界面" translatedTo "Right click on the machine to open performance monitoring interface") { gray() }
    }

    val HeaterMachineTooltips = ComponentListSupplier {
        add("通过燃烧对四周机器进行加热" translatedTo "Burning to heat up around machines") { aqua() }
        add(ComponentSlang.TemperatureMax(HeaterMachine.MaxTemperature))
        add("前方被阻挡后停止加热" translatedTo "Stop heating after front side is blocked.") { aqua() }
        add("根据温度发出红石信号" translatedTo "Emits redstone signal according to the temperature.") { aqua() }
        add(ComponentSlang.Star(1) + ("机器过热会" translatedTo "When machine is too hot, it will ") + ComponentSlang.Explosion) { aqua() }
        add(ComponentSlang.BewareOfBurns)
    }

    val ElectricHeaterMachineTooltips = ComponentListSupplier {
        add("使用电力对四周机器进行加热" translatedTo "Use electricity to heat up around machines") { aqua() }
        add(ComponentSlang.TemperatureMax(ElectricHeaterMachine.MaxTemperature))
        add(ComponentSlang.Star(1) + ("此机器不会爆炸" translatedTo "This machine will not explode")) { aqua() }
        add(ComponentSlang.BewareOfBurns)
    }

    val MultiblockCrateMachineTooltips = ComponentListSupplier {
        add("多方块箱子" translatedTo "Multiblock Crate") { aqua() }
        add("可以存储大量物品" translatedTo "Can store many many items") { aqua() }
        add("右键点击以打开界面" translatedTo "Right click to open the interface") { gray() }
        add(ComponentSlang.Capacity(MultiblockCrateMachine.Capacity.toString())) { aqua() }
    }
}
