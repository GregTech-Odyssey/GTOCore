package com.gtocore.common.item.misc

import com.gtolib.api.annotation.Scanned
import com.gtolib.api.annotation.language.RegisterLanguage

@Scanned
class OrganTranslation {
    @Scanned
    companion object {
        @RegisterLanguage(cn = "使用量", en = "Using")
        val using: String = "gtocore.organ.using"

        @RegisterLanguage(cn = "耐久", en = "durability")
        val durability: String = "gtocore.organ.durability"

        @RegisterLanguage(cn = "级别: %s", en = "Level: %s")
        val level: String = "gtocore.organ.level"

        @RegisterLanguage(cn = "此器官套装将提供速度加成: %s", en = "This organ set will provide speed boost: %s")
        val speedBoost: String = "gtocore.organ.speedBoost"

        @RegisterLanguage(cn = "此器官套装将提供距离加成: %s格", en = "This organ set will provide speed boost: %s Block")
        val blockReach: String = "gtocore.organ.blockReach"

        @RegisterLanguage(cn = "此器官套装将提供无限夜视", en = "This organ set will provide infinite night vision")
        val nightVision: String = "gtocore.organ.nightVision"

        @RegisterLanguage(cn = "此器官套装将提供无限飞行", en = "This organ set will provide infinite flight")
        val flight: String = "gtocore.organ.flight"
    }
}
