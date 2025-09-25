package com.gtocore.common.data.translation

import com.gtocore.api.lang.ComponentListSupplier
import com.gtocore.api.lang.toLiteralSupplier
import com.gtocore.api.misc.AutoInitialize
import com.gtocore.utils.setTooltips

import appeng.core.definitions.AEItems
import appeng.core.definitions.AEParts
import com.glodblock.github.extendedae.common.EPPItemAndBlock
import com.gregtechceu.gtceu.utils.FormattingUtil

object GTOItemTooltips : AutoInitialize<GTOItemTooltips>() {
    // 升级模块 - 速度
    val SpeedUpgradeModuleTooltips = { coefficient: Double, gCoefficient: Double ->
        ComponentListSupplier {
            setTranslationPrefix("upgrade_module")

            val s1 = FormattingUtil.formatNumbers(coefficient)
            val s2 = FormattingUtil.formatNumbers(gCoefficient)

            highlight("提升机器运作速度" translatedTo "Speed up machine operation")
            increase(
                ("直接应用系数(越低越好): " translatedTo "Direct application coefficient (the lower, the better): ") + "${s1}x".toLiteralSupplier()
                    .aqua(),
            )
            increase(
                ("重复应用博弈系数(越低越好): " translatedTo "Repeated application of the gambling coefficient (the lower, the better):") + "${s2}x".toLiteralSupplier()
                    .aqua(),
            )
        }
    }

    // 升级模块 - 能量
    val EnergyUpgradeModuleTooltips = { coefficient: Double, gCoefficient: Double ->
        ComponentListSupplier {
            setTranslationPrefix("upgrade_module")

            val s1 = FormattingUtil.formatNumbers(coefficient)
            val s2 = FormattingUtil.formatNumbers(gCoefficient)

            highlight("降低机器功耗" translatedTo "Reduce machine power consumption")
            increase(
                ("直接应用系数(越低越好): " translatedTo "Direct application coefficient (the lower, the better): ") + "${s1}x".toLiteralSupplier()
                    .aqua(),
            )
            increase(
                ("重复应用博弈系数(越低越好): " translatedTo "Repeated application of the gambling coefficient (the lower, the better):") + "${s2}x".toLiteralSupplier()
                    .aqua(),
            )
        }
    }

    // 样板修改器
    val PatternModifierTooltips = ComponentListSupplier {
        setTranslationPrefix("pattern_modifier")

        section("便捷地修改样板" translatedTo "Modify patterns easily")
        function("它可以自动翻倍样板，不用任何操作" translatedTo "It can automatically double the pattern, no operation is required")
        content(ComponentSlang.RecommendedToUse("样板总成" translatedTo "Pattern Buffer"))
        increase("PRO版本可以批量应用" translatedTo "PRO version can apply in batch")
    }

    // AE2 订单
    val OrderTooltips = ComponentListSupplier {
        setTranslationPrefix("order")

        miraculousTools("AE2 订单" translatedTo "AE2 Order")

        section(ComponentSlang.MainFunction)
        guide("右键可以放入一个虚拟物品，例如多方块主机" translatedTo "Right click to put a virtual item, such as a multi-block machine")
        guide("不需要再在铁砧使用告示牌命名" translatedTo "No longer need to use a sign to name it in anvil")
        function("可以作为AE自动合成的大型机器产物" translatedTo "Can be used as a large machine product for AE2 automatic crafting")
        function("当此合成完成时，会自动取消，无需手动取消" translatedTo "When the crafting is completed, it will automatically cancel, no need to cancel manually")
    }

    // 割草镰刀
    val GrassHarvesterTooltips = ComponentListSupplier {
        setTranslationPrefix("grass_harvester")

        miraculousTools("割草镰刀" translatedTo "Grass Harvester")

        section(ComponentSlang.MainFunction)
        increase("极大地提升小麦种子掉落概率" translatedTo "Greatly increases the drop rate of wheat seeds")
        guide("右键草以收割小麦种子和稀有作物" translatedTo "Right-click grass to harvest wheat seeds and rare crops")
        info("前期大量获取种子去种地的好帮手" translatedTo "A good helper for obtaining seeds in large quantities in the early game")
    }

