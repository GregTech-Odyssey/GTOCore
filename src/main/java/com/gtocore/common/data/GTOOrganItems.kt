package com.gtocore.common.data

import com.gtocore.common.data.GTOOrganItems.TierOrganTypes
import com.gtocore.common.data.translation.OrganTranslation
import com.gtocore.common.data.translation.OrganTranslation.organModifierDescriptions
import com.gtocore.common.item.OrganModifierBehaviour
import com.gtocore.common.item.misc.OrganItemBase
import com.gtocore.common.item.misc.OrganItemBase.OrganItem
import com.gtocore.common.item.misc.OrganItemBase.TierOrganItem
import com.gtocore.common.item.misc.OrganType
import com.gtocore.common.item.misc.TierData.Companion.tier_names
import com.gtocore.utils.toSeconds

import com.gregtechceu.gtceu.api.GTValues
import com.gregtechceu.gtceu.api.item.ComponentItem
import com.gregtechceu.gtceu.api.item.component.ElectricStats
import com.gregtechceu.gtceu.common.data.GTItems.attach
import com.gregtechceu.gtceu.common.item.TooltipBehavior
import com.gtolib.GTOCore
import com.gtolib.utils.register.ItemRegisterUtils.item
import com.tterrag.registrate.util.entry.ItemEntry

import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

object GTOOrganItems {
    fun init() {
        registerTierOrganItem()
    }

    // ////////////////////////////////
    // ****** 翅膀 ******//
    // //////////////////////////////
    val FAIRY_WING = OrganItemBase.registerOrganItem(
        id = "fairy_wing",
        organType = OrganType.Wing,
        resourceName = "fairy_wing",
        en = "Fairy Wing",
        cn = "翅膀 妖精之翼",
        itemFactory = { properties, organType -> OrganItem(properties.durability(4.hours.toSeconds()), organType) },
        onRegister = attach(
            TooltipBehavior(OrganTranslation.flightInfo2::apply),
        ),
    )
    val MANA_STEEL_WING = OrganItemBase.registerOrganItem(
        id = "mana_steel_wing",
        organType = OrganType.Wing,
        resourceName = "mana_steel_wing",
        en = "Mana Steel Wing",
        cn = "翅膀 魔力钢之翼",
        itemFactory = { properties, organType -> OrganItem(properties.durability(15.minutes.toSeconds()), organType) },
        onRegister = attach(
            TooltipBehavior(OrganTranslation.flightInfo2::apply),
        ),
    )
    val MECHANICAL_WING = OrganItemBase.registerOrganItem(
        id = "mechanical_wing",
        organType = OrganType.Wing,
        resourceName = "mechanical_wing",
        en = "Mechanical Wing",
        cn = "翅膀 电动机械之翼",
        itemFactory = { properties, organType -> OrganItem(properties, organType) },
        onRegister = attach(
            ElectricStats.createElectricItem(
                GTValues.V[GTValues.EV] * (32.hours.toSeconds()),
                GTValues.EV,
            ),
        ),
    )

    // ////////////////////////////////
    // ****** 编辑器 ******//
    // //////////////////////////////
    val ORGAN_MODIFIER = item("organ_modifier", "器官修改器", { properties -> ComponentItem.create(properties.stacksTo(1).setNoRepair()) })
        .lang("Organ Modifier")
        .model { ctx, prov -> prov.generated(ctx, GTOCore.id("item/organ/item/visceral_editor")) }
        .onRegister(attach(OrganModifierBehaviour()))
        .onRegister(attach(TooltipBehavior(organModifierDescriptions::apply)))
        .register()
    val TierOrganTypes = listOf(OrganType.Eye, OrganType.Spine, OrganType.Lung, OrganType.Liver, OrganType.Heart, OrganType.LeftArm, OrganType.RightArm, OrganType.LeftLeg, OrganType.RightLeg)
    val TierOrganMap = mutableMapOf<OrganType, MutableList<ItemEntry<TierOrganItem>>>()
}

private fun registerTierOrganItem() {
    (0..4).forEach { tier ->
        TierOrganTypes.forEach { organType ->
            val itemEntries = GTOOrganItems.TierOrganMap.computeIfAbsent(organType, { mutableListOf() })
            val itemEntry = OrganItemBase.registerOrganItem(
                id = "tier_${tier}_${organType.key}", // e.g. "tier_1_eye"
                organType = organType,
                resourceName = "tier$tier", // e.g. "tier1"
                en = "Standard Organ ${organType.key.replaceFirstChar { it.uppercase() }} ${tier_names[tier].second}", // e.g. "Organ Eye Tier 1"
                cn = "标准器官 ${organType.cn} ${tier_names[tier].first}", // e.g. "器官 眼睛 Tier 1"
                itemFactory = { properties, organType -> TierOrganItem(tier, properties, organType) },
            )
            itemEntries.add(itemEntry)
        }
    }
}
