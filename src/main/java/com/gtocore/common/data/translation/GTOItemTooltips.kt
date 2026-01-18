package com.gtocore.common.data.translation

import com.gtocore.api.lang.ComponentListSupplier
import com.gtocore.api.lang.ComponentSupplier
import com.gtocore.api.lang.toLiteralSupplier
import com.gtocore.api.misc.AutoInitialize
import com.gtocore.common.block.BlockMap
import com.gtocore.common.data.GTOBlocks
import com.gtocore.common.data.GTOItems
import com.gtocore.config.GTOConfig
import com.gtocore.utils.setTooltips

import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks

import appeng.core.definitions.AEBlocks
import appeng.core.definitions.AEItems
import appeng.core.definitions.AEParts
import com.glodblock.github.extendedae.common.EPPItemAndBlock
import com.gregtechceu.gtceu.GTCEu
import com.gregtechceu.gtceu.common.data.GTItems
import com.gregtechceu.gtceu.common.data.GTMachines
import com.gregtechceu.gtceu.utils.FormattingUtil
import dev.shadowsoffire.apotheosis.adventure.Adventure
import earth.terrarium.adastra.common.registry.ModBlocks
import vazkii.botania.common.block.BotaniaBlocks.fabulousPool

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

    val ConversionSimulationCard = ComponentListSupplier {
        setTranslationPrefix("conversion_simulation_card")

        function("为大型方块转换室提供模拟转换环境" translatedTo "Provides a simulated conversion environment for large block conversion chambers")
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

    val TravelStaff = ComponentListSupplier {
        setTranslationPrefix("the_staff_of_travelling")

        info("末影接口经典款式传送工具仿制品" translatedTo "A replica of the classic teleportation tool of the Ender IO Inc.")

        section(ComponentSlang.MainFunction)
        guide("右键选择目标并传送" translatedTo "Right click to select target and teleport")
        guide("潜行右键可短距离折跃" translatedTo "Shift + Right click to blink a short distance")
        guide("左键可以切换三种模式" translatedTo "Left click air to switch between three modes")
        command("1.可以选中所有目标" translatedTo "First mode: Can select all targets")
        command("2.可以在每个区块选中一个目标" translatedTo "Second mode: Can select one target per block")
        command("3.可以选中点击到的目标" translatedTo "Third mode: Can select the target you clicked")
        info("很多AE节点现在都可以作为传送锚点" translatedTo "Many AE nodes can now be used as teleport anchors")
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
    fun initLanguage() {
        listOf(AEParts.STORAGE_BUS.asItem()).forEach {
            it.setTooltips(
                ComponentListSupplier {
                    setTranslationPrefix("storage_bus")

                    section("与存储设备进行交互" translatedTo "Interact with storage devices")
                    info("经过优化，吞吐量性能卓越" translatedTo "Throughput performance is excellent")
                }.editionByGTONormal(),
            )
        }
        listOf(AEBlocks.CONDENSER.asItem()).forEach {
            it.setTooltips(
                ComponentListSupplier {
                    setTranslationPrefix("matter_condenser")

                    story("近年垃圾回收商引起的一次事故让大家把目光投向了这台物质聚合器。" translatedTo "A recent incident involving a waste recycler has turned people's attention to this Matter Condenser.")
                },
            )
        }

        listOf(EPPItemAndBlock.TAG_STORAGE_BUS.asItem(), EPPItemAndBlock.MOD_STORAGE_BUS.asItem(), EPPItemAndBlock.PRECISE_STORAGE_BUS).forEach {
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
        val bomb = { block: Block, activateItem: Item ->
            block.asItem().setTooltips(
                ComponentListSupplier {
                    setTranslationPrefix("charge_bomb")

                    if (GTCEu.isDataGen() || !GTOConfig.INSTANCE.gamePlay.disableChargeBomb) {
                        info(
                            ComponentSupplier(
                                Component.translatable(
                                    "gtocore.tooltip.item.activate_by",
                                    activateItem.description,
                                ),
                            ),
                        )
                        info("也可以把它喂给热爆花..." translatedTo "It can also be fed to entropinnyum flowers...")
                    } else {
                        error(
                            ComponentSupplier(
                                Component.translatable(
                                    "gtocore.tooltip.item.charge_bomb.disabled",
                                ),
                            ),
                        )
                    }
                },
            )
        }
        bomb(GTOBlocks.NAQUADRIA_CHARGE.get(), GTItems.QUANTUM_STAR.asItem())
        bomb(GTOBlocks.LEPTONIC_CHARGE.get(), GTItems.GRAVI_STAR.asItem())
        bomb(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.get(), GTOItems.UNSTABLE_STAR.asItem())

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

        fabulousPool.asItem().setTooltips(
            ComponentListSupplier {
                setTranslationPrefix("fabulous_pool")

                content("能存一千普通池子的魔力" translatedTo "Can hold the mana of one thousand regular pools")
            }.editionByGTONormal(),
        )

        listOf(AEItems.CERTUS_QUARTZ_KNIFE.asItem(), AEItems.NETHER_QUARTZ_KNIFE.asItem()).forEach {
            it.setTooltips(
                ComponentListSupplier {
                    highlight("Shift+右键方块可以复制其名称，便于命名" translatedTo "Shift+Right click a block to copy its name, which is convenient for naming")
                }.editionByGTONormal(),
            )
        }

        listOf(ModBlocks.OXYGEN_DISTRIBUTOR.get().asItem()).forEach {
            it.setTooltips(
                ComponentListSupplier {
                    command("直接通入氧气与电以工作" translatedTo "Needs oxygen and power to work")
                }.editionByGTONormal(),
            )
        }

        listOf(Adventure.Items.BOSS_SUMMONER.get()).forEach {
            it.setTooltips(
                ComponentListSupplier {
                    setTranslationPrefix("boss_summoner")

                    info("由捕捉附魔击杀神化Boss概率掉落" translatedTo "Dropped by killing Apotheosis Bosses with the Capture enchantment")
                },
            )
        }

        listOf(Blocks.OBSIDIAN.asItem()).forEach {
            it.setTooltips(
                ComponentListSupplier {
                    setTranslationPrefix("obsidian")
                    story("要不试试在它上面用砧板切洋葱？" translatedTo "How about trying to chop onions on it with a cutting board?")
                },
            )
        }
        GTMachines.MUFFLER_HATCH.forEach {
            it?.asItem()?.setTooltips(
                ComponentListSupplier {
                    setTranslationPrefix("muffler_hatch")

                    if (GTOConfig.INSTANCE.gamePlay.disableMufflerPart) {
                        info(
                            ComponentSupplier(
                                Component.translatable(
                                    "gtocore.tooltip.item.muffler.disabled",
                                ),
                            ),
                        )
                    } else {
                        info(
                            ComponentSupplier(
                                Component.translatable(
                                    "gtocore.tooltip.item.muffler.enabled",
                                ),
                            ),
                        )
                    }
                },
            )
        }
    }

    // 泛银河系格雷科技掌上银行
    val PalmSizedBankTooltips = ComponentListSupplier {
        setTranslationPrefix("item.palm_sized_bank")
        miraculousTools("泛银河系格雷科技掌上银行" translatedTo "Pan-Galactic Grey Technology Palm-Sized Bank")
        section("一款集成了跨星系金融服务的便携式终端，由格雷科技星际金融部研发。" translatedTo "A portable terminal integrated with interstellar financial services, developed by Grey Technology Interstellar Finance Division.")
        section("支持跨星系账户实时同步，无论身处哪个殖民星均可管理资产。" translatedTo "Supports real-time synchronization of interstellar accounts, allowing asset management across any colony.")
        section("采用量子加密技术，账户信息无法被破解或篡改，安全等级达到星系标准。" translatedTo "Uses quantum encryption technology; account information cannot be hacked or tampered with, meeting galactic security standards.")
        section("内置能量核心，无需额外供电，可持续运行 730 标准日。" translatedTo "Built-in energy core, no external power required, can operate continuously for 730 standard days.")
        guide("右键打开银行界面，支持存款、取款及向其他认证账户转账。" translatedTo "Right-click to open the bank interface, supporting deposit, withdrawal, and transfer to other certified accounts.")
        story("最初为格雷科技员工专属金融工具，后因需求扩大面向全星系公民开放。" translatedTo "Initially an exclusive financial tool for Grey Technology employees, later opened to all galactic citizens due to high demand.")
        highlight("请勿向未认证账户转账，星际金融法对跨境诈骗有严格处罚。" translatedTo "Do not transfer to uncertified accounts; interstellar financial laws have strict penalties for cross-border fraud.") { color(0xFF5555) }
    }
}
