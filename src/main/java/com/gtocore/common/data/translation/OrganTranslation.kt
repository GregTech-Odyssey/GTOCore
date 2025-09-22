package com.gtocore.common.data.translation

import com.gtocore.api.lang.ComponentListSupplier
import com.gtocore.api.lang.initialize
import com.gtocore.api.lang.toLiteralSupplier
import com.gtocore.api.lang.translatedTo
import com.gtocore.api.misc.AutoInitialize

object OrganTranslation : AutoInitialize<OrganTranslation>() {
    // 器官效果描述
    val level = { level: Int -> (("级别: " translatedTo "Level: ").gold() + (level.toLiteralSupplier().red().bold())) }.initialize()
    val speedBoostInfo = { speed: Float -> (("此套器官将提供" translatedTo "This organ set will provide") + "%.2f".format(speed).toLiteralSupplier().red().bold() + ("速度加成" translatedTo "Speed Boost")).green() }.initialize()
    val blockReachInfo = { reach: Int -> (("此套器官将提供" translatedTo "This organ set will provide") + reach.toLiteralSupplier().gold().bold() + ("触及距离加成" translatedTo "Block Reach")).yellow() }.initialize()
    val flightInfo = (("此套器官将提供" translatedTo "This organ set will provide") + ComponentSlang.Infinite.bold() + ("飞行能力" translatedTo "Flight Capability")).lightPurple().initialize()
    val flightInfo2 = (("此器官将提供" translatedTo "This organ will provide") + ("飞行能力" translatedTo "Flight Capability")).lightPurple().initialize()

    // 杂项
    val durability = ("耐久" translatedTo "durability").initialize()
    val organModifierName = ("器官修改器" translatedTo "Organ Modifier").initialize()

    // 器官编辑器
    val organModifierDescriptions = ComponentListSupplier {
        setTranslationPrefix("organ_modifier_item")
        add("此物品将允许你修改器官" translatedTo "This item will allow you to modify your organ") { green() }
        add(("不同的器官套装将提供" translatedTo "Different organ sets will provide ") + ("不同" translatedTo "different ").gold() + ("的效果" translatedTo "effects")) { green() }
        add("右键以打开修改界面" translatedTo "Right click to open the modification interface") { gray() }
        add(ComponentSlang.Star(1) + ("大多数星球需要" translatedTo "Most planets require ") + ("一定级别" translatedTo "a-certain level ").red() + ("的器官套装" translatedTo "organ set")) { lightPurple() }
    }.initialize()
}
