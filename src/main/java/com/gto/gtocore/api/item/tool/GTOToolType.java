package com.gto.gtocore.api.item.tool;

import com.gregtechceu.gtceu.api.data.tag.TagUtil;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.gregtechceu.gtceu.api.item.tool.ToolHelper;
import com.gregtechceu.gtceu.common.item.tool.behavior.*;

import net.minecraft.tags.ItemTags;

public class GTOToolType {

    public static final GTToolType VAJRA = GTToolType.builder("vajra")
            .idFormat("%s_vajra")
            .toolTag(TagUtil.createItemTag("tools/wrenches", false))
            .toolTag(TagUtil.createItemTag("tools/wrench", false))
            .toolTag(TagUtil.createItemTag("tools/wire_cutters", false))
            .toolTag(TagUtil.createItemTag("pickaxes", true))
            .toolTag(TagUtil.createItemTag("shovels", true))
            .toolTag(TagUtil.createItemTag("swords", true))
            .toolTag(TagUtil.createItemTag("hoes", true))
            .toolTag(ItemTags.CLUSTER_MAX_HARVESTABLES)
            .harvestTag(TagUtil.createBlockTag("mineable/hoe", true))
            .harvestTag(TagUtil.createBlockTag("mineable/sword"))
            .harvestTag(TagUtil.createBlockTag("mineable/pickaxe", true))
            .harvestTag(TagUtil.createBlockTag("mineable/shovel", true))
            .toolStats(b -> b.blockBreaking().crafting().sneakBypassUse()
                    .attackDamage(10.0F).attackSpeed(2.0F)
                    .behaviors(GrassPathBehavior.INSTANCE, DisableShieldBehavior.INSTANCE,
                            TreeFellingBehavior.INSTANCE, LogStripBehavior.INSTANCE,
                            ScrapeBehavior.INSTANCE, WaxOffBehavior.INSTANCE,
                            BlockRotatingBehavior.INSTANCE, ToolModeSwitchBehavior.INSTANCE)
                    .brokenStack(ToolHelper.SUPPLY_POWER_UNIT_IV))
            .toolClasses(GTToolType.PICKAXE, GTToolType.SHOVEL, GTToolType.AXE,
                    GTToolType.WRENCH, GTToolType.WIRE_CUTTER)
            .build();
}
