package com.gtocore.common.item.misc

import com.gtocore.api.gui.graphic.GTOToolTipComponent
import com.gtocore.api.gui.graphic.GTOTooltipComponentItem
import com.gtocore.api.gui.graphic.impl.GTOProgressToolTipComponent
import com.gtocore.api.gui.graphic.impl.toPercentageWith
import com.gtocore.api.gui.helper.ProgressBarColorStyle

import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

import com.gregtechceu.gtceu.api.item.ComponentItem
import com.gtolib.GTOCore
import com.gtolib.utils.TagUtils
import com.gtolib.utils.register.ItemRegisterUtils.item
import com.tterrag.registrate.util.entry.ItemEntry
import com.tterrag.registrate.util.nullness.NonNullConsumer

class TierData {
    companion object {
        val tier_names = listOf(
            "宝宝级" to "Baby",
            "标准级" to "Standard",
            "强化级" to "Reinforced",
            "战术级" to "Tactical",
            "原型级" to "Prototype",
        )
        val MovementSpeedFunction: (Int) -> Double = { tier -> 0.1 * 0.1 * tier * 1.5 }
        val BlockReachFunction = 2
    }
}
enum class OrganType(val key: String, val cn: String, val slotCount: Int = 1) {
    Wing("wing", "翅膀", slotCount = 8), // 支持顺序使用,
    Eye("eye", "眼睛"),
    Spine("spine", "脊椎"),
    Lung("lung", "肺"),
    Liver("liver", "肝脏"),
    Heart("heart", "心脏"),
    LeftArm("left_arm", "左臂"),
    RightArm("right_arm", "右臂"),
    LeftLeg("left_leg", "左腿"),
    RightLeg("right_leg", "右腿"),
//    Other("other", "其他"),
}

sealed class OrganItemBase(properties: Properties, val organType: OrganType) :
    ComponentItem(properties),
    GTOTooltipComponentItem {
    override fun attachGTOTooltip(itemStack: ItemStack?, tooltips: MutableList<GTOToolTipComponent>) {
        // 耐久度使用进度条
        run {
            val maxDamage = itemStack?.maxDamage ?: 0
            if (maxDamage <= 0) return@run
            val damage = itemStack?.damageValue ?: 0
            tooltips.add(
                GTOProgressToolTipComponent(
                    percentage = 1 - (damage toPercentageWith maxDamage),
                    progressColorStyle = ProgressBarColorStyle.DURATION,
                    text = "${Component.translatable(OrganTranslation.durability).string} : ${maxDamage - damage}/${maxDamage}s",
                ),
            )
        }
    }

    companion object {
        fun <T : OrganItemBase> registerOrganItem(id: String, organType: OrganType, resourceName: String, en: String, cn: String, itemFactory: (Properties, OrganType) -> T, onRegister: NonNullConsumer<T> = NonNullConsumer { }): ItemEntry<T> {
            val resourcePath = "item/organ/part/${organType.key}/$resourceName"
            val tag = TagUtils.createTag(GTOCore.id("organ_${organType.key}"))
            val itemBuilder = item(id, "器官 $cn", { p -> itemFactory(p.stacksTo(1).setNoRepair(), organType) })
                .lang("organ $en ")
                .tag(tag)
                .model { ctx, prov -> prov.generated(ctx, GTOCore.id(resourcePath)) }
            itemBuilder.onRegister(onRegister)
            return itemBuilder.register()
        }
    }
    class OrganItem(properties: Properties, organType: OrganType) : OrganItemBase(properties, organType)
    class TierOrganItem(val tier: Int, properties: Properties, organType: OrganType) : OrganItemBase(properties, organType) {
        override fun appendHoverText(stack: ItemStack, level: Level?, tooltipComponents: MutableList<Component?>, isAdvanced: TooltipFlag) {
            tooltipComponents.add(Component.translatable(OrganTranslation.level, "Tier $tier"))
            if (tier >= 1)tooltipComponents.add(Component.translatable(OrganTranslation.speedBoost, ((1..tier).sumOf { TierData.MovementSpeedFunction(it) * 10 }).toString()))
            if (tier >= 1)tooltipComponents.add(Component.translatable(OrganTranslation.nightVision))
            if (tier >= 2)tooltipComponents.add(Component.translatable(OrganTranslation.blockReach, 2))
            if (tier >= 4)tooltipComponents.add(Component.translatable(OrganTranslation.flight))

            super.appendHoverText(stack, level, tooltipComponents, isAdvanced)
        }
    }
}