    // 时间扭曲者
    val TimeTwisterTooltips = ComponentListSupplier {
        setTranslationPrefix("time_twister")

        miraculousTools("时间扭曲者" translatedTo "Time Twister")

        section("启动加速" translatedTo "Acceleration for normal block entities")
        function("普通点击：消耗8192 EU能量，加速一次" translatedTo "Normal click: Consume 8192 EU energy, accelerate once")
        function("Shift点击：消耗819200 EU能量，持续100刻内加速目标方块" translatedTo "Shift click: Consume 819200 EU energy, accelerate the target block for 100 ticks")

        section("加速方式" translatedTo "Acceleration methods")
        function("普通机器：不额外消耗EU能量，每次200tick" translatedTo "For normal machines: No extra EU consumption, 200 ticks per acceleration")
        function("GT机器：每次使当前正在工作的机器进度立即增加最多50%" translatedTo "For GT machines: immediately increase current progress by up to 50% per use")

        section("能量消耗" translatedTo "Energy consumption:")
        command("使用无线能量系统作为能量来源" translatedTo "Use wireless energy system as energy source")
        command("不同操作消耗不同数量的EU" translatedTo "Different operations consume different amounts of EU")
        command("加速GT机器时，根据难度模式消耗相应倍数EU能量" translatedTo "When accelerating GT machines, consume EU energy according to the difficulty mode")
    }

    // Modification
    init {
        listOf(AEParts.STORAGE_BUS.asItem()).forEach {
            it.setTooltips(
                ComponentListSupplier {
                    setTranslationPrefix("storage_bus")

                    section("与存储设备进行交互" translatedTo "Interact with storage devices")
                    command(("仅支持读取" translatedTo "Only supports reading ") + ("物品和流体" translatedTo "items and fluids ").gold() + ("两种类型" translatedTo "two types"))
                    info("经过优化，吞吐量性能卓越" translatedTo "Throughput performance is excellent")
                }.editionByGTONormal(),
            )
        }

        listOf(EPPItemAndBlock.TAG_STORAGE_BUS.asItem(), EPPItemAndBlock.MOD_STORAGE_BUS.asItem()).forEach {
            it.setTooltips(
                ComponentListSupplier {
                    setTranslationPrefix("pattern_modifier")

                    section("与存储设备进行交互" translatedTo "Interact with storage devices")
                    command(("仅支持读取" translatedTo "Only supports reading ") + ("物品和流体" translatedTo "items and fluids ").gold() + ("两种类型" translatedTo "two types"))
                    info(("自动化中，" translatedTo "In automation, ") + ComponentSlang.RecommendedToUse("存储总线" translatedTo "Storage Bus"))
                    info("经过优化，吞吐量性能卓越" translatedTo "Throughput performance is excellent")
                }.editionByGTONormal(),
            )
        }

        listOf(EPPItemAndBlock.PATTERN_MODIFIER.asItem()).forEach {
            it.setTooltips(PatternModifierTooltips)
        }

        listOf(
            AEItems.ITEM_CELL_1K.asItem(),
            AEItems.ITEM_CELL_4K.asItem(),
            AEItems.ITEM_CELL_16K.asItem(),
            AEItems.ITEM_CELL_64K.asItem(),
            AEItems.FLUID_CELL_1K.asItem(),
            AEItems.FLUID_CELL_4K.asItem(),
            AEItems.FLUID_CELL_16K.asItem(),
            AEItems.FLUID_CELL_64K.asItem(),
        ).forEach {
            it.setTooltips(
                ComponentListSupplier {
                    setTranslationPrefix("me_storage_cell")

                    highlight("存储容量最高是原来的二倍" translatedTo "Storage capacity is doubled compared to the original")
                }.editionByGTONormal(),
            )
        }

        listOf(AEItems.ITEM_CELL_256K.asItem(), AEItems.FLUID_CELL_256K.asItem()).forEach {
            it.setTooltips(
                ComponentListSupplier {
                    setTranslationPrefix("me_storage_cell")

                    highlight("存储容量最高是原来的二倍" translatedTo "Storage capacity is doubled compared to the original")
                    content("你走到了单个存储元件的尽头" translatedTo "You've reached the end of a single storage cell")
                    increase(ComponentSlang.RecommendedToUse("ME存储器 (多方块结构)" translatedTo "ME Storage (MultiBlock)"))
                    increase("他最高可以实现不限类型的无限存储" translatedTo "It can even store unlimited amounts of items and fluids without type limit")
                }.editionByGTONormal(),
            )
        }
    }
}
