package com.gtocore.data.recipe.research

import com.gtocore.api.lang.ComponentListSupplier
import com.gtocore.api.misc.AutoInitialize
import com.gtocore.common.data.translation.*
import com.gtocore.data.recipe.builder.research.ExResearchManager.writeAnalyzeResearchToMap

import com.gregtechceu.gtceu.GTCEu
import com.gregtechceu.gtceu.utils.collection.O2OOpenCacheHashMap
import com.gtolib.api.lang.CNEN

object AnalyzeData : AutoInitialize<AnalyzeData>() {

    val langMap: Map<String, CNEN> = if (GTCEu.isDataGen()) O2OOpenCacheHashMap() else emptyMap()

    private val researchTooltips = mutableMapOf<String, ComponentListSupplier>()

    fun getTooltip(researchKey: String): ComponentListSupplier? = researchTooltips[researchKey]

    private var itemRegister = false

    override fun init() {
        initErrorResearchData()
        initSampleResearchData()
        itemRegister = true
    }

    private fun initErrorResearchData() {
        addResearch(
            "error1",
            "§k1§r错误§k1§r",
            "§k1§rError§k1§r",
            0,
            1,
            ComponentListSupplier {
                setTranslationPrefix("research.error1")
                info("只是一个意外罢了" translatedTo "It was just an accident")
            },
        )
        addResearch(
            "error2",
            "§k22§r错误§k22§r",
            "§k22§rError§k22§r",
            0,
            2,
            ComponentListSupplier {
                setTranslationPrefix("research.double_error")
                info("只是两个意外罢了" translatedTo "It was just two accidents")
            },
        )
        addResearch(
            "error3",
            "§k333§r错误§k333§r",
            "§k333§rError§k333§r",
            0,
            3,
        )
        addResearch(
            "error4",
            "§k4444§r错误§k4444§r",
            "§k4444§rError§k4444§r",
            0,
            4,
        )
        addResearch(
            "error5",
            "§k55555§r错误§k55555§r",
            "§k55555§rError§k55555§r",
            0,
            4,
        )
    }

    private fun initSampleResearchData() {
        addResearch(
            "基础材料研究",
            "Basic Material Study",
            1,
            1,
            ComponentListSupplier {
                setTranslationPrefix("research.basic_material_study")
                section("研究金属与矿物特性" translatedTo "Study metal and mineral properties")
            },
        )
        addResearch(
            "能量传输研究",
            "Energy Transmission Research",
            1,
            2,
        )
    }

    private fun addResearch(cnName: String, enName: String, dataTier: Int, dataCrystal: Int, tooltip: ComponentListSupplier? = null) {
        addResearch(enName.replace(" ", "_").lowercase(), cnName, enName, dataTier, dataCrystal, tooltip)
    }

    private fun addResearch(key: String, cnName: String, enName: String, dataTier: Int, dataCrystal: Int, tooltip: ComponentListSupplier? = null) {
        if (!itemRegister) {
            tooltip?.let { researchTooltips[key] = it }
            if (GTCEu.isDataGen()) (langMap as O2OOpenCacheHashMap).put(key, CNEN(cnName, enName))
        } else {
            writeAnalyzeResearchToMap(key, dataTier, dataCrystal)
        }
    }
}
